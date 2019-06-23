package es.ucm.fdi.tp.extra.calculadora.modelo.operadores;

import es.ucm.fdi.tp.extra.calculadora.modelo.ExcNumeroNoValido;
import es.ucm.fdi.tp.extra.calculadora.modelo.ParserOperacionAritmetica;

public class Division extends OperadorBinario {

	public Division() {
		super(0.0, 0.0);
	}
	
	public Division(double op1, double op2) {
		super(op1, op2);
	}

	public String toString() {
		return  "(" + op1 + "/" + op2 + ")";
	}

	public double ejecuta() {
		if (op2 != 0.0)
			return op1 / op2;
		else if (op1 < 0.0)
			return Double.NEGATIVE_INFINITY;
		else
			return Double.POSITIVE_INFINITY;
	}
	
	public OperadorAritmetico parsea(String oa) throws ExcNumeroNoValido {
		String[] elems = oa.split(" ");
		if (elems.length == 3 && elems[1].equalsIgnoreCase("/") &&
				ParserOperacionAritmetica.esNumero(elems[0]) &&
				ParserOperacionAritmetica.esNumero(elems[2]))
			return new Division(ParserOperacionAritmetica.parseaNum(elems[0]),ParserOperacionAritmetica.parseaNum(elems[2]));
		else
			return null;		
	}
}
