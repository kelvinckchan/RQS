package AppKickstarter.Server;


import java.util.ArrayList;

public class TicketHandler {
	public static ArrayList<Ticket> TqueueSize0 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> TqueueSize1 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> TqueueSize2 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> TqueueSize3 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> TqueueSize4 = new ArrayList<Ticket>();

	public static void add(Ticket t) {
		int size = t.getClientWithTicket().getnPerson();

		if (size > 8) {
			TqueueSize4.add(t);
		} else if (size <= 8 && size > 6) {
			TqueueSize3.add(t);
		} else if (size <= 6 && size > 4) {
			TqueueSize2.add(t);
		} else if (size <= 4 && size > 2) {
			TqueueSize1.add(t);
		} else {
			TqueueSize0.add(t);
		}

	}

}
