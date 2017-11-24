//package AppKickstarter.Gui;
//
//import java.awt.EventQueue;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.InputEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.JFrame;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
//import javax.swing.JPopupMenu;
//import javax.swing.JScrollPane;
//import javax.swing.SwingUtilities;
//import javax.swing.border.BevelBorder;
//import javax.swing.border.SoftBevelBorder;
//import javax.swing.event.PopupMenuEvent;
//import javax.swing.event.PopupMenuListener;
//import javax.swing.table.DefaultTableModel;
//
//import AppKickstarter.Server.Table;
//import AppKickstarter.myHanlderThreads.TableHandler;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//
//import javax.swing.JButton;
//import javax.swing.JDialog;
//import javax.swing.JList;
//import java.awt.FlowLayout;
//import javax.swing.JInternalFrame;
//import javax.swing.JSplitPane;
//import javax.swing.JTable;
//import javax.swing.JToggleButton;
//import javax.swing.ListSelectionModel;
//import javax.swing.GroupLayout;
//import javax.swing.GroupLayout.Alignment;
//import java.awt.GridLayout;
//
//public class StaffPanel extends JPanel {
//
//	private JFrame StaffPanel;
//	public JPopupMenu popup;
//	int rows = 5;
//	int columns = 10;
//	private JTable AckTable;
//	private JTable FloorPlan;
//	private ArrayList<Table> TableList;
//	JPanel TablePanel;
//	List<JButton> btnList;
//
//	public void SetFrameVisible(StaffPanel window) {
//		window.StaffPanel.setVisible(true);
//
//	}
//
//	public StaffPanel(ArrayList<Table> TableList) {
//		this.TableList = TableList;
//		initialize();
//	}
//
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		StaffPanel = new JFrame();
//		StaffPanel.setTitle("StaffPanel");
//		StaffPanel.getContentPane().setBackground(new Color(192, 192, 192));
//		StaffPanel.setBounds(100, 100, 1000, 400);
//		StaffPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		StaffPanel.getContentPane().setLayout(null);
//
//		TablePanel = new JPanel();
//		TablePanel.setBackground(Color.WHITE);
//		TablePanel.setBounds(10, 11, 712, 351);
//		StaffPanel.getContentPane().add(TablePanel);
//
//		JPanel TicketPanel = new JPanel();
//		TicketPanel.setBackground(new Color(255, 255, 255));
//		TicketPanel.setBounds(732, 11, 251, 351);
//		StaffPanel.getContentPane().add(TicketPanel);
//
//		JScrollPane scrollPane = new JScrollPane(AckTable);
//		TicketPanel.add(scrollPane);
//
//		AckTable = new JTable();
//		AckTable.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
//		AckTable.setCellSelectionEnabled(true);
//		AckTable.setToolTipText("TicketCall Wait For Ack");
//		TicketPanel.add(AckTable);
//
//		for (int row = 0; row < rows; row++) {
//			for (int column = 0; column < columns; column++) {
//				final JToggleButton button = new JToggleButton(" seat " + column);
//			}
//		}
//
//		btnList = new ArrayList<JButton>();
//
//		for (int i = 0; i < TableList.size(); i++) {
//			Table t = TableList.get(i);
//			JButton btnAbc = new JButton(String.valueOf(t.getTableNo()));
//
//			if (t.getState().equals("Hold")) {
//				btnAbc.setForeground(Color.pink);
//				btnAbc.setBackground(Color.yellow);
//
//			} else if (t.getState().equals("CheckedIn")) {
//				btnAbc.setForeground(Color.white);
//				btnAbc.setBackground(Color.red);
//
//			} else {
//				btnAbc.setBackground(Color.green);
//				btnAbc.setForeground(Color.blue);
//			}
//			btnAbc.setOpaque(true);
//			btnAbc.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//
//					int Spending = 123;
//					TableHandler.CheckOutTable(t.getTableNo(), Spending);
//					System.out.println("***** CheckOut ***" + t.getTableNo());
//				}
//			});
//			btnList.add(btnAbc);
//			TablePanel.add(btnAbc);
//
//			System.out.println("*************" + TableList.get(i).getTableNo());
//		}
//
//	}
//
//	public void updateJTableForTableList(ArrayList<Table> tableList2) {
//		for (int i = 0; i < TableList.size(); i++) {
//			Table t = TableList.get(i);
//			JButton btnAbc = btnList.get(i);
//
//			if (t.getState().equals("Hold")) {
//				btnAbc.setForeground(Color.pink);
//				btnAbc.setBackground(Color.yellow);
//
//			} else if (t.getState().equals("CheckedIn")) {
//				btnAbc.setForeground(Color.white);
//				btnAbc.setBackground(Color.red);
//
//			} else {
//				btnAbc.setBackground(Color.green);
//				btnAbc.setForeground(Color.blue);
//			}
//			btnAbc.setOpaque(true);
//			btnAbc.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//
//					int Spending = 123;
//					TableHandler.CheckOutTable(t.getTableNo(), Spending);
//					System.out.println("***** CheckOut ***" + t.getTableNo());
//				}
//			});
//			TablePanel.add(btnAbc);
//
//			// System.out.println("*************" + TableList.get(i).getTableNo());
//		}
//	}
//
//}
