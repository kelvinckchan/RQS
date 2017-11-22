package AppKickstarter.myHanlderThreads;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TableHandler {
	protected AppKickstarter appKickstarter;
	protected MBox mbox = null;
	protected Logger log = null;

	private static ArrayList<Table> TableList;
	private ArrayList<Boolean> Availability;

	// NTables_1=2
	// NTables_2=2
	// NTables_3=2
	// NTables_4=1
	// NTables_5=1
	public TableHandler(AppKickstarter appKickstarter) {
		this.appKickstarter = appKickstarter;
		log = appKickstarter.getLogger();
		createTable();
	}

	public void createTable() {
		String tName = "NTables_";
		TableList = new ArrayList<Table>();

		for (int i = 1; i <= 5; i++) {
			int AmountOfTable = Integer.valueOf(appKickstarter.getProperty(tName + i));
			for (int a = 0; a < AmountOfTable; a++) {
				int tNo = ((i - 1) * 100) + a;
				int tSize = i * 2;
				TableList.add(new Table(tNo, tSize));
				log.fine("Create Table> no:" + tNo + " with Size:" + tSize);
			}
		}

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

	// public void MatchAndCheckIn(Ticket ticket) {
	// Table avaTable = MatchTable(ticket);
	// if (avaTable != null)
	// CheckInTable(ticket, avaTable);
	// }

	public Table MatchTable(Ticket t) {
		ArrayList<Table> AvailbleTable = CheckAvailableTable();
		for (int i = 0; i < AvailbleTable.size(); i++) {
			if (AvailbleTable.get(i).getTableSize() == t.getClientWithTicket().getnPerson()
					|| AvailbleTable.get(i).getTableSize() - 1 == t.getClientWithTicket().getnPerson()) {

				return AvailbleTable.get(i);
			}
		}
		return null;
	}

	int ts;
	String logstring = "";

	public void print() {

		for (ts = 1; ts <= 5; ts++) {
			logstring += ("\nTables[" + (ts - 1) + "]:\t");
			TableList.stream().filter(t -> t.getTableSize() == (ts * 2)).forEach(t -> {
				if (t.getTicketAtTable().size() > 0) {
					String tno = String.valueOf(t.getTableNo());
					for (tno.length(); tno.length() < 4;) {
						tno = 0 + tno;
					}
					logstring += ("[" + tno + ", " + t.getTicketAtTable().get(0).getClientWithTicket().getClientID()
							+ "]\t");
				} else {
					logstring += ("[.................]\t");

				}
			});
		}
		log.info(logstring + "\n");
		log.info("--------------------------------------------------------------");
	}

	public LocalDateTime CheckInTable(Ticket ticket, Table table) {
		ticket.setCheckIn(LocalDateTime.now());
		table.addTicketToTable(ticket);
		TableList.set(findtable(table), table);
		Availability.set(findtable(table), false);
		print();
		return LocalDateTime.now();
	}

	public void HoldTable(Ticket ticket, Table table) {
		Availability.set(findtable(table), false);
	}

	public LocalDateTime CheckOutTable(Ticket ticket) {
		ticket.setCheckOut(LocalDateTime.now());
		Table table = TableList.stream().filter(t -> Objects.equals(t.getTicketAtTable(), ticket)).findFirst().get();
		table.removeTicketToTable(ticket);
		TableList.set(findtable(table), table);
		Availability.set(findtable(table), true);
		print();
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
