package es.ucm.fdi.tp.was;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import es.ucm.fdi.tp.game.was.WasAction;
import es.ucm.fdi.tp.game.was.WasFigures;
import es.ucm.fdi.tp.game.was.WasState;
import es.ucm.fdi.tp.game.was.WasWolfMovements;
import es.ucm.fdi.tp.launcher.MainPr5;

public class WasStateTest {
	
	@Test
	public void wolfSurroundedOfSheeps() { 
		// creo un tablero inicial
		int board[][] = new int[WasState.DIM][];
        for (int i = 0; i < WasState.DIM; i++) {
        	board[i] = new int[WasState.DIM];
        	for (int j = 0; j < WasState.DIM; j++) {
        		if ((i + j) % 2 == 0)
        			board[i][j] = WasFigures.WHITE.getRepresentation();
        		else board[i][j] = WasFigures.BLACK.getRepresentation();
        	}
        }
        int wolfY = 1, wolfX = 1;
        // situo el lobo encerrado entre 2 ovejas, las otras 2 las dejo en su posición inicial
        board[wolfY][wolfX] = WasFigures.WOLF.getRepresentation();
        for (WasWolfMovements wsm : WasWolfMovements.values())
			board[wsm.getY() + wolfY][wsm.getX() + wolfX] = WasFigures.SHEEP.getRepresentation();
		// compruebo que las ovejas hayan ganado
		assertEquals("El lobo esta acorralado, no tiene movimientos, las ovejas ganan",
				true, WasState.isWinner(board, WasFigures.SHEEP.getRepresentation()));
		
	}
	@Test
	public void wolfIsAtTop() { 
		// creo un tablero inicial
		int board[][] = new int[WasState.DIM][];
        for (int i = 0; i < WasState.DIM; i++) {
        	board[i] = new int[WasState.DIM];
        	for (int j = 0; j < WasState.DIM; j++) {
        		if ((i + j) % 2 == 0)
        			board[i][j] = WasFigures.WHITE.getRepresentation();
        		else board[i][j] = WasFigures.BLACK.getRepresentation();
        	}
        }
        // situo el lobo en la fila inicial para ver si ha ganado
        board[0][7] = WasFigures.WOLF.getRepresentation();
        board[0][1] = WasFigures.SHEEP.getRepresentation();
        board[0][3] = WasFigures.SHEEP.getRepresentation();
        board[0][5] = WasFigures.SHEEP.getRepresentation();
        board[2][7] = WasFigures.SHEEP.getRepresentation();
		// compruebo que el lobo ha ganado
		assertEquals("El lobo ha llegado al final y gana",
				true, WasState.isWinner(board, WasFigures.WOLF.getRepresentation()));
		
	}
	
	@Test
	public void initialPositionWolfMovements() { 
		// creo el estado inicial del tablero
		WasState initialState = new WasState(WasState.DIM);
		// creo la lista de acciones validas iniciales para el lobo
		List<WasAction> valid = initialState.validActions(WasFigures.WOLF.getRepresentation());
		// compruebo que el lobo solo tenga 1 movimiento inicial
		assertEquals("El lobo solo tiene 1 movimiento inicial", 1, valid.size());
		
	}
	
	@Test
	public void afterInitialPositionWolfMovements() {
		// creo un tablero inicial
		int board[][] = new int[WasState.DIM][];
        for (int i = 0; i < WasState.DIM; i++) {
        	board[i] = new int[WasState.DIM];
        	for (int j = 0; j < WasState.DIM; j++) {
        		if ((i + j) % 2 == 0)
        			board[i][j] = WasFigures.WHITE.getRepresentation();
        		else board[i][j] = WasFigures.BLACK.getRepresentation();
        	}
        }
        // situo el lobo después de su movimiento inicial
        board[WasState.DIM - 2][1] = WasFigures.WOLF.getRepresentation();
        /* muevo la primera oveja hacia abajo a la derecha simplemente para probar los
        movimientos del lobo*/
        board[1][2] = WasFigures.SHEEP.getRepresentation();
        board[0][3] = WasFigures.SHEEP.getRepresentation();
        board[0][5] = WasFigures.SHEEP.getRepresentation();
        board[0][7] = WasFigures.SHEEP.getRepresentation();
        // creo el siguiente estado al inicial del lobo y pongo el turno al lobo
		WasState nextToInitialState = new WasState(WasFigures.SHEEP.getRepresentation(), board, false);
		// creo una lista de acciones validas para el lobo
		List<WasAction> valid = nextToInitialState.validActions(WasFigures.WOLF.getRepresentation());
		// compruebo que sean validas las 4 acciones iniciales
		assertEquals("El lobo tiene 4 movimientos", 4, valid.size());
	}
	@Test
	public void possibleSheepMovements() {
		WasState InitialState = new WasState(WasState.DIM);
		// creo una lista de acciones validas para las ovejas
		List<WasAction> valid = InitialState.validActions(WasFigures.SHEEP.getRepresentation());
		// compruebo que sean validas los movimientos de las ovejas, son 7 ya que 
		// una de ellas esta en un lateral
		assertEquals("Las ovejas tienen 2 movimientos iniciales menos 1", 7, valid.size());
		
		//Compruebo que si una oveja está en un lateral solo tiene 1 movimiento
		// creo un tablero inicial
		int board[][] = new int[WasState.DIM][];
        for (int i = 0; i < WasState.DIM; i++) {
        	board[i] = new int[WasState.DIM];
        	for (int j = 0; j < WasState.DIM; j++) {
        		if ((i + j) % 2 == 0)
        			board[i][j] = WasFigures.WHITE.getRepresentation();
        		else board[i][j] = WasFigures.BLACK.getRepresentation();
        	}
        }
        // situo el lobo en una posición cualquiera 
        board[5][2] = WasFigures.WOLF.getRepresentation();
        /* situo 4 ovejas en laterales diferentes*/
        board[3][0] = WasFigures.SHEEP.getRepresentation();
        board[2][7] = WasFigures.SHEEP.getRepresentation();
        board[0][7] = WasFigures.SHEEP.getRepresentation();
        board[1][0] = WasFigures.SHEEP.getRepresentation();
        //creo el estado
        WasState nextState = new WasState(WasFigures.WOLF.getRepresentation(), board, false);
        //creo la lista de acciones validas para las ovejas
        List<WasAction> valid1 = nextState.validActions(WasFigures.SHEEP.getRepresentation());
        assertEquals("Las ovejas en los laterales solo tienen 1 movimiento cada 1", 4, valid1.size());
	}
	@Test
	public void Experiments(){
		String args[] = { "was", "rand", "rand" };
		for (int i = 0; i < 100; ++i){
			MainPr5.main(args);
		}
	}
	

}
