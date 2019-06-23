package es.ucm.fdi.tp.game.was3D;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameState;

/**
 * Representa un estado del juego Wolf And Sheep, con su respectivo tablero.
 * @author Diego Acuña y Zihao Hong
 *
 */
public class Was3DState extends GameState<Was3DState, Was3DAction> {

	private static final long serialVersionUID = -2641387354190472377L;
	
	private final int turn; // representa el turno, 1 para las ovejas, 0 para el lobo
    private final boolean finished; // booleano que representa si ha terminado el juego o no, true si termina, false si aun no 
    private final int[][] board; // matriz que representa un tablero de casillas blancas y negras alternadas
    private final int winner; // entero que representa al ganador, 1 ovejas, 0 lobo

    public final static int DIM = 8; // constante que representa la dimension del tablero
    public final static int PLAYERS = 2; // constante que representa el numero de jugadores que interactua en el juego
    /**
     * Constructora que crea el estado inicial del juego
     * @param dim, dimension del tablero
     */
    public Was3DState(int dim) { 
    	// llama a la constructora de la superclase pasando por parámetro el entero
    	// 2 ya que es un juego de dos jugadores.
        super(PLAYERS);
        
        // comprueba si la dimension que se le pasa como parametro se corresponde
        // con la constante que representa la dimension del tablero
        if (dim != DIM) {
            throw new IllegalArgumentException("Expected DIM to be " + DIM);
        }
        /* crea un nuevo tablero de casillas blancas y negras, blancas -2 y 
         * representacion " ", negras -1 y representacion "." de tamaño DIMxDIM
         */
        board = new int[dim][];
        for (int i=0; i < dim; i++) {
            board[i] = new int[dim];
            for (int j = 0; j < dim; j++) {
            	if ((i + j) % 2 == 0)
            		board[i][j] = Was3DFigures.WHITE.getRepresentation();
            	else board[i][j] = Was3DFigures.BLACK.getRepresentation();
            }
        }
        
        int contador;
        
        contador = 0;
        /* situamos las 4 ovejas en la primera fila en las casillas negras
        ademas las columnas las recorremos de dos en dos y solo miramos las 
        posiciones que se corresponderian con casillas negras (j = (i+1) % 2) 
        (j += 2) */
        for(int i = 0; i < DIM && contador < Was3DFigures.SHEEP.getNumber(); ++i){
    		for(int j = (i + 1) % 2; j < DIM && contador < Was3DFigures.SHEEP.getNumber(); j += 2){
    			board[i][j] = Was3DFigures.SHEEP.getRepresentation();
    			++contador;
    		}
        }
        
        contador = 0;
        /*Situamos el lobo en la esquina inferior izquierda del tablero*/
        for(int i = DIM - 1; i >= 0 && contador < Was3DFigures.WOLF.getNumber(); --i){
    		for(int j = (i + 1) % 2; j < DIM && contador < Was3DFigures.WOLF.getNumber(); j += 2){
    			board[i][j] = Was3DFigures.WOLF.getRepresentation();
    			++contador;
    		}
        }
        /*el turno ahora es el del lobo que es quien empieza siempre*/
        this.turn = 0;
        /*a -1 porque no hay ganador al principio*/
        this.winner = -1;
        /*a false ya que aun no ha terminado el juego */
        this.finished = false;
    }
    /**
     * Crea un estado que no es el inicial
     * @param turn, turno del jugador al que le toca
     * @param board, tablero actual
     * @param finished, booleano que indica si ha terminado el juego o no
     */
    public Was3DState(int turn, int[][] board, boolean finished) {
    	super(PLAYERS); 
        this.board = board;
        this.turn = (turn + 1) % 2;
        this.finished = finished;
        if (this.finished)
        	this.winner = turn;
        else this.winner = -1;
    } 
    /**
     * Comprueba si una accion es valida o no
     * @param action, accion a realizar
     * @param board2, tablero sobre el que se va a hacer el movimiento
     * @return true si la accion es valida, false si no lo es
     */
    public boolean isValid(Was3DAction action, int[][] board2) {
    	/*si el juego no ha terminado y que la columna y fila destino esten dentro
    	del tablero*/
        if (!isFinished() && action.getRowFin() >= 0 && 
        		action.getRowFin() < board2.length && action.getColFin() >= 0 
        		&& action.getColFin() < board2.length) {
        	// booleano que dira si es valida o no la accion
        	boolean valid = true;
        	/*si le toca a las ovejas o al lobo comprueba que solo se puedan mover
        	1 casilla y que sea una casilla negra*/
        	if (action.getPlayerNumber() == Was3DFigures.SHEEP.getRepresentation())
        		valid = action.getRowFin() - action.getRowIni() == 1;
        	else if (action.getPlayerNumber() == Was3DFigures.WOLF.getRepresentation())
        		valid = Math.abs(action.getRowFin() - action.getRowIni()) == 1;
        	valid = valid && Math.abs(action.getColFin() - action.getColIni()) == 1;
        	return valid && board2[action.getRowFin()][action.getColFin()] == Was3DFigures.BLACK.getRepresentation();
        }
        else {
        	/*si el juego ha terminado ya o si la columna o fila destino 
        	estan fuera del rango del tablero, la accion no es valida*/
            return false;
        }
    }

    /**
     * Comprueba las acciones validas para un jugador y devuelve una lista de
     * acciones validas
     * @param playerNumber, numero del jugador
     * @return valid, lista de acciones validas para el jugador playerNumber
     */
    public List<Was3DAction> validActions(int playerNumber) {
        ArrayList<Was3DAction> valid = new ArrayList<>();
        // si hemos terminado devolvemos una lista vacia
        if (finished) {
            return valid;
        }
        int contador;
        //ver movimientos posibles para las ovejas
        if(playerNumber == Was3DFigures.SHEEP.getRepresentation()){
        	contador = 0;
        	/* = (i+1) % 2 calcula en que fila estamos para saber
    		la alternacion entre casillas blancas y negras en las columnas*/
    		/*las recorremos de dos en dos porque solo hay ovejas en las
    		casillas negras y compruebo que he encontrado las 4 ovejas*/
        	for(int i = 0; i < DIM && contador < Was3DFigures.SHEEP.getNumber(); ++i){
        		for(int j = (i+1) % 2; j < DIM && contador < Was3DFigures.SHEEP.getNumber(); j+=2){
        			// comprueba si un movimiento es posible para las ovejas
        			if(at(i,j) == Was3DFigures.SHEEP.getRepresentation()){ // si hay una oveja en la pos i,j
        				contador++; // aumento mi contador de ovejas encontradas
        				Was3DAction wa; // creo una nueva accion
        				/* recorro todos los posibles movimientos que tiene una oveja
        				creo una nueva accion con la fila y columna origen y destino de 
        				la accion y compruebo si ese movimiento es valido y lo añado a la lista
        				de acciones validas
        				 */
        				for (Was3DSheepMovements wsm : Was3DSheepMovements.values()){
        					wa = new Was3DAction(Was3DFigures.SHEEP.getRepresentation(), i, j, wsm.getY() + i, wsm.getX() + j);
        					if (isValid(wa, this.board))
        						valid.add(wa);	
        				}
        			}
        		}
        	}
        }
        // ver movimientos posibles para el lobo
        else if (playerNumber == Was3DFigures.WOLF.getRepresentation()){
        	contador = 0;
        	/* j= (i+1) % 2 calcula en que fila estamos para saber
    		la alternacion entre casillas blancas y negras en las columnas*/
    		/*las recorremos de dos en dos porque el lobo solo puede estar en las
    		casillas negras y compruebo que he encontrado el numero de lobos correspondiente*/
        	for(int i = DIM-1; i >= 0 && contador < Was3DFigures.WOLF.getNumber(); --i){
        		for(int j = (i + 1) % 2; j < DIM && contador < Was3DFigures.WOLF.getNumber(); j+=2){
        			// compruebo movimientos posibles para el Lobo
        			if(at(i,j) == Was3DFigures.WOLF.getRepresentation()){
        				contador++; // aumento el contador de lobos encontrados
        				Was3DAction wa; // creo una nueva accion
        				/* recorro todos los posibles movimientos que tiene un lobo
        				creo una nueva accion con la fila y columna origen y destino de 
        				la accion y compruebo si ese movimiento es valido y lo añado a la lista
        				de acciones validas
        				 */
        				for (Was3DWolfMovements wsm : Was3DWolfMovements.values()){
        					wa = new Was3DAction(Was3DFigures.WOLF.getRepresentation(), i, j, wsm.getY() + i, wsm.getX() + j);
        					if (isValid(wa, this.board))
        						valid.add(wa);	
        				}
        			}
        		}
        	}
        }
        return valid;
    }
    
    /**
     * Comprueba si en este estado del juego, playerNumber ha ganado, ya sea
     * que haya ganado el lobo porque las ovejas no tienen movimientos, haya 
     * llegado a la fila superior del tablero o haya pasado a las ovejas y estas
     * como no pueden volver hacia atras ya ha ganado. O las ovejas han ganado
     * cuando acorralan al lobo y este no tiene movimientos
     * @param board2, tablero sobre el cual se comprueba si playerNumber ha ganado
     * @param playerNumber, jugador actual, 0 para lobo, 1 para ovejas
     * @return won, true si playerNumber ha ganado, false en caso contrario
     */
    public static boolean isWinner(int[][] board2, int playerNumber) {
    	boolean won = false;
    	// creo un nuevo estado con el board2 y playerNumber
    	Was3DState ws = new Was3DState(playerNumber, board2, false);
    	/*compruebo si su lista de acciones validas es vacia segun a quien le toque
    	el turno, entonces si es el turno del lobo habran ganado las ovejas y viceversa*/
    	if (ws.validActions(ws.getTurn()).isEmpty())
        	won = true;
    	else{
    		//si el jugador es el lobo
    		if (playerNumber == Was3DFigures.WOLF.getRepresentation()){
    			boolean found = false;
    			//busco el lobo en la primera fila, si lo encuentro ya ha ganado
    			for(int i = 1; i < board2.length; i+=2){
        			if(board2[0][i] == Was3DFigures.WOLF.getRepresentation()){
        				won = true;
        				found = true;
        			}
        		}
    			//busco los lobos o las ovejas, si lo que he encontrado es un lobo
    			//antes que una oveja, el lobo ha ganado, en caso contrario se sigue jugando
		    	for(int i = 0; i < board2.length && !found; ++i){
		        	for(int j = (i + 1) % 2; j < board2.length && !found; j+=2){
		        		if(board2[i][j] == Was3DFigures.WOLF.getRepresentation() || 
		        				board2[i][j] == Was3DFigures.SHEEP.getRepresentation()){
		        			found = true;
		        			won = board2[i][j] == Was3DFigures.WOLF.getRepresentation();
		        		}
		        	}
		        }
    		}
    	}

    	return won;
    }
    /**
     * Devuelve lo que haya en la posicion row, col del tablero
     * @param row, fila del tablero
     * @param col, columna del tablero
     * @return el entero que haya en dicha posicion
     */
    public int at(int row, int col) {
        return board[row][col];
    }
    /**
     * Devuelve el turno del jugador
     */
    public int getTurn() {
        return turn;
    }
    /**
     * Devuelve true si ha terminado el juego, false en caso contrario
     */
    public boolean isFinished() {
        return finished;
    }
    /**
     * Devuelve 0 si ha ganado el lobo, 1 si han ganado las ovejas
     */
    public int getWinner() {
        return winner;
    }

    /**
     * 
     * @return una copia del tablero
     */
    public int[][] getBoard() {
        int[][] copy = new int[board.length][];
        for (int i=0; i<board.length; i++) copy[i] = board[i].clone();
        return copy;
    }
    /**
     * Representa el tablero de DIMxDIM de casillas blancas y negras, 
     * las casillas blancas se representan con " " y las negras con
     * "."
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for(int i = 0; i< board.length; ++i)
        	sb.append("_" + i);
        sb.append("\n");
        for (int i = 0; i<board.length; i++) {
            sb.append(i + "|");
            for (int j = 0 ; j < board.length; j++) {
        		sb.append(" " + Was3DFigures.getCaracter(board[i][j]));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}
