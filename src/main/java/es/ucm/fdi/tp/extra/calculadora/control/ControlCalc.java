package es.ucm.fdi.tp.extra.calculadora.control;

import es.ucm.fdi.tp.extra.calculadora.modelo.ModeloCalc;
import es.ucm.fdi.tp.extra.calculadora.modelo.ModeloCalcObserver;

public class ControlCalc {
	private ModeloCalc modelo;

	public ControlCalc(ModeloCalc modelo) {
		this.modelo = modelo;
	}
	
	public void calcular(String expr) {
		expr = expr.trim();
		if (!expr.equals(""))
			modelo.calcular(expr);
	}
	
	public void comprobar(String expr) {
		expr = expr.trim();
		if (!expr.equals(""))
			modelo.comprobar(expr);
	}
	
	public void addObserver(ModeloCalcObserver o) {
		modelo.addObserver(o);
	}

	public void removeObserver(ModeloCalcObserver o) {
		modelo.removeObserver(o);
	}
}
