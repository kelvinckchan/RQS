package AppKickstarter.Msg;

import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class MsgParser {

	public static Msg IncomingMsgParser(String sender, MBox mbox, String incomingMsg) {
		String[] SplitedMsg = incomingMsg.split(":");
		String Type = SplitedMsg[0];
		String MsgDetail = SplitedMsg[1];
		switch (Type) {
		case "TicketReq":
			return new Msg(sender, mbox, Msg.Type.TicketReq, MsgDetail);
		case "TicketAck":
			return new Msg(sender, mbox, Msg.Type.TicketAck, MsgDetail);
		case "CheckOut":
			return new Msg(sender, mbox, Msg.Type.CheckOut, MsgDetail);
		default:
			break;
		}
		return null;
	}

}
