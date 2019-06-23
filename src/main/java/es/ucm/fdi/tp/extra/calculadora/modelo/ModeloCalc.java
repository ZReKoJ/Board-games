package es.ucm.fdi.tp.extra.calculadora.modelo;
import java.util.ArrayList;

import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.OperadorAritmetico;


public class ModeloCalc {
	private ArrayList<ModeloCalcObserver> observers;
	
	public ModeloCalc() {
		observers = new ArrayList<ModeloCalcObserver>();
	}

	public void comprobar(String expr) {
		try {
			OperadorAritmetico op = ParserOperacionAritmetica.parsea(expr);
			if (op != null)
				notificaComprobacionOK();
			else
				notificaError("No te entiendo...");
		} catch(ExcNumeroNoValido e) {
			notificaError(e.getMessage());
		}		
	}
	
	public void calcular(String expr) {
		try {
			OperadorAritmetico op = ParserOperacionAritmetica.parsea(expr);
			if (op != null) {
				double res = op.ejecuta();
				notificaResultado(res);
			} else 
				notificaError("No te entiendo: [" + expr + "]");
		} catch(ExcNumeroNoValido e) {
			notificaError(e.getMessage());
		}
	}
	
	public void addObserver(ModeloCalcObserver o) {
		observers.add(o);
	}

	public void removeObserver(ModeloCalcObserver o) {
		
	}

	private void notificaResultado(double res) {
		for (ModeloCalcObserver ob : observers) 
			ob.resultadoCalc(res);
	}
	
	private void notificaError(String msg) {
		for (ModeloCalcObserver ob : observers) 
			ob.errorCalc(msg);
	}
	
	private void notificaComprobacionOK(){
		for (ModeloCalcObserver ob : observers) 
			ob.comprobacionOK();		
	}
	
}
