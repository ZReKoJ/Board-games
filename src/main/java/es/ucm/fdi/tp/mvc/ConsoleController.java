package es.ucm.fdi.tp.mvc;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;

public class ConsoleController<S extends GameState<S,A>, A extends GameAction<S,A>>
extends GameController<S,A>{
	//add fields here
	private List<GamePlayer> players;
	private boolean stopped;
	
	public ConsoleController(List<GamePlayer> players, GameTable<S,A> game) {
	// add code here
		super(game);
		this.players = players;
	}

	@Override
	public void run() {
		int playerCount = 0;
		for (GamePlayer p : this.players) {
			p.join(playerCount++); // welcome each player, and assign playerNumber
		}
		startGame();
		while (!this.game.getState().isFinished() && !stopped) {
			// request move
			A action = this.players.get(this.game.getState().getTurn()).requestAction(this.game.getState());
			// apply move
			makeMove(action);
		}
		stopGame(); // innecesario
	}
	@Override
	public void stopGame(){
		super.stopGame();
		stopped = true;
	}
	@Override
	public void startGame(){
		super.startGame();
		stopped = false;
	}
	@Override
	public void makeMove(A action){
		super.makeMove(action);
		stopped = false;
	}

	
}
