package AppKickstarter.Server;

//======================================================================
// ObserverBatman
public class TicketReqObserver extends Observer {
	private final String name = "Batman";
	private int ch;

	// ------------------------------------------------------------
	// ObserverBatman
	public TicketReqObserver(Subject subject, int ch) {
		super(subject);
		this.ch = ch;
	} // ObserverBatman

	// ------------------------------------------------------------
	// update
	public void update() {
		String status = subject.getStatus();
		System.out.println(
				name + ": new status: [" + status + "] --> contains " + chkStatus(status) + " '" + (char) ch + "'.");
	} // update

	// ------------------------------------------------------------
	// chkStatus
	public int chkStatus(String status) {
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
