package es.ucm.fdi.tp.game.was3D;

import es.ucm.fdi.tp.base.model.GameAction;
/**
 * Clase que representa la accion (movimiento) a realizar sobre las 
 * ovejas o el lobo en el juego Wolf And Sheep
 * @author Diego Acuña, Zihao Hong
 *
 */
public class Was3DAction implements GameAction<Was3DState, Was3DAction> {

	private static final long serialVersionUID = -8491198872908329925L;
	
	private int player; // numero del jugador, 0 para el lobo, 1 para la oveja
    private int row_ini; // fila inicial
    private int col_ini; // columna inicial
    private int row_fin; // fila destino
    private int col_fin; // columna destino
    
    public Was3DAction(int player, int row_ini, int col_ini, int row_fin, int col_fin) {
        this.player = player; //jugador 0 o 1, es decir, Lobo u Oveja
        this.row_ini = row_ini; // fila origen 
        this.col_ini = col_ini;// columna origen
        this.col_fin = col_fin; // columna destino
        this.row_fin = row_fin; // fila destino
    }
    
    /**
     * @return devuelve el numero del jugador actual
     */
    public int getPlayerNumber() {
        return player;
    }
    
    /**
     * Aplica el movimiento al estado de entrada y devuelve el siguiente 
     * estado del juego.
     * @param state, el estado al que se le aplica el movimiento
     * @return next, devuelve el siguiente estado tras aplicar el movimiento
     */
    public Was3DState applyTo(Was3DState state) {
    	// comprueba si es el turno del jugador
        if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }
        
        // realiza el movimiento
        int[][] board = state.getBoard();
        board[this.row_ini][this.col_ini] = Was3DFigures.BLACK.getRepresentation();
        board[this.row_fin][this.col_fin] = player;

        // actualiza el estado dependiendo de si ya ha ganado el jugador que 
        // toque o no.
        Was3DState next;
        if (Was3DState.isWinner(board, state.getTurn())) {
        	//ha ganado el lobo o las ovejas
            next = new Was3DState(state.getTurn(), board, true);
        } 
        else {
        	// aun no ha ganado ninguno de los dos
            next = new Was3DState(state.getTurn(), board, false);
        }
        
        return next;
    }
    /**
     * 
     * @return devuelve la fila inicial
     */
    public int getRowIni() {
        return this.row_ini;
    }
    /**
     * 
     * @return devuelve la columna inicial
     */
    public int getColIni() {
        return this.col_ini;
    }
    /**
     * 
     * @return devuelve la fila destino
     */
    public int getRowFin() {
        return this.row_fin;
    }
    /**
     * 
     * @return devuelve la columna destino
     */
    public int getColFin() {
        return this.col_fin;
    }
    /**
     * Muestra la informacion correspondiente a una accion (movimiento)
     * mostrando la casilla origen y a la que se quiere mover la ficha
     */
    public String toString() {
		return "place " + player + " from ("+ this.row_ini + ", " + this.col_ini + ")"
				+ " at (" + this.row_fin + ", " + this.col_fin + ")";
    }

	
}