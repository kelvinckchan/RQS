package AppKickstarter.myHanlderThreads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.Server.TicketQueue;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.Msg;

public class TicketHandler extends AppThread {
	public static List<TicketQueue> TqueueList;
	private static int ServerForgetItQueueSz;

	public TicketHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		createTicketQueue();
	}

	public void createTicketQueue() {
		String tName = "NTables_";
		this.ServerForgetItQueueSz = Integer.valueOf(appKickstarter.getProperty("ServerForgetItQueueSz"));
		TqueueList = new ArrayList<TicketQueue>();
		TqueueList.add(new TicketQueue(2, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(4, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(6, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(8, ServerForgetItQueueSz));
		TqueueList.add(new TicketQueue(10, ServerForgetItQueueSz));
		log.fine("Create TqueueList> ");

	}

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

	}

	public static Ticket ReqForTicket(Client reqClient) {
		TicketQueue avaQueue = getTicketQueueTicketReq(reqClient);
		if (avaQueue != null) {
			Ticket t = new Ticket(reqClient);
			avaQueue.addTicketToQueue(t);
			return t;
		}
		return null;
	}

	public static TicketQueue getTicketQueueTicketReq(Client reqClient) {
		TicketQueue q = TqueueList.get((reqClient.getnPerson() - 1) / 2);
		if (q.getTicketQueue().size() < ServerForgetItQueueSz) {
			return q;
		}
		return null;
	}

}
