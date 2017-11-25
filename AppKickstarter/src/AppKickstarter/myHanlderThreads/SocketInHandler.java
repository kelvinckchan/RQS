package AppKickstarter.myHanlderThreads;

import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.IncomingMsgParser;

public class SocketInHandler extends AppThread {
	private final int sleepTime = 2000;
	private DataInputStream in;
	private int TimerIDForMatchTicketQueue;

	/**
	 * @param id
	 * @param appKickstarter
	 */
	public SocketInHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		this.TimerIDForMatchTicketQueue = appKickstarter.getTimerIDForMatchTicketQueue();

	}

	public void run() {
		log.info(id + ": starting...");
		try {
			Timer.setSimulationTimer("TicketHandler", appKickstarter.getThread("TicketHandler").getMBox(), 10,
					TimerIDForMatchTicketQueue);
			log.info(id + ": waiting For incoming Msg...");

			while (true) {
				this.in = appKickstarter.getDataInputStream();
				byte[] buffer = new byte[1024];
				in.read(buffer);
				String IncomingMsg = new String(buffer);
				log.info(id + ": IncomingMsg> " + IncomingMsg);

				// Send Parsered Msg to MsgHandler
				appKickstarter.getThread("MsgHandler").getMBox()
						.send(IncomingMsgParser.IncomingMsgParser(this.id, this.mbox, IncomingMsg));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run
} 
