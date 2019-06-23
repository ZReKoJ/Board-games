package es.ucm.fdi.tp.extra.calculadora.modelo;

public interface ModeloCalcObserver {
	void resultadoCalc(double resultado);
	void errorCalc(String msg);
	public void comprobacionOK();
}
