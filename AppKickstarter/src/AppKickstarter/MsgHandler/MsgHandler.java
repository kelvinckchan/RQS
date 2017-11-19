
package AppKickstarter.MsgHandler;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;
import AppKickstarter.Ticket;
import AppKickstarter.TicketHandler;

//======================================================================
// ThreadB
// Server ThreadB
public class MsgHandler extends AppThread {
	private final int sleepTime = 2000;
	private String FullMsg;

	// ------------------------------------------------------------
	// ThreadB
	public MsgHandler(String id, AppKickstarter appKickstarter, String FullMsg) {
		super(id, appKickstarter);
		this.FullMsg = FullMsg;
	} // ThreadB

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {

			Msg ParseredMsg = IncomingMsgParser(FullMsg);
			// TableEmpty -> TicketCall
		}
		// Msg msg = mbox.receive();
		//
		// log.info(id + ": message received: [" + msg + "].");
		//
		// switch (msg.getType()) {
		// case Hello:
		// log.info(id + ": " + msg.getSender() + " is saying Hello to me!!!");
		// msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is
		// Thread B!"));
		// break;
		//
		// case Terminate:
		// quit = true;
		// break;
		//
		// // case TicketRep:
		// // msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.TicketRep, "HiHi, this
		// is
		// // Thread B!"));
		// // break;
		// //
		// // case TicketCall:
		// //
		// // break;
		// // case TableAssign:
		// //
		// // break;
		// // case QueueTooLong:
		// //
		// // break;
		//
		// default:
		// log.severe(id + ": unknown message type!!");
		// break;
		// }
		// }
		//
		
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run

	public Msg IncomingMsgParser(String FullMsg) {
		String[] SplitedMsg = FullMsg.split(":");
		String Type = SplitedMsg[0];
		String MsgDetail = SplitedMsg[1];

		String[] DetailParts = MsgDetail.trim().split("\\s+");
		String ClientId;
		int nPerson, TicketId, TableId;

		switch (Type) {

		case "TicketReq":
			ClientId = DetailParts[0];
			nPerson = Integer.valueOf(DetailParts[1]);

			if (QueueTooLong.Check(nPerson) == false) {
				TicketRep rep = new TicketRep(id, mbox, Msg.Type.TicketRep, ClientId + "," + nPerson);
				Ticket t = new Ticket(ClientId, nPerson);
				TicketHandler.add(t);
			} else {
				QueueTooLong q = new QueueTooLong(id, mbox, Msg.Type.QueueTooLong, "QueueTooLong");
			}

			break;

		case "TicketAck": /// Table Assign
			TicketId = Integer.valueOf(DetailParts[0]);
			TableId = Integer.valueOf(DetailParts[1]);
			nPerson = Integer.valueOf(DetailParts[2]);

			// Table Assign
			
			break;

		/// Where it start?
//		case "TicketCall":
//			TicketId = Integer.valueOf(DetailParts[0]);
//			TableId = Integer.valueOf(DetailParts[1]);
//
//			// TicketCall rep = new TicketCall(id, mbox, Msg.Type.TicketCall, "TicketCall: "
//			// + ClientId + " " + nPerson+ " "+ rep.getTicketNo());
//			break;

		default:
			log.severe(id + ": unknown message type!!");
			break;

		}
		return null;
	}

} // ThreadB
