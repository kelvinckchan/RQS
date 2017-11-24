
package AppKickstarter.myHanlderThreads;

import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;

import java.time.LocalDateTime;
import java.util.Objects;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.*;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Ticket;

/**
 * This class implements a Message Handler
 * 
 * @author
 * @version 1.0
 */
public class MsgHandler extends AppThread {
	private final int sleepTime = 2000;
	private int TicketAckWaitingTime;
	private int mode = appKickstarter.getMode();
	private TableHandler tableHandler;
	private int KickOutTime = 30 * 1000;

	/**
	 * This constructs a Message Handler with id and appKickstarter
	 * 
	 * @param id
	 *            :
	 * @param appKickstarter
	 *            :
	 */
	public MsgHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		tableHandler = new TableHandler("TableHandler", appKickstarter);
		new Thread(tableHandler).start();
		this.TicketAckWaitingTime = Integer.valueOf(appKickstarter.getProperty("TicketAckWaitingTime"));
	}

	/**
	 * Separate different types of messages (TicketReq, TicketAck, CheckOut,
	 * TicketCall, TimesUp, Terminate)
	 * 
	 */
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {

			Msg msg = mbox.receive();
			log.info(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {

			case TicketReq:
				HandlerTicketReq(msg);
				break;

			case TicketAck:
				// Receive TicketAsk: TicketID TableNo nPerson
				// Cancel Timer TicketCall: TicketID TableNo
				// CheckIn Table
				HandlerTicketAck(msg);
				break;

			case CheckOut:
				// Receive CheckOut: TableNo TotalSpending
				// CheckOutTable
				CheckOut checkOut = ((CheckOut) msg.getCommand());
				tableHandler.CheckOutTable(checkOut.getTableNo(), checkOut.getTotalSpending());
				if (mode == 1)
					Timer.cancelTimer("TableHandler", appKickstarter.getThread("TableHandler").getMBox(),
							checkOut.getTableNo());
				log.info(id + ": CheckOutTable> " + checkOut.getTableNo() + "!");

				break;

			case TimesUp:
				log.info(id + ": TimesUP! from>" + msg.getSender() + "> " + msg.getDetails());
				break;

			case Terminate:
				quit = true;
				break;
			default:
				log.severe(id + ": unknown message type!!> " + msg.getDetails() + " From> " + msg.getSender());
				break;
			}
		}

		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run

	/**
	 * This constructs a handler for ticket request with msg
	 * 
	 * @param msg
	 *            : The message of ticket request
	 */
	private void HandlerTicketReq(Msg msg) {
		Client ReqClient = ((TicketReq) msg.getCommand()).getClient();
		Ticket ticket = null;
		try {
			ticket = TicketHandler.GetTicketAndAddToTicketQueueIfQueueNotFull(ReqClient);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Try Requesting a Ticket, Return a Ticket if Not QueueTooLong
		if (ticket != null) {
			appKickstarter.getThread("SocketOutHandler").getMBox()
					.send(new Msg(id, mbox, Msg.Type.TicketRep, new TicketRep(ReqClient, ticket)));
		} else {
			appKickstarter.getThread("SocketOutHandler").getMBox()
					.send(new Msg(id, mbox, Msg.Type.QueueTooLong, new QueueTooLong(ReqClient)));
		}
	}

	/**
	 * This constructs a handler for ticket acknowledgement with msg
	 * 
	 * @param msg
	 *            : The message of ticket acknowledgement
	 */

	private void HandlerTicketAck(Msg msg) {
		// Receive TicketAsk: TicketID TableNo nPerson
		// Cancel Timer TicketCall: TicketID TableNo
		// CheckIn Table
		TicketAck ticketAck = ((TicketAck) msg.getCommand());
		Ticket TicketWaiting = TicketHandler.FindWaitingTicketAndPoll(ticketAck.getTicketID());
		if (TicketWaiting != null) {
			LocalDateTime CheckedIn = tableHandler.CheckInWaitingTicketToTable(TicketWaiting, ticketAck.getTableNo());
			if (CheckedIn != null) {
				if (mode == 1) {
					Timer.cancelTimer("TicketHandler", appKickstarter.getThread("TicketHandler").getMBox(),
							TicketWaiting.getTicketID());
					appKickstarter.getThread("SocketOutHandler").getMBox().send(new Msg(id, mbox, Msg.Type.TableAssign,
							new TableAssign(TicketWaiting, ticketAck.getTableNo())));
				}
				log.info(id + ": CheckedInTable Ticket> " + TicketWaiting.getTicketID() + " Waiting For CheckOut");
			} else {
				log.info(id + ": Not Able To CheckIn Ticket> " + ticketAck.getTicketID() + " !");
			}
		} else {
			log.info(id + ": Not Able To CheckIn Ticket> " + ticketAck.getTicketID() + " !");
		}
	}

} // ThreadB
