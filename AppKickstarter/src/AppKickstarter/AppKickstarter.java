package AppKickstarter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.Hashtable;

import AppKickstarter.timer.Timer;
import AppKickstarter.misc.*;
import AppKickstarter.myHanlderThreads.*;

//New commit

//======================================================================
// AppKickstarter
public class AppKickstarter {
	private String cfgFName = null;
	private Properties cfgProps = null;
	private Hashtable<String, AppThread> appThreads = null;
	private String id = null;
	private Logger log = null;
	private ConsoleHandler logConHd = null;
	private FileHandler logFileHd = null;
	private Timer timer = null;
	private ThreadA threadA;
	private SocketInHandler socketInHandler;
	private SocketOutHandler socketOutHandler;
	private TableHandler tableManager;
	private TicketHandler ticketHandler;
	private String ServerIP;
	private int ServerPort;
	private Socket socket;
	DataInputStream in;
	DataOutputStream out;

	// ------------------------------------------------------------
	// main
	public static void main(String[] args) {
		AppKickstarter appKickstarter = new AppKickstarter("AppKickstarter", "etc/MyApp.cfg");
		appKickstarter.startApp();
		try {
			Thread.sleep(5000 * 10000);
		} catch (Exception e) {
		}
		// appKickstarter.stopApp();
	} // main

	// ------------------------------------------------------------
	// AppKickstarter
	private AppKickstarter(String id) {
		this(id, "etc/MyApp.cfg");
	} // AppKickstarter

	// ------------------------------------------------------------
	// AppKickstarter
	private AppKickstarter(String id, String cfgFName) {
		this(id, cfgFName, false);
	} // AppKickstarter

	// ------------------------------------------------------------
	// AppKickstarter
	private AppKickstarter(String id, String cfgFName, boolean append) {
		this.id = id;
		this.cfgFName = cfgFName;
		logConHd = null;
		logFileHd = null;
		id = getClass().getName();

		// set my thread name
		Thread.currentThread().setName(this.id);

		// read system config from property file
		try {
			cfgProps = new Properties();
			FileInputStream in = new FileInputStream(cfgFName);
			cfgProps.load(in);
			in.close();
			logConHd = new ConsoleHandler();
			logConHd.setFormatter(new LogFormatter());
			logFileHd = new FileHandler("etc/" + id + ".log", append);
			logFileHd.setFormatter(new LogFormatter());
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open config file (" + cfgFName + ").");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Error reading config file (" + cfgFName + ").");
			System.exit(-1);
		}

		// get and configure logger
		log = Logger.getLogger(id);
		log.addHandler(logConHd);
		log.addHandler(logFileHd);
		log.setUseParentHandlers(false);
		log.setLevel(Level.ALL);
		logConHd.setLevel(Level.FINER);
		logFileHd.setLevel(Level.ALL);
		appThreads = new Hashtable<String, AppThread>();
	} // AppKickstarter

	// ------------------------------------------------------------
	// startApp
	private void startApp() {
		// start our application
		log.info("");
		log.info("");
		log.info("============================================================");
		log.info(id + ": Application Starting...");

		this.ServerIP = getProperty("ServerIP");
		this.ServerPort = Integer.valueOf(getProperty("ServerPort"));
		log.info(id + ": Listening at ServerIP>" + ServerIP + " ServerPort>" + ServerPort + "...");
		try {
			this.socket = new ServerSocket(ServerPort).accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info(id + ": accepted...");

		// create threads
		// timer = new Timer("timer", this);
		// threadA = new ThreadA("ThreadA", this);
		socketInHandler = new SocketInHandler("SocketInHandler", this);
		socketOutHandler = new SocketOutHandler("SocketOutHandler", this);
		tableManager = new TableHandler("TableManager", this);
		ticketHandler = new TicketHandler("TicketHandler", this);
		// start threads
		// new Thread(timer).start();
		// new Thread(threadA).start();
		tableManager.createTable();
		new Thread(tableManager).start();
		new Thread(ticketHandler).start();
		new Thread(socketInHandler).start();
		new Thread(socketOutHandler).start();

	} // startApp

	// ------------------------------------------------------------
	// stopApp
	private void stopApp() {
		log.info("");
		log.info("");
		log.info("============================================================");
		log.info(id + ": Application Stopping...");
		threadA.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
		socketInHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
		timer.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	} // stopApp

	// ------------------------------------------------------------
	// getThread
	public Socket getSocket() {
		synchronized (socket) {
			return socket;
		}
	} // getThread

	// ------------------------------------------------------------
	// regThread
	public void regThread(AppThread appThread) {
		log.fine(id + ": registering " + appThread.getID());
		synchronized (appThreads) {
			appThreads.put(appThread.getID(), appThread);
		}
	} // regThread

	// ------------------------------------------------------------
	// unregThread
	public void unregThread(AppThread appThread) {
		log.fine(id + ": unregistering " + appThread.getID());
		synchronized (appThreads) {
			appThreads.remove(appThread.getID());
		}
	} // unregThread

	// ------------------------------------------------------------
	// getThread
	public AppThread getThread(String id) {
		synchronized (appThreads) {
			return appThreads.get(id);
		}
	} // getThread

	// ------------------------------------------------------------
	// getLogger
	public Logger getLogger() {
		return log;
	} // getLogger

	// ------------------------------------------------------------
	// getLogConHd
	public ConsoleHandler getLogConHd() {
		return logConHd;
	}
	// getLogConHd

	// ------------------------------------------------------------
	// getLogFileHd
	public FileHandler getLogFileHd() {
		return logFileHd;
	} // getLogFileHd

	// ------------------------------------------------------------
	// getProperty
	public String getProperty(String property) {
		String s = cfgProps.getProperty(property);

		if (s == null) {
			log.severe(id + ": getProperty(" + property + ") failed.  Check the config file (" + cfgFName + ")!");
		}
		return s;
	} // getProperty

	// ------------------------------------------------------------
	// getSimulationTime (in seconds)
	public long getSimulationTime() {
		return timer.getSimulationTime();
	} // getSimulationTime
} // AppKickstarter
