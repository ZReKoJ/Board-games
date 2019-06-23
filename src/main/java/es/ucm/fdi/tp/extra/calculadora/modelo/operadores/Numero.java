package es.ucm.fdi.tp.extra.calculadora.modelo.operadores;

import es.ucm.fdi.tp.extra.calculadora.modelo.ExcNumeroNoValido;
import es.ucm.fdi.tp.extra.calculadora.modelo.ParserOperacionAritmetica;

public class Numero extends OperadorUnario {

	public Numero() {
		super(0.0);
	}

	public Numero(double op) {
		super(op);
	}

	@Override
	public String toString() {
		return  Double.toString(op);
	}

	@Override
	public double ejecuta() {
		return op;
	}

	public OperadorAritmetico parsea(String oa) throws ExcNumeroNoValido {
		String[] elems = oa.split(" ");
		if (elems.length == 1 && 
				ParserOperacionAritmetica.esNumero(elems[0]))
			return new Numero(ParserOperacionAritmetica.parseaNum(elems[0]));
		else
			return null;	
	}
}
