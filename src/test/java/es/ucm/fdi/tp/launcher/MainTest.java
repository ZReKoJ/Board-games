package es.ucm.fdi.tp.launcher;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MainTest {

	@Test
	public void tooManyArguments() {
		String args[] = {"was", "console", "smart", "smart"};
		assertEquals("Demasiados argumentos", false, MainPr4.validArguments(args));
	}
	
	@Test
	public void tooLessArguments() {
		String args[] = {"was", "console"};
		assertEquals("Pocos argumentos", false, MainPr4.validArguments(args));
	}
	
	@Test
	public void incorrectGameName() {
		String args[] = {"chess", "console", "smart"};
		assertEquals("Nombre de juego incorrecto", false, MainPr4.validArguments(args));
	}

}
