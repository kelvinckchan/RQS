package AppKickstarter.MsgHandler;

import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TicketRep extends Msg {
	int TicketNo;

	public TicketRep(String sender, MBox senderMBox, Type type, String details) {
		super(sender, senderMBox, type, details);
		// TODO Auto-generated constructor stub
	}

	public int getTicketNo() {
		TicketNo += 1;
		return TicketNo;
	}

}
