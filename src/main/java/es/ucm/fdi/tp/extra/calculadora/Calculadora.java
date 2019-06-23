package es.ucm.fdi.tp.extra.calculadora;
import java.awt.EventQueue;

import es.ucm.fdi.tp.extra.calculadora.control.ControlCalc;
import es.ucm.fdi.tp.extra.calculadora.modelo.ModeloCalc;
import es.ucm.fdi.tp.extra.calculadora.vista.VistaCalc;


public class Calculadora {

	public static void main(String[] args) {
		
		ModeloCalc modelo = new ModeloCalc();
		final ControlCalc control = new ControlCalc(modelo);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				final VistaCalc vista = new VistaCalc(control);
				vista.setVisible(true);
			}});
	}
}
