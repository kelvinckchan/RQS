package AppKickstarter.Msg;

import AppKickstarter.Server.Client;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class IncomingMsgParser {

	public static Msg IncomingMsgParser(String sender, MBox mbox, String incomingMsg) {
		String[] SplitedMsg = incomingMsg.split(":");
		String Type = SplitedMsg[0];
		String MsgDetail = SplitedMsg[1];
		String[] DetailParts = MsgDetail.trim().split("\\s+");
		switch (Type) {
		case "TicketReq":
			return new Msg(sender, mbox, Msg.Type.TicketReq,
					new TicketReq(new Client(DetailParts[0], Integer.valueOf(DetailParts[1]))));
		case "TicketAck":
			return new Msg(sender, mbox, Msg.Type.TicketAck, new TicketAck(Integer.valueOf(DetailParts[0]),
					Integer.valueOf(DetailParts[1]), Integer.valueOf(DetailParts[2])));
		case "CheckOut":
			return new Msg(sender, mbox, Msg.Type.CheckOut,
					new CheckOut(Integer.valueOf(DetailParts[0]), Integer.valueOf(DetailParts[1])));
		default:
			break;
		}
		return null;
	}

}
