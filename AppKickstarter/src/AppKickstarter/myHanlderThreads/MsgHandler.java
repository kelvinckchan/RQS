
package AppKickstarter.myHanlderThreads;

import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;

import java.time.LocalDateTime;
import java.util.Objects;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.*;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Ticket;

//======================================================================
// ThreadB
// Server ThreadB
public class MsgHandler extends AppThread {
	private final int sleepTime = 2000;
	private int TicketAckWaitingTime;

	// ------------------------------------------------------------
	// ThreadB
	public MsgHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		this.TicketAckWaitingTime = Integer.valueOf(appKickstarter.getProperty("TicketAckWaitingTime"));
	} // ThreadB

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {

			Msg msg = mbox.receive();
			log.info(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {

			case TicketReq:
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
					appKickstarter.getThread("TicketHandler").getMBox()
							.send(new Msg(id, mbox, Msg.Type.TicketRep, new TicketRep(ReqClient, ticket)));

				} else {
					appKickstarter.getThread("SocketOutHandler").getMBox()
							.send(new Msg(id, mbox, Msg.Type.QueueTooLong, new QueueTooLong(ReqClient)));
				}

				break;

			case TicketAck:
				// Receive TicketAsk: TicketID TableNo nPerson
				// Cancel Timer TicketCall: TicketID TableNo
				// CheckIn Table
				TicketAck ticketAck = ((TicketAck) msg.getCommand());
				Ticket TicketWaiting = TicketHandler.FindWaitingTicketAndPoll(ticketAck.getTicketID());
				if (TicketWaiting != null) {
					LocalDateTime CheckedIn = TableHandler.CheckInWaitingTicketToTable(TicketWaiting,
							ticketAck.getTableNo());
					if (CheckedIn != null) {
						Timer.cancelTimer("TicketHandler", mbox, TicketWaiting.getTicketID());
						appKickstarter.getThread("SocketOutHandler").getMBox().send(new Msg(id, mbox,
								Msg.Type.TableAssign, new TableAssign(TicketWaiting, ticketAck.getTableNo())));
						log.info(id + ": CheckedInTable Ticket> " + TicketWaiting.getTicketID()
								+ " Waiting For CheckOut");
					} else {
						log.info(id + ": Not Able To CheckIn Ticket> " + ticketAck.getTicketID() + " !");
					}
				} else {
					log.info(id + ": Not Able To CheckIn Ticket> " + ticketAck.getTicketID() + " !");
				}

				break;

			case CheckOut:
				// Receive CheckOut: TableNo TotalSpending
				// CheckOutTable
				CheckOut checkOut = ((CheckOut) msg.getCommand());
				TableHandler.CheckOutTable(checkOut.getTableNo(), checkOut.getTotalSpending());
				log.info(id + ": CheckOutTable> " + checkOut.getTableNo() + "!");

				break;

			case TicketCall:
				// Send TicketCall: TicketID TableNo
				// Set Timer for Waiting TicketAck
				// appKickstarter.getThread("SocketOutHandler").getMBox().send(msg);
				// TicketCall ticketcall = (TicketCall) msg.getCommand();
				// Timer.setTimer(id, mbox, TicketAckWaitingTime,
				// ticketcall.getTicket().getTicketID());
				// log.info(id + ": TicketCall Sent> " + ticketcall.getTicket().getTicketID() +
				// " Wait For TickerAck");
				break;

			case TimesUp:
				log.info(id + ": TimesUP! from>" + msg.getSender() + "> " + msg.getDetails());
				// int ticketID = Integer.valueOf(msg.getDetails().substring(2, 6));
				// TicketHandler.removeFromWaitForAckTicketQueue(ticketID);
				// // Waited too long for TicketAck... Remove Ticket from
				// // TicketHandler.WaitForAckTicketQueue
				// // UnHold Table
				// TableHandler.UnHoldTable(ticketID);
				break;

			case Hello:
				log.info(id + ": " + msg.getSender() + " is saying Hello to me!!!");
				msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is Thread B!"));
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

} // ThreadB
