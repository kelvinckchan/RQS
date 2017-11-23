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
				log.fine("Create Table> no:" + String.format("%05d", tNo) + " with Size:" + tSize);
			}
		}

	}

	public static Table MatchAvailableTable(Ticket ticket) {
		Table avaTable = null;

		avaTable = TableList.stream()
				.filter(t -> t.getAvailable() && (t.getTableSize() >= ticket.getClientWithTicket().getnPerson()
						&& t.getTableSize() <= ticket.getClientWithTicket().getnPerson() + 2))
				.findFirst().orElse(null);

		if (avaTable != null) {
	
			return avaTable;
		}
		return null;
	}

	static int ts;
	static String logstring;

	public static void PrintAllTable() {
		logstring = "";
		for (ts = 1; ts <= 5; ts++) {
			logstring += ("\nTables[" + (ts - 1) + "]:\t");
			TableList.stream().filter(t -> t.getTableSize() == (ts * 2)).forEach(t -> {
				if (t.getTicketAtTable().size() > 0) {
					String tno = String.format("%05d", t.getTableNo());
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

	static String logstring2;

	public static void printTicketQueue() {
		logstring2 = "\n";
		TicketHandler.TqueueList.forEach(q -> {
			logstring2 += ("TicketQueue[" + q.getForTableSize()) + "]: ";
			q.getTicketQueue().forEach(t -> {
				logstring2 += (">" + String.format("%05d", t.getTicketID())) + " ";
			});
			logstring2 += "\n";
		});
		log.info(logstring2);
	}

	public static LocalDateTime CheckInWaitingTicketToTable(Ticket TicketWaiting, int TableNo) {
		
		return CheckInTable(TicketWaiting, getTableByTableNo(TableNo));
	}

	public static LocalDateTime CheckInTable(Ticket ticket, Table table) {
//		if (table.getAvailable()) {
			ticket.setCheckIn(LocalDateTime.now());
//			table.setAvailable(false);
//			table.addTicketToTable(ticket);
			TableList.set(FindTableIndex(table), table);
			// Availability.set(FindTableInTableList(table), false);
			
			PrintAllTable();
			return LocalDateTime.now();
//		}
//		return null;
	}

	public static void HoldTable(Ticket ticket, Table table) {
		table.setAvailable(false);
		table.addTicketToTable(ticket);
		TableList.set(FindTableIndex(table), table);
	}

	public static void UnHoldTable(int ticketID) {
		System.out.println("unhold> " + ticketID);
		Table tableHeldByTicket = TableList.stream()
				.filter(t -> t.getTicketAtTable().size() > 0 && t.getTicketAtTable().get(0).getTicketID() == ticketID)
				.findFirst().orElse(null);
		if (tableHeldByTicket != null) {
			tableHeldByTicket.setAvailable(true);
			tableHeldByTicket.removeTicketToTable(ticketID);
			TableList.set(FindTableIndex(tableHeldByTicket), tableHeldByTicket);
		}
	}

	public static LocalDateTime CheckOutTable(int TableNo, int totalSpending) {
		TotalSpending += totalSpending;
		Table table = getTableByTableNo(TableNo);
		Ticket ticketAtTable = table.getTicketAtTable().size() > 0 ? table.getTicketAtTable().get(0) : null;
		if (ticketAtTable != null) {
			ticketAtTable.setCheckOut(LocalDateTime.now());
			table.setAvailable(true);
			table.removeTicketToTable(ticketAtTable);
			table.clearTable();
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
