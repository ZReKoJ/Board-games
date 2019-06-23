package es.ucm.fdi.tp.launcher;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.mvc.ConsoleController;
import es.ucm.fdi.tp.mvc.GameController;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.mvc.GameView;
import es.ucm.fdi.tp.view.ConsoleView;
import es.ucm.fdi.tp.view.GameWindow;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MainPr5 {
	
	private static void usage(String msg) {
		System.err.println(msg);
		System.out.println("At least you need to insert 2 arguments in order to play");
		System.out.println("Game must be either ttt or was");
		System.out.println("Game can only be viewed as Console or GUI");
		System.exit(1);
	}
	
	private static <S extends GameState<S, A>, A extends GameAction<S,A>>
	void startConsoleMode(String gType, Games game, String playerModes[]) {
		// create the list of players as in assignment 4
		List<GamePlayer> players = new ArrayList<GamePlayer>();
		final GameTable<S, A> table = new GameTable(game.getState());
		if(Games.find(gType)){
			for(int i = 0; i < playerModes.length; ++i){
				if(playerModes[i].equalsIgnoreCase("manual"))
					players.add(new ConsolePlayer("player " + i, new Scanner(System.in)));
				else if(playerModes[i].equalsIgnoreCase("rand"))
					players.add(new RandomPlayer("player " + i));
				else if(playerModes[i].equalsIgnoreCase("smart"))
					players.add(new SmartPlayer("player " + i, 5));
				else{
					throw new GameError("El jugador " + playerModes[i] + " no es valido");
				}
			}
		}
		else{
			throw new GameError("El juego " + gType + " no es valido");
		}
		new ConsoleView<S,A>(table);
		new ConsoleController<S,A>(players, table).run();
	}

	private static <S extends GameState<S, A>, A extends GameAction<S,A>>
	void startGUIMode(final String gType, Games game, String playerModes[]) {
		if (playerModes.length != 0 && playerModes.length != game.getPlayers())
			usage("Incorrect number of arguments");
		final GameTable<S, A> table = new GameTable(game.getState());
		final GameController<S,A> gameCtrl = new GameController<S,A>(table);
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		for (int i = 0; i < game.getState().getPlayerCount(); ++i){
			final GamePlayer rand = new RandomPlayer("Player - " + i);
			final ConcurrentAiPlayer smart = new ConcurrentAiPlayer("Player - " + i);
			rand.join(i);
			smart.join(i);
			final int id = i;
			String mode;
			if (playerModes.length == game.getPlayers()) mode = playerModes[i];
			else mode = null;
			try {
				SwingUtilities.invokeAndWait( new Runnable() {
					@Override
					public void run() {
						new GameWindow<S, A>(game.toString(), id, mode, rand, smart, (GameView<S, A>) game.getView(), gameCtrl, table).setBounds(
								(int) (dim.getWidth() * id / game.getState().getPlayerCount()),
								(int) (0),
								(int) (dim.getWidth() / game.getState().getPlayerCount()),
								(int) (dim.getHeight() - 50)
								);
					}
				});
			} catch (GameError | InvocationTargetException | InterruptedException e) {
				System.err.println("Some error occurred while creating the view ...");
				System.exit(1);
			}
		}
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				gameCtrl.run();
			}
		});
	}

	public static void main(String[] args) {
		if (args.length < 2) { usage("Incorrect number of arguments"); }
		Games game = Games.getGame(args[0]);
		if (game == null) { usage("Invalid game"); }
		try {
			String[] otherArgs = Arrays.copyOfRange(args, 2, args.length);
			switch (args[1]) {
				case "console":
					startConsoleMode(args[0], game, otherArgs);
					break;
				case "gui":
					startGUIMode(args[0], game, otherArgs);
					break;
				default:
					usage("Invalid view mode: " + args[1]);
					break;
			}
		} catch (GameError e) { System.err.println(e); }
	}
}
