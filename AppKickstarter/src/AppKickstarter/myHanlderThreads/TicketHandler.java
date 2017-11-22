package AppKickstarter.myHanlderThreads;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.TicketCall;
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
	TableHandler tableHandler;
	private final int sleepTime = 5000;
	static TicketQueue TicketQueue_0, TicketQueue_1, TicketQueue_2, TicketQueue_3, TicketQueue_4;

	public TicketHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		createTicketQueue();
		this.tableHandler = new TableHandler(this.appKickstarter);
	}

	public void createTicketQueue() {
		String tName = "NTables_";
		this.ServerForgetItQueueSz = Integer.valueOf(appKickstarter.getProperty("ServerForgetItQueueSz"));
		// TicketQueue_0 = new TicketQueue(2, ServerForgetItQueueSz);
		// TicketQueue_0.addObs(new TicketQueueObserver(TicketQueue_0));
		// TicketQueue_1 = new TicketQueue(4, ServerForgetItQueueSz);
		// TicketQueue_1.addObs(new TicketQueueObserver(TicketQueue_1));
		// TicketQueue_2 = new TicketQueue(6, ServerForgetItQueueSz);
		// TicketQueue_2.addObs(new TicketQueueObserver(TicketQueue_2));
		// TicketQueue_3 = new TicketQueue(8, ServerForgetItQueueSz);
		// TicketQueue_3.addObs(new TicketQueueObserver(TicketQueue_3));
		// TicketQueue_4 = new TicketQueue(10, ServerForgetItQueueSz);
		// TicketQueue_4.addObs(new TicketQueueObserver(TicketQueue_4));
		// TqueueList.add(TicketQueue_0);
		// TqueueList.add(TicketQueue_1);
		// TqueueList.add(TicketQueue_2);
		// TqueueList.add(TicketQueue_3);
		// TqueueList.add(TicketQueue_4);
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

			log.info(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {

			case TimesUp:
				TqueueList.forEach(q -> {
					log.info("TqW_" + q.getForTableSize());
					q.getTicketQueue().forEach(t -> {
						log.info("Tiq:" + t.getTicketID());
					});
				});

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

	public static Ticket ReqForTicket(Client reqClient) {
		TicketQueue avaQueue = TqueueList.get((reqClient.getnPerson() - 1) / 2);
		System.out.println(reqClient.getClientID() + " " + reqClient.getnPerson() + " avaQSize: "
				+ avaQueue.getForTableSize() + "> " + avaQueue.getTicketQueue().size());
		if (avaQueue.getTicketQueue().size() < ServerForgetItQueueSz) {
			Ticket t = new Ticket(reqClient);
			try {
				System.out.println(avaQueue.addTicketToQueue(t) + ", " + t.getTicketID() + " Added TtQ["
						+ avaQueue.getForTableSize() + "]" + avaQueue.getTicketQueue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return t;
		}
		return null;
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
			if (status.equals("Add")) {
				Ticket incomingTicket = ((TicketQueue) subject).getTicketQueue().peek();
				log.info(name + ": [" + status + "] Find Table for Ticket> " + incomingTicket.getTicketID());
				Table avaTable = tableHandler.MatchTable(incomingTicket);
				if (avaTable != null) {
					log.info("Found Table & Poll> " + ((TicketQueue) subject).getTicketQueue().poll());
					
					String TicketCalldetail = String.format("TicketCall: %s %s", incomingTicket.getTicketID(),
							avaTable.getTableNo());
					tableHandler.HoldTable(incomingTicket, avaTable);

					appKickstarter.getThread("MsgHandler").getMBox()
							.send(new TicketCall(id, mbox, Msg.Type.TicketCall, TicketCalldetail));
				}

			}
		} // update

		// ------------------------------------------------------------
		// toString
		public String toString() {
			return name;
		} // toString
	}
}
