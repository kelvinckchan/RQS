package AppKickstarter.myThreads;

import AppKickstarter.misc.*;
import AppKickstarter.AppKickstarter;

//======================================================================
// ThreadB
// Server ThreadB
public class ThreadB extends AppThread {
	private final int sleepTime = 2000;

	// ------------------------------------------------------------
	// ThreadB
	public ThreadB(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
	} // ThreadB

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();

			log.info(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {
			case Hello:
				log.info(id + ": " + msg.getSender() + " is saying Hello to me!!!");
				msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is Thread B!"));
				break;

			case Terminate:
				quit = true;
				break;

			case TicketRep:
				msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.TicketRep, "HiHi, this is Thread B!"));
				break;

			case TicketCall:

				break;
			case TableAssign:

				break;
			case QueueTooLong:

				break;

			default:
				log.severe(id + ": unknown message type!!");
				break;
			}
		}

		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run
} // ThreadB
