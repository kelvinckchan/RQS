package AppKickstarter.Server;

import java.util.ArrayList;

public class Table {

	private int TableNo;
	private int TableSize;
	private int AvailableSize;
	private ArrayList<Ticket> TicketAtTable;

	public Table(int TableNo, int TableSize) {
		this.TableNo = TableNo;
		this.TableSize = TableSize;
		this.TicketAtTable = new ArrayList<Ticket>();
	}

	public int getTableNo() {
		return this.TableNo;
	}

	public int getTableSize() {
		return this.TableSize;
	}

	public int getAvailableSize() {
		return this.AvailableSize;
	}

	public void addTicketToTable(Ticket t) {
		this.TicketAtTable.add(t);
	}

	public void removeTicketToTable(Ticket t) {
		this.TicketAtTable.remove(t);
	}

	public ArrayList<Ticket> getTicketAtTable() {
		return this.TicketAtTable;
	}

}
