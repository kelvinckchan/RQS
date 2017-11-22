package AppKickstarter.Msg;

import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;

public class TicketCall extends Command {
	private Ticket ticket;
	private Table table;

	public TicketCall(Ticket ticket, Table table) {
		this.ticket = ticket;
		this.table = table;
	}

	public Ticket getTicket() {
		return this.ticket;
	}

	public Table getTable() {
		return this.table;
	}

	@Override
	public String toString() {
		return String.format("TicketCall: %s %s", ticket.getTicketID(), table.getTableNo());
	}
}
