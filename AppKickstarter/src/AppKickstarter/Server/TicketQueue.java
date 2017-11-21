package AppKickstarter.Server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TicketQueue extends Subject {
	private String status = "";
	private static Queue<Ticket> ticketQueue;
	private int ForTableSize;
	private int ServerForgetItQueueSz;
	private ArrayList<Observer> observers = new ArrayList<>();

	public TicketQueue(int ForTableSize, int ServerForgetItQueueSz) {
		this.ticketQueue = new LinkedList<Ticket>();
		this.ForTableSize = ForTableSize;
		this.ServerForgetItQueueSz = ServerForgetItQueueSz;
		new TicketQueueObserver(this);
	}

	// ------------------------------------------------------------
	// addTicketToQueue
	public boolean addTicketToQueue(Ticket t) {
		if (this.ticketQueue.size() < ServerForgetItQueueSz) {
			this.ticketQueue.add(t);
			setStatus("Add");
			notifyObs();
			return true;
		}
		return false;
	} // addTicketToQueue

	// ------------------------------------------------------------
	// addTicketToQueue
	public boolean removeTicketToQueue(Ticket t) {
		if (this.ticketQueue.size() > 0) {
			this.ticketQueue.remove(t);
			setStatus("Remove");
			notifyObs();
			return true;
		}
		return false;
	} // addTicketToQueue

	// ------------------------------------------------------------
	// getStatus
	public Queue<Ticket> getTicketQueue() {
		return ticketQueue;
	} // getStatus

	public int getForTableSize() {
		return ForTableSize;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getStatus() {
		return status;
	}
}
