package AppKickstarter.MsgHandler;

import AppKickstarter.Server.TicketHandler;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class QueueTooLong extends Msg {
	final static int queueSize = 20;

	public QueueTooLong(String sender, MBox senderMBox, Type type, String details) {
		super(sender, senderMBox, type, details);
	}

	public static boolean Check(int nPerson) {
		boolean bool = false;
		if (nPerson > 8) {
			if (TicketHandler.TqueueSize4.size() >= queueSize)
				return true;
		} else if (nPerson <= 8 && nPerson > 6) {
			if (TicketHandler.TqueueSize3.size() >= queueSize)
				return true;
		} else if (nPerson <= 6 && nPerson > 4) {
			if (TicketHandler.TqueueSize2.size() >= queueSize)
				return true;
		} else if (nPerson <= 4 && nPerson > 2) {
			if (TicketHandler.TqueueSize1.size() >= queueSize)
				return true;
		} else {
			if (TicketHandler.TqueueSize0.size() >= queueSize)
				return true;
		}

		return bool;
	}
}
