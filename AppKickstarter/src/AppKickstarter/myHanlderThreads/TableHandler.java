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
import AppKickstarter.timer.Timer;

/**
 * TableHandler Class, for handling the Table
 * 
 * @author
 * @version 1.0
 */
public class TableHandler extends AppThread {

	private static ArrayList<Table> TableList;
	private static int TotalSpending;
	private int mode = appKickstarter.getMode();
	private static int KickOutTime = 30 * 1000;
	private static int PrintTime = 3600;
	private static int PrintTimerID = 20000;

	/**
	 * This constructs TableHandler with id and appKickstarter
	 * 
	 * @param id:
	 *            Thread Name
	 * @param appKickstarter:
	 */
	public TableHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		createTable();
	}

	@Override
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();
			switch (msg.getType()) {
			case TimesUp:
				int timerID;
				try {
					timerID = Integer.parseInt(msg.getDetails().substring(1, 6));
					log.fine("NTimerID: " + timerID);
				} catch (NumberFormatException e) {
					timerID = Integer.parseInt(msg.getDetails().substring(1, 5));
				}
				// TimesUP For PrintALLTable
				if (timerID == PrintTimerID) {
					PrintAllTable();
					Timer.setSimulationTimer(id, mbox, PrintTime, PrintTimerID);
				} else {
					if (mode == 1) {
						WaitedTooLongForCheckOut(timerID);
					}
				}
				break;
			default:
				log.severe(id + ": unknown message type!!");
				break;
			}
		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");

	}

	/**
	 * This Method for creating Table when Server start
	 * 
	 */
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

	/**
	 * CheckOut the Table when WaitedTooLongForCheckOut
	 */
	public void WaitedTooLongForCheckOut(int timerID) {
		int TableNo = timerID;
		CheckOutTable(TableNo, 200);
		log.fine(id + ": CLient in Table> " + TableNo + " ate too Long, Kick out.");
	}

	/**
	 * Match Ticket with Available Table according to nPerson and Table Size and
	 * Table Availability
	 * 
	 * @param Ticket
	 *            : ticket
	 * @return Table
	 * 
	 */
	public static Table MatchAvailableTable(Ticket ticket) {
		Table avaTable = TableList.stream()
				.filter(table -> table.getState().equals("Available")
						&& (table.getTableSize() >= ticket.getClientWithTicket().getnPerson()
								&& table.getTableSize() <= ticket.getClientWithTicket().getnPerson() + 3))
				.findFirst().orElse(null);

		if (avaTable != null) {
			return avaTable;
		}

		return null;
	}

	static int ts;
	static String logstring;

	/**
	 * This Method is for Printing the Table in the log
	 */
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
		PrintTicketQueue();
		log.info("--------------------------------------------------------------");
	}

	static String logstring2;

	/**
	 * This Method is for Printing the Ticket Queue in the log
	 */
	public static void PrintTicketQueue() {
		logstring2 = "\n";
		TicketHandler.TqueueList.forEach(q -> {
			logstring2 += ("TicketQueue[" + q.getForTableSize()) + "] (" + q.getTicketQueue().size() + "): ";
			q.getTicketQueue().forEach(t -> {
				logstring2 += (">" + String.format("%05d", t.getTicketID())) + " ";
			});
			logstring2 += "\n";
		});
		log.info(logstring2);
	}

	/**
	 * This method is for holding table when TicketCall is sent to the client, set
	 * the table state to Hold and add Ticket to the Table
	 * 
	 * @param Ticket
	 *            : ticket
	 * @param Table
	 *            : table
	 * 
	 */

	public static void HoldTable(Ticket ticket, Table table) {
		log.fine("Hold> Tid=" + ticket.getTicketID() + " TableNo=" + table.getTableNo());
		if (table.getState().equals("Available")) {
			table.setAvailable(false);
			table.addTicketToTable(ticket);
			table.setHoldState();
			TableList.set(FindTableIndex(table), table);
		}
	}

	/**
	 * This method is for unholding table when the server waited too long for the
	 * TicketAck, set the table state to Available and remove Ticket to the Table
	 * 
	 * @param Ticket
	 *            : ticket
	 * @param Table
	 *            : table
	 * 
	 */
	public static void UnHoldTable(int ticketID) {
		log.fine("Unhold> Tid=" + ticketID);
		Table tableHeldByTicket = TableList.stream()
				.filter(t -> t.getTicketAtTable().size() > 0 && t.getTicketAtTable().get(0).getTicketID() == ticketID)
				.findFirst().orElse(null);
		if (tableHeldByTicket != null && tableHeldByTicket.getState().equals("Hold")) {
			tableHeldByTicket.setAvailable(true);
			tableHeldByTicket.setAvailableState();
			tableHeldByTicket.removeTicketToTable(ticketID);
			TableList.set(FindTableIndex(tableHeldByTicket), tableHeldByTicket);
		}
	}

	/**
	 * This Check in the waiting ticket to table
	 * 
	 * @param TicketWaiting
	 *            : Ticket
	 * @param TableNo
	 *            : TableNo
	 * @return Check in Time
	 * 
	 */
	public LocalDateTime CheckInWaitingTicketToTable(Ticket TicketWaiting, int TableNo) {
		return CheckInTable(TicketWaiting, getTableByTableNo(TableNo));
	}

	/**
	 * This Check in the waiting ticket to table, Set table state to CheckedIn
	 * 
	 * @param TicketWaiting
	 *            : Ticket
	 * @param Table
	 *            : table
	 * @return Check in Time
	 * 
	 */
	public LocalDateTime CheckInTable(Ticket ticket, Table table) {
		// if (table.getAvailable()) {
		ticket.setCheckIn(LocalDateTime.now());
		// table.setAvailable(false);
		// table.addTicketToTable(ticket);
		table.setCheckedInState();
		TableList.set(FindTableIndex(table), table);
		PrintAllTable();
		Timer.setSimulationTimer(id, mbox, KickOutTime, table.getTableNo());
		return LocalDateTime.now();
		// }
		// return null;
	}

	/**
	 * This Check out the waiting ticket to table, if the table is checkedin, set
	 * the table state to available, remove the ticket at the table, add the
	 * totalspending
	 * 
	 * @param int
	 *            : TableNo
	 * @param int
	 *            : totalSpending
	 * @return Check out Time
	 * 
	 */
	public static LocalDateTime CheckOutTable(int TableNo, int totalSpending) {
		Table table = getTableByTableNo(TableNo);
		if (table.getState() == "CheckedIn") {
			Ticket ticketAtTable = table.getTicketAtTable().size() > 0 ? table.getTicketAtTable().get(0) : null;
			if (ticketAtTable != null) {
				ticketAtTable.setCheckOut(LocalDateTime.now());
				table.setAvailable(true);
				table.setAvailableState();
				table.removeTicketToTable(ticketAtTable);
				table.clearTable();
			}
			TableList.set(FindTableIndex(table), table);
			TotalSpending += totalSpending;
			log.info("Checked Out Table> " + TableNo + " TotalSpending: $" + TotalSpending);
			return LocalDateTime.now();
		}
		return null;
	}

	/**
	 * Get the Table in TableList by the TableNo
	 * 
	 * @param int
	 *            : TableNo
	 * @return Table
	 * 
	 */
	public static Table getTableByTableNo(int TableNo) {
		return TableList.stream().filter(t -> Objects.equals(t.getTableNo(), TableNo)).findFirst().get();
	}

	/**
	 * Get the Table index in TableList
	 * 
	 * @param Table
	 *            : table
	 * @return index
	 * 
	 */

	private static int FindTableIndex(Table table) {
		for (int i = 0; i < TableList.size(); i++) {
			if (table.getTableNo() == TableList.get(i).getTableNo()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Get the TableHandler.TableList
	 * 
	 * @return ArrayList
	 *         <Table>
	 *         TableList
	 * 
	 */
	public static ArrayList<Table> getTableList() {
		return TableList;
	}

}
