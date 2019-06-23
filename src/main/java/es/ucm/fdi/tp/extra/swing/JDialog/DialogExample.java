package es.ucm.fdi.tp.extra.swing.JDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class DialogExample extends JFrame implements ActionListener {

	String names[];
	JComboBox<String> list;

	public DialogExample() {
		super("Dialog Examples");
		initGUI();
	}

	private void initGUI() {

		JPanel mainPanel = new JPanel();
		names = new String[9];
		for (int i = 0; i < names.length; i++)
			names[i] = "Dialog " + i;
		JComboBox<String> list = new JComboBox<String>(names);
		list.setSelectedIndex(0);
		list.addActionListener(this);

		mainPanel.add(new JLabel("Select a dialog"));
		mainPanel.add(list);
		mainPanel.setOpaque(true);

		this.setContentPane(mainPanel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	// In this method, we create a square JPanel of a colour and set size
	// specified by the arguments.

	public void actionPerformed(ActionEvent e) {

		@SuppressWarnings("unchecked")
		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		JFrame frame = new JFrame("Dialog");
		switch (cb.getSelectedIndex()) {
		case 0:
			JOptionPane.showMessageDialog(frame,
					"Eggs are not supposed to be green.");
			break;
		case 1:
			JOptionPane.showMessageDialog(frame,
					"Eggs are not supposed to be green.", "Inane warning",
					JOptionPane.WARNING_MESSAGE);
			break;
		case 2:
			JOptionPane.showMessageDialog(frame,
					"Eggs are not supposed to be green.", "Inane error",
					JOptionPane.ERROR_MESSAGE);
			break;
		case 3:
			JOptionPane.showMessageDialog(frame,
					"Eggs are not supposed to be green.", "A plain message",
					JOptionPane.INFORMATION_MESSAGE);
			break;
		case 4:
			ImageIcon icon = createImageIcon("calvin.jpg");
			JOptionPane.showMessageDialog(frame,
					"Eggs are not supposed to be green.",
					"Inane custom dialog", JOptionPane.INFORMATION_MESSAGE,
					icon);
			break;
		case 5:
			int n = JOptionPane.showOptionDialog(frame,
					"Would you like some green eggs to go " + "with that ham?",
					"A Silly Question", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			System.out.println(n);
			break;
		case 6:
			Object[] options1 = { "Yes, please", "No, thanks",
					"No eggs, no ham!" };
			int n1 = JOptionPane.showOptionDialog(frame,
					"Would you like some green eggs to go " + "with that ham?",
					"A Silly Question", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options1, options1[2]);
			System.out.println(n1);
			break;
		case 7:
			Object[] possibilities = { "ham", "spam", "yam" };
			String s = (String) JOptionPane.showInputDialog(frame,
					"Complete the sentence:\n" + "\"Green eggs and...\"",
					"Customized Dialog", JOptionPane.PLAIN_MESSAGE, null,
					possibilities, "ham");
			System.out.println(s);
			break;
		case 8:
			String s1 = (String) JOptionPane.showInputDialog(frame,
					"Complete the sentence:\n" + "\"Green eggs and...\"",
					"Customized Dialog", JOptionPane.PLAIN_MESSAGE, null, null,
					"ham");
			System.out.println(s1);
			break;
		}

	}

	protected static ImageIcon createImageIcon(String path) {
		String imgURL = "src/JDialog/calvin.jpg";
		return new ImageIcon(imgURL);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DialogExample();
			}
		});
	}
}
