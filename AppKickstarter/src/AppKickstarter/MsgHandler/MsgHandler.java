
package AppKickstarter.MsgHandler;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;

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

			Msg msg = mbox.receive();

			log.info(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {
			case Hello:
				log.info(id + ": " + msg.getSender() + " is saying Hello to me!!!");
				msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is Thread B!"));
				break;

			case Terminate:
				quit = true;
				break;

//			case TicketRep:
//				msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.TicketRep, "HiHi, this is Thread B!"));
//				break;
//
//			case TicketCall:
//
//				break;
//			case TableAssign:
//
//				break;
//			case QueueTooLong:
//
//				break;

			default:
				log.severe(id + ": unknown message type!!");
				break;
			}
		}

		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run

	public Msg IncomingMsgParser(String FullMsg) {
		String[] SplitedMsg = FullMsg.split(":");
		String Type = SplitedMsg[0];
		String MsgDetail = SplitedMsg[1];

		String[] DetailParts = MsgDetail.trim().split("\\s+");

		switch (Type) {

		case "TicketRep":
			String ClientId = DetailParts[0];
			int nPerson = Integer.valueOf(DetailParts[1]);
//			TicketRep rep = new TicketRep(id, mbox, Msg.Type.TicketRep, ClientId + "," + nPerson+ ","+ rep.getTicketNo());

			break;

		case "TicketCall":
			
//			TicketCall rep = new TicketCall(id, mbox, Msg.Type.TicketCall, "TicketCall: " + ClientId + " " + nPerson+ " "+ rep.getTicketNo());
			break;

		case "TableAssign":

			break;

		case "QueueTooLong":

			break;

		default:
			log.severe(id + ": unknown message type!!");
			break;

		}

		return null;
	}

} // ThreadB
