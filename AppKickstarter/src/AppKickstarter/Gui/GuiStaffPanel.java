package AppKickstarter.Gui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.*;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.Msg;
import AppKickstarter.myHanlderThreads.TableHandler;
import AppKickstarter.timer.Timer;

public class GuiStaffPanel extends AppThread {
	private final int sleepTime = 2000;
	StaffPanel window;

	public GuiStaffPanel(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		CreatePanel();
		this.TableList = TableHandler.getTableList();
	}

	private ArrayList<Table> TableList;
	private Queue<TicketCall> Tcqueue = new LinkedList<TicketCall>();

	@Override
	public void run() {
		log.info(id + ": starting...");
		Timer.setSimulationTimer(id, mbox, sleepTime);

		for (boolean quit = false; !quit;) {
			Msg msg = mbox.receive();
			switch (msg.getType()) {
			case TicketCall:
				TicketCall tc = (TicketCall) msg.getCommand();
				Tcqueue.add(tc);
				break;
			case TimesUp:
				Timer.setSimulationTimer(id, mbox, sleepTime);
				this.TableList = TableHandler.getTableList();
				// gui...display TableList
				window.updateJTableForTableList(TableList);
				break;
			default:
				log.severe(id + ": unknown message type!!");
				break;
			}
		}
		// declaring our departure
		appKickstarter.unregThread(this);
		log.info(id + ": terminating...");

	}

	public void CreatePanel() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new StaffPanel();
					window.SetFrameVisible(window);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void SendTicketAck(int TicketID) {
		TicketCall nextTicketCall = Tcqueue.poll();
		Ticket nextTicket = nextTicketCall.getTicket();
		TicketAck ticketAck = new TicketAck(nextTicket.getTicketID(), nextTicketCall.getTable().getTableNo(),
				nextTicket.getClientWithTicket().getnPerson());
		appKickstarter.getThread("MsgHandler").getMBox().send(new Msg(id, mbox, Msg.Type.TicketAck, ticketAck));
	}

	// Selected table and checkout button clicked, send Checkout To server
	public void SendCheckout(int TableNo) {
		int Spending = 1234;// Random
		appKickstarter.getThread("MsgHandler").getMBox()
				.send(new Msg(id, mbox, Msg.Type.CheckOut, new CheckOut(TableNo, Spending)));
	}
}
