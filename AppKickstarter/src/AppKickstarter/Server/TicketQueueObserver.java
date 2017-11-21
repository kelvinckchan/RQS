package AppKickstarter.Server;

//======================================================================
// ObserverBatman
public class TicketQueueObserver extends Observer {
	private final String name = "TicketQueue";

	// ------------------------------------------------------------
	// ObserverBatman
	public TicketQueueObserver(Subject subject) {
		super(subject);
	} // ObserverBatman

	// ------------------------------------------------------------
	// update
	public void update() {
		String status = subject.getStatus();
		if (status.equals("Add")) {
			System.out.println(name + ": new status: [" + status + "] > Find Table for Ticket ");
		} else {
			System.out.println(name + ": new status: [" + status + "]  ");
		}
	} // update

	// ------------------------------------------------------------
	// chkStatus
	public int checkTable(String status) {
		int cnt = 0;

		// check ticket queue, if(fulled){sent Qtoolong}else{add to ticket queue and
		// sent Ticketrep}

		return cnt;
	} // chkStatus

	// ------------------------------------------------------------
	// toString
	public String toString() {
		return name;
	} // toString
} // ObserverBatman
