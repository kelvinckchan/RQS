package AppKickstarter.myHanlderThreads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.Msg;

public class TicketHandler extends AppThread {
	public static List<Queue<Ticket>> TqueueList = new ArrayList<Queue<Ticket>>();
	public static Queue<Ticket> TqueueSize0 = new LinkedList<Ticket>();
	public static Queue<Ticket> TqueueSize1 = new LinkedList<Ticket>();
	public static Queue<Ticket> TqueueSize2 = new LinkedList<Ticket>();
	public static Queue<Ticket> TqueueSize3 = new LinkedList<Ticket>();
	public static Queue<Ticket> TqueueSize4 = new LinkedList<Ticket>();
	private static int ServerForgetItQueueSz;

	public TicketHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		this.ServerForgetItQueueSz = Integer.valueOf(appKickstarter.getProperty("ServerForgetItQueueSz"));
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

	public static synchronized void addTicketToQueue(Ticket t) {
		int size = t.getClientWithTicket().getnPerson();

		if (size > 8) {
			TqueueSize4.add(t);
		} else if (size <= 8 && size > 6) {
			TqueueSize3.add(t);
		} else if (size <= 6 && size > 4) {
			TqueueSize2.add(t);
		} else if (size <= 4 && size > 2) {
			TqueueSize1.add(t);
		} else {
			TqueueSize0.add(t);
		}

	}

	public static boolean Check(int nPerson) {
		boolean bool = false;
		if (nPerson > 8) {
			if (TicketHandler.TqueueSize4.size() >= ServerForgetItQueueSz)
				return true;
		} else if (nPerson <= 8 && nPerson > 6) {
			if (TicketHandler.TqueueSize3.size() >= ServerForgetItQueueSz)
				return true;
		} else if (nPerson <= 6 && nPerson > 4) {
			if (TicketHandler.TqueueSize2.size() >= ServerForgetItQueueSz)
				return true;
		} else if (nPerson <= 4 && nPerson > 2) {
			if (TicketHandler.TqueueSize1.size() >= ServerForgetItQueueSz)
				return true;
		} else {
			if (TicketHandler.TqueueSize0.size() >= ServerForgetItQueueSz)
				return true;
		}

		return bool;
	}

}
