package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;

public class ConsoleView<S extends GameState<S,A>, A extends
GameAction<S,A>>
implements GameObserver<S,A> {
	
	
	public ConsoleView(GameObservable<S,A> gameTable){
		gameTable.addObserver(this);
	}
	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		if(e.getType() == GameEvent.EventType.Change ||
				e.getType() == GameEvent.EventType.Start){
			System.out.println("Turn for the player " + e.getState().getTurn());
			System.out.println("Current State:");
			System.out.println(e.getState());
		}
		else if(e.getType() == GameEvent.EventType.Stop){
			// game over
			String endText = "The game ended: ";
					
			int winner = e.getState().getWinner();
						
			if(winner == -1){
				endText += "draw!";
			}
			else{
				endText += "player " + winner + " has won!";
			}
			System.out.println(endText);
		}
		else {
			System.err.println("ERROR");
		}
		
	}
	

}
