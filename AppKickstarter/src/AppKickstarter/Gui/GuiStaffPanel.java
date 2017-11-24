package AppKickstarter.Gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.*;
import AppKickstarter.Server.Table;
import AppKickstarter.misc.AppThread;
import AppKickstarter.misc.Msg;
import AppKickstarter.myHanlderThreads.TableHandler;
import AppKickstarter.timer.Timer;

public class GuiStaffPanel extends AppThread {
	private final int sleepTime = 30;
	StaffPanel window;

	public GuiStaffPanel(String id, AppKickstarter appKickstarter) {
		super(id, appKickstarter);
		this.TableList = TableHandler.getTableList();
		log.info("OPen Staff Panel");
		CreatePanel(TableList);
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
				window.updateJButtonForAck(Tcqueue);
				break;
			case TimesUp:
				Timer.setSimulationTimer(id, mbox, sleepTime);
				this.TableList = TableHandler.getTableList();
				// gui...display TableList
				window.updateJButtonForTableList(TableList);
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

	public void CreatePanel(ArrayList<Table> TableList) {
		window = new StaffPanel(TableList);
		window.SetFrameVisible(window);
	}

	public void SendTicketAck(int TicketID, int TableNo, int nPerson) {
		// TicketCall nextTicketCall = Tcqueue.poll();
		// Ticket nextTicket = nextTicketCall.getTicket();
		TicketAck ticketAck = new TicketAck(TicketID, TableNo, nPerson);
		appKickstarter.getThread("MsgHandler").getMBox().send(new Msg(id, mbox, Msg.Type.TicketAck, ticketAck));
	}

	// Selected table and checkout button clicked, send Checkout To server
	public void SendCheckout(Table table) {
		int Spending = (new Random().nextInt((50 - 40) + 1) + 40)
				* table.getTicketAtTable().get(0).getClientWithTicket().getnPerson();
		appKickstarter.getThread("MsgHandler").getMBox()
				.send(new Msg(id, mbox, Msg.Type.CheckOut, new CheckOut(table.getTableNo(), Spending)));
	}

	class StaffPanel extends JPanel {

		private JFrame StaffPanel;
		public JPopupMenu popup;
		int rows = 5;
		int columns = 10;
		private ArrayList<Table> TableList;
		private JPanel TablePanel;
		private List<JButton> btnList;
		private JButton btnSendTicketack;

		public void SetFrameVisible(StaffPanel window) {
			window.StaffPanel.setVisible(true);
		}

		public StaffPanel(ArrayList<Table> TableList) {
			this.TableList = TableList;
			initialize();
		}

		public boolean updateJButtonForAck(Queue<TicketCall> tcqueue) {
			if (tcqueue.size() > 0) {
				btnSendTicketack.setText(tcqueue.peek().toString());
				btnSendTicketack.setBackground(Color.yellow);
				return true;
			}
			return false;
		}

		/**
		 * Initialize the contents of the frame.
		 */
		private void initialize() {
			StaffPanel = new JFrame();
			StaffPanel.setTitle("StaffPanel");
			StaffPanel.getContentPane().setBackground(new Color(192, 192, 192));
			StaffPanel.setBounds(100, 100, 1000, 400);
			StaffPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			StaffPanel.getContentPane().setLayout(null);

			TablePanel = new JPanel();
			TablePanel.setBackground(Color.WHITE);
			TablePanel.setBounds(10, 11, 712, 351);
			TablePanel.setLayout(new GridLayout(5, 10));
			StaffPanel.getContentPane().add(TablePanel);

			btnSendTicketack = new JButton("Wait For TicketCall");
			btnSendTicketack.setBounds(744, 11, 239, 351);
			StaffPanel.getContentPane().add(btnSendTicketack);
			btnSendTicketack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Tcq> " + Tcqueue.peek());
					if (Tcqueue.size() > 0) {
						TicketCall tc = Tcqueue.poll();
						int tid = tc.getTicket().getTicketID();
						SendTicketAck(tc.getTicket().getTicketID(), tc.getTable().getTableNo(),
								tc.getTicket().getClientWithTicket().getnPerson());
						System.out.println("***** CheckOut ***" + tid);
						if (!updateJButtonForAck(Tcqueue)) {
							btnSendTicketack.setText("Wait For TicketCall");
							btnSendTicketack.setBackground(Color.white);
						}
					}
				}
			});

			btnList = new ArrayList<JButton>();

			for (int i = 0; i < TableList.size(); i++) {
				Table t = TableList.get(i);
				JButton btnAbc = new JButton(String.format("%04d", t.getTableNo()));
				btnAbc.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 10),
						BorderFactory.createEmptyBorder(5, 5, 10, 10)));
				if (t.getState().equals("Hold")) {
					btnAbc.setForeground(Color.MAGENTA);
					btnAbc.setBackground(Color.yellow);

				} else if (t.getState().equals("CheckedIn")) {
					btnAbc.setForeground(Color.black);
					btnAbc.setBackground(Color.red);

				} else {
					btnAbc.setBackground(Color.green);
					btnAbc.setForeground(Color.blue);
				}
				btnAbc.setOpaque(true);
				for (ActionListener al : btnAbc.getActionListeners())
					btnAbc.removeActionListener(al);
				btnAbc.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (t.getState().equals("CheckedIn")) {
							SendCheckout(t);
							System.out.println("CheckOut TableNo>" + t.getTableNo());
						}
					}
				});
				btnList.add(btnAbc);
				TablePanel.add(btnAbc);
			}

		}

		public void updateJButtonForTableList(ArrayList<Table> tableList) {
			for (int i = 0; i < tableList.size(); i++) {
				Table t = tableList.get(i);
				JButton btnAbc = btnList.get(i);
				if (t.getState().equals("Hold")) {
					btnAbc.setForeground(Color.MAGENTA);
					btnAbc.setBackground(Color.yellow);
					btnAbc.setText(t.getOneTicket().getClientWithTicket().getClientID());
				} else if (t.getState().equals("CheckedIn")) {
					btnAbc.setForeground(Color.white);
					btnAbc.setBackground(Color.red);
					btnAbc.setText(t.getOneTicket().getClientWithTicket().getClientID());
				} else {
					btnAbc.setBackground(Color.green);
					btnAbc.setForeground(Color.blue);
					btnAbc.setText(String.format("%04d", t.getTableNo()));
				}
				// TablePanel.add(btnAbc);
			}
		}

	}
}
