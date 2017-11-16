package AppKickstarter.myThreads;

import AppKickstarter.misc.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import AppKickstarter.AppKickstarter;

//======================================================================
// ThreadB
public class ServerThread extends AppThread {
	private final int sleepTime = 2000;
	private int ServerPort;
	private String ServerIp;
	private Socket socket;
	DataInputStream in;
	DataOutputStream out;

	// ------------------------------------------------------------
	// ThreadB
	public ServerThread(String id, AppKickstarter appKickstarter) throws IOException {
		super(id, appKickstarter);
		this.ServerPort = Integer.valueOf(appKickstarter.getProperty("ServerPort"));
		this.ServerIp = appKickstarter.getProperty("ServerIP");
		log.info(id + ": Listening at ServerPort=" + ServerPort + " IP=" + ServerIp);
	} // ThreadB

	// ------------------------------------------------------------
	// run
	public void run() {
		log.info(id + ": starting...");
		try {
			ServerSocket serverSocket = new ServerSocket(ServerPort);
			this.socket = serverSocket.accept();
			log.info(id + ": accepted..." + socket.isConnected());
			in = new DataInputStream(socket.getInputStream());
			log.info(id + ": Try reading... ");

			for (boolean quit = false; !quit;) {
				int cmdLength = 10;
				byte[] command = new byte[1024];
				in.read(command);
				String cmdStr = new String(command);
				// String cmdStr = in.readUTF();
				log.info(id + "> Command Received: " + cmdStr);
				String[] msgs = cmdStr.split(":");
				String msgType = msgs[0];
				log.info(id + "> msg type: " + msgType);
				String[] msgDetail = msgs[1].trim().split("\\s+");
				for (String s : msgDetail) {
					log.info(id + "> msg Details[" + msgDetail.length + "]: " + s);
				}

				Msg msg = mbox.receive();

				// log.info(id + ": message received: [" + msg + "].");

				// switch (msg.getType()) {
				// case Hello:
				// log.info(id + ": " + msg.getSender() + " is saying Hello to me!!!");
				// msg.getSenderMBox().send(new Msg(id, mbox, Msg.Type.HiHi, "HiHi, this is
				// Thread B!"));
				// break;
				//
				// case Terminate:
				// quit = true;
				// break;
				//
				// default:
				// log.severe(id + ": unknown message type!!");
				// break;
				// }
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

} // ThreadB
