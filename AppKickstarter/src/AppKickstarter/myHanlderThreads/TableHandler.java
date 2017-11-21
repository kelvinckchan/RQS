package AppKickstarter.myHanlderThreads;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.Msg;

public class TableHandler extends AppThread {

	private static ArrayList<Table> TableList;
	private ArrayList<Boolean> Availability;

	public TableHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
	}
	// NTables_1=2
	// NTables_2=2
	// NTables_3=2
	// NTables_4=1
	// NTables_5=1

	@Override
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();

			log.info(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {
			case Hello:
				log.info(id + ": " + msg.getSender() + " is saying Hello to me!!!");
				msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is Thread B!"));
				break;

			case Terminate:
				quit = true;
				break;

			default:
				log.severe(id + ": unknown message type!!");
				break;
			}
		}

		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");

	} // run

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

	public LocalDateTime CheckInTable(Ticket ticket, Table table) {
		ticket.setCheckIn(LocalDateTime.now());
		table.addTicketToTable(ticket);
		TableList.set(findtable(table), table);
		Availability.set(findtable(table), false);
		return LocalDateTime.now();
	}

	public LocalDateTime CheckOutTable(Ticket ticket, Table table) {
		ticket.setCheckOut(LocalDateTime.now());
		table.removeTicketToTable(ticket);
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
