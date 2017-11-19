package AppKickstarter;

import java.util.ArrayList;
import java.util.LinkedList;

public class Table {
	private int TableId;
	private int TableSize;
	private int AvailableSize;

	private int[] FixedTableSize = { 10, 10, 8, 2, 2 };

	private int[] TableSize0;
	private int[] TableSize1;
	private int[] TableSize2;
	private int[] TableSize3;
	private int[] TableSize4;

	LinkedList<Ticket> InTable;
	private ArrayList<Ticket> TicketAtTable;
	// NTables: [ 10 10 8 2 2 ]

	public void createTable() {
		String name;
		
		for (int i : FixedTableSize) {
			name = "TableSize" + 0;
			System.out.println("********************* "+i);
			System.out.println("********************* Name "+name);
		}

	}

	public int getTableNo() {
		return this.TableId;
	}

	public int getTableSize() {
		return this.TableSize;
	}

	public int AvailableSize(int AvailableSize) {
		return this.AvailableSize;
	}

	public ArrayList<Ticket> getTicketAtTable() {
		return this.TicketAtTable;
	}

	public Table(int TableId, int AvailableSize, int TableSize) {
		InTable = new LinkedList<Ticket>();
	}
}
