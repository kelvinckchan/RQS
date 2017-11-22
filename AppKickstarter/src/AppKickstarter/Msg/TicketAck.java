package AppKickstarter.Msg;

import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class TicketAck extends Command {
	private int TicketID;
	private int TableNo;
	private int nPerson;

	public TicketAck(int TicketID, int TableNo, int nPerson) {
		this.TicketID = TicketID;
		this.TableNo = TableNo;
		this.nPerson = nPerson;
	}

	public int getTicketID() {
		return this.TicketID;
	}

	public int getTableNo() {
		return this.TableNo;
	}

	public int getNPerson() {
		return this.nPerson;
	}

	@Override
	public String toString() {
		return String.format("TicketAck: %s %s %s", this.TicketID, this.TableNo, this.nPerson);
	}
}
