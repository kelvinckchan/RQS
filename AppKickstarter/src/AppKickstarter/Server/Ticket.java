package AppKickstarter.Server;

import java.time.LocalDateTime;

public class Ticket {
	static int AccTicketID = 1;
	int TicketID;
	LocalDateTime checkIn, checkOut;
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
