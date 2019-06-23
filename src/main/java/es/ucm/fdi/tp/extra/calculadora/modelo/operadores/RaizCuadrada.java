package es.ucm.fdi.tp.extra.calculadora.modelo.operadores;

import es.ucm.fdi.tp.extra.calculadora.modelo.ExcNumeroNoValido;
import es.ucm.fdi.tp.extra.calculadora.modelo.ParserOperacionAritmetica;

public class RaizCuadrada extends OperadorUnario {

	public RaizCuadrada() {
		super(0.0);
	}
	
	public RaizCuadrada(double op) {
		super(op);
	}
	@Override
	public String toString() {
		return  "raiz(" + op + ")";
	}

	@Override
	public double ejecuta() {
		return Math.sqrt(op);
	}

	public OperadorAritmetico parsea(String oa) throws ExcNumeroNoValido {
		String[] elems = oa.split(" ");
		if (elems.length == 2 && elems[0].equalsIgnoreCase("raiz") &&
				ParserOperacionAritmetica.esNumero(elems[1]))
			return new RaizCuadrada(ParserOperacionAritmetica.parseaNum(elems[1]));
		else
			return null;		
	}
}
