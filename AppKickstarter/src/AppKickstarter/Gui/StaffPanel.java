package AppKickstarter.Gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridLayout;

public class StaffPanel extends JPanel {

	private JFrame frame;
	public JPopupMenu popup;
	int rows = 5;
	int columns = 10;

	private ArrayList<Table> TableList;

	public void SetFrameVisible(StaffPanel window) {
		window.frame.setVisible(true);

	}

	public StaffPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 477, 496);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				final JToggleButton button = new JToggleButton(" seat " + column);
			}
		}


	}

	public void updateJTableForTableList(ArrayList<Table> tableList2) {

	}

}
