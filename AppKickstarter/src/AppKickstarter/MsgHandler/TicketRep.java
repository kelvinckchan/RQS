package AppKickstarter.MsgHandler;

import AppKickstarter.Server.Client;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TicketRep extends Msg {

	
	public TicketRep(String sender, MBox senderMBox, Type type, String details) {
		super(sender, senderMBox, type, details);
	}
 

	public void getTicketNo() {
	}

}
