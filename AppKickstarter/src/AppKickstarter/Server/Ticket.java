package AppKickstarter.Server;

import java.time.LocalDateTime;

public class Ticket {
	private static int AccTicketID = 1;
	private int TicketID;
	private LocalDateTime checkIn, checkOut;
	private Client ClientWithTicket;

	public Ticket(Client ClientWithTicket) {
		this.TicketID = AccTicketID++;
		this.ClientWithTicket = ClientWithTicket;
	}

	public void setCheckIn(LocalDateTime localDateTime) {
		this.checkIn = localDateTime;
	}

	public void setCheckOut(LocalDateTime checkOut) {
		this.checkOut = checkOut;
	}

	public LocalDateTime getCheckIn() {
		return this.checkIn;
	}

	public LocalDateTime getcheckOut() {
		return this.checkOut;
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
