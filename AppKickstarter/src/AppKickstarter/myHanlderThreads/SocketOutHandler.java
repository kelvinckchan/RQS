package AppKickstarter.myHanlderThreads;

import AppKickstarter.misc.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.MsgParser;

//======================================================================
// ServerThread
public class SocketOutHandler extends AppThread {
	private final int sleepTime = 2000;
	private PrintWriter out;
	// private String ServerIP;
	// private int ServerPort;
//	private Socket socket;
//	private DataOutputStream out;

	// ------------------------------------------------------------
	// ServerThread
	public SocketOutHandler(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
//		this.socket = appKickstarter.getSocket();
		

	} // ServerThread

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");
//		try {

			for (boolean quit = false; !quit;) {
//				this.out = appKickstarter.getDataOutputStream();
				this.out = appKickstarter.getPrintWriter();
//				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				Msg msg = mbox.receive();
//				log.info(id + ": message to be sent: [" + msg + "].");
				out.println(msg.getDetails());
				out.flush();
//				out.close();
				log.info(id + ": message Sent: [" + msg.getDetails() + "].");
			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run
} // ServerThread
