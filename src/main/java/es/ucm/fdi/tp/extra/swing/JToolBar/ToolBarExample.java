package es.ucm.fdi.tp.extra.swing.JToolBar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ToolBarExample extends JFrame implements ActionListener {

	private JTextArea textArea;

	public ToolBarExample() {
		super("[=] ToolBar [=]");
		initGUI();
	}

	private void initGUI() {

		// We create a bottom JPanel to place everything on.
		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));

		textArea = new JTextArea(5, 30);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		mainPanel.add(scrollPane);

		JToolBar toolBar = new JToolBar();
		mainPanel.add(toolBar, BorderLayout.PAGE_START);

		JButton load = new JButton();
		load.setActionCommand("load");
		load.setToolTipText("Load a file");
		load.addActionListener(this);
		load.setIcon(new ImageIcon("open.png"));
		toolBar.add(load);

		JButton save = new JButton();
		save.setActionCommand("save");
		save.setToolTipText("Save a file");
		save.addActionListener(this);
		save.setIcon(new ImageIcon("save.png"));
		toolBar.add(save);

		String[] names = { "Times Roman", "Arial", "Comic" };
		JComboBox<String> list = new JComboBox<String>(names);
		list.setSelectedIndex(0);
		list.addActionListener(this);
		list.setMaximumSize(new Dimension(150, 60));
		toolBar.add(list);

		JTextField text = new JTextField(8);
		text.setMaximumSize(new Dimension(150, 60));
		toolBar.add(text);

		mainPanel.setOpaque(true);

		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		textArea.append("Clicked on " + e.getActionCommand() + "\n");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ToolBarExample();
			}
		});
	}

}
