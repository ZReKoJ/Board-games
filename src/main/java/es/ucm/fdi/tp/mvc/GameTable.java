package es.ucm.fdi.tp.mvc;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;

/**
 * An event-driven game engine.
 * Keeps a list of players and a state, and notifies observers
 * of any changes to the game.
 */
public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

	protected S initialState;
	protected S currentState;
	protected List<GameObserver<S,A>> obs;
	protected boolean active;
	protected int num_players;
	

    public GameTable(S initState) {
    	if(initState == null) throw new GameError("Initial state can´t be null");
    	this.initialState = initState;
    	this.currentState = initState;
    	this.obs = new ArrayList<GameObserver<S,A>>();
    	this.active = false;
    	this.num_players = initState.getPlayerCount();
    }
    public void start() {
    	this.currentState = this.initialState;
    	this.active = true;
    	notifyObservers(new GameEvent<S , A>(EventType.Start, null, this.currentState, null, "The game has started"));
    }
    public void stop() {
    	if(!this.active){
    		GameError err = new GameError("Game already stopped!");
    		notifyObservers(new GameEvent<S, A>(EventType.Error, null, this.currentState, err, err.getMessage()));
    	}
    	else{
    		this.active = false;
    		if(this.currentState.getWinner() != -1)
    			notifyObservers(new GameEvent<S , A>(EventType.Stop, null, this.currentState, null, "Winner is player " + this.currentState.getWinner()));
    		else
    			notifyObservers(new GameEvent<S , A>(EventType.Stop, null, this.currentState, null, "DRAW"));
    		notifyObservers(new GameEvent<S , A>(EventType.Stop, null, this.currentState, null, "The game has stopped"));
    	}
    }
    public void execute(A action) {
    	if(active && !this.currentState.isFinished() && action.getPlayerNumber() == this.currentState.getTurn()){
    		this.currentState = action.applyTo(this.currentState);
    		notifyObservers(new GameEvent<S , A>(EventType.Change, action, this.currentState, null, "The game state has changed"));
    		notifyObservers(new GameEvent<S,A>(EventType.Info, action, this.currentState, null, action.toString()));
    	}
    	else{
    		GameError err = new GameError("Cannot perform action!");
    		notifyObservers(new GameEvent<S,A>(EventType.Error, action, this.currentState, err, err.getMessage()));
    	}
    }
    
    public void sendInfo(String msg){
    	notifyObservers(new GameEvent<S,A>(EventType.Info, null, this.currentState, null, msg));
    }
    
    private void notifyObservers(GameEvent<S, A> e) {
		for(GameObserver<S,A> o : this.obs){
			o.notifyEvent(e);
		}
	}
	public S getState() {
		return this.currentState;
    }

    public void addObserver(GameObserver<S, A> o) {
    	this.obs.add(o); 
    }
    
    public void removeObserver(GameObserver<S, A> o) {
    	this.obs.remove(o);
    }
}
