package es.ucm.fdi.tp.extra.swing.Introduccion;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Ejemplo3 extends JFrame {

	private static final long serialVersionUID = 1L;

	public Ejemplo3() {
		
		super("Mi primera ventana - Ejemplo3");
		this.setSize(320, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			JButton boton = new JButton("Pulsa para saludar");
			boton.addActionListener(new MiActionListener());
		
		this.getContentPane().add(boton);
		
	}

	public class MiActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			System.out.println("Hola Mundo!");
		}
		
	}

	public static void main(String []args) {

		final Ejemplo3 v = new Ejemplo3();
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				v.setVisible(true);
			}
			
		});
		
	}
	
}
