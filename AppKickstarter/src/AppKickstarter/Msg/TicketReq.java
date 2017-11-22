package AppKickstarter.Msg;

import AppKickstarter.Server.Client;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TicketReq extends Command {
	private Client client;

	public TicketReq(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return this.client;
	}

	@Override
	public String toString() {
		return String.format("TicketReq: %s %s", client.getClientID(), client.getnPerson());
	}

}
