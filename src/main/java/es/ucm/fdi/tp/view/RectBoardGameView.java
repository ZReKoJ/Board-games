 package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameView;
import es.ucm.fdi.tp.view.component.CenteredSquareLayout;
import es.ucm.fdi.tp.view.component.InfoArea;
import es.ucm.fdi.tp.view.component.JBoard;
import es.ucm.fdi.tp.view.component.PlayerInfoTable;

@SuppressWarnings("serial")
public abstract class RectBoardGameView<S extends GameState<S,A>, A extends GameAction<S,A>> 
extends GameView<S,A> {

	protected S state;
	protected JBoard jboard;
	private JPanel sidePanel;
	protected GameWindow<S, A> gameWindow;
	protected InfoArea infoArea;
	protected PlayerInfoTable playerInfoTable;
	protected boolean block;

	protected RectBoardGameView(){
		super();
		initGUI();
	}
	// declara dos paneles, uno en el centro con tablero, y uno en el lado con el
	// textArea, posibles modificaciones sobre esto en examen ********
	private void initGUI() { 
		this.setLayout(new BorderLayout());
		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.setDividerLocation(400);
		this.jboard = new JBoard() {

				@Override
				protected void keyTyped(int keyCode) {
					RectBoardGameView.this.keyTyped(keyCode);
				}
				@Override
				protected void keyReleased(int keyCode){
					RectBoardGameView.this.keyReleased(keyCode);
				}
				@Override
				protected void keyPressed(int keyCode) {
					RectBoardGameView.this.keyPressed(keyCode);
				}
	
				@Override
				protected Color getColor(int player) {
					return RectBoardGameView.this.getColor(player);
				}
	
				@Override
				protected Integer getPosition(int row, int col) {
					return RectBoardGameView.this.getPosition(row, col);
				}
		
				@Override
				protected Color getBackground(int row, int col) {
					return RectBoardGameView.this.getColorBG(row, col);
					//return (row+col) % 2 == 0 ? Color.LIGHT_GRAY : Color.BLACK;
				}
	
				@Override
				protected int getNumRows() {
					return RectBoardGameView.this.getNumRows();
				}
	
				@Override
				protected int getNumCols() {
					return RectBoardGameView.this.getNumCols();
				}
	
				@Override
				protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
					RectBoardGameView.this.mouseClicked(row, col, clickCount, mouseButton);
				}
				@Override
				protected ImageIcon getImage(int figure) {
					return RectBoardGameView.this.getImage(figure);
				}
				
			};
			JPanel boardPanel = new JPanel(new CenteredSquareLayout());
			boardPanel.add(jboard);
			mainPanel.add(boardPanel);
				
			this.sidePanel = new JPanel();
			sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
				this.infoArea = new InfoArea(){

					@Override
					public void chat(String msg) {
						gameWindow.getGameCtrl().sendInfo("Player " + gameWindow.getPlayerId() + ": " + msg);
					}
					
				};
				
			sidePanel.add(this.infoArea);
				this.playerInfoTable = new PlayerInfoTable(){
					@Override
					protected void changeColor(int row){
						this.colorChooser.setSelectedColorDialog(colors.get(row));
						this.colorChooser.openDialog();
						if (colorChooser.getColor() != null) {
							colors.put(row, colorChooser.getColor());
							RectBoardGameView.this.jboard.repaint();
						}
					}

					@Override
					protected void changeColorBackground(int row) {
						this.colorChooser.setSelectedColorDialog(colorsB.get(row));
						this.colorChooser.openDialog();
						if (colorChooser.getColor() != null) {
							colorsB.put(row, colorChooser.getColor());
							RectBoardGameView.this.jboard.repaint();
						}
					}
				};
				
			sidePanel.add(this.playerInfoTable);
			mainPanel.add(sidePanel);
			this.add(mainPanel);
		addInfoArea(this.infoArea.getContent());
	}
	
	protected Color getColorBG(int row, int col) {
		return this.playerInfoTable.getColorBG(row, col);
	}
	protected abstract Color getColor(int player);
	
	private void addInfoArea(String msg) {
		showInfoMessage(msg);
	}
	@Override
	public void showInfoMessage(String msg) {
		this.infoArea.addContent(msg);
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
	public void setGameViewCtrl(GameWindow<S,A> gameWindow){
		this.gameWindow = gameWindow;
	}
	
	@Override
	public void update(S state) {
		this.state = state;
		if(this.state.isFinished()){
			if (!block) {
				this.gameWindow.getGameCtrl().stopGame();
			}
		}
		jboard.repaint();
	}


	protected abstract int getNumRows();
	
	protected abstract int getNumCols();
	
	protected abstract ImageIcon getImage(int figure);
	
	protected abstract int getPosition(int row, int col);
	
	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);
	
	protected abstract void keyTyped(int keyCode);
	
	protected abstract void keyPressed(int keyCode);
	
	protected abstract void keyReleased(int keyCode);
}
