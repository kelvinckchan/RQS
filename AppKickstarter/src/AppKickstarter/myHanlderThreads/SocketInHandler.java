package AppKickstarter.myHanlderThreads;

import AppKickstarter.misc.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.MsgParser;

//======================================================================
// ServerThread
public class SocketInHandler extends AppThread {
	private final int sleepTime = 2000;
	// private String ServerIP;
	// private int ServerPort;
	private Socket socket;
	DataInputStream in;
	DataOutputStream out;

	// ------------------------------------------------------------
	// ServerThread
	public SocketInHandler(String id, AppKickstarter appKickstarter) {
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

			log.info(id + ": waiting For incoming Msg...");
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			MsgHandler msghandler = new MsgHandler("MsgHandler", appKickstarter);
			new Thread(msghandler).start();
			while (true) {
				byte[] buffer = new byte[1024];
				in.read(buffer);
				String IncomingMsg = new String(buffer);
				log.info(id + ": IncomingMsg> " + IncomingMsg);

				
				
				
				// MsgParser
				msghandler.getMBox().send(MsgParser.IncomingMsgParser(this.id,this.mbox,IncomingMsg));

				if (!socket.isConnected())
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");
	} // run
} // ServerThread
