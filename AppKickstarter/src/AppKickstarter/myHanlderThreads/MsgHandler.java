
package AppKickstarter.myHanlderThreads;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.QueueTooLong;
import AppKickstarter.Msg.TicketRep;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Ticket;

//======================================================================
// ThreadB
// Server ThreadB
public class MsgHandler extends AppThread {
	private final int sleepTime = 2000;

	// ------------------------------------------------------------
	// ThreadB
	public MsgHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
	} // ThreadB

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {

			Msg msg = mbox.receive();
			log.info(id + ": message received: [" + msg + "].");
			String[] DetailParts = msg.getDetails().trim().split("\\s+");
			int TicketId, TableId, nPerson;
			String ClientId;

			switch (msg.getType()) {

			case TicketReq:
				Client ReqClient = new Client(DetailParts[0], Integer.valueOf(DetailParts[1]));
				Ticket ticket = TicketHandler.ReqForTicket(ReqClient);
				if (ticket != null) {
					String TicketRepDetail = String.format("TicketRep: %s %s %s", ReqClient.getClientID(),
							ReqClient.getnPerson(), ticket.getTicketID());
					appKickstarter.getThread("SocketOutHandler").getMBox()
							.send(new TicketRep(id, mbox, Msg.Type.TicketRep, TicketRepDetail));
				} else {

					String QueueTooLongDetail = String.format("QueueTooLong: %s %s", ReqClient.getClientID(),
							ReqClient.getnPerson());
					appKickstarter.getThread("SocketOutHandler").getMBox()
							.send(new QueueTooLong(id, mbox, Msg.Type.QueueTooLong, QueueTooLongDetail));

				}

				break;

			case TicketAck:
				TicketId = Integer.valueOf(DetailParts[0]);
				TableId = Integer.valueOf(DetailParts[1]);
				nPerson = Integer.valueOf(DetailParts[2]);

				// TicketCall rep = new TicketCall(id, mbox, Msg.Type.TicketCall, "TicketCall: "
				// + ClientId + " " + nPerson+ " "+ rep.getTicketNo());
				break;

			case Hello:
				log.info(id + ": " + msg.getSender() + " is saying Hello to me!!!");
				msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is Thread B!"));
				break;

			case Terminate:
				quit = true;
				break;
			default:
				log.severe(id + ": unknown message type!!");
				break;
			}
		}

		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run

} // ThreadB
