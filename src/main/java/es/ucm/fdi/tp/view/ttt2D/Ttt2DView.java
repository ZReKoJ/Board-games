package es.ucm.fdi.tp.view.ttt2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import es.ucm.fdi.tp.game.ttt2D.Ttt2DAction;
import es.ucm.fdi.tp.game.ttt2D.Ttt2DState;
import es.ucm.fdi.tp.mvc.GameView;
import es.ucm.fdi.tp.view.GameWindow;
import es.ucm.fdi.tp.view.component.InfoArea;
import es.ucm.fdi.tp.view.component.PlayerInfoTable;

@SuppressWarnings("serial")
public class Ttt2DView extends GameView<Ttt2DState,Ttt2DAction> {
	
	protected Ttt2DState state;
	protected TttBoard jboard;
	protected GameWindow<Ttt2DState,Ttt2DAction> gameWindow;
	protected InfoArea infoArea;
	protected PlayerInfoTable playerInfoTable;
	protected TttHorizonView specialView;
	protected int id;
	protected boolean block;
	
	public Ttt2DView(){
		super();
		initGUI();
	}
	
	private void initGUI() { 
		this.setLayout(new BorderLayout());
		this.jboard = new TttBoard() {
	
				@Override
				protected Color getColor(int player) {
					return Ttt2DView.this.getColor(player);
				}
	
				@Override
				protected Color getBackground(int row, int col) {
					return Ttt2DView.this.getColorBG(row, col);
				}
	
				@Override
				protected int getNumRows() {
					return Ttt2DView.this.getNumRows();
				}
	
				@Override
				protected int getNumCols() {
					return Ttt2DView.this.getNumCols();
				}
	
				@Override
				protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
					Ttt2DView.this.mouseClicked(row, col, clickCount, mouseButton);
				}

				@Override
				protected void createBoardData() {
					this.board = Ttt2DView.this.state.getBoard();
					if (Ttt2DView.this.state.isFinished()) this.finished = true;
					else this.finished = false;
				}
				
			};
			this.infoArea = new InfoArea(){

				@Override
				public void chat(String msg) {
					gameWindow.getGameCtrl().sendInfo("Player " + id + ": " + msg);
				}
				
			};
			this.playerInfoTable = new PlayerInfoTable(){
				@Override
				protected void changeColor(int row){
					this.colorChooser.setSelectedColorDialog(colors.get(row));
					this.colorChooser.openDialog();
					if (colorChooser.getColor() != null) {
						colors.put(row, colorChooser.getColor());
						Ttt2DView.this.jboard.repaint();
					}
				}
				@Override
				protected void changeColorBackground(int row) {
					this.colorChooser.setSelectedColorDialog(colorsB.get(row));
					this.colorChooser.openDialog();
					if (colorChooser.getColor() != null) {
						colorsB.put(row, colorChooser.getColor());
						Ttt2DView.this.jboard.repaint();
					}
				}
			};
			this.specialView = new TttHorizonView();

	        JPanel mainPanel = new JPanel(new BorderLayout());
	        JSplitPane viewPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	        JToolBar infoPanel = new JToolBar();
	        
	        viewPanel.setResizeWeight(0.7);
	        viewPanel.setPreferredSize(new Dimension(800, 800));
			
			infoPanel.add(this.infoArea);
			infoPanel.add(this.playerInfoTable);

			viewPanel.add(this.specialView);
			viewPanel.add(jboard);
			
			mainPanel.add(infoPanel, BorderLayout.SOUTH);
			mainPanel.add(viewPanel, BorderLayout.CENTER);
			
			this.add(mainPanel, BorderLayout.CENTER);
			
		addInfoArea(this.infoArea.getContent());
	}
	
	private void addInfoArea(String msg) {
		showInfoMessage(msg);
	}
	
	protected int getNumRows() {
		if(this.state != null) return this.state.getBoard().length;
		else return 0;
	}
	
	protected int getNumCols() {
		if(this.state != null) return this.state.getBoard().length;
		else return 0;
	}
	
	protected int getPosition(int row, int col){
		if (this.state.at(row, col) == this.id) return this.id;
		else return -1;
	}
	
	protected Color getColor(int player) {
		return this.playerInfoTable.getColor(player);
	}
	
	protected Color getColorBG(int row, int col) {
		return this.playerInfoTable.getColorBG(row, col);
	}
	
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton){
		if(!block && this.state.at(row, col) != this.id){
			this.gameWindow.getGameCtrl().makeMove(new Ttt2DAction(state.getTurn(), row, col));
			this.jboard.actions.add(new Ttt2DAction(state.getTurn(), row, col));
			showInfoMessage("You clicked on " + row + " " + col);
		}
		else if (block)
			showInfoMessage("It is not your turn");
		else
			showInfoMessage("This position is already occupied");
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
	public void update(Ttt2DState state) {
		this.state = state;
		if(this.state.isFinished()){
			if (!block) {
				this.gameWindow.getGameCtrl().stopGame();
			}
		}
		this.specialView.update(state);
		jboard.repaint();
	}
	
	@Override
	public void showInfoMessage(String msg) {
		this.infoArea.addContent(msg);
		this.repaint();
	}
	
	@Override
	public void setGameViewCtrl(GameWindow<Ttt2DState,Ttt2DAction> gameWindow){
		this.gameWindow = gameWindow;
		this.id = gameWindow.getPlayerId();
		this.jboard.setId(gameWindow.getPlayerId());
		this.specialView.setId(gameWindow.getPlayerId());
	}
}
