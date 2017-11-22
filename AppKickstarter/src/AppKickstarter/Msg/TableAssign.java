package AppKickstarter.Msg;

import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TableAssign extends Command {

	private Ticket ticket;
	private Table table;
	private int TableNo;

	public TableAssign(Ticket ticket, Table table) {
		this.ticket = ticket;
		this.table = table;
	}

	public TableAssign(Ticket calledTicket, int tableNo) {
		this.ticket = calledTicket;
		this.TableNo = tableNo;
	}

	public Ticket getTicket() {
		return this.ticket;
	}

	public Table getTable() {
		return this.table;
	}

	public int getTableNo() {
		return this.TableNo;
	}

	@Override
	public String toString() {
		return String.format("TableAssign: %s %s", ticket.getTicketID(), TableNo);
	}

}
