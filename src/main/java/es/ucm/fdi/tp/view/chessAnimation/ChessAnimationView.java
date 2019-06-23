package es.ucm.fdi.tp.view.chessAnimation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import es.ucm.fdi.tp.game.chessAnimation.ChessAnimationAction;
import es.ucm.fdi.tp.game.chessAnimation.ChessAnimationAction.Special;
import es.ucm.fdi.tp.game.chessAnimation.ChessAnimationBoard;
import es.ucm.fdi.tp.game.chessAnimation.ChessAnimationBoard.Piece;
import es.ucm.fdi.tp.game.chessAnimation.ChessAnimationState;
import es.ucm.fdi.tp.mvc.GameView;
import es.ucm.fdi.tp.view.GameWindow;
import es.ucm.fdi.tp.view.component.InfoArea;

@SuppressWarnings("serial")
public class ChessAnimationView extends GameView<ChessAnimationState,ChessAnimationAction> {
	
	protected Map<Piece, OnePieceFigure> chess;
	protected Map<OnePieceFigure, Boolean> active;
	private ChessAnimationState state;
	private ChessAnimationBoard board;;
	private ChessOnePieceView jBoard;
	protected InfoArea infoArea;
	private GridButton grid;
	private GameWindow<ChessAnimationState, ChessAnimationAction> gameWindow;
	private int id;
	private boolean block;
	private Clip clip = null;
	
	private Piece[] pieces = Piece.values();
	private OnePieceFigure[] figures = OnePieceFigure.values();
	private Special changeType;
	
	public ChessAnimationView() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		chess = new HashMap<>();
		active = new HashMap<>();
		for (int i = 0; i < pieces.length - 2; ++i){
			chess.put(pieces[i], figures[i]);
			active.put(figures[i], true);
		}
		for (int i = pieces.length - 2; i < figures.length; ++i)
			active.put(figures[i], false);
		
		this.setLayout(new BorderLayout());
		
		this.jBoard = new ChessOnePieceView(){

			@Override
			protected int getNumRows() {
				if(state != null) return state.getDimension();
				else return 0;
			}

			@Override
			protected int getNumCols() {
				if(state != null) return state.getDimension();
				else return 0;
			}

			@Override
			protected byte getValue(int row, int col) {
				//return board.get(row, col);
				return board.get(row, col);
			}

			@Override
			protected OnePieceFigure getFigure(byte representation) {
				return chess.get(Piece.valueOf(representation));
			}

			@Override
			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				byte selected = 0x10, value = getValue(invertValue(row, col, true), invertValue(row, col, false));
				Piece sel = null, pi = Piece.valueOf(value);
				if (mouseButton == 1){
					if (Piece.valueOf(this.showOnePieceFigure) == Piece.Empty) {
						if (_SELECTED_ROW == -1 || _SELECTED_COL == -1){
							if (pi != Piece.Empty && value / 8 == id){
								_SELECTED_ROW = row; _SELECTED_COL = col;
							}
						}
						else {
							selected = board.get(invertValue(_SELECTED_ROW, _SELECTED_COL, true), invertValue(_SELECTED_ROW, _SELECTED_COL, false));
							sel = Piece.valueOf(selected);
							if (_SELECTED_ROW == row && _SELECTED_COL == col){
								_SELECTED_ROW = -1; _SELECTED_COL = -1;
							}
							else if (pi == Piece.Empty || value / 8 != id){
								if (!block) {
									ChessAnimationAction action = null;
									for (ChessAnimationAction ca : state.validActions(id)) {
										if (invertValue(_SELECTED_ROW, _SELECTED_COL, true) == ca.getSrcRow() && 
											invertValue(_SELECTED_ROW, _SELECTED_COL, false) == ca.getSrcCol() && 
											invertValue(row, col, true) == ca.getDstRow() && 
											invertValue(row, col, false) == ca.getDstCol()){
											action = ca;
										}
									}
									if (action != null) {
										if (sel == Piece.Pawn && _SELECTED_COL == _COLS - 2 && col == _COLS - 1){
											changeType = Special.None;
											JButton[] buttons = new JButton[4];
											JPanel choosePanel = new JPanel(new GridLayout(2, 2));
											for (int i = buttons.length; i > 0; --i){
												final int j = i;
												buttons[i - 1] = new JButton(new ImageIcon(getImage(PATH + "onepiece/figure/" + chess.get(pieces[i]).getName() + "/" + "icon.png")));
												buttons[i - 1].addActionListener(new ActionListener(){

													@Override
													public void actionPerformed(ActionEvent e) {
														switch (j){
														case 1: changeType = Special.QueenB; break;
														case 2: changeType = Special.QueenN; break;
														case 3: changeType = Special.QueenR; break;
														case 4: changeType = Special.QueenQ; break;
														}
														JOptionPane.getRootFrame().dispose();
													}
													
												});
												choosePanel.add(buttons[i - 1]);
											}
											JOptionPane.showMessageDialog(null, choosePanel, "I am a lazy butterfly and I am going through the methamorfosis ...", JOptionPane.CANCEL_OPTION, null);
											gameWindow.getGameCtrl().makeMove(new ChessAnimationAction(action.getPlayerNumber(),
																									   action.getSrcRow(),
																									   action.getSrcCol(),
																									   action.getDstRow(),
																									   action.getDstCol(),
																									   changeType));
										}
										else gameWindow.getGameCtrl().makeMove(action);
										if (pi != Piece.Empty) this.showOnePieceFigure = selected;
										ChessAnimationView.this.playSound(sel);
										_SELECTED_ROW = -1; _SELECTED_COL = -1;
									}
									else {
										if (state.isInCheck()) showInfoMessage("You are in Check!");
										else showInfoMessage("This movement isn't valid");
									}
								}
								else showInfoMessage("It isn't your turn yet");
							}
							else {
								_SELECTED_ROW = row; _SELECTED_COL = col;
							}
						}
					}
					else this.showOnePieceFigure = 0x10;
					_POINTER = 0;
				}
				else if (mouseButton == 3){
					if (pi == Piece.Empty) _BACKGROUND_ENUM = ((_BACKGROUND_ENUM + _NUM_PIC_BACKGROUND + 1) % _NUM_PIC_BACKGROUND);
				}
				System.out.println("Mouse: " + clickCount + " clicks at position (" + row + "," + col + ") with Button " + mouseButton);
				this.repaint();	
			}
		};
		this.grid = new GridButton(){

			@Override
			protected int getNumFigures() {
				return chess.size();
			}

			@Override
			protected String getFigureName(int repre) {
				return pieces[repre].name();
			}

			@Override
			protected String getIconPath(int figure) {
				return getFigure(figure).getName();
			}

			@Override
			protected OnePieceFigure getFigure(int repre) {
				return chess.get(pieces[repre]);
			}

			@Override
			protected void changeFigure(int repre, int i, int j) {
				OnePieceFigure fig = chess.get(pieces[repre]);
				int k = 0;
				while (!figures[k].getName().equalsIgnoreCase(fig.getName())) ++k;
				int signal = k;
				++k;
				while (active.get(figures[k % figures.length]) && k % figures.length != signal) ++k;
				if (k % figures.length != signal){
					chess.remove(pieces[repre]);
					chess.put(pieces[repre], figures[k % figures.length]);
					active.put(figures[signal], false);
					active.put(figures[k % figures.length], true);
					image = getImage(PATH + "onepiece/figure/" + getIconPath(i) + "/" + "icon.png");
					buttons[i][j].setIcon(new ImageIcon(image));
					ChessAnimationView.this.playSound(pieces[repre]);
					jBoard.repaint();
				}
			}

			@Override
			protected void playSound(int identification) {
				ChessAnimationView.this.playSound(pieces[identification]);
			}
			
		};
		
		this.infoArea = new InfoArea(){

			@Override
			public void chat(String msg) {
				gameWindow.getGameCtrl().sendInfo("Player " + id + ": " + msg);
			}
			
		};
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel viewPanel = new JPanel(new BorderLayout());
		
		viewPanel.add(this.jBoard, BorderLayout.CENTER);
		viewPanel.add(this.grid, BorderLayout.EAST);
		
		mainPanel.add(viewPanel, BorderLayout.CENTER);
		mainPanel.add(this.infoArea, BorderLayout.PAGE_END);
		
		this.add(mainPanel, BorderLayout.CENTER);
	}
	
	public void playSound(Piece pi){
		if (clip != null) clip.close();
		OnePieceFigure fig = chess.get(pi);
		File soundFile = new File(GameWindow.RESOURCE + "onepiece/figure/" + fig.getName() + "/battleDirection/battleDirection (" + ((int) (Math.random() * 100) % fig.getBattleDirection() + 1) + ").wav");
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception e1) { e1.printStackTrace(); }
	}

	@Override
	public void enable() {
		this.block = false;
	}

	@Override
	public void disable() {
		this.block = true;
	}

	@Override
	public void update(ChessAnimationState state) {
		this.state = state;
		this.board = state.getBoard();
		//if (this.state.isInCheck()) showInfoMessage("You are in Check!");
		if(this.state.isFinished()) if (!block) this.gameWindow.getGameCtrl().stopGame();
		this.jBoard.setWin(this.state.getWinner()); 
		this.jBoard.repaint();
	}

	@Override
	public void showInfoMessage(String msg) {
		this.infoArea.addContent(msg);
		this.repaint();
	}

	@Override
	public void setGameViewCtrl(GameWindow<ChessAnimationState, ChessAnimationAction> gameWindow) {
		this.gameWindow = gameWindow;
		this.id = gameWindow.getPlayerId();
		this.jBoard.setId(this.id);
	}
}
