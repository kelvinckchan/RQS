package AppKickstarter.myHanlderThreads;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.TicketCall;
import AppKickstarter.Msg.TicketRep;
import AppKickstarter.Msg.TicketReq;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Observer;
import AppKickstarter.Server.Subject;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.Server.TicketQueue;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;

public class TicketHandler extends AppThread {
	public static List<TicketQueue> TqueueList = new ArrayList<TicketQueue>();
	private static int ServerForgetItQueueSz;
	static TableHandler tableHandler;
	private final int sleepTime = 10;
	private static BlockingQueue<TicketCall> WaitForAckTicketQueue = new LinkedBlockingQueue<TicketCall>();

	public TicketHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		createTicketQueue();
		this.tableHandler = new TableHandler("TableHandler", this.appKickstarter);

	}

	public void createTicketQueue() {
		String tName = "NTables_";
		this.ServerForgetItQueueSz = Integer.valueOf(appKickstarter.getProperty("ServerForgetItQueueSz"));
		TqueueList.add(new TicketQueue(2, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(4, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(6, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(8, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(10, ServerForgetItQueueSz));
		TqueueList.forEach(q -> {
			q.addObs(new TicketQueueObserver(q));
		});

		log.fine("Create TqueueList> ");
	}

	@Override
	public void run() {
		log.info(id + ": starting...");
		Timer.setTimer(id, mbox, sleepTime);
		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();

			// log.info(id + ": message received: [" + msg + "].");
			switch (msg.getType()) {

			case TimesUp:
				MatchAllTicketQueue();
				Timer.setTimer(id, mbox, sleepTime);
				break;

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

	}

	// public static void MatchTicketForSize(int TableSize) {
	// TicketQueue ticketqueue = TqueueList.get((TableSize - 1) / 2);
	// Table avaTable;
	// for (Ticket incomingTicket : ticketqueue.getTicketQueue()) {
	// avaTable = tableHandler.MatchAvailableTable(incomingTicket);
	// if (avaTable != null) {
	// Ticket WaitForAckTicket = incomingTicket;
	// ticketqueue.removeTicketFromQueue(incomingTicket);
	// TicketCall tickCall = new TicketCall(WaitForAckTicket, avaTable);
	//
	// try {
	// WaitForAckTicketQueue.put(tickCall);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// appKickstarter.getThread("MsgHandler").getMBox().send(new Msg(id, mbox,
	// Msg.Type.TicketCall, tickCall));
	// tableHandler.print();
	// }
	// }
	// }

	public static Ticket ReqForTicket(Client reqClient) throws InterruptedException {
		TicketQueue ticketQueue = TqueueList.get((reqClient.getnPerson() - 1) / 2);
		// System.out.println(reqClient.getClientID() + " " + reqClient.getnPerson() + "
		// avaQSize: "
		// + avaQueue.getForTableSize() + "> " + avaQueue.getTicketQueue().size());
		if (ticketQueue.getTicketQueue().size() < ServerForgetItQueueSz) {
			Ticket t = new Ticket(reqClient);
			ticketQueue.addTicketToQueue(t);

			return t;
		}
		return null;
	}

	public static BlockingQueue<TicketCall> getWaitForAckTicketQueue() {
		return WaitForAckTicketQueue;
	}

	public static void removeFromWaitForAckTicketQueue(int TicketID) {
		WaitForAckTicketQueue.removeIf(tc -> tc.getTicket().getTicketID() == TicketID);
	}

	public static TicketCall FindWaitingTicketAndPoll(int TicketId) {
		TicketCall tc = WaitForAckTicketQueue.stream()
				.filter(t -> Objects.equals(t.getTicket().getTicketID(), TicketId)).findFirst().orElse(null);
		if (tc != null)
			WaitForAckTicketQueue.remove(tc);
		return tc;
	}

	public void MatchAllTicketQueue() {
		for (TicketQueue tq : TqueueList) {
			MatchTicketQueue(tq);
		}
	}

	public void MatchTicketQueue(TicketQueue ticketqueue) {
		// Find Table For Ticket
		Table avaTable = null;
		for (Ticket incomingTicket : ticketqueue.getTicketQueue()) {
			avaTable = tableHandler.MatchAvailableTable(incomingTicket);
			if (avaTable != null) {
				// If Found, Create TicketCall and Sent,
				// Poll Ticket From TicketQueue
				// Put Ticket in WaitForAckTicketQueue
				Ticket WaitForAckTicket = incomingTicket;
				ticketqueue.removeTicketFromQueue(incomingTicket);
				TicketCall tickCall = new TicketCall(WaitForAckTicket, avaTable);

				try {
					WaitForAckTicketQueue.put(tickCall);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				log.info("Found Table & Poll TicketQueue> Tid=" + WaitForAckTicket);
				appKickstarter.getThread("MsgHandler").getMBox().send(new Msg(id, mbox, Msg.Type.TicketCall, tickCall));
				tableHandler.print();
			} else {
				// log.info("No Table Available For > Tid=" + incomingTicket.getTicketID());
			}
		}
	}

	private class TicketQueueObserver extends Observer {
		private final String name = "TicketQueueObserver";

		// ------------------------------------------------------------
		// TicketQueueObserver
		public TicketQueueObserver(Subject subject) {
			super(subject);
		} // TicketQueueObserver

		// ------------------------------------------------------------
		// update
		public void update() {
			String status = subject.getStatus();
			// When Ticket Added To TicketQueue
			if (status.equals("Add")) {
				MatchTicketQueue(((TicketQueue) subject));
				log.info(name + ": [" + status + "] Find Table for Ticket> ");
			}
		} // update

		// ------------------------------------------------------------
		// toString
		public String toString() {
			return name;
		} // toString
	}

}
