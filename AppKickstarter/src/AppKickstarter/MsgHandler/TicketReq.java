package AppKickstarter.MsgHandler;

import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TicketReq extends Msg {
	int TicketNo;

	public TicketReq(String sender, MBox senderMBox, Type type, String details) {
		super(sender, senderMBox, type, details);
	}

	
	
	public int getTicketNo(String ClientId, int Person) {
		TicketNo += 1;
		// TicketRep rep = new TicketRep(TicketNo);

		return TicketNo;

	}
}
