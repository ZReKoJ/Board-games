package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
/**
 * Demo main, used to test game functionality. You can use it as an inspiration,
 * but we expect you to build your own main-class.
 */


public class MainPr4 {
	public static <S extends GameState<S, A>, A extends GameAction<S, A>> int playGame(GameState<S, A> initialState,
			List<GamePlayer> players) {
		int playerCount = 0;
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign
									// playerNumber
		}
		@SuppressWarnings("unchecked")
		S currentState = (S) initialState;
		System.out.println("\n" + currentState);

		while (!currentState.isFinished()) {
			// request move
			A action = players.get(currentState.getTurn()).requestAction(currentState);
			// apply move
			currentState = action.applyTo(currentState);
			System.out.println("After action:\n" + currentState);

			if (currentState.isFinished()) {
				// game over
				String endText = "The game ended: ";
				int winner = currentState.getWinner();
				endText += "player " + (winner + 1) + " (" + players.get(winner).getName() + ") won!";
				System.out.println(endText);
			}
		}
		return currentState.getWinner();
	}
	
	/**
	 * Crea el estado inicial para el juego gameName
	 * @param gameName, nombre del juego
	 * @return GameState, estado inicial del juego correspondiente
	 */
	public static GameState<?,?> createInitialState(String[] args){
		if (args.length > 0){
			GameState<?,?> gs = Games.getGame(args[0]).getState();
			if (gs != null && args.length == gs.getPlayerCount() + 1)
				return gs;
			else {
				System.out.println("El juego " + args[0] + " no es valido");
				return null;
			}
		}
		else return null;
	}
	/**
	 * Crea un jugador para el juego gameName, con el tipo playerType
	 * y el nombre playerName
	 * @param gameName, nombre del juego
	 * @param playerType, tipo del jugador
	 * @param playerName, nombre del jugador
	 * @return GamePlayer, un jugador del tipo playerType para el juego gameName
	 * y null si el juego no existe o el tipo de jugador no existe.
	 */
	public static GamePlayer createPlayer(String gameName, String playerType, String playerName){
		if(Games.find(gameName)){
			if(playerType.equalsIgnoreCase("console"))
				return new ConsolePlayer(playerName, new Scanner(System.in));
			else if(playerType.equalsIgnoreCase("rand"))
				return new RandomPlayer(playerName);
			else if(playerType.equalsIgnoreCase("smart"))
				return new SmartPlayer(playerName, Games.getGame(gameName).getSmartDepth());
			else{
				System.err.println("El jugador " + playerType + " no es valido");
				return null;
			}
		}
		else{
			System.err.println("El juego " + gameName + " no es valido");
			return null;
		}
	}
	/**
	 * Main method, crea los jugadores y el estado inicial correspondiente al 
	 * tipo de juego y de jugadores
	 * 
	 * @param args, args[0] es el nombre del juego, los demas son tipo de jugadores
	 */
	public static void main(String[] args) {
		//test();
		boolean end = false;
		List<GamePlayer> players = new ArrayList<GamePlayer>();
		GameState<?, ?> state = createInitialState(args);
		GamePlayer player;
		String name_player;
		if(state != null){
			for(int i = 1; i < args.length && !end ; ++i){
				name_player = "player " + (i - 1);
				player = createPlayer(args[0], args[i], name_player);
				if(player == null)
					end = true;
				else
					players.add(player);
			}
			if(args[1].equalsIgnoreCase("console")){
				//llamo a consoleMode()
			}
			else if (args[1].equalsIgnoreCase("gui")){
				//llamo a guiMode() ???
			}
			if(!end)
				playGame(state,players);
		}
		else{
			System.err.println("ERROR");
			System.exit(1);
		}
	}
	/**
	 * Comprueba que los argumentos de args sean validos, creado para las pruebas
	 * de JUnit
	 * @param args
	 * @return true, si los argumentos son validos, false en caso contrario
	 */
	public static boolean validArguments(String[] args) {
		if (args.length > 0 && Games.find(args[0])){
			return (Games.getGame(args[0]).getPlayers() + 1) == args.length;
		}
		return false;
	}
	/*
	@Test
	public static void test() throws IOException{
		MainTest mt = new MainTest();
		mt.tooManyArguments();
		mt.tooLessArguments();
		mt.incorrectGameName();
		TttStateTest tst = new TttStateTest();
		tst.testSaveLoad();
		WasStateTest wst = new WasStateTest();
		wst.wolfSurroundedOfSheeps();
		wst.wolfIsAtTop();
		wst.initialPositionWolfMovements();
		wst.afterInitialPositionWolfMovements();
		wst.possibleSheepMovements();
	}
	*/
}

