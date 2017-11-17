package AppKickstarter.MsgHandler;

import AppKickstarter.TicketHandler;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class QueueTooLong extends Msg {
	final static int queueSize = 20;

	public QueueTooLong(String sender, MBox senderMBox, Type type, String details) {
		super(sender, senderMBox, type, details);
		// TODO Auto-generated constructor stub
	}

	public static boolean Check(int nPerson) {
		boolean bool = false;
		if (nPerson > 8) {
			if (TicketHandler.TquereSize4.size() >= queueSize) {
				bool = true;
			}
			;
		} else if (nPerson <= 8 && nPerson > 6) {
			if (TicketHandler.TquereSize3.size() >= queueSize) {
				bool = true;
			}
			;
		} else if (nPerson <= 6 && nPerson > 4) {
			if (TicketHandler.TquereSize2.size() >= queueSize) {
				bool = true;
			}
			;
		} else if (nPerson <= 4 && nPerson > 2) {
			if (TicketHandler.TquereSize1.size() >= queueSize) {
				bool = true;
			}
			;
		} else {
			if (TicketHandler.TquereSize0.size() >= queueSize) {
				bool = true;
			}
			;
		}

		return bool;
	}
}
