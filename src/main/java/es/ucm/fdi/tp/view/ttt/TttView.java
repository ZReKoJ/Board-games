package es.ucm.fdi.tp.view.ttt;

import java.awt.Color;

import javax.swing.ImageIcon;

import es.ucm.fdi.tp.game.ttt.TttAction;
import es.ucm.fdi.tp.game.ttt.TttState;
import es.ucm.fdi.tp.view.RectBoardGameView;

@SuppressWarnings("serial")
public class TttView extends RectBoardGameView<TttState,TttAction>{
	
	public TttView(){
		super();
	}

	@Override
	protected int getNumRows() {
		if(this.state != null) return this.state.getBoard().length;
		else return 0;
	}
	
	@Override
	protected int getNumCols() {
		if(this.state != null) return this.state.getBoard().length;
		else return 0;
	}
	
	@Override
	protected int getPosition(int row, int col) {
		//return state != null && this.state.at(row, col);
		if (state != null) return this.state.at(row,col);
		else return -1;
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		if(!block && this.state.at(row, col) == -1){
			TttAction action = new TttAction(state.getTurn(), row, col);
			this.gameWindow.getGameCtrl().makeMove(action);
			showInfoMessage("You clicked on " + action.getRow() + " " + action.getCol());
		}
		else if (block)
			showInfoMessage("It is not your turn");
		else
			showInfoMessage("This position is already occupied");
	}

	@Override
	protected void keyTyped(int keyCode) {
		showInfoMessage("");
	}

	@Override
	protected void keyPressed(int keyCode) {}

	@Override
	protected void keyReleased(int keyCode) {}

	@Override
	protected Color getColor(int player) {
		return this.playerInfoTable.getColor(player);
	}

	@Override
	protected ImageIcon getImage(int figure) {
		return null;
	}
}
