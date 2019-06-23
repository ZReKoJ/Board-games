package es.ucm.fdi.tp.extra.calculadora.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import es.ucm.fdi.tp.extra.calculadora.control.ControlCalc;
import es.ucm.fdi.tp.extra.calculadora.modelo.ModeloCalcObserver;


public class VistaCalc extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ControlCalc control;
	private MiPanelPantalla pnlPantalla;
	MiPanelMensajes pnlMensajes;
	
	private static final String OP_SUMAR = " + ";
	private static final String OP_RESTAR = " - ";
	private static final String OP_MULTIPLICAR = " * ";
	private static final String OP_DIVIDIR = " / ";
	private static final String ESPACIO = " ";
	
	public VistaCalc(ControlCalc c) {
		super("Calculadora con MVC");

		control = c;
		
		this.setSize(450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		JPanel pnlNorth = new JPanel();
		
		pnlPantalla = new MiPanelPantalla();
		pnlMensajes = new MiPanelMensajes();
		
		this.getContentPane().add(pnlNorth,BorderLayout.NORTH);
		pnlNorth.setLayout(new GridLayout(2,1,5,5));
		pnlNorth.add(pnlPantalla);		
		pnlNorth.add(pnlMensajes);
		control.addObserver(pnlPantalla);
		control.addObserver(pnlMensajes);

		
		JPanel pnlSouth = new JPanel();
		pnlSouth.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlSouth.setLayout(new GridLayout(1,3,5,5));
		this.getContentPane().add(pnlSouth,BorderLayout.SOUTH);

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new LimpiarListener());
		JButton btnCalcular = new JButton("Calcular");
		btnCalcular.addActionListener(new CalcularListener());
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VistaCalc.this.dispose();
			}
		});
		pnlSouth.add(btnLimpiar);
		pnlSouth.add(btnCalcular);
		pnlSouth.add(btnSalir);
		
		JPanel pnlCentro = new JPanel();
		pnlCentro.setLayout(new BoxLayout(pnlCentro,BoxLayout.X_AXIS));
		JPanel pnlNumeros = new JPanel();
		pnlNumeros.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlNumeros.setLayout(new GridLayout(4,3,5,5));
		for (int i = 1; i < 10; i++) {
		    JButton btnI = creaBotonOp(Integer.toString(i));
		    pnlNumeros.add(btnI);
		}
		pnlNumeros.add(creaBotonOp("."));
		pnlNumeros.add(creaBotonOp("0"));
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new BorrarListener());
		pnlNumeros.add(btnBorrar);

		pnlCentro.add(pnlNumeros);
		
		JPanel pnlOperaciones = new JPanel();
		pnlOperaciones.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlOperaciones.setLayout(new GridLayout(4,1,5,5));
		pnlOperaciones.add(creaBotonOp(OP_SUMAR));
		pnlOperaciones.add(creaBotonOp(OP_RESTAR));
		pnlOperaciones.add(creaBotonOp(OP_MULTIPLICAR));
		pnlOperaciones.add(creaBotonOp(OP_DIVIDIR));
		pnlCentro.add(pnlOperaciones);

		JPanel pnlPrincipal = new JPanel();
		pnlPrincipal.setLayout(new BorderLayout());
		pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		this.getContentPane().add(pnlCentro,BorderLayout.CENTER);

	}
	
	private JButton creaBotonOp(final String lbl) {
		JButton btnOp = new JButton(lbl);
		btnOp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnlPantalla.append(lbl);
				control.comprobar(pnlPantalla.getText());
			}
		});
		return btnOp;
	}

	private class LimpiarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			pnlPantalla.limpiar();
		}
	}
	
	private class BorrarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			pnlPantalla.backspace();
			control.comprobar(pnlPantalla.getText());
		}
	}
	
	private class CalcularListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				control.calcular(pnlPantalla.getText());
			}
		}
	
	private class MiPanelPantalla extends JPanel implements ModeloCalcObserver {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JLabel lblPantallaCalc;
	
		public MiPanelPantalla() {
			this.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(5,5,5,5),
					BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
			lblPantallaCalc = new JLabel(ESPACIO);
			this.add(lblPantallaCalc);
		}
		
		public String getText() {
			return lblPantallaCalc.getText();
		}
		
		public void limpiar() {
			lblPantallaCalc.setText(ESPACIO);
		}
		
		public void backspace() {
			String str = lblPantallaCalc.getText().trim();
			if (!str.equals(""))
				lblPantallaCalc.setText(str.substring(0, str.length()-1));
		}

		public void append(String lbl) {
			lblPantallaCalc.setText(lblPantallaCalc.getText() + lbl);
		}
		
		@Override
		public void comprobacionOK() {}

		@Override
		public void resultadoCalc(double resultado) {
			lblPantallaCalc.setText(Double.toString(resultado));
		}

		@Override
		public void errorCalc(String msg) {}
	}

	private class MiPanelMensajes extends JPanel implements ModeloCalcObserver {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JLabel lblMensajesCalc;
		
		public MiPanelMensajes() {
			this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			lblMensajesCalc = new JLabel(ESPACIO);
			this.add(lblMensajesCalc);
		}

		@SuppressWarnings("unused")
		public void limpiar() {
			lblMensajesCalc.setText(ESPACIO);
		}
		
		@Override
		public void comprobacionOK() {
			lblMensajesCalc.setText(ESPACIO);
		}

		@Override
		public void resultadoCalc(double resultado) {}

		@Override
		public void errorCalc(String msg) {
			lblMensajesCalc.setText(msg);
		}

	}

}
