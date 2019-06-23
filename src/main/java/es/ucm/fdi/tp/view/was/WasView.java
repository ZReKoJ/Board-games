package es.ucm.fdi.tp.view.was;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import es.ucm.fdi.tp.game.was.WasAction;
import es.ucm.fdi.tp.game.was.WasFigures;
import es.ucm.fdi.tp.game.was.WasState;
import es.ucm.fdi.tp.view.RectBoardGameView;

@SuppressWarnings("serial")
public class WasView extends RectBoardGameView<WasState, WasAction>{
	
	protected boolean firstClickRecieved;
	int origRow;
	int origCol;
	int destRow;
	int destCol;
	
	public WasView(){
		super();
		this.firstClickRecieved = true;
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
		if (state != null) return this.state.at(row,col);
		else return -1;
	}

	@Override 
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		if(!block){
			if(firstClickRecieved){
				firstClickRecieved = false;
				if(getPosition(row,col) == gameWindow.getPlayerId()){
					this.origRow = row;
					this.origCol = col;
					showInfoMessage("You selected on the cell [" + this.origRow + "," + this.origCol + "]");
				}
				else{
					showInfoMessage("Select another position");
				}
			}
			else{
				firstClickRecieved = true;
				if(getPosition(row,col) == WasFigures.BLACK.getRepresentation()){
					this.destRow = row;
					this.destCol = col;
					WasAction action = new WasAction(state.getTurn(), origRow, origCol ,destRow,destCol);
					if(this.state.isValid(action, this.state.getBoard()))
						this.gameWindow.getGameCtrl().makeMove(action);
					else
						showInfoMessage("That is not a valid movement");
				}
				else{
					showInfoMessage("Select another position");
				}
			}
		}
		else{
			showInfoMessage("It is not your turn");
		}
	}	
	@Override
	protected void keyTyped(int keyCode){
		
	}
	@Override
	protected void keyPressed(int keyCode) {
		
	}
	@Override
	protected void keyReleased(int keyCode) {
		if(keyCode == KeyEvent.VK_ESCAPE){
			firstClickRecieved = true;
			showInfoMessage("Selection cancelled");
			showInfoMessage("Click on source cell");
		}
	}

	@Override
	protected Color getColor(int player) {
		return this.playerInfoTable.getColor(player);
	}

	@Override
	protected ImageIcon getImage(int figure) {
		return null;
	}
}
