package AppKickstarter.myThreads;

import AppKickstarter.misc.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import AppKickstarter.AppKickstarter;

//======================================================================
// ServerThread
public class ServerThread extends AppThread {
	private final int sleepTime = 2000;
	private String ServerIP;
	private int ServerPort;
	private Socket socket;
	DataInputStream in;
	DataOutputStream out;

	// ------------------------------------------------------------
	// ServerThread
	public ServerThread(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		this.ServerIP = appKickstarter.getProperty("ServerIP");
		this.ServerPort = Integer.valueOf(appKickstarter.getProperty("ServerPort"));

	} // ServerThread

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");
		try {
			log.info(id + ": Listening at ServerIP>"+ServerIP+" ServerPort>"+ServerPort+"...");
			this.socket = new ServerSocket(ServerPort).accept();
			log.info(id + ": accepted...");
			log.info(id + ": waiting For incoming Msg...");
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			for (boolean quit = false; !quit&&socket.isConnected();) {
				byte[] buffer = new byte[1024];
				in.read(buffer);
				String IncomingMsg = new String(buffer);
				log.info(id + ": IncomingMsg> "+IncomingMsg);
				
				//MsgParser new Runnable
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run
} // ServerThread
