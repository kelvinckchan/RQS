package AppKickstarter.Msg;

import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

public class QueueTooLong extends Msg {
	

	public QueueTooLong(String sender, MBox senderMBox, Type type, String details) {
		super(sender, senderMBox, type, details);
	}

	
}
