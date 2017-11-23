package AppKickstarter.myHanlderThreads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
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
	private final int sleepTime = 10;
	private static Queue<Ticket> WaitForAckTicketQueue = new LinkedList<Ticket>();

	private int TicketAckWaitingTime;

	public TicketHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		createTicketQueue();

		this.TicketAckWaitingTime = Integer.valueOf(appKickstarter.getProperty("TicketAckWaitingTime"));
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
		Timer.setSimulationTimer(id, mbox, sleepTime, 9999);

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();

			// log.info(id + ": message received: [" + msg + "].");
			switch (msg.getType()) {
			case TicketRep:
				TicketRep ticketRep = (TicketRep) msg.getCommand();
				Ticket ticketReped = ticketRep.getTicket();

				// MatchAllTicketQueue();

				break;
			case TimesUp:
				// MatchAllTicketQueue();
				// Timer.setTimer(id, mbox, sleepTime);
				if (Integer.valueOf(msg.getDetails().substring(2, 6)) == 9999) {
					MatchAllTicketQueue();
					Timer.setSimulationTimer(id, mbox, sleepTime, 9999);
				} else {
					log.info(id + ": TimesUP! from>" + msg.getSender() + "> " + msg.getDetails());
					int ticketID = Integer.valueOf(msg.getDetails().substring(2, 6));
					boolean TicketWaitingRemoved = removeFromWaitForAckTicketQueue(ticketID);
					log.info(id + ": Tid=" + ticketID + " Removed> " + TicketWaitingRemoved);
					// Waited too long for TicketAck... Remove Ticket from
					// TicketHandler.WaitForAckTicketQueue
					// UnHold Table
					MatchAllTicketQueue();
					TableHandler.UnHoldTable(ticketID);
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

	public static Ticket GetTicketAndAddToTicketQueueIfQueueNotFull(Client reqClient) throws InterruptedException {
		TicketQueue ticketQueue = TqueueList.get((reqClient.getnPerson() - 1) / 2);

		if (ticketQueue.getTicketQueue().size() < ServerForgetItQueueSz) {
			Ticket t = new Ticket(reqClient);
			ticketQueue.addTicketToQueue(t);
			return t;
		}

		return null;
	}

	public static Queue<Ticket> getWaitForAckTicketQueue() {
		return WaitForAckTicketQueue;
	}

	public boolean removeFromWaitForAckTicketQueue(int TicketID) {
		log.info("removeFromWaitForAckTicketQueue");
		WaitForAckTicketQueue.forEach(t -> log.info(TicketID + "< >tid=" + t.getTicketID()));

		return WaitForAckTicketQueue.removeIf(tc -> tc.getTicketID() == TicketID);
	}

	public static Ticket FindWaitingTicketAndPoll(int TicketId) {
		Ticket tc = WaitForAckTicketQueue.stream().filter(t -> Objects.equals(t.getTicketID(), TicketId)).findFirst()
				.orElse(null);
		if (tc != null)
			WaitForAckTicketQueue.remove(tc);
		return tc;
	}

	public void MatchAllTicketQueue() {
		for (TicketQueue tq : TqueueList) {
			MatchTicketInQueueWithTable(tq);
		}
	}

	public void MatchTicketInQueueWithTable(TicketQueue ticketqueue) {
		// Find Table For Ticket
		Table avaTable = null;
		for (Ticket incomingTicket : ticketqueue.getTicketQueue()) {
			avaTable = TableHandler.MatchAvailableTable(incomingTicket);
			if (avaTable != null) {
				// If Found, Create TicketCall and Sent,
				// Poll Ticket From TicketQueue
				// Put Ticket in WaitForAckTicketQueue
				Ticket WaitForAckTicket = incomingTicket;
				ticketqueue.removeTicketFromQueue(incomingTicket);
				TicketCall tickCall = new TicketCall(WaitForAckTicket, avaTable);

				WaitForAckTicketQueue.add(tickCall.getTicket());

				log.info("Found Table & Poll TicketQueue> Tid=" + WaitForAckTicket.getTicketID() + ", TableNo="
						+ avaTable.getTableNo());
				appKickstarter.getThread("SocketOutHandler").getMBox()
						.send(new Msg(id, mbox, Msg.Type.TicketCall, tickCall));
				TableHandler.HoldTable(WaitForAckTicket, avaTable);
				Timer.setSimulationTimer(id, mbox, TicketAckWaitingTime, WaitForAckTicket.getTicketID());
				log.info(id + ": SetTimer>  TimerID=" + WaitForAckTicket.getTicketID());

				log.info(id + ": TicketCall Sent> Tid=" + WaitForAckTicket.getTicketID() + " Wait For TickerAck");

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
				// MatchTicketQueue(((TicketQueue) subject));
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
