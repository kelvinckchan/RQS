
package AppKickstarter.MsgHandler;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Ticket;
import AppKickstarter.Server.TicketHandler;

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
			case Hello:
				log.info(id + ": " + msg.getSender() + " is saying Hello to me!!!");
				msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is Thread B!"));
				break;

			case Terminate:
				quit = true;
				break;

			case TicketReq:

				ClientId = DetailParts[0];
				nPerson = Integer.valueOf(DetailParts[1]);

				if (QueueTooLong.Check(nPerson) == false) {
					Ticket t = new Ticket(new Client(ClientId, nPerson));
					TicketHandler.add(t);

					String TicketRepDetail = String.format("%s %s %s", ClientId, String.valueOf(nPerson),
							String.valueOf(t.getTicketID()));

					TicketRep rep = new TicketRep(id, mbox, Msg.Type.TicketRep, TicketRepDetail);
					appKickstarter.getThread("SocketOutHandler").getMBox().send(rep);
				} else {
					QueueTooLong q = new QueueTooLong(id, mbox, Msg.Type.QueueTooLong, "QueueTooLong");
				}

				break;

			case TicketAck:
				TicketId = Integer.valueOf(DetailParts[0]);
				TableId = Integer.valueOf(DetailParts[1]);
				nPerson = Integer.valueOf(DetailParts[2]);

				// TicketCall rep = new TicketCall(id, mbox, Msg.Type.TicketCall, "TicketCall: "
				// + ClientId + " " + nPerson+ " "+ rep.getTicketNo());
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
