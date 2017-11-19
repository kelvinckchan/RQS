package AppKickstarter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Ticket {
	private String ClientId;
	private int nPerson;
	private static int TableId;
	// ArrayList<Ticket> Tquere = new ArrayList<Ticket>();
	LocalDateTime checkIn, checkOut;

	public Ticket(String ClientId, int nPerson) {
		TableId += 1;
		this.Ticket(ClientId, nPerson, TableId);
	}

	private void Ticket(String ClientId, int nPerson, int TableId) {
		this.ClientId = ClientId;
		this.nPerson = nPerson;
	}

	public int getTableId() {
		return TableId;
	}

	public int getnPerson() {
		return nPerson;
	}

	public void setCheckIn(LocalDateTime localDateTime) {
		this.checkIn = localDateTime;
	}

	public void setCheckOut(LocalDateTime checkOut) {
		this.checkOut = checkOut;
	}

	public int getTotalSpending() {
		return checkOut.compareTo(checkIn);
	}
}
