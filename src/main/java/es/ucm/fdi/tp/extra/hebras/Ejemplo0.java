package es.ucm.fdi.tp.extra.hebras;

// Ejemplo motivador del uso de threads.  
// NO FUNCIONA CORRECTAMENTE. Ver Ejemplo2.java.

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Ejemplo0 extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lbl;
	private int contador;

	public Ejemplo0() {
		super("Contador de segundos que bloquea el programa");
		this.getContentPane().setLayout(new FlowLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel pnlContador = new JPanel();
		this.getContentPane().add(pnlContador);		
		pnlContador.setBorder(BorderFactory.createTitledBorder("Contador"));
		pnlContador.setLayout(new BorderLayout());
			
		final JButton btnStart = new JButton("Iniciar");
		btnStart.setFont(new Font("Dialog",Font.PLAIN,36));
		pnlContador.add(btnStart, BorderLayout.NORTH);
			
		lbl = new JLabel("0");
		lbl.setFont(new Font("Dialog",Font.PLAIN,36));
		pnlContador.add(lbl, BorderLayout.CENTER);
			
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ejecutaContador();
			}
		});

		this.pack();
	}
		
	private void ejecutaContador() {
		long espera = System.currentTimeMillis();
		// Esperamos 4000 ms. ESPERA ACTIVA
		while (espera + 4000 > System.currentTimeMillis()) 
			{}	
		contador++;
		final String strNum = Integer.toString(contador);
		lbl.setText(strNum);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    Ejemplo0 v = new Ejemplo0();
				v.setVisible(true);
			}});

	}

}
