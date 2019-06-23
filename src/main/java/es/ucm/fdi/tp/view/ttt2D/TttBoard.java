package es.ucm.fdi.tp.view.ttt2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.game.ttt2D.Ttt2DAction;

public abstract class TttBoard extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4518722262994516431L;

	private int _CELL_HEIGHT = 50;
	private int _CELL_WIDTH = 50;
	protected int[][] board;
	protected List<Ttt2DAction> actions = new ArrayList<Ttt2DAction>();
	protected int id;
	protected boolean finished = false;

	public TttBoard() {
		initGUI();
	}

	private void initGUI() {
		setBorder(BorderFactory.createRaisedBevelBorder());
		
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				TttBoard.this.requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int col = (e.getX() / _CELL_WIDTH);
				int row = (e.getY() / _CELL_HEIGHT);

				int mouseButton = 0;

				if (SwingUtilities.isLeftMouseButton(e))
					mouseButton = 1;
				else if (SwingUtilities.isMiddleMouseButton(e))
					mouseButton = 2;
				else if (SwingUtilities.isRightMouseButton(e))
					mouseButton = 3;

				if (mouseButton == 0)
					return; // Unknown button, don't know if it is possible!

				TttBoard.this.mouseClicked(row, col, e.getClickCount(), mouseButton);
			}
		});

		this.setPreferredSize(new Dimension(400, 400));
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		fillBoard(g);
	}

	private void fillBoard(Graphics g) {
		int numCols = getNumCols();
		int numRows = getNumRows();

		if (numCols <= 0 || numRows <= 0) {
			g.setColor(Color.BLACK);
			g.drawString("Waiting for game to start!", 20, this.getHeight() / 2);
			return;
		}

		_CELL_WIDTH = this.getWidth() / numCols;
		_CELL_HEIGHT = this.getHeight() / numRows;

		createBoardData();
		if (!finished) mergeBoardData();
		
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				drawCell(i, j, g);
	}

	private void drawCell(int row, int col, Graphics g) {
		int x = col * _CELL_WIDTH;
		int y = row * _CELL_HEIGHT;

		g.setColor( getBackground(row, col));
		g.fillRect(x, y, _CELL_WIDTH, _CELL_HEIGHT);

		int p = board[row][col];
		Color c = getColor(p);
		if((!finished && p == this.id) || (finished && p != -1)){
			g.setColor(c);
			g.fillOval(x, y, _CELL_WIDTH, _CELL_HEIGHT);
		}

	}
	
	protected abstract void createBoardData();

	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);

	protected abstract Color getColor(int player);

	protected abstract Color getBackground(int row, int col);

	protected abstract int getNumRows();

	protected abstract int getNumCols();

	protected int getSepPixels() { return 2; }

	protected void mergeBoardData() {
		boolean emptyBoard = true;
		for (int i = 0; i < board.length && emptyBoard; ++i)
			for (int j = 0; j < board[0].length && emptyBoard; ++j)
				emptyBoard = emptyBoard && board[i][j] == -1;
		if (!emptyBoard){
			for (Ttt2DAction a : this.actions)
				board[a.getRow()][a.getCol()] = this.id;
		}
		else this.actions.clear();
	}
	
	public void setId (int id){
		this.id = id;
	}
	
}
