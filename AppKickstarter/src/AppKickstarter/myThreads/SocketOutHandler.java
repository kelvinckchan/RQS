package AppKickstarter.myThreads;

import AppKickstarter.misc.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import AppKickstarter.AppKickstarter;
import AppKickstarter.MsgHandler.MsgHandler;
import AppKickstarter.MsgHandler.MsgParser;

//======================================================================
// ServerThread
public class SocketOutHandler extends AppThread {
	private final int sleepTime = 2000;
	// private String ServerIP;
	// private int ServerPort;
	private Socket socket;
	DataInputStream in;
	DataOutputStream out;

	// ------------------------------------------------------------
	// ServerThread
	public SocketOutHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		this.socket = appKickstarter.getSocket();
		// this.ServerIP = appKickstarter.getProperty("ServerIP");
		// this.ServerPort = Integer.valueOf(appKickstarter.getProperty("ServerPort"));

	} // ServerThread

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");
		try {
			for (boolean quit = false; !quit;) {
				Msg msg = mbox.receive();
				log.info(id + ": message to be sent: [" + msg + "].");
				out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF(msg.getDetails());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run
} // ServerThread
