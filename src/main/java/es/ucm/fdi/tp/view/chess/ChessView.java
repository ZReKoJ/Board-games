package es.ucm.fdi.tp.view.chess;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.game.chess.ChessAction;
import es.ucm.fdi.tp.game.chess.ChessAction.Special;
import es.ucm.fdi.tp.game.chess.ChessBoard;
import es.ucm.fdi.tp.game.chess.ChessBoard.Piece;
import es.ucm.fdi.tp.game.chess.ChessState;
import es.ucm.fdi.tp.view.RectBoardGameView;

@SuppressWarnings("serial")
public class ChessView extends RectBoardGameView<ChessState, ChessAction>{

	private HashMap<Integer, ImageIcon> chessFigures;

	private int origRow = -1;
	private int origCol = -1;
	protected boolean kingMoved = false;
	protected boolean longCastleMoved = false;
	protected boolean shortCastleMoved = false;
	
	public ChessView(){
		super();
		chessFigures = new HashMap<>();
		for (Piece p : Piece.values()){
			if (p != Piece.Empty && p != Piece.Outside){
				chessFigures.put((int) p.black(), new ImageIcon( Utils.loadImage("chess/" + Piece.iconName(p.black()))));
				chessFigures.put((int) p.white(), new ImageIcon(Utils.loadImage("chess/" + Piece.iconName(p.white()))));
			}
		}
	}

	@Override
	protected int getNumRows() {
		if(this.state != null)
			return this.state.getDimension();
		return 0;
	}
	
	@Override
	protected int getNumCols() {
		if(this.state!=null)
			return this.state.getDimension();
		return 0;
	}
	
	@Override
	protected int getPosition(int row, int col) {
		return this.state.at(row, col);
	}
	
	@Override 
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		int selected, value = getPosition(row, col);
		Piece sel = null, pi = Piece.valueOf((byte) value);
			if (origRow == -1 || origCol == -1){
				if (pi != Piece.Empty && value / 8 == gameWindow.getPlayerId()){
					origRow = row; origCol = col;
					showInfoMessage("You selected the cell (" + row + "," + col + ")");
				}
			}
			else {
				selected = getPosition(origRow, origCol);
				sel = Piece.valueOf((byte) selected);
				if (origRow == row && origCol == col){
					origRow = -1; origCol = -1;
					showInfoMessage("You deselected the cell (" + row + "," + col + ")");
				}
				else if (pi == Piece.Empty || value / 8 != gameWindow.getPlayerId()){
					ChessAction action = null;
					for (ChessAction cha : state.validActions(gameWindow.getPlayerId())) {
						if (origRow == cha.getSrcRow() && 
							origCol == cha.getSrcCol() && 
							row == cha.getDstRow() && 
							col == cha.getDstCol()){
							action = cha;
						}
					}
					if (action != null) {
						if (sel == Piece.Pawn && (row == getNumRows() - 1 || row == 0)){
							Special type = Special.None;
							String s = (String) JOptionPane.showInputDialog(new JFrame(),
									"Choose your figure to transform ...",
									"Customized Dialog", JOptionPane.PLAIN_MESSAGE, null,
									 new Object[]{ "None", "Queen", "Rook", "Knight", "Bishop"}, "None");
							if (s != null){
								switch (s) {
								case "Queen": type = Special.QueenQ; break;
								case "Rook": type = Special.QueenR; break;
								case "Knight": type = Special.QueenN; break;
								case "Bishop": type = Special.QueenB; break;
								default: type = Special.None; break;
								}
							}
							System.out.println(s);
							gameWindow.getGameCtrl().makeMove(new ChessAction(action.getPlayerNumber(),
																					   action.getSrcRow(),
																					   action.getSrcCol(),
																					   action.getDstRow(),
																					   action.getDstCol(),
																					   type));
						}
						else gameWindow.getGameCtrl().makeMove(action);
						origRow = -1; origCol = -1;
					}
				}
				else {
					origRow = row; origCol = col;
					showInfoMessage("You selected the cell (" + row + "," + col + ")");
				}
			}
		System.out.println("Mouse: " + clickCount + " clicks at position (" + row + "," + col + ") with Button " + mouseButton);
		this.repaint();	
	}

	@Override
	protected Color getColor(int player) {
		if(Piece.valueOf((byte)player) != Piece.Empty){
			if(ChessBoard.white((byte)player))
				return this.playerInfoTable.getColor(0);
			else
				return this.playerInfoTable.getColor(1);
		}
		else
			return this.playerInfoTable.getColor(2);
	}
	@Override
	protected void keyTyped(int keyCode){
		
	}
	@Override
	protected void keyPressed(int keyCode) {
		
	}
	@Override
	protected void keyReleased(int keyCode) {

	}

	@Override
	protected ImageIcon getImage(int figure) {
		return chessFigures.get(figure);
	}
	

}
