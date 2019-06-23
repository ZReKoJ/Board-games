package es.ucm.fdi.tp.extra.calculadora.modelo.operadores;

import es.ucm.fdi.tp.extra.calculadora.modelo.ExcNumeroNoValido;
import es.ucm.fdi.tp.extra.calculadora.modelo.ParserOperacionAritmetica;

public class Opuesto extends OperadorUnario {

	public Opuesto() {
		super(0.0);
	}

	public Opuesto(double op) {
		super(op);
	}

	@Override
	public String toString() {
		return  "(-" + op + ")";
	}

	@Override
	public double ejecuta() {
		return -1.0*op;
	}

	public OperadorAritmetico parsea(String oa) throws ExcNumeroNoValido {
		String[] elems = oa.split(" ");
		if (elems.length == 2 && elems[0].equalsIgnoreCase("-") &&
				ParserOperacionAritmetica.esNumero(elems[1]))
			return new Opuesto(ParserOperacionAritmetica.parseaNum(elems[1]));
		else
			return null;	
	}
}
