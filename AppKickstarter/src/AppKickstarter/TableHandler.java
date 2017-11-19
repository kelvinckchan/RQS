package AppKickstarter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TableHandler {

	ArrayList<Table> TableList;
	ArrayList<Boolean> Availability;
	ArrayList<Table> result;

	private ArrayList<Integer> TableSize0 = new ArrayList<Integer>(10);
	private ArrayList<Integer> TableSize1 = new ArrayList<Integer>(10);
	private ArrayList<Integer> TableSize2 = new ArrayList<Integer>(8);
	private ArrayList<Integer> TableSize3 = new ArrayList<Integer>(2);
	private ArrayList<Integer> TableSize4 = new ArrayList<Integer>(2);
	
	public TableHandler(ArrayList<Table> TableList) {
		this.TableList = TableList;
		Availability = new ArrayList<Boolean>();
		for (Table x : this.TableList) {
			Availability.add(true);
		}
	}

	
	public ArrayList<Table> CheckAvailableTable() {
		result = new ArrayList<Table>();
		for (int i = 0; i < TableList.size(); i++) {
			if (Availability.get(i) == true) {
				result.add(TableList.get(i));
			}
		}
		return result;
	}

	public Table MatchTable(Ticket t, ArrayList<Table> AvailbleTable) {

		for (int i = 0; i < AvailbleTable.size(); i++) {
//			if (AvailbleTable.get(i).g == t.size || AvailbleTable.get(i).TableSize - 1 == t.size
//					|| AvailbleTable.get(i).TableSize - 2 == t.size) {
//
//				return AvailbleTable.get(i);
//			}
		}
		return null;
	}

	public LocalDateTime CheckInTable(Ticket ticket, Table table) {
		ticket.setCheckIn(LocalDateTime.now());
		table.InTable.add(ticket);
		TableList.set(findtable(table), table);
		Availability.set(findtable(table), false);
		return LocalDateTime.now();
	}

	public LocalDateTime CheckOutTable(Ticket ticket, Table table) {
		table.InTable.getLast().setCheckOut(LocalDateTime.now());
		TableList.set(findtable(table), table);
		Availability.set(findtable(table), true);
		return LocalDateTime.now();

	}

	private int findtable(Table table) {
		for (int i = 0; i < TableList.size(); i++) {
//			if (table.getTableNo(i) == TableList.get(i).getTableNo(i)) {
//				return i;
//			}
		}
		return -1;
	}

}
