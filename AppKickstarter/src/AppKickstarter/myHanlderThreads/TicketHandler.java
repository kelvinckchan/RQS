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

/**
 * TicketHandler Class, for handling the Ticket Queue
 * 
 * @author
 * @version 1.0
 */
public class TicketHandler extends AppThread {
	public static List<TicketQueue> TqueueList = new ArrayList<TicketQueue>();
	private static int ServerForgetItQueueSz;
	private final int sleepTime = 10;
	private int TimerIDForMatchTicketQueue;
	private static Queue<Ticket> WaitForAckTicketQueue = new LinkedList<Ticket>();
	private int TicketAckWaitingTime;
	private int mode = appKickstarter.getMode();

	/**
	 * This constructs TicketHandler with id and appKickstarter
	 * 
	 * @param id:
	 *            Thread Name
	 * @param appKickstarter:
	 */
	public TicketHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		createTicketQueue();

		this.TicketAckWaitingTime = Integer.valueOf(appKickstarter.getProperty("TicketAckWaitingTime"));
		this.TimerIDForMatchTicketQueue = appKickstarter.getTimerIDForMatchTicketQueue();
	}

	/**
	 * This Method for creating TicketQueue when Server start
	 */
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

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();
			switch (msg.getType()) {
			case TimesUp:
				int timerID;
				try {
					timerID = Integer.parseInt(msg.getDetails().substring(1, 6));
					log.fine("NTimerID: " + timerID);
				} catch (NumberFormatException e) {
					timerID = Integer.valueOf(msg.getDetails().substring(1, 5));
				}
				if (timerID != TimerIDForMatchTicketQueue) {
					if (mode == 1) {
						WaitedTooLongForTicketAck(msg, timerID);
					}
				}
				MatchAllTicketQueue();
				log.fine(id + ": Set Next Timer.");
				Timer.setSimulationTimer(id, mbox, sleepTime, TimerIDForMatchTicketQueue);

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
	 * Waited too long for TicketAck, Remove Ticket from
	 * TicketHandler.WaitForAckTicketQueue and UnHold Table
	 */

	public void WaitedTooLongForTicketAck(Msg msg, int timerID) {
		log.fine(id + ": TimesUP! from>" + msg.getSender() + "> " + msg.getDetails());
		int ticketID = timerID;
		boolean TicketWaitingRemoved = removeFromWaitForAckTicketQueue(ticketID);
		log.fine(id + ": Tid=" + ticketID + " Removed> " + TicketWaitingRemoved);
		TableHandler.UnHoldTable(ticketID);
	}

	/**
	 * Get Ticket And Add To Ticket Queue If Queue Not Full
	 * 
	 * @param Client
	 *            : reqClient
	 * @return Ticket
	 * 
	 */
	public static Ticket GetTicketAndAddToTicketQueueIfQueueNotFull(Client reqClient) throws InterruptedException {
		TicketQueue ticketQueue = TqueueList.get((reqClient.getnPerson() - 1) / 2);
		if (ticketQueue.getTicketQueue().size() < ServerForgetItQueueSz) {
			Ticket t = new Ticket(reqClient);
			ticketQueue.addTicketToQueue(t);
			return t;
		}
		return null;
	}

	/**
	 * Get TicketHandler.WaitForAckTicketQueue
	 * 
	 * @return Queue<Ticket> WaitForAckTicketQueue
	 * 
	 */
	public static Queue<Ticket> getWaitForAckTicketQueue() {
		return WaitForAckTicketQueue;
	}

	/**
	 * remove Ticket From WaitForAckTicketQueue
	 * 
	 * @param int
	 *            : TicketID
	 * 
	 * @return true if Removed
	 * 
	 */
	public boolean removeFromWaitForAckTicketQueue(int TicketID) {
		log.info("removeFromWaitForAckTicketQueue");
		WaitForAckTicketQueue.forEach(t -> log.info(TicketID + "< >tid=" + t.getTicketID()));
		boolean removed;
		try {
			removed = WaitForAckTicketQueue.removeIf(tc -> tc.getTicketID() == TicketID);
		} catch (Exception e) {
			removed = false;
		}
		return removed;
	}

	/**
	 * Find Waiting Ticket And Poll From TicketHandler.WaitForAckTicketQueue
	 * 
	 * @param int
	 *            : TicketId
	 * 
	 * @return Ticket
	 * 
	 */
	public static Ticket FindWaitingTicketAndPoll(int TicketId) {
		Ticket tc = WaitForAckTicketQueue.stream().filter(t -> Objects.equals(t.getTicketID(), TicketId)).findFirst()
				.orElse(null);
		if (tc != null)
			WaitForAckTicketQueue.remove(tc);
		return tc;
	}

	/** MatchAllTicketQueue */
	public void MatchAllTicketQueue() {
		log.finer("Match All TicketQueue.");
		for (TicketQueue tq : TqueueList) {
			MatchTicketInQueueWithTable(tq);
		}
	}

	/**
	 * Find Table For Ticket, Match Ticket In Queue With Table. If Available Table
	 * is Found, Create TicketCall and Sent, Poll Ticket From TicketQueue Put Ticket
	 * in WaitForAckTicketQueue. Hold the Table for the Matched Ticket. Set Timer
	 * For Waiting TicketAck
	 * 
	 */
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

				log.fine("Found Table & Poll TicketQueue> Tid=" + WaitForAckTicket.getTicketID() + ", TableNo="
						+ avaTable.getTableNo());
				if (mode == 1) {
					appKickstarter.getThread("SocketOutHandler").getMBox()
							.send(new Msg(id, mbox, Msg.Type.TicketCall, tickCall));
				} else if (mode == 2) {
					appKickstarter.getThread("GuiStaffPanel").getMBox()
							.send(new Msg(id, mbox, Msg.Type.TicketCall, tickCall));
				}

				TableHandler.HoldTable(WaitForAckTicket, avaTable);
				Timer.setSimulationTimer(id, mbox, TicketAckWaitingTime, WaitForAckTicket.getTicketID());
				log.fine(id + ": SetTimer>  TimerID=" + WaitForAckTicket.getTicketID());
				log.info(id + ": TicketCall Sent> Tid=" + WaitForAckTicket.getTicketID() + " Wait For TickerAck");
			} else {
				if (incomingTicket.getWaitedTooLong() && mode == 1) {
					ticketqueue.removeTicketFromQueue(incomingTicket);
					log.fine("Ticket Waited Too Long> " + incomingTicket.getTicketID() + " remove From Queue");
				} else {
					log.finer("No Table For> Tid=" + incomingTicket.getTicketID() + " Cid="
							+ incomingTicket.getClientWithTicket().getClientID());
				}
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
