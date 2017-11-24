package AppKickstarter.Gui;

public class temp {

}
//
//import java.awt.EventQueue;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.InputEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.ArrayList;
//
//import javax.swing.JFrame;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
//import javax.swing.JPopupMenu;
//import javax.swing.SwingUtilities;
//import javax.swing.border.BevelBorder;
//import javax.swing.event.PopupMenuEvent;
//import javax.swing.event.PopupMenuListener;
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
//import javax.swing.JToggleButton;
//import javax.swing.BoxLayout;
//import java.awt.GridLayout;
//
//public class staffPanel extends JPanel {
//
//	private JFrame frame;
//	public JPopupMenu popup;
//	int rows = 5;
//	int columns = 10;
//
//	private ArrayList<Table> TableList;
//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					staffPanel window = new staffPanel();
//					window.frame.setVisible(true);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public staffPanel() {
//		createTable();
//		initialize();
//
//	}
//
//	public void createTable() {
//		TableList = new ArrayList<Table>();
//		for (int i = 1; i <= 32; i++) {
//			int tNo = ((i - 1) * 100);
//			int tSize = i * 2;
//			if (i % 2 == 0) {
//				TableList.add(new Table(tNo, tSize).setAvailableState());
//			} else {
//				TableList.add(new Table(tNo, tSize).setHoldState());
//			}
//		}
//	}
//
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		frame = new JFrame();
//		frame.setBounds(100, 100, 680, 355);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(null);
//		
//		JPanel TablePanel = new JPanel();
//		TablePanel.setBounds(0, 0, 411, 333);
//		frame.getContentPane().add(TablePanel);
//		
//		JPanel TicketPanel = new JPanel();
//		TicketPanel.setBounds(411, 0, 269, 333);
//		frame.getContentPane().add(TicketPanel);
//
//		for (int row = 0; row < rows; row++) {
//			for (int column = 0; column < columns; column++) {
//				final JToggleButton button = new JToggleButton(" seat " + column);
//			}
//		}
//
//		// JButton btnAbc = new JButton("abc");
//		// btnAbc.setBackground(Color.red);
//		// btnAbc.setOpaque(true);
//		// panel.add(btnAbc);
//
//		/*
//		 * JButton btnEfg = new JButton("efg"); btnEfg.setBackground(Color.green);
//		 * btnEfg.setOpaque(true); panel.add(btnEfg);
//		 * 
//		 * JButton btnHij = new JButton("hij"); btnHij.setBackground(Color.yellow);
//		 * btnHij.setOpaque(true); panel.add(btnHij);
//		 */
//
//		MouseListener mouseListener = new MouseAdapter() {
//			public void mousePressed(MouseEvent mouseEvent) {
//				int modifiers = mouseEvent.getModifiers();
//				if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
//					System.out.println("Right button pressed.");
//
//				}
//			}
//
//			public void mouseReleased(MouseEvent mouseEvent) {
//				if (SwingUtilities.isRightMouseButton(mouseEvent)) {
//					System.out.println("Right button released.");
//
//				}
//			}
//		};
//
//		int[] size = { 10, 10, 8, 2, 2 };
//		
//		for (int i = 0; i < 5; i++) {
//			for (int j = 0; j < size[i]; j++) {
//				Table t = TableList.get(j);
//				JButton btnAbc = new JButton(String.valueOf(t.getTableNo()));
//
//				if (t.getState().equals("Hold")) {
//					btnAbc.setForeground(Color.pink);
//					btnAbc.setBackground(Color.red);
//
//				} else {
//					btnAbc.setBackground(Color.green);
//					btnAbc.setForeground(Color.blue);
//				}
//				btnAbc.setOpaque(true);
//				// btnAbc.addMouseListener(mouseListener);
//				btnAbc.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						System.out.println("*****DIu CheckOut ***"+t.getTableNo());
//					}
//				});
//				TablePanel.add(btnAbc); 
//
//				System.out.println("*************" + TableList.get(j).getTableNo());
//			}
//		}
//	}
//
//}
