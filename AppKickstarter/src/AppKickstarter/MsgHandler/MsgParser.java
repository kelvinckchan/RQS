package AppKickstarter.MsgHandler;

import AppKickstarter.misc.Msg;

public class MsgParser {

	public static Msg IncomingMsgParser(String FullMsg) {
		String[] SplitedMsg = FullMsg.split(":");
		String Type = SplitedMsg[0];
		String MsgDetail = SplitedMsg[1];
		String[] DetailParts = MsgDetail.trim().split("\\s+");

		switch (Type) {

		case "TicketRep":
			String ClientId = DetailParts[0];
			int nPerson = Integer.valueOf(DetailParts[1]);
			// TicketRep rep = new TicketRep(id, mbox, Msg.Type.TicketRep, ClientId + "," +
			// nPerson+ ","+ rep.getTicketNo());

			break;

		case "TicketCall":

			// TicketCall rep = new TicketCall(id, mbox, Msg.Type.TicketCall, "TicketCall: "
			// + ClientId + " " + nPerson+ " "+ rep.getTicketNo());
			break;

		case "TableAssign":

			break;

		case "QueueTooLong":

			break;

		default:
			break;

		}

		return null;
	}

}
