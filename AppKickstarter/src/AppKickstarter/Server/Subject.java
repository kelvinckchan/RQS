package AppKickstarter.Server;

import java.util.ArrayList;

//======================================================================
// Subject
public abstract class Subject {
	private ArrayList<Observer> observers = new ArrayList<>();

	// ------------------------------------------------------------
	// addObs
	public final void addObs(Observer observer) {
		System.out.println("Adding observer: " + observer);
		observers.add(observer);
	} // addObs

	// ------------------------------------------------------------
	// removeObs
	public final void removeObs(Observer observer) {
		System.out.println("Removing observer: " + observer);
		observers.remove(observer);
	} // removeObs

	// ------------------------------------------------------------
	// notifyObs
	public final void notifyObs() {
		for (Observer o : observers) {
			o.update();
		}
	} // notifyObs

	public abstract void setStatus(String status);
	public abstract String getStatus();
} // Subject
