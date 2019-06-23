package es.ucm.fdi.tp.extra.swing.Introduccion;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Ejemplo1 {
	
	public static void main(String []args) {
		
		final JFrame ventana = new JFrame("Mi primera ventana - Ejemplo 1");
		ventana.setSize(320, 200);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				ventana.setVisible(true);
			}
			
		});
		
	}
	
}
