package AppKickstarter.Gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import AppKickstarter.Server.Table;

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
import javax.swing.DefaultListModel;

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
	JList t1, t2, t3, t4, t5;

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

		JPanel TablePanel = new JPanel();
		TablePanel.setBackground(Color.WHITE);
		TablePanel.setBounds(8, 11, 712, 351);
		StaffPanel.getContentPane().add(TablePanel);
		
		TablePanel.setLayout(null);

//		JScrollPane scrollPane_1 = new JScrollPane();
//		scrollPane_1.setBounds(0, 6, 712, 351);
		t1 = new JList();
		t1.setBounds(0, 0, 712 / 5, 351);
		t2 = new JList();
		t1.setBounds(142, 0, 712 / 5, 351);
		t3 = new JList();
		t1.setBounds(284, 0, 712 / 5, 351);
		t4 = new JList();
		t1.setBounds(560, 0, 712 / 5, 351);
		t5 = new JList();
		t1.setBounds(1200, 0, 712 / 5, 351);
		InsertDataToFloorPlan();
//		scrollPane_1.add(t1);
//		scrollPane_1.add(t2);
//		scrollPane_1.add(t3);
//		scrollPane_1.add(t4);
//		scrollPane_1.add(t5);

//		TablePanel.add(scrollPane_1);

		TablePanel.add(t1);
		
		
		JPanel TicketPanel = new JPanel();
		TicketPanel.setBackground(new Color(255, 255, 255));
		TicketPanel.setBounds(732, 11, 251, 351);
		StaffPanel.getContentPane().add(TicketPanel);

		JScrollPane scrollPane = new JScrollPane(AckTable);
		TicketPanel.add(scrollPane);

		AckTable = new JTable();
		AckTable.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		AckTable.setCellSelectionEnabled(true);
		AckTable.setToolTipText("TicketCall Wait For Ack");
		TicketPanel.add(AckTable);

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				final JToggleButton button = new JToggleButton(" seat " + column);
			}
		}

		MouseListener mouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				int modifiers = mouseEvent.getModifiers();
				if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
					System.out.println("Right button pressed.");

				}
			}

			public void mouseReleased(MouseEvent mouseEvent) {
				if (SwingUtilities.isRightMouseButton(mouseEvent)) {
					System.out.println("Right button released.");

				}
			}
		};

	}

	public void InsertDataToFloorPlan() {
System.out.println("Table"+TableList.size());
//		String[] TableHeading = new String[] { "Table 1-2", "Table 3-4", "Table 5-6", "Table 7-8", "Table 9-10" };
		List<Integer> tbWSize2 = TableList.stream().filter(t -> t.getTableSize() == 2).map(table -> table.getTableNo())
				.collect(Collectors.toList());
		List<Integer> tbWSize4 = TableList.stream().filter(t -> t.getTableSize() == 4).map(table -> table.getTableNo())
				.collect(Collectors.toList());
		List<Integer> tbWSize6 = TableList.stream().filter(t -> t.getTableSize() == 6).map(table -> table.getTableNo())
				.collect(Collectors.toList());
		List<Integer> tbWSize8 = TableList.stream().filter(t -> t.getTableSize() == 8).map(table -> table.getTableNo())
				.collect(Collectors.toList());
		List<Integer> tbWSize10 = TableList.stream().filter(t -> t.getTableSize() == 10).map(table -> table.getTableNo())
				.collect(Collectors.toList());

		DefaultListModel listModel1 = new DefaultListModel();
		for (int i = 0; i < tbWSize2.size(); i++) {
			listModel1.addElement(tbWSize2.get(i));
		}
		
		t1.setModel(listModel1);
		DefaultListModel listModel2 = new DefaultListModel();
		for (int i = 0; i < tbWSize4.size(); i++) {
			listModel1.addElement(tbWSize4.get(i));
		}
		
		t2.setModel(listModel2);
		DefaultListModel listModel3 = new DefaultListModel();
		for (int i = 0; i < tbWSize6.size(); i++) {
			listModel1.addElement(tbWSize6.get(i));
		}
		t3.setModel(listModel1);
		DefaultListModel listModel4 = new DefaultListModel();
		for (int i = 0; i < tbWSize8.size(); i++) {
			listModel1.addElement(tbWSize8.get(i));
		}
		t4.setModel(listModel1);
		DefaultListModel listModel5 = new DefaultListModel();
		for (int i = 0; i < tbWSize10.size(); i++) {
			listModel1.addElement(tbWSize10.get(i));
		}
		t5.setModel(listModel1);

		// List<Object> Col1 = new ArrayList<Object>();
		// for (int t : tbWSize2) {
		// System.out.println(t);
		// }
		//
		// for (Table t : TableList) {
		// }

		// Object[][] AllTable = new Object[][] { tbWSize2, tbWSize4, tbWSize6,
		// tbWSize8, tbWSize10 };

		for (int t : tbWSize2) {
			System.out.println(t);
		}
		System.out.println("------------");
		for (int t : tbWSize4) {
			System.out.println(t);
		}
		System.out.println("------------");
		for (int t : tbWSize6) {
			System.out.println(t);
		}
		System.out.println("------------");
		for (int t : tbWSize8) {
			System.out.println(t);
		}
		System.out.println("------------");
		for (int t : tbWSize10) {
			System.out.println(t);
		}
		// FloorPlan.setModel(new DefaultTableModel(list.toArray(new Object[][] {}), new
		// String[] { "TableNo" }));
	}
}
