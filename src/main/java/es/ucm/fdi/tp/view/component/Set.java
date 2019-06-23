package es.ucm.fdi.tp.view.component;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Set extends JPanel {

	protected ArrayList<JButton> buttons;
	
	public interface ControlPane {
		void addButton();
	}
	
	public Set(){
		buttons = new ArrayList<>();
		initGUI();
	}
	
	private void initGUI(){
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setVisible(true);
	}
	
}
