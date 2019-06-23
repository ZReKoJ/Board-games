package es.ucm.fdi.tp.extra.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ScrollPaneExample extends JFrame {

	public ScrollPaneExample() {
		super("[=] ScrollPane Example [=]");
		initGUI();
	}

	private void initGUI() {

		// As usual, we create our bottom-level panel.
		JPanel mainPanel = new JPanel(new BorderLayout());

		// This is the story we took from Wikipedia.
		String story = "The Internet Foundation Classes (IFC) were a graphics "
				+ "library for Java originally developed by Netscape Communications "
				+ "Corporation and first released on December 16, 1996.\n\n"
				+ "On April 2, 1997, Sun Microsystems and Netscape Communications"
				+ " Corporation announced their intention to combine IFC with other"
				+ " technologies to form the Java Foundation Classes. In addition "
				+ "to the components originally provided by IFC, Swing introduced "
				+ "a mechanism that allowed the look and feel of every component "
				+ "in an application to be altered without making substantial "
				+ "changes to the application code. The introduction of support "
				+ "for a pluggable look and feel allowed Swing components to "
				+ "emulate the appearance of native components while still "
				+ "retaining the benefits of platform independence. This feature "
				+ "also makes it easy to have an individual application's appearance "
				+ "look very different from other native programs.\n\n"
				+ "Originally distributed as a separately downloadable library, "
				+ "Swing has been included as part of the Java Standard Edition "
				+ "since release 1.2. The Swing classes are contained in the "
				+ "javax.swing package hierarchy.\n\n";

		// We create the TextArea and pass the story in as an argument.
		// We also set it to be non-editable, and the line and word wraps set to
		// true.
		JTextArea storyArea = new JTextArea(story);
		storyArea.setEditable(true);
		storyArea.setLineWrap(true);
		storyArea.setWrapStyleWord(true);

		// We create the ScrollPane and instantiate it with the TextArea as an
		// argument
		// along with two constants that define the behaviour of the scrollbars.
		JScrollPane area = new JScrollPane(storyArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// We then set the preferred size of the scrollpane.
		area.setPreferredSize(new Dimension(300, 200));

		// and add it to the GUI.
		mainPanel.add(area);
		mainPanel.setOpaque(true);

		this.setContentPane(mainPanel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(350, 300);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ScrollPaneExample();
			}
		});
	}
}
