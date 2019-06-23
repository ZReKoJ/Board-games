package es.ucm.fdi.tp.game.chessAnimation;

import static es.ucm.fdi.tp.game.chess.ChessState.BLACK;

import es.ucm.fdi.tp.base.model.GameAction;

/**
 * A chess action. Contains source, destination, and minimal
 * information for special moves (castling, queening, ...).
 */
@SuppressWarnings("serial")
public class ChessAnimationAction implements GameAction<ChessAnimationState, ChessAnimationAction> {

	private int player;
    private int srcRow;
    private int srcCol;
    private int dstRow;
    private int dstCol;
    private Special special;
    private boolean check;

    protected static final Special[] QUEENING_SPECIALS = new Special[]{
            Special.QueenQ, Special.QueenR, Special.QueenB, Special.QueenN
    };

    /**
     * Special moves (special != None) involve more than simply
     * placing the piece from src into dst.
     */
    public enum Special {
        None(null),
        EnPassant(null),
        LongCastle(null),
        ShortCastle(null),
        // do not change order! 1st (Queen) is default choice
        QueenQ(ChessAnimationBoard.Piece.Queen),
        QueenR(ChessAnimationBoard.Piece.Rook),
        QueenN(ChessAnimationBoard.Piece.Knight),
        QueenB(ChessAnimationBoard.Piece.Bishop);

        private final ChessAnimationBoard.Piece crowned;
        Special(ChessAnimationBoard.Piece crowned) {
            this.crowned = crowned;
        }
        ChessAnimationBoard.Piece getCrowned() {
            return crowned;
        }
    };

    /**
     * Create a simple non-special move. Notice that the check
     * field, if needed, must be set externally. This is because checking
     * for check usually involves testing out the move.
     *
     * @param player that moves
     * @param srcRow for moving piece
     * @param srcCol for moving piece
     * @param dstRow for moving piece
     * @param dstCol for moving piece
     */
    public ChessAnimationAction(int player, int srcRow, int srcCol,
                       int dstRow, int dstCol) {
        this(player, srcRow, srcCol, dstRow, dstCol, Special.None);
    }

    /**
     * Create a full chess move.
     * @param player that moves
     * @param srcRow for moving piece
     * @param srcCol for moving piece
     * @param dstRow for moving piece
     * @param dstCol for moving piece
     * @param special type of this move
     */
    public ChessAnimationAction(int player, int srcRow, int srcCol,
                       int dstRow, int dstCol, Special special) {
        this.player = player;
        this.srcRow = srcRow;
        this.srcCol = srcCol;
        this.dstRow = dstRow;
        this.dstCol = dstCol;
        this.special = special;
    }

    protected void setCheck(boolean check) {
        this.check = check;
    }

    public int getPlayerNumber() {
        return player;
    }

    /**
     * Allows moving on a board without a full surrounding ChessState.
     * This is useful during move-generation to discard moves that
     * would result in check.
     * @param board to move on
     */
    protected void applyTo(ChessAnimationBoard board) {
        byte p = board.get(srcRow, srcCol);
        board.set(dstRow, dstCol, p);
        switch (special) {
            case None:
                board.set(srcRow, srcCol, ChessAnimationBoard.EMPTY);
                break;
            case LongCastle:
                board.set(srcRow, 3, board.get(srcRow, 0));
                board.set(srcRow, 0, ChessAnimationBoard.EMPTY);
                board.set(srcRow, 4, ChessAnimationBoard.EMPTY);
                break;
            case ShortCastle:
                board.set(srcRow, 5, board.get(srcRow, 7));
                board.set(srcRow, 7, ChessAnimationBoard.EMPTY);
                board.set(srcRow, 4, ChessAnimationBoard.EMPTY);
                break;
            case EnPassant:
                board.set(srcRow, srcCol, ChessAnimationBoard.EMPTY);
                board.set(srcRow, dstCol, ChessAnimationBoard.EMPTY);
                break;
            default: // queening!
                board.set(srcRow, srcCol, ChessAnimationBoard.EMPTY);
                board.set(dstRow, dstCol, special.getCrowned().forTurn(player));
                break;
        }
    }

    public ChessAnimationState applyTo(ChessAnimationState state) {
        if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }

        // make move
        ChessAnimationBoard board = state.getBoard();
        applyTo(board);
        byte p = (byte)state.at(srcRow, srcCol);
        ChessAnimationBoard.Piece piece = ChessAnimationBoard.Piece.valueOf(p);

        int[] canCastle = state.canCastle();
        int enPassant = -1;

        if (piece == ChessAnimationBoard.Piece.King) {
            // no more castling for you
            canCastle[player] = (special == Special.LongCastle
                            || special == Special.ShortCastle) ?
                    ChessAnimationState.CASTLE_DONE : 0;
        } else if (piece == ChessAnimationBoard.Piece.Rook) {
            // a rook that moves can no longer be used in castling
            if (srcCol == 0) canCastle[player] &= ~ChessAnimationState.CASTLE_LONG;
            if (srcCol == 7) canCastle[player] &= ~ChessAnimationState.CASTLE_SHORT;
        } else if (piece == ChessAnimationBoard.Piece.Pawn && Math.abs(srcRow-dstRow)>1) {
            enPassant = srcCol;
        }

        // update state (also checks & sets winner)
        return new ChessAnimationState(state, board, canCastle, enPassant, check);
    }

    public int getSrcRow() {
		return srcRow;
	}

	public int getSrcCol() {
		return srcCol;
	}

	public int getDstRow() {
		return dstRow;
	}

	public int getDstCol() {
		return dstCol;
	}

	public String toString() {
        return (player == BLACK ? "black" : "white")
                + " from (" + srcRow + ", " + srcCol + ")"
                + " to (" + dstRow + ", " + dstCol + ")"
                + (special == Special.None ? "" : ": " + special)
                + (check ? " Check!" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessAnimationAction)) return false;

        ChessAnimationAction that = (ChessAnimationAction) o;

        if (player != that.player) return false;
        if (srcRow != that.srcRow) return false;
        if (srcCol != that.srcCol) return false;
        if (dstRow != that.dstRow) return false;
        if (dstCol != that.dstCol) return false;
        return special == that.special;

    }

    @Override
    public int hashCode() {
        int result = player;
        result = 31 * result + srcRow;
        result = 31 * result + srcCol;
        result = 31 * result + dstRow;
        result = 31 * result + dstCol;
        result = 31 * result + special.hashCode();
        return result;
    }
}
