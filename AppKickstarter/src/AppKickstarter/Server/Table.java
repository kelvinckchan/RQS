package AppKickstarter.Server;

import java.util.ArrayList;

public class Table {
	
	private int TableNo;
	private int TableSize;
	private int AvailableSize;
	private ArrayList<Ticket> TicketAtTable;
	
	public int getTableNo(int TableNo) {
		return this.TableNo;
	}
	
	public int getTableSize(int TableSize) {
		return this.TableSize;
	}
	
	public int AvailableSize(int AvailableSize) {
		return this.AvailableSize;
	}
	
	public ArrayList<Ticket> getTicketAtTable() {
		return this.TicketAtTable;
	}
	

}
