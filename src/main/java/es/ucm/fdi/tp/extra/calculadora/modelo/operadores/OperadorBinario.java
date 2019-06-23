package es.ucm.fdi.tp.extra.calculadora.modelo.operadores;

public abstract class OperadorBinario implements OperadorAritmetico {
	protected double op1;
	protected double op2;
	
	public OperadorBinario(double op1, double op2) {
		this.op1 = op1;
		this.op2 = op2;
	}
	
	
}
