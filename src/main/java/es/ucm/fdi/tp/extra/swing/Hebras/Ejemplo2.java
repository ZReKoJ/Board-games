package es.ucm.fdi.tp.extra.swing.Hebras;import java.awt.BorderLayout;
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
import javax.swing.SwingUtilities;

public class Ejemplo2 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ejemplo2() {
		super("Ejemplo de múltiples hilos de ejecución");
		this.getContentPane().setLayout(new FlowLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		MiContador contador1 = new MiContador();
		this.getContentPane().add(contador1);
		MiContador contador2 = new MiContador();
		this.getContentPane().add(contador2);
		MiContador contador3 = new MiContador();
		this.getContentPane().add(contador3);
		
		this.pack();
	}
	
	private class MiContador extends JPanel implements  Runnable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JLabel lbl;
		private Thread hiloContador = null;
		
		public MiContador() {
			this.setBorder(BorderFactory.createTitledBorder("Contador"));
			this.setLayout(new BorderLayout());
			
			final JButton btnStart = new JButton("Iniciar");
			btnStart.setFont(new Font("Dialog",Font.PLAIN,36));
			this.add(btnStart, BorderLayout.NORTH);
			
			lbl = new JLabel("0");
			lbl.setFont(new Font("Dialog",Font.PLAIN,36));
			this.add(lbl, BorderLayout.CENTER);
			
			final JButton btnStop = new JButton("Parar");
			btnStop.setFont(new Font("Dialog",Font.PLAIN,36));
			this.add(btnStop, BorderLayout.SOUTH);

			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (hiloContador == null) {
						btnStart.setEnabled(false);
						hiloContador = new Thread(MiContador.this);
						hiloContador.start();
					}
				}
			});

			btnStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (hiloContador != null) {
						hiloContador.interrupt();
						hiloContador = null;
					}
					btnStart.setEnabled(true);
				}
			});
		}
		
		public void run() {
			int desde = 1;
			while (!Thread.currentThread().isInterrupted()) {
				//System.out.println("desde=" + Integer.toString(desde));
				final String strNum = Integer.toString(desde);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						lbl.setText(strNum);
					}
				});
				@SuppressWarnings("unused")
				long espera = System.currentTimeMillis();
				//while (espera + 300 > System.currentTimeMillis()) {}
				// Este bucle while hace espera activa.
				// Es mejor utilizar espera pasiva con sleep(millis):
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					return;
				} 
				desde++;
			}
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Ejemplo2 v = new Ejemplo2();
				v.setVisible(true);
			}});

	}

}
