package AppKickstarter.MsgHandler;

import AppKickstarter.Ticket.Ticket;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TicketRep extends Msg {
	private static int TicketNo = 0;

	public TicketRep(String sender, MBox senderMBox, Type type, String details) {
		super(sender, senderMBox, type, details);
		
		TicketCreate(TicketNo);
		// TODO Auto-generated constructor stub
	}

	public void TicketCreate(int TicketNo) {
//		Ticket t = new Ticket

	}

	public int getTicketNo() {
		return TicketNo;
	}

}
