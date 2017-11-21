package AppKickstarter.misc;

//======================================================================
// News
public class IncomingMsg extends Subject {
	private String status = "";

	// ------------------------------------------------------------
	// setStatus
	public void setStatus(String status) {
		System.out.println("Status updated from " + this.status + " to " + status);
		this.status = status;
		notifyObs();
	} // setStatus

	// ------------------------------------------------------------
	// getStatus
	public String getStatus() {
		return status;
	} // getStatus
} // News
