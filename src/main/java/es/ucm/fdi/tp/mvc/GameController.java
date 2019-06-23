package es.ucm.fdi.tp.mvc;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public class GameController<S extends GameState<S,A>, A extends GameAction <S,A>> {
	
	protected GameTable<S,A> game;
	
	public GameController(GameTable<S,A> game){
		this.game = game;
	}	
	public void makeMove(A action){
		game.execute(action);
	}
	public void stopGame(){
		game.stop();
	}
	public void startGame(){
		game.start();
	}
	public void run(){
		startGame();
	}
	public void sendInfo (String msg){
		game.sendInfo(msg);
	}
}
