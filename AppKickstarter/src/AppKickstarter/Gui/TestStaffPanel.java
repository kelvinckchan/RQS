package AppKickstarter.Gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import AppKickstarter.Msg.TicketCall;
import AppKickstarter.Server.Client;
import AppKickstarter.Server.Table;
import AppKickstarter.Server.Ticket;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import java.awt.FlowLayout;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import java.awt.Component;

public class TestStaffPanel extends JPanel {

	private JFrame StaffPanel;
	public JPopupMenu popup;
	int rows = 5;
	int columns = 10;

	private ArrayList<Table> TableList;
	private JTable AckTable;
	private JTable FloorPlan;
	JPanel TablePanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestStaffPanel window = new TestStaffPanel();
					window.StaffPanel.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestStaffPanel() {
		createTable();
		initialize();

	}

	public void createTable() {
		TableList = new ArrayList<Table>();
		for (int i = 1; i <= 32; i++) {
			int tNo = ((i - 1) * 100);
			int tSize = i * 2;
			if (i % 2 == 0) {
				TableList.add(new Table(tNo, tSize).setAvailableState());
			} else {
				TableList.add(new Table(tNo, tSize).setHoldState());
			}
		}
		TableList.add(new Table(123, 10).setHoldState());
		TableList.add(new Table(123, 8).setHoldState());
		TableList.add(new Table(123, 6).setHoldState());
		TableList.add(new Table(123, 4).setHoldState());
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
		
		JButton btnSendTicketack = new JButton("Send TicketAck");
		btnSendTicketack.setBounds(744, 11, 239, 351);
		StaffPanel.getContentPane().add(btnSendTicketack);

		JScrollPane scrollPane = new JScrollPane(AckTable);
		scrollPane.setBounds(6, 6, 239, 339);
		

		AckTable = new JTable();
		AckTable.setBounds(6, 6, 239, 339);
		AckTable.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		AckTable.setCellSelectionEnabled(true);
		AckTable.setToolTipText("TicketCall Wait For Ack");
		updateJTableForAck();
		scrollPane.add(AckTable);
//		TicketPanel.add(scrollPane);
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				final JToggleButton button = new JToggleButton(" seat " + column);
			}
		}

		for (int i = 0; i < TableList.size(); i++) {
			Table t = TableList.get(i);
			JButton btnAbc = new JButton(String.valueOf(t.getTableNo()));

			if (t.getState().equals("Hold")) {
				btnAbc.setForeground(Color.pink);
				btnAbc.setBackground(Color.red);

			} else {
				btnAbc.setBackground(Color.green);
				btnAbc.setForeground(Color.blue);
			}
			btnAbc.setOpaque(true);
			btnAbc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// SendCheckout( t.getTableNo());
					System.out.println("***** CheckOut ***" + t.getTableNo());
				}
			});
			TablePanel.add(btnAbc);

			System.out.println("*************" + TableList.get(i).getTableNo());
		}

	}

	private Queue<TicketCall> Tcqueue = new LinkedList<TicketCall>();

	public void updateJTableForAck() {

		Tcqueue.add(new TicketCall(new Ticket(111, new Client("", 1)), new Table(123, 4)));
		Object[] row = Tcqueue.stream().map(tc -> tc.getTicket().getTicketID()).toArray();
		DefaultTableModel model = (DefaultTableModel) AckTable.getModel();
		model.addRow(row);
		AckTable.repaint();
	}
}