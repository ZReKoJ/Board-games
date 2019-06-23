package es.ucm.fdi.tp.view.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class InfoArea extends JPanel {
	 private JTextArea statusArea;
	 private JTextField text;
	 private JPanel ctrlPabel;
	 
	 public InfoArea(){
		 initGUI();
	 }
	 private void initGUI() {
		  this.setLayout(new BorderLayout());
		  this.setBorder(BorderFactory.createTitledBorder("Status Messages"));
		  statusArea = new JTextArea(15,20);
		  statusArea.setEditable(false);
		  statusArea.setLineWrap(true);
		  statusArea.setWrapStyleWord(true);
		  JScrollPane statusAreaScroll = new JScrollPane(statusArea);
		   
		  this.ctrlPabel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  text = new JTextField(8);
		  JButton sendMessage = new JButton("Send");
		  sendMessage.addActionListener(new ActionListener() {
		   @Override
		   public void actionPerformed(ActionEvent e) {
			   String t = text.getText().trim();
			   if (!t.equalsIgnoreCase(""))
				   chat(text.getText().trim());
			   text.setText("");
		   }
		  });
		  JButton clear = new JButton("Clear");
		  clear.addActionListener(new ActionListener() {
		   @Override
		   public void actionPerformed(ActionEvent e) {
		    clearStatusAreaContent();
		   }
		  });
		  text.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					String t = text.getText().trim();
					   if (!t.equalsIgnoreCase(""))
						   chat(text.getText().trim());
					   text.setText("");
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {}
			  
		  });
		  ctrlPabel.add(text);
		  ctrlPabel.add(sendMessage);
		  ctrlPabel.add(clear);
		  this.add(statusAreaScroll, BorderLayout.CENTER);
		  this.add(ctrlPabel, BorderLayout.PAGE_END);
		  this.setPreferredSize(new Dimension(200,200));
		  this.setMinimumSize(new Dimension(200,200));
		   
	 }
	 final protected void clearStatusAreaContent(){
	  statusArea.setText("");
	 }
	 public void addContent(String msg){
	  statusArea.append("* " + msg + "\n");
	 }
	 public void setContent(String msg){
	  statusArea.setText(msg);
	 }
	 public String getContent(){
	  return statusArea.getText();
	 }
	 public abstract void chat(String msg);
}