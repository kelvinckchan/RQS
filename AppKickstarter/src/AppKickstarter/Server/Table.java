package AppKickstarter.Server;

import java.util.ArrayList;

public class Table {

	private int TableNo;
	private int TableSize;
	private boolean Available;
	private int AvailableSize;
	private ArrayList<Ticket> TicketAtTable;

	public Table(int TableNo, int TableSize) {
		this.TableNo = TableNo;
		this.TableSize = TableSize;
		this.AvailableSize = TableSize;
		this.Available = true;
		this.TicketAtTable = new ArrayList<Ticket>();
	}

	public int getTableNo() {
		return this.TableNo;
	}

	public int getTableSize() {
		return this.TableSize;
	}

	public void setAvailable(boolean Available) {
		this.Available = Available;
	}

	public boolean getAvailable() {
		return this.Available;
	}

	public int getAvailableSize() {
		return this.AvailableSize;
	}

	public void addTicketToTable(Ticket t) {
		this.Available = false;
		AvailableSize -= t.getClientWithTicket().getnPerson();
		this.TicketAtTable.add(t);
	}

	public void clearTable() {
		this.TicketAtTable.clear();
	}
	
	public void removeTicketToTable(Ticket t) {
		this.Available = true;
		AvailableSize += t.getClientWithTicket().getnPerson();
		this.TicketAtTable.remove(t);
	}

	public ArrayList<Ticket> getTicketAtTable() {
		return this.TicketAtTable;
	}

	public void removeTicketToTable(int ticketID) {
		TicketAtTable.removeIf(t -> t.getTicketID() == ticketID);
	}

}
