package AppKickstarter.Server;

import java.time.LocalDateTime;

public class Ticket {
	final int TicketID;
	LocalDateTime checkIn, checkOut;
	private Client ClientWithTicket;

	public Ticket(int TicketID, Client ClientWithTicket) {
		this.TicketID = TicketID;
		this.ClientWithTicket = ClientWithTicket;
	}

	public void setCheckIn(LocalDateTime localDateTime) {
		this.checkIn = localDateTime;
	}

	public void setCheckOut(LocalDateTime checkOut) {
		this.checkOut = checkOut;
	}

	public int getTicketID() {
		return this.TicketID;
	}

	public int getTotalSpending() {
		return checkOut.compareTo(checkIn);
	}

	public Client getClientWithTicket() {
		return this.ClientWithTicket;

	}

}
