package AppKickstarter.ServerObj;

import java.util.ArrayList;

public class Table {

	private int TableNo;
	private int TableSize;
	private int AvailableSize;
	private ArrayList<Ticket> TicketAtTable;

	public Table() {

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

	public ArrayList<Ticket> getTicketAtTable() {
		return this.TicketAtTable;
	}

}
