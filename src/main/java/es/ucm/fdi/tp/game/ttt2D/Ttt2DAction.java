package es.ucm.fdi.tp.game.ttt2D;

import es.ucm.fdi.tp.base.model.GameAction;

/**
 * An action for TickTackToe.
 */
public class Ttt2DAction implements GameAction<Ttt2DState, Ttt2DAction> {

	private static final long serialVersionUID = -8491198872908329925L;
	
	private int player;
    private int row;
    private int col;

    public Ttt2DAction(int player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
    }

    public int getPlayerNumber() {
        return player;
    }

    public Ttt2DState applyTo(Ttt2DState state) {
        if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }

        // make move
        int[][] board = state.getBoard();
	    if (board[row][col] == Ttt2DState.EMPTY){
	        board[row][col] = player;
        }

        // update state
        Ttt2DState next;
        if (Ttt2DState.isWinner(board, state.getTurn())) {
            next = new Ttt2DState(state, board, true, state.getTurn());
        } else if (Ttt2DState.isDraw(board)) {
            next = new Ttt2DState(state, board, true, -1);
        } else {
            next = new Ttt2DState(state, board, false, -1);
        }
        return next;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return "place " + player + " at (" + Math.abs(row) + ", " + Math.abs(col) + ")";
    }

}
