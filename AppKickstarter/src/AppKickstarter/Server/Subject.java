package AppKickstarter.Server;

import java.util.ArrayList;

/**
 * This class implements a subject.
 * 
 * @author user
 * @version 1.0
 */
public abstract class Subject {
	private ArrayList<Observer> observers = new ArrayList<>();

	/**
	 * This adds the observer.
	 * 
	 * @param observer
	 *            : The current observer
	 */
	public final void addObs(Observer observer) {
		System.out.println("Adding observer: " + observer);
		observers.add(observer);
	}

	/**
	 * This removes the observer.
	 * 
	 * @param observer
	 *            : The current observer
	 */
	public final void removeObs(Observer observer) {
		System.out.println("Removing observer: " + observer);
		observers.remove(observer);
	}

	/**
	 * This updates the observer.
	 */
	public final void notifyObs() {
		for (Observer o : observers) {
			o.update();
		}
	}

	/**
	 * {@inheritDoc} This implementation also does in different class such as
	 * Ticket, Table, Client, etc.
	 * 
	 */
	public abstract void setStatus(String status);

	/**
	 * {@inheritDoc} This implementation also does in different message type such as
	 * TicketReq, TicketRep, TicketCall, TicketAck, etc.
	 * 
	 */
	public abstract String getStatus();
}