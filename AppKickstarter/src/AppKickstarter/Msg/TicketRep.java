package AppKickstarter.Msg;

import AppKickstarter.Server.Client;
import AppKickstarter.Server.Ticket;

public class TicketRep extends Command {

	private Client client;
	private Ticket ticket;

	public TicketRep(Client client, Ticket ticket) {
		this.client = client;
		this.ticket = ticket;
	}

	public Client getClient() {
		return this.client;
	}

	public Ticket getTicket() {
		return this.ticket;
	}

	@Override
	public String toString() {
		return String.format("TicketRep: %s %s %s", this.client.getClientID(), this.client.getnPerson(),
				this.ticket.getTicketID());
	}

}
