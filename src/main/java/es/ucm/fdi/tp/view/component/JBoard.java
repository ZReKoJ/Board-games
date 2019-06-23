package es.ucm.fdi.tp.view.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public abstract class JBoard extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4518722262994516431L;

	private int _CELL_HEIGHT = 50;
	private int _CELL_WIDTH = 50;
	private int _SEPARATOR = -2;
	
	protected int _SELECTED_ROW = -1;
	protected int _SELECTED_COL = -1;

	public enum Shape {
		CIRCLE, RECTANGLE
	}

	public JBoard() {
		initGUI();
	}

	private void initGUI() {
		setBorder(BorderFactory.createRaisedBevelBorder());

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				JBoard.this.keyTyped(e.getExtendedKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				JBoard.this.keyReleased(e.getKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				JBoard.this.keyPressed(e.getKeyCode());
			}
		});
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
				JBoard.this.requestFocus();
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

				JBoard.this.mouseClicked(row, col, e.getClickCount(), mouseButton);
			}
		});
		
		_SEPARATOR = getSepPixels();
		if ( _SEPARATOR < 0 ) _SEPARATOR = 0;

		this.setPreferredSize(new Dimension(400, 400));
		this.setMinimumSize(new Dimension(400, 400));
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

		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				drawCell(i, j, g);
	}

	private void drawCell(int row, int col, Graphics g) {
		int x = col * _CELL_WIDTH;
		int y = row * _CELL_HEIGHT;

		g.setColor( getBackground(row, col));
		g.fillRect(x, y, _CELL_WIDTH, _CELL_HEIGHT);

		int p = getPosition(row, col);

		Color c = getColor(p);
		
		if(p >= 0){
			g.setColor(c);
			g.fillOval(x, y, _CELL_WIDTH, _CELL_HEIGHT);
			ImageIcon im = getImage(p);
			if (im != null) g.drawImage(im.getImage(), x, y, _CELL_WIDTH, _CELL_HEIGHT, this);
		}

	}

	protected abstract void keyTyped(int keyCode);
	
	protected abstract void keyPressed(int keyCode);

	protected abstract void keyReleased(int keyCode);

	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);

	protected abstract ImageIcon getImage(int figure);

	protected abstract Color getColor(int player);

	protected abstract Integer getPosition(int row, int col);
	
	protected abstract Color getBackground(int row, int col);

	protected abstract int getNumRows();

	protected abstract int getNumCols();
	
	protected int getSepPixels() {
		return 2;
	}

}
