package AppKickstarter.Msg;

import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TicketReq extends Msg {


	public TicketReq(String sender, MBox senderMBox, Type type, String details) {
		super(sender, senderMBox, type, details);
	}
}
