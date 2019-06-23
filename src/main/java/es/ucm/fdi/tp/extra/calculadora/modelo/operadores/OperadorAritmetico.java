package es.ucm.fdi.tp.extra.calculadora.modelo.operadores;

import es.ucm.fdi.tp.extra.calculadora.modelo.ExcNumeroNoValido;

public interface OperadorAritmetico  {
	
	public abstract OperadorAritmetico parsea(String oa) throws ExcNumeroNoValido;
	public abstract double ejecuta();
}
