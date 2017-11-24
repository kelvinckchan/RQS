package AppKickstarter.Gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import AppKickstarter.AppKickstarter;
import AppKickstarter.Msg.*;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;
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
				// window.updateJTableForAck(Tcqueue);
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
	public void SendCheckout(int TableNo) {
		int Spending = 1234;// Random
		appKickstarter.getThread("MsgHandler").getMBox()
				.send(new Msg(id, mbox, Msg.Type.CheckOut, new CheckOut(TableNo, Spending)));
	}

	class StaffPanel extends JPanel {

		private JFrame StaffPanel;
		public JPopupMenu popup;
		int rows = 5;
		int columns = 10;
		private JTable AckTable;
		private JTable FloorPlan;
		private ArrayList<Table> TableList;
		JPanel TablePanel;
		List<JButton> btnList;

		public void SetFrameVisible(StaffPanel window) {
			window.StaffPanel.setVisible(true);

		}

		public StaffPanel(ArrayList<Table> TableList) {
			this.TableList = TableList;
			initialize();
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
			StaffPanel.getContentPane().add(TablePanel);

			// JPanel TicketPanel = new JPanel();
			// TicketPanel.setBackground(new Color(255, 255, 255));
			// TicketPanel.setBounds(732, 11, 251, 351);
			// StaffPanel.getContentPane().add(TicketPanel);

			JButton btnSendTicketack = new JButton("Send TicketAck");
			btnSendTicketack.setBounds(744, 11, 239, 351);
			StaffPanel.getContentPane().add(btnSendTicketack);
			btnSendTicketack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Tcq> " + Tcqueue.peek());
					if (Tcqueue.size() > 0) {
						TicketCall tc = Tcqueue.poll();
						int tid = tc.getTicket().getTicketID();
						SendTicketAck(tc.getTicket().getTicketID(),tc.getTable().getTableNo(),tc.getTicket().getClientWithTicket().getnPerson());
						System.out.println("***** CheckOut ***" + tid);
					}
				}
			});
			// JScrollPane scrollPane = new JScrollPane(AckTable);
			// scrollPane.setBounds(6, 6, 239, 339);
			//
			//
			// AckTable = new JTable();
			// AckTable.setBounds(6, 6, 239, 339);
			// AckTable.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null,
			// null));
			// AckTable.setCellSelectionEnabled(true);
			// AckTable.setToolTipText("TicketCall Wait For Ack");
			// scrollPane.add(AckTable);
			// TicketPanel.add(scrollPane);

			// for (int row = 0; row < rows; row++) {
			// for (int column = 0; column < columns; column++) {
			// final JToggleButton button = new JToggleButton(" seat " + column);
			// }
			// }

			btnList = new ArrayList<JButton>();

			for (int i = 0; i < TableList.size(); i++) {
				Table t = TableList.get(i);
				JButton btnAbc = new JButton(String.valueOf(t.getTableNo()));

				if (t.getState().equals("Hold")) {
					btnAbc.setForeground(Color.pink);
					btnAbc.setBackground(Color.yellow);

				} else if (t.getState().equals("CheckedIn")) {
					btnAbc.setForeground(Color.black);
					btnAbc.setBackground(Color.red);

				} else {
					btnAbc.setBackground(Color.green);
					btnAbc.setForeground(Color.blue);
				}
				btnAbc.setOpaque(true);
				btnAbc.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						int Spending = 123;
						TableHandler.CheckOutTable(t.getTableNo(), Spending);
						System.out.println("***** CheckOut ***" + t.getTableNo());
					}
				});
				btnList.add(btnAbc);
				TablePanel.add(btnAbc);

				System.out.println("*************" + TableList.get(i).getTableNo());
			}

		}

		public void updateJTableForAck(Queue<TicketCall> tcqueue) {
			// Object[] row = tcqueue.stream().map(tc ->
			// tc.getTicket().getTicketID()).toArray();
			// DefaultTableModel model = (DefaultTableModel) AckTable.getModel();
			// model.addRow(row);
			// AckTable.repaint();
		}

		public void updateJButtonForTableList(ArrayList<Table> tableList2) {
			for (int i = 0; i < TableList.size(); i++) {
				Table t = TableList.get(i);
				JButton btnAbc = btnList.get(i);

				if (t.getState().equals("Hold")) {
					btnAbc.setForeground(Color.pink);
					btnAbc.setBackground(Color.yellow);

				} else if (t.getState().equals("CheckedIn")) {
					btnAbc.setForeground(Color.white);
					btnAbc.setBackground(Color.red);

				} else {
					btnAbc.setBackground(Color.green);
					btnAbc.setForeground(Color.blue);
				}
				btnAbc.setOpaque(true);
				btnAbc.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						int Spending = 123;
						TableHandler.CheckOutTable(t.getTableNo(), Spending);
						System.out.println("***** CheckOut ***" + t.getTableNo());
					}
				});
				TablePanel.add(btnAbc);

				// System.out.println("*************" + TableList.get(i).getTableNo());
			}
		}

	}
}
