package AppKickstarter.ServerObj;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class TableManager {
	ArrayList<Table> TableList;
	ArrayList<Boolean> Availability;

	public TableManager(ArrayList<Table> TableList) {
		this.TableList = TableList;
		Availability = new ArrayList<Boolean>();
		for (Table t : this.TableList) {
			Availability.add(true);
		}
	}

	public ArrayList<Table> CheckAvailableTable() {
		ArrayList<Table> result = new ArrayList<Table>();
		for (int i = 0; i < TableList.size(); i++) {
			if (Availability.get(i) == true) {
				result.add(TableList.get(i));
			}
		}
		return result;
	}

	public Table MatchTable(Ticket t, ArrayList<Table> AvailbleTable) {

		for (int i = 0; i < AvailbleTable.size(); i++) {
			if (AvailbleTable.get(i).getTableSize() == t.getClientWithTicket().getnPerson()
					|| AvailbleTable.get(i).getTableSize() - 1 == t.getClientWithTicket().getnPerson()) {

				return AvailbleTable.get(i);
			}
		}
		return null;
	}

	public LocalDateTime CheckInTable(Ticket ticket, Table table) {
		ticket.setCheckIn(LocalDateTime.now());
		table.getTicketAtTable().add(ticket);
		TableList.set(findtable(table), table);
		Availability.set(findtable(table), false);
		return LocalDateTime.now();
	}

	public LocalDateTime CheckOutTable(Ticket ticket, Table table) {
		table.getTicketAtTable().stream().filter(t -> Objects.equals(t.getTicketID(), ticket.getTicketID())).findFirst()
				.get().setCheckOut(LocalDateTime.now());
		TableList.set(findtable(table), table);
		Availability.set(findtable(table), true);
		return LocalDateTime.now();

	}

	private int findtable(Table table) {
		for (int i = 0; i < TableList.size(); i++) {
			if (table.getTableNo() == TableList.get(i).getTableNo()) {
				return i;
			}
		}
		return -1;
	}

}
