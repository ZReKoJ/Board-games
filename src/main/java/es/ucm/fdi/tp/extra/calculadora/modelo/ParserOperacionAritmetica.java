package es.ucm.fdi.tp.extra.calculadora.modelo;

import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.Division;
import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.Multiplicacion;
import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.Numero;
import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.OperadorAritmetico;
import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.Opuesto;
import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.RaizCuadrada;
import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.Resta;
import es.ucm.fdi.tp.extra.calculadora.modelo.operadores.Suma;

public class ParserOperacionAritmetica {

	private static OperadorAritmetico[] ops = {
		new Numero(),
		new Opuesto(), 
		new RaizCuadrada(), 
		new Suma(), 
		new Resta(), 
		new Multiplicacion(), 
		new Division()};

	public static OperadorAritmetico parsea(String oa) throws ExcNumeroNoValido {
		int i = 0;
		OperadorAritmetico operador = null;
		while (i < ops.length && operador == null) {
			operador = ops[i].parsea(oa);
			i++;
		}
		
		return operador;
	}
	
	public static boolean esNumero(String str) {
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static double parseaNum(String str) throws ExcNumeroNoValido {
		try {
			double d = Double.parseDouble(str);
			return d;
		} catch (Exception e) {
			throw new ExcNumeroNoValido("Número no válido: [" + str + "]");
		}
	}
	
}
