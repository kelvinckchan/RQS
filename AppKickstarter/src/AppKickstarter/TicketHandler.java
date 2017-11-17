package AppKickstarter;

import java.util.ArrayList;

public class TicketHandler {
	public static ArrayList<Ticket> TquereSize0 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> TquereSize1 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> TquereSize2 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> TquereSize3 = new ArrayList<Ticket>();
	public static ArrayList<Ticket> TquereSize4 = new ArrayList<Ticket>();

	public static void add(Ticket t) {
		int size = t.getnPerson();

		if (size > 8) {
			TquereSize4.add(t);
		} else if (size <= 8 && size > 6) {
			TquereSize3.add(t);
		} else if (size <= 6 && size > 4) {
			TquereSize2.add(t);
		} else if (size <= 4 && size > 2) {
			TquereSize1.add(t);
		} else {
			TquereSize0.add(t);
		}

	}

}
