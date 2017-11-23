package AppKickstarter.Server;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TicketQueue extends Subject {
	private String status = "";
	private BlockingQueue<Ticket> ticketQueue;
	private int ForTableSize;
	private int ServerForgetItQueueSz;
	private Ticket LastRemovedTicket;

	public TicketQueue(int ForTableSize, int ServerForgetItQueueSz) {
		this.ticketQueue = new LinkedBlockingQueue<Ticket>();
		this.ForTableSize = ForTableSize;
		this.ServerForgetItQueueSz = ServerForgetItQueueSz;
	}

	// ------------------------------------------------------------
	// addTicketToQueue
	public Queue<Ticket> addTicketToQueue(Ticket t) throws InterruptedException {
		// if (ticketQueue.size() < ServerForgetItQueueSz) {
		this.setStatus("Add");
		t.setInQueueTime();
		this.ticketQueue.put(t);
		// this.ticketQueue.forEach(tk -> System.out.println("Adddd" +
		// tk.getTicketID()));
		notifyObs();
		return this.ticketQueue;
		// }
		// return false;
	} // addTicketToQueue

	// ------------------------------------------------------------
	// addTicketToQueue
	public boolean removeTicketFromQueue(Ticket t) {
		if (this.ticketQueue.size() > 0) {
			this.ticketQueue.remove(t);
//			this.LastRemovedTicket = t;
//			this.setStatus("Remove");
//			notifyObs();
			return true;
		}
		return false;
	} // addTicketToQueue

	public Ticket getLastRemovedTicket() {
		return this.LastRemovedTicket;
	}

	public Ticket pollTicketQueue() {
		return this.ticketQueue.poll();
	}

	public Ticket peekTicketQueue() {
		return this.ticketQueue.peek();
	}

	// ------------------------------------------------------------
	// getStatus
	public Queue<Ticket> getTicketQueue() {
		return this.ticketQueue;
	} // getStatus

	public int getForTableSize() {
		return this.ForTableSize;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

}
