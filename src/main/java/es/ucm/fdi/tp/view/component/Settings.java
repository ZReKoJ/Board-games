package es.ucm.fdi.tp.view.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

import es.ucm.fdi.tp.base.Utils;

@SuppressWarnings("serial")
public abstract class Settings extends JToolBar{
	private JButton randButton, smartButton, restartButton, closeButton;
	private JComboBox<String> playerMode;
	protected String[] pM = "Manual Random Smart".split(" +");
	private JSpinner nThreadsSpinner;
	private JSpinner timeOutSpinner;
	private JLabel comboLabel;
	private JLabel spinnerLabel;
	private JButton stopButton;

	public Settings() {
		initGUI();
	}
	private void initGUI(){
		
		randButton = new JButton(new ImageIcon(Utils.loadImage("dice.png")));
		randButton.setToolTipText("random"); // Cuando pones el raton en el icono te sale el texto
		randButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("rand");
				randPressed();
			}
		});
		
		smartButton = new JButton(new ImageIcon(Utils.loadImage("nerd.png")));
		smartButton.setToolTipText("smart");
		smartButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("smart");
				smartPressed();
			}
		});
		
		restartButton = new JButton(new ImageIcon(Utils.loadImage("restart.png")));
		restartButton.setToolTipText("restart");
		restartButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("restart");
				restartPressed();
			}
		});
		
		closeButton = new JButton(new ImageIcon(Utils.loadImage("exit.png")));
		closeButton.setToolTipText("exit");
		closeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("close");
				closePressed();
			}
		});
		
		playerMode = new JComboBox<String>(pM);
		playerMode.setSelectedIndex(getComboBoxIndex());
		playerMode.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e){
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				comboBoxChanged((String)cb.getSelectedItem());
			}
		});
		
		comboLabel = new JLabel(new ImageIcon(Utils.loadImage("brain.png")));
		comboLabel.setOpaque(true);
		
		spinnerLabel = new JLabel( new ImageIcon(Utils.loadImage("timer.png")));
		
		nThreadsSpinner = new JSpinner( new SpinnerNumberModel(getSmartThreads(), 1, Runtime.getRuntime().availableProcessors(), 1));
		nThreadsSpinner.addChangeListener( (e) -> setSmartThreads((Integer)nThreadsSpinner.getValue()));
		
		timeOutSpinner = new JSpinner( new SpinnerNumberModel(getSmartTimeOut(),0, 5000, 500));
		timeOutSpinner.addChangeListener( (e) -> setSmartTimeOut((Integer)timeOutSpinner.getValue()));
		
		stopButton = new JButton("");
		stopButton.setIcon(new ImageIcon(Utils.loadImage("stop.png")));
		stopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.print("stop");
				Settings.this.stopPressed();
			}
		});
		stopButton.setEnabled(false);
		
		Dimension buttonDim = new Dimension(35, 35);
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Control"));
			controlPanel.add(randButton);
			randButton.setPreferredSize(buttonDim);
			controlPanel.add(smartButton);
			smartButton.setPreferredSize(buttonDim);
			controlPanel.add(restartButton);
			restartButton.setPreferredSize(buttonDim);
			controlPanel.add(closeButton);
			closeButton.setPreferredSize(buttonDim);
			controlPanel.add(new JLabel("PlayerMode"));
			controlPanel.add(playerMode);
		JPanel smartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		smartPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Smart Move"));
			smartPanel.add(comboLabel);
			smartPanel.add(nThreadsSpinner);
			smartPanel.add(new JLabel("threads"));
			smartPanel.add(spinnerLabel);
			smartPanel.add(timeOutSpinner);
			smartPanel.add(new JLabel("ms."));
			smartPanel.add(stopButton);
			stopButton.setPreferredSize(buttonDim);
		this.add(controlPanel);
		this.add(smartPanel);
		
	}


	protected abstract int getSmartThreads();
	
	protected abstract void setSmartThreads(int n);
	
	protected abstract int getSmartTimeOut();
	
	protected abstract void setSmartTimeOut(int n);
	
	protected abstract void randPressed();
	
	protected abstract void smartPressed();
	
	protected abstract void restartPressed();
	
	protected abstract void closePressed();
	
	protected abstract void stopPressed();
	
	protected abstract void comboBoxChanged(String option);
	
	protected abstract int getComboBoxIndex();
	
	public void enable(){
		this.randButton.setEnabled(true);
		this.smartButton.setEnabled(true);
		//this.restartButton.setEnabled(true);
	}
	
	public void disable(){
		this.randButton.setEnabled(false);
		this.smartButton.setEnabled(false);
		//this.restartButton.setEnabled(false);
	}
	public void setThinking(boolean thinking) {
		if(thinking){
			stopButton.setEnabled(true);
			comboLabel.setBackground(Color.YELLOW);
			comboLabel.setOpaque(true);
		}
		else{
			comboLabel.setBackground(null);
			comboLabel.setOpaque(true);
			stopButton.setEnabled(false);
		}
	}
	
}

