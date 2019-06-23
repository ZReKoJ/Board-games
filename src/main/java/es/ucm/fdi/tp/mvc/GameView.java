package es.ucm.fdi.tp.mvc;

import javax.swing.JComponent;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.GameWindow;

@SuppressWarnings("serial")
public abstract class GameView <S extends GameState<S,A>, A extends GameAction<S,A>> 
extends JComponent{
	
	public abstract void enable();
	public abstract void disable();
	// repintar el tablero, Jboard.repaint
	public abstract void update(S state); 
	// hacer que el controlador que nos manda el main está con nosotros
	//lo fija en esta clase, es para que el controlador que creamos en el main 
	//siga en todas las clases
	public abstract void showInfoMessage(String msg);
	public abstract void setGameViewCtrl(GameWindow<S,A> gameWindow);
	
}
