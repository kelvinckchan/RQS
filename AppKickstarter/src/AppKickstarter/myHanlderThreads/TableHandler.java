package AppKickstarter.myHanlderThreads;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TableHandler extends AppThread {

	private static ArrayList<Table> TableList;
	// private static ArrayList<Boolean> Availability;
	private static int TotalSpending;

	public TableHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		this.appKickstarter = appKickstarter;
		createTable();
	}

	@Override
	public void run() {

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

		// Availability = new ArrayList<Boolean>();
		// for (Table t : this.TableList) {
		// Availability.add(true);
		// }
	}

	public Table MatchAvailableTable(Ticket ticket) {
		Table avaTable = null;
		avaTable = TableList.stream()
				.filter(t -> t.getAvailable() && (t.getTableSize() == ticket.getClientWithTicket().getnPerson()
						|| t.getTableSize() == ticket.getClientWithTicket().getnPerson() - 1))
				.findFirst().orElse(null);
		if (avaTable != null) {
			HoldTable(ticket, avaTable);
			return avaTable;
		}
		return null;
	}

	// public ArrayList<Table> CheckAvailableTable() {
	// // ArrayList<Table> result = new ArrayList<Table>();
	// // for (int i = 0; i < TableList.size(); i++) {
	// // if (Availability.get(i)) {
	// // result.add(TableList.get(i));
	// // }
	// // }
	// return TableList.stream().filter(t ->
	// t.getAvailable()).collect(Collectors.toCollection(ArrayList<Table>::new));
	// }
	//
	// public Table MatchTable(Ticket t) {
	// ArrayList<Table> AvailbleTable = CheckAvailableTable();
	//
	// for (int i = 0; i < AvailbleTable.size(); i++) {
	// if (AvailbleTable.get(i).getTableSize() ==
	// t.getClientWithTicket().getnPerson()
	// || AvailbleTable.get(i).getTableSize() - 1 ==
	// t.getClientWithTicket().getnPerson()) {
	//
	// return AvailbleTable.get(i);
	// }
	// }
	// return null;
	// }

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
					if (t.getTicketAtTable().get(0) != null)
						logstring += ("[" + tno + ", " + t.getTicketAtTable().get(0).getClientWithTicket().getClientID()
								+ "]\t");
				} else {
					logstring += ("[.................]\t");

				}
			});
		}
		log.info(logstring + "\n");
		printTicketQueue();
		log.info("--------------------------------------------------------------");
	}

	String logs;

	public void printTicketQueue() {
		logs = "\n";
		TicketHandler.TqueueList.forEach(q -> {
			logs += ("TicketQueue[" + q.getForTableSize()) + "]: ";
			q.getTicketQueue().forEach(t -> {
				logs += (">" + t.getTicketID()) + " ";
			});
			logs += "\n";
		});
		log.info(logs);
	}

	public static LocalDateTime CheckInWaitingTicketToTable(Ticket TicketWaiting, int TableNo) {
		return CheckInTable(TicketWaiting, getTableByTableNo(TableNo));
	}

	public static LocalDateTime CheckInTable(Ticket ticket, Table table) {
		ticket.setCheckIn(LocalDateTime.now());

		// Availability.set(FindTableInTableList(table), false);
		return LocalDateTime.now();
	}

	public void HoldTable(Ticket ticket, Table table) {
		table.setAvailable(false);
		table.addTicketToTable(ticket);
		TableList.set(FindTableIndex(table), table);
	}

	public static void UnHoldTable(int ticketID) {
		System.out.println("unhold> " + ticketID);
		Table table = TableList.stream()
				.filter(t -> t.getTicketAtTable().size() > 0 && t.getTicketAtTable().get(0).getTicketID() == ticketID)
				.findFirst().orElse(null);
		if (table != null) {
			table.setAvailable(true);
			table.removeTicketToTable(ticketID);
			TableList.set(FindTableIndex(table), table);
		}
	}

	public static LocalDateTime CheckOutTable(int TableNo, int totalSpending) {
		TotalSpending += totalSpending;
		Table table = getTableByTableNo(TableNo);
		Ticket ticketAtTable = ticketAtTable = table.getTicketAtTable().size() > 0 ? table.getTicketAtTable().get(0)
				: null;
		if (ticketAtTable != null) {
			ticketAtTable.setCheckOut(LocalDateTime.now());
			table.removeTicketToTable(ticketAtTable);
		}
		TableList.set(FindTableIndex(table), table);
		// Availability.set(FindTableInTableList(table), true);

		// TicketHandler.MatchTicketForSize(table.getTableSize());

		return LocalDateTime.now();
	}

	public static Table getTableByTableNo(int TableNo) {
		return TableList.stream().filter(t -> Objects.equals(t.getTableNo(), TableNo)).findFirst().get();
	}

	private static int FindTableIndex(Table table) {
		for (int i = 0; i < TableList.size(); i++) {
			if (table.getTableNo() == TableList.get(i).getTableNo()) {
				return i;
			}
		}
		return -1;
	}

}
