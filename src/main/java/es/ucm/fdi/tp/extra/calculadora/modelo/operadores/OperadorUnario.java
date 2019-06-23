package es.ucm.fdi.tp.extra.calculadora.modelo.operadores;

public abstract class OperadorUnario implements OperadorAritmetico {
	protected double op;

	public OperadorUnario() {
		op = 0.0;
	}

	public OperadorUnario(double op) {
		this.op = op;
	}
}
