package es.ucm.fdi.tp.launcher;

import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.game.chess.ChessState;
import es.ucm.fdi.tp.game.chessAnimation.ChessAnimationState;
import es.ucm.fdi.tp.game.ttt.TttState;
import es.ucm.fdi.tp.game.ttt2D.Ttt2DState;
import es.ucm.fdi.tp.game.was.WasState;
import es.ucm.fdi.tp.game.was3D.Was3DState;
import es.ucm.fdi.tp.mvc.GameView;
import es.ucm.fdi.tp.view.chess.ChessView;
import es.ucm.fdi.tp.view.chessAnimation.ChessAnimationView;
import es.ucm.fdi.tp.view.ttt.TttView;
import es.ucm.fdi.tp.view.ttt2D.Ttt2DView;
import es.ucm.fdi.tp.view.was.WasView;
import es.ucm.fdi.tp.view.was3D.Was3DView;

public enum Games{
	TicTacToe(3, 2, 5, "ttt TTT Ttt TicTaeToe TresEnRaya", new TttState(3), new TttView()),
	TicTacToe2D(Ttt2DState.DIM, Ttt2DState.PLAYERS, 5, "ttt2D", new Ttt2DState(Ttt2DState.DIM), new Ttt2DView()),
	WolfAndSheep(WasState.DIM, WasState.PLAYERS, 5, "was WAS Was WolfAndSheep ElLoboYLaOveja", new WasState(WasState.DIM), new WasView()),
	WolfAndSheep3D(Was3DState.DIM, Was3DState.PLAYERS, 5, "was3D", new Was3DState(Was3DState.DIM), new Was3DView()),
	Chess(8, 2, 5, "chess", new ChessState(), new ChessView()),
	ChessAnimation(8, 2, 3, "chessAnimation", new ChessAnimationState(), new ChessAnimationView());

	private String[] names;
	private int smartDepth;
	private GameState<?, ?> state;
	private GameView<?, ?> view;
	private int dimension;
	private int players;
	
	Games(int d, int p, int smartDepth, String n, GameState<?, ?> s, GameView<?, ?> view){
		this.setDimension(d);
		this.setPlayers(p);
		this.setNames(n.split(" +"));
		this.setSmartDepth(smartDepth);
		this.setState(s);
		this.setView(view);
	}
	
	public static Games getGame(String name){
		for (Games g : Games.values()){
			for (int i = 0; i < g.names.length; ++i){
				if (name.equalsIgnoreCase(g.names[i]))
					return g;
			}
		}
		return null;
	}
	
	public static boolean find(String name){
		for (Games g : Games.values()){
			for (int i = 0; i < g.names.length; ++i){
				if (name.equalsIgnoreCase(g.names[i]))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return the names
	 */
	public String[] getNames() {
		return names;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(String[] names) {
		this.names = names;
	}

	/**
	 * @return the state
	 */
	public GameState<?, ?> getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(GameState<?, ?> state) {
		this.state = state;
	}

	/**
	 * @return the dimension
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * @param dimension the dimension to set
	 */
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	/**
	 * @return the players
	 */
	public int getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(int players) {
		this.players = players;
	}

	/**
	 * @return the view
	 */
	public GameView<?, ?> getView() {
		try {
			return view.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param view the view to set
	 */
	public void setView(GameView<?, ?> view) {
		this.view = view;
	}

	/**
	 * @return the smartDepth
	 */
	public int getSmartDepth() {
		return smartDepth;
	}

	/**
	 * @param smartDepth the smartDepth to set
	 */
	public void setSmartDepth(int smartDepth) {
		this.smartDepth = smartDepth;
	}
	
}
