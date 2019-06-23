package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.launcher.Games;
import es.ucm.fdi.tp.mvc.GameController;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;
import es.ucm.fdi.tp.mvc.GameView;
import es.ucm.fdi.tp.view.component.Settings;


@SuppressWarnings("serial")
public class GameWindow <S extends GameState<S, A>, A extends GameAction<S, A>>
extends JFrame implements GameObserver<S,A>{
	public static String RESOURCE = "D:/Programs/Resources/pr5pr62017ucm/";
	
	private String gameName;
	private int playerId;
	private GamePlayer randPlayer;
	private ConcurrentAiPlayer smartPlayer;
	private GameController<S,A> gameCtrl;
	private GameObservable<S,A> game;
	
	public enum PlayerMode{
		MANUAL("Manual"),
		RANDOM("Random"),
		AI("Smart");
		private String name;
		PlayerMode(String name){ this.name = name; }
		@Override
		public String toString(){ return this.name;	}
	}

	private GameView<S,A> gameView;
	private PlayerMode playerMode;
	private Settings setting;
	private int currTurn;
	private boolean activeGame;
	//private boolean initialized;
	private S state;
	private int smartTimeOut = ConcurrentAiPlayer.DEFAULT_TIME;
	private int smartNThreads = ConcurrentAiPlayer.DEFAULT_THREADS;
	private Thread smartThread = null;
	
	// faltan mas atributos
	public GameWindow(String gameName, int playerId, String mode, GamePlayer randPlayer, ConcurrentAiPlayer smartPlayer,
	GameView<S,A> gameView, GameController<S,A> gameCtrl, GameObservable<S,A> game){
		super("Juegos para tableros");
		this.gameName = gameName;
		this.playerId = playerId;
		this.randPlayer = randPlayer;
		this.smartPlayer = smartPlayer;
		this.gameView = gameView;
		this.gameCtrl = gameCtrl;
		this.gameView.setGameViewCtrl(this);
		this.game = game;
		switch(mode){
			case "smart": this.playerMode = PlayerMode.AI; break;
			case "rand": this.playerMode = PlayerMode.RANDOM; break;
			default: this.playerMode = PlayerMode.MANUAL; break;
		}
		initGUI();
		game.addObserver(this);
	}
	
	private void initGUI() {
		if (this.gameName.equalsIgnoreCase(Games.TicTacToe2D.toString()))
			tttModel();
		else if (this.gameName.equalsIgnoreCase(Games.WolfAndSheep3D.toString()))
			wasModel(); 
		else if (this.gameName.equalsIgnoreCase(Games.ChessAnimation.toString()))
			chessModel(); 
		else normalModel();
	}

	private void normalModel(){
		this.setIconImage(Utils.loadImage("icon.png"));
		JPanel mainPanel = new JPanel(new BorderLayout(5,5));
		JPanel northPanel = new JPanel(new BorderLayout(5,5)); // cambiar para que quede bien
		this.setting = new Settings() {
			@Override
			protected void smartPressed() {
				if (!activeGame) gameView.showInfoMessage("The game has already ended");
				else if (smartPlayer.getPlayerNumber() != currTurn)
					gameView.showInfoMessage("It isn't your turn");
				else makeSmartMove();
			}

			@Override
			protected void randPressed() {
				if (!activeGame) gameView.showInfoMessage("The game has already ended");
				else if (randPlayer.getPlayerNumber() != currTurn)
					gameView.showInfoMessage("It isn't your turn");
				else makeRandMove();
			}

			@Override
			protected void restartPressed() {
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Are sure you want to restart?", "Restart",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (n == 0) {
					gameCtrl.startGame();
				}
				
			}

			@Override
			protected void closePressed() {
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (n == 0) {
					System.exit(1);
				}

			}

			@Override
			protected void comboBoxChanged(String option) {
				changePlayerMode(option);
				gameView.showInfoMessage("You changed into " + option + " mode");
			}

			@Override
			protected int getComboBoxIndex() {
				String mode = GameWindow.this.getPlayerMode().toString();
				int i = 0;
				while (i < this.pM.length && !this.pM[i].equalsIgnoreCase(mode)) ++i;
				return i;
			}

			@Override
			protected int getSmartThreads() {
				return GameWindow.this.smartNThreads;
			}

			@Override
			protected void setSmartThreads(int n) {
				GameWindow.this.smartNThreads = n;
			}

			@Override
			protected int getSmartTimeOut() {
				return GameWindow.this.smartTimeOut;
			}

			@Override
			protected void setSmartTimeOut(int n) {
				GameWindow.this.smartTimeOut = n;
			}

			@Override
			protected void stopPressed() {
				GameWindow.this.cancelSmartMove();
			}
		};
			northPanel.add(this.setting);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(gameView, BorderLayout.CENTER);
		mainPanel.setOpaque(true);
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		this.setVisible(true);
	}
	
	private void tttModel(){
		this.setIconImage(getImage(RESOURCE + "disney/icon.png"));
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setting = new Settings() {
			@Override
			protected void smartPressed() {
				if (!activeGame) gameView.showInfoMessage("The game has already ended");
				else if (smartPlayer.getPlayerNumber() != currTurn)
					gameView.showInfoMessage("It isn't your turn");
				else makeSmartMove();
			}

			@Override
			protected void randPressed() {
				if (!activeGame) gameView.showInfoMessage("The game has already ended");
				else if (randPlayer.getPlayerNumber() != currTurn)
					gameView.showInfoMessage("It isn't your turn");
				else makeRandMove();
			}

			@Override
			protected void restartPressed() {
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Are sure you want to restart?", "Restart",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (n == 0) {
					gameCtrl.startGame();
				}
				
			}

			@Override
			protected void closePressed() {
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (n == 0) {
					System.exit(1);
				};
			}

			@Override
			protected void comboBoxChanged(String option) {
				changePlayerMode(option);
				gameView.showInfoMessage("You changed into " + option + " mode");
			}

			@Override
			protected int getComboBoxIndex() {
				String mode = GameWindow.this.getPlayerMode().toString();
				int i = 0;
				while (i < this.pM.length && !this.pM[i].equalsIgnoreCase(mode)) ++i;
				return i;
			}

			@Override
			protected int getSmartThreads() {
				return GameWindow.this.smartNThreads;
			}

			@Override
			protected void setSmartThreads(int n) {
				GameWindow.this.smartNThreads = n;
			}

			@Override
			protected int getSmartTimeOut() {
				return GameWindow.this.smartTimeOut;
			}

			@Override
			protected void setSmartTimeOut(int n) {
				GameWindow.this.smartTimeOut = n;
			}

			@Override
			protected void stopPressed() {
				GameWindow.this.cancelSmartMove();
			}
		};
		mainPanel.add(setting, BorderLayout.NORTH);
		mainPanel.add(gameView, BorderLayout.CENTER);
		mainPanel.setOpaque(true);
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		this.setVisible(true);
	}
	
	private void wasModel(){
		this.setIconImage(getImage(RESOURCE + "pokemon/icon.png"));
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setting = new Settings() {
			@Override
			protected void smartPressed() {
				if (!activeGame) gameView.showInfoMessage("The game has already ended");
				else if (smartPlayer.getPlayerNumber() != currTurn)
					gameView.showInfoMessage("It isn't your turn");
				else makeSmartMove();
			}

			@Override
			protected void randPressed() {
				if (!activeGame) gameView.showInfoMessage("The game has already ended");
				else if (randPlayer.getPlayerNumber() != currTurn)
					gameView.showInfoMessage("It isn't your turn");
				else makeRandMove();
			}

			@Override
			protected void restartPressed() {
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Are sure you want to restart?", "Restart",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (n == 0) {
					gameCtrl.startGame();
				}
				
			}

			@Override
			protected void closePressed() {
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (n == 0) {
					System.exit(1);
				};
			}

			@Override
			protected void comboBoxChanged(String option) {
				changePlayerMode(option);
				gameView.showInfoMessage("You changed into " + option + " mode");
			}

			@Override
			protected int getComboBoxIndex() {
				String mode = GameWindow.this.getPlayerMode().toString();
				int i = 0;
				while (i < this.pM.length && !this.pM[i].equalsIgnoreCase(mode)) ++i;
				return i;
			}

			@Override
			protected int getSmartThreads() {
				return GameWindow.this.smartNThreads;
			}

			@Override
			protected void setSmartThreads(int n) {
				GameWindow.this.smartNThreads = n;
			}

			@Override
			protected int getSmartTimeOut() {
				return GameWindow.this.smartTimeOut;
			}

			@Override
			protected void setSmartTimeOut(int n) {
				GameWindow.this.smartTimeOut = n;
			}

			@Override
			protected void stopPressed() {
				GameWindow.this.cancelSmartMove();
			}
		};
		mainPanel.add(setting, BorderLayout.NORTH);
		mainPanel.add(gameView, BorderLayout.CENTER);
		mainPanel.setOpaque(true);
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 500);
		this.setVisible(true);
	}
	
	private void chessModel(){
		this.setIconImage(getImage(RESOURCE + "onepiece/icon.png"));
		
		try {
			File soundFile = new File(GameWindow.RESOURCE + "onepiece/onePieceTreasureCruise.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception e1) { e1.printStackTrace(); }
		
		JPanel mainPanel = new JPanel(new BorderLayout(5,5));
		
		this.setting = new Settings() {
			@Override
			protected void smartPressed() {
				if (!activeGame) gameView.showInfoMessage("The game has already ended");
				else if (smartPlayer.getPlayerNumber() != currTurn)
					gameView.showInfoMessage("It isn't your turn");
				else makeSmartMove();
			}

			@Override
			protected void randPressed() {
				if (!activeGame) gameView.showInfoMessage("The game has already ended");
				else if (randPlayer.getPlayerNumber() != currTurn)
					gameView.showInfoMessage("It isn't your turn");
				else makeRandMove();
			}

			@Override
			protected void restartPressed() {
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Are sure you want to restart?", "Restart",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (n == 0) {
					gameCtrl.startGame();
				}
				
			}

			@Override
			protected void closePressed() {
				int n = JOptionPane.showOptionDialog(new JFrame(),
						"Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (n == 0) {
					System.exit(1);
				}

			}

			@Override
			protected void comboBoxChanged(String option) {
				changePlayerMode(option);
				gameView.showInfoMessage("You changed into " + option + " mode");
			}

			@Override
			protected int getComboBoxIndex() {
				String mode = GameWindow.this.getPlayerMode().toString();
				int i = 0;
				while (i < this.pM.length && !this.pM[i].equalsIgnoreCase(mode)) ++i;
				return i;
			}

			@Override
			protected int getSmartThreads() {
				return GameWindow.this.smartNThreads;
			}

			@Override
			protected void setSmartThreads(int n) {
				GameWindow.this.smartNThreads = n;
			}

			@Override
			protected int getSmartTimeOut() {
				return GameWindow.this.smartTimeOut;
			}

			@Override
			protected void setSmartTimeOut(int n) {
				GameWindow.this.smartTimeOut = n;
			}

			@Override
			protected void stopPressed() {
				GameWindow.this.cancelSmartMove();
			}
		};
		mainPanel.add(setting, BorderLayout.NORTH);
		mainPanel.add(gameView, BorderLayout.CENTER);
		mainPanel.setOpaque(true);
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		this.setVisible(true);
	}

	private void handleEvent(GameEvent<S, A> e) {
		gameView.showInfoMessage(e.toString());
		this.state = e.getState();
		this.currTurn = this.state.getTurn();
		switch(e.getType()){
		case Start:
			super.setTitle(this.gameName + " (view for player " + playerId + ")");
			this.activeGame = true;
			cancelSmartMove();
			if (this.playerId == this.currTurn) {
				this.gameView.enable();
				if (this.setting != null && playerMode == PlayerMode.MANUAL) this.setting.enable();
			}
			else {
				this.gameView.disable();
				if (this.setting != null) this.setting.disable();
			}
			this.gameView.update(this.state);
			checkMode();
			break;
		case Change:
			if (this.playerId == this.currTurn) {
				this.gameView.enable();
				if (this.setting != null && playerMode == PlayerMode.MANUAL) this.setting.enable();
			}
			else {
				this.gameView.disable();
				if (this.setting != null) this.setting.disable();
			}
			this.gameView.update(this.state);
			checkMode();
			break;
		case Stop:
			this.activeGame = false;
			this.gameView.disable();
			if (this.setting != null) this.setting.enable();
			this.gameView.update(this.state);
			break;
		case Error:
			if (this.playerId == this.currTurn){
				this.gameView.update(this.state);
				JOptionPane.showMessageDialog(new JFrame(), e.getError().getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			break;
		case Info:
			break;
		}
	}
	
	public void changePlayerMode(String mode){
		boolean check = false;
		if (playerMode == PlayerMode.MANUAL) check = true;
		
		switch (mode){
			case "Manual": playerMode = PlayerMode.MANUAL; break;
			case "Random": playerMode = PlayerMode.RANDOM; break;
			case "Smart": playerMode = PlayerMode.AI; break;	
		}
		if (check) checkMode();
	}
	private void checkMode(){
		
		switch (playerMode){
		case RANDOM:
			makeRandMove();
			break;
		case AI:
			makeSmartMove();
			break;
		default:
			break;
		}
	}
	
	public void makeSmartMove(){
		if (activeGame && smartPlayer.getPlayerNumber() == currTurn){
			if(smartThread == null){
				smartThread = new Thread(()->{
					SwingUtilities.invokeLater(()->{
						setting.setThinking(true);
					});
					long time0 = System.currentTimeMillis();
					smartPlayer.setMaxThreads(smartNThreads);
					smartPlayer.setTimeout(smartTimeOut);
					if (state != null && !state.isFinished()){
						A action = smartPlayer.requestAction(state);
						System.out.println(Thread.getAllStackTraces().keySet());
						long time1 = System.currentTimeMillis();
						if(action != null){
							SwingUtilities.invokeLater(() -> {
								
								gameView.showInfoMessage(smartPlayer.getEvaluationCount() + 
										" nodes in " + (time1 - time0) + " ms. Value = " + 
										String.format("%.5f", smartPlayer.getValue()));

								gameCtrl.makeMove(action);
								setting.setThinking(false);
							});
							
						}
					}
					try {
						SwingUtilities.invokeAndWait(() -> { smartThread = null; });
					}
					catch (Exception e){
						
					}
				});	
			smartThread.start();
			}
			else
				this.gameView.showInfoMessage("I am still thinking!");
		}
	}
	
	public void makeRandMove(){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				if (activeGame && randPlayer.getPlayerNumber() == currTurn)
					gameCtrl.makeMove(randPlayer.requestAction(state));
			}
		});
	}

	protected void cancelSmartMove() {
		if(this.smartThread != null && this.smartThread.isAlive()){
			this.smartThread.interrupt();
			this.gameView.showInfoMessage("Smart move cancelled");
			this.setting.setThinking(false);
			this.smartThread = null;
		}
	}
	
	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		
		SwingUtilities.invokeLater(new Runnable(){
			
			@Override
			public void run(){
				handleEvent(e);
			}

		});
	}
	
	public BufferedImage getImage(String path){
		BufferedImage image = null;
		try {
			 image = ImageIO.read(new File(path));
		} catch (IOException ex) {
			System.err.println(ex);
			System.err.println(path);
		}
		return image;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public GamePlayer getRandPlayer() {
		return randPlayer;
	}

	public void setRandPlayer(GamePlayer randPlayer) {
		this.randPlayer = randPlayer;
	}

	public GamePlayer getSmartPlayer() {
		return smartPlayer;
	}

	public void setSmartPlayer(ConcurrentAiPlayer smartPlayer) {
		this.smartPlayer = smartPlayer;
	}

	public GameController<S, A> getGameCtrl() {
		return gameCtrl;
	}

	public void setGameCtrl(GameController<S, A> gameCtrl) {
		this.gameCtrl = gameCtrl;
	}

	public GameObservable<S, A> getGame() {
		return game;
	}

	public void setGame(GameObservable<S, A> game) {
		this.game = game;
	}

	public GameView<S, A> getGameView() {
		return gameView;
	}

	public void setGameView(GameView<S, A> gameView) {
		this.gameView = gameView;
	}

	public PlayerMode getPlayerMode() {
		return playerMode;
	}

	public void setPlayerMode(PlayerMode playerMode) {
		this.playerMode = playerMode;
	}

	public int getCurrTurn() {
		return currTurn;
	}

	public void setCurrTurn(int currTurn) {
		this.currTurn = currTurn;
	}

	public boolean isActiveGame() {
		return activeGame;
	}

	public void setActiveGame(boolean activeGame) {
		this.activeGame = activeGame;
	}

	
}
