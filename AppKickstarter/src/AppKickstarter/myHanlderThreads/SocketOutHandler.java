package AppKickstarter.myHanlderThreads;

import AppKickstarter.misc.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.IncomingMsgParser;

public class SocketOutHandler extends AppThread {
	private final int sleepTime = 2000;
	private PrintWriter out;

	/**
	 * 
	 * @param id
	 * @param appKickstarter
	 */
	public SocketOutHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
	}

	public void run() {
		log.info(id + ": starting...");
		for (boolean quit = false; !quit;) {
			this.out = appKickstarter.getPrintWriter();
			Msg msg = mbox.receive();
			out.println(msg.getDetails());
			out.flush();
			log.info(id + ": message Sent: [" + msg.getDetails() + "].");
		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run
}
