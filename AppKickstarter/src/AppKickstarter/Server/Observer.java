package AppKickstarter.Server;

/**
 * This class implements a observer.
 * 
 * @author user
 * @version 1.0
 */
public abstract class Observer {
	public abstract void update();

	protected Subject subject;

	/**
	 * This constructs a observer with subject.
	 * 
	 * @param subject
	 *            : The current subject
	 */
	public Observer(Subject subject) {
		this.subject = subject;
	} 
} 
