package es.ucm.fdi.tp.view.ttt2D;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.game.ttt2D.Ttt2DState;
import es.ucm.fdi.tp.view.GameWindow;

@SuppressWarnings("serial")
public class TttHorizonView extends JComponent {
	
	public enum Action {
		BOARD, BASE, BACKGROUND
	}

	private int _PRESENTATION_COUNTER = 0;
	private int _COUNTER_LIMIT = 10;
	// Panel characteriscs
	private int _PANEL_WIDTH = this.getWidth();
	private int _PANEL_HEIGHT = this.getHeight();
	private int _DIMENSION = 0;
	private int _INIT_DEGREE = 270;
	private int _DEGREE = _INIT_DEGREE;
	private int _CELL_DIM;
	private int _MARGIN;
	private double _CENTER;
	private int _UP_LEFT;
	private int _DOWN_LEFT;
	private int _UP_RIGHT;
	private int _DOWN_RIGHT;
	private int _EAST;
	private int _WEST;
	private int _SOUTH;
	private int _INIT_X;
	
	private int _NUM_PIC_HEAD = 55;
	private int _NUM_PIC_TAIL = 55;
	private int _NUM_PIC_BASE = 6;
	private int _NUM_PIC_BACKGROUND = 6;
	
	private int _HEAD_ENUM = 0;
	private int _TAIL_ENUM = 1;
	private int _BASE_ENUM = 0;
	private int _BACKGROUND_ENUM = 0;

	public String PATH = GameWindow.RESOURCE;
	public String PATTERN = "%0" + 3 + "d";
	public String PATH_HEAD = "disney/figure/figure";
	public String PATH_TAIL = "disney/figure/figure";
	public String PATH_BASE = "disney/base/base";
	public String PATH_BACKGROUND = "disney/background/background";
	
	BufferedImage head, tail, base, background;
	
	private Ttt2DState state;
	private int[][] board;
	private int id;
	
	public TttHorizonView(){
		initGUI();
	}
	
	private void initGUI() {
		setBorder(BorderFactory.createRaisedBevelBorder());

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				TttHorizonView.this.keyTyped(e.getKeyChar());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				TttHorizonView.this.setDrag(e.getX());
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				TttHorizonView.this.requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX(), y = e.getY();
				
				int mouseButton = 0;
				if (SwingUtilities.isLeftMouseButton(e)) mouseButton = 1;
				else if (SwingUtilities.isMiddleMouseButton(e)) mouseButton = 2;
				else if (SwingUtilities.isRightMouseButton(e)) mouseButton = 3;
				else return; // Unknown button, don't know if it is possible!
				
				if (_EAST < x && x < _PANEL_WIDTH - _WEST){
					if (y < _PANEL_HEIGHT - _SOUTH) TttHorizonView.this.mouseClicked(y, x, e.getClickCount(), mouseButton, Action.BOARD);
					else TttHorizonView.this.mouseClicked(y, x, e.getClickCount(), mouseButton, Action.BASE);
				}
				else TttHorizonView.this.mouseClicked(y, x, e.getClickCount(), mouseButton, Action.BACKGROUND);
			}
		});

		addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e){
				
			}
			
		});
		
		addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				TttHorizonView.this.mouseDragged(e.getX());
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
		});
		
		this.setPreferredSize(new Dimension(400, 400));
		this.setMinimumSize(new Dimension(400, 400));
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		fillBoard(g);
	}

	private void fillBoard(Graphics g) {
		_PANEL_WIDTH = this.getWidth();
		_PANEL_HEIGHT = this.getHeight();
		
		if (_DIMENSION <= 0) {
			background = getImage(PATH + "disney/screenProtector" + ".jpg");
			g.drawImage(background, 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
			return;
		}
		
		_CELL_DIM = (int) (Math.sqrt((_PANEL_WIDTH * _PANEL_WIDTH) / 2) / _DIMENSION);
		_CENTER = _DIMENSION / 2.0;
		_SOUTH = _PANEL_WIDTH / 20;
		_MARGIN = _CELL_DIM / 2;
		
		head = getImage(PATH + PATH_HEAD + String.format(PATTERN, _HEAD_ENUM) + "/image_part_" + String.format(PATTERN, ((_DEGREE + 360 - _INIT_DEGREE) % 360) / 9 + 1) + ".png");
		tail = getImage(PATH + PATH_TAIL + String.format(PATTERN, _TAIL_ENUM) + "/image_part_" + String.format(PATTERN, ((_DEGREE + 360 - _INIT_DEGREE) % 360) / 9 + 1) + ".png");
		base = getImage(PATH + PATH_BASE + String.format(PATTERN, _BASE_ENUM) + ".png");
		background = getImage(PATH + PATH_BACKGROUND + String.format(PATTERN, _BACKGROUND_ENUM) + ".jpg");
		g.drawImage(background, 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
		
		if (0 <= _DEGREE && _DEGREE < 90){
			for (int j = 0; j < _DIMENSION; ++j)
				for (int i = _DIMENSION - 1; i >= 0; --i)
				drawCell(i, j, g);
		}
		else if (90 <= _DEGREE && _DEGREE < 180){
			for (int i = _DIMENSION - 1; i >= 0; --i)
				for (int j = _DIMENSION - 1; j >= 0; --j)
					drawCell(i, j, g);
		}
		else if (180 <= _DEGREE && _DEGREE < 270){
			for (int j = _DIMENSION - 1; j >= 0; --j)
				for (int i = 0; i < _DIMENSION; ++i)
					drawCell(i, j, g);
		}
		else if (270 <= _DEGREE && _DEGREE < 360){
			for (int j = 0; j < _DIMENSION; ++j)
				for (int i = 0; i < _DIMENSION; ++i)
					drawCell(i, j, g);
		}
		
		_UP_RIGHT = getDistance(_DIMENSION - _CENTER, -_CENTER, _DEGREE);
		_UP_LEFT = getDistance(-_CENTER, -_CENTER, _DEGREE);
		_DOWN_LEFT = getDistance(-_CENTER, _DIMENSION - _CENTER, _DEGREE);
		_DOWN_RIGHT = getDistance(_DIMENSION - _CENTER, _DIMENSION - _CENTER, _DEGREE);
		
		int max, min;
		if (Math.abs(_UP_LEFT) > Math.abs(_UP_RIGHT)){
			max = Math.abs(_UP_LEFT);
			min = Math.abs(_DOWN_LEFT);
		}
		else {
			max = Math.abs(_UP_RIGHT);
			min = Math.abs(_DOWN_RIGHT);
		}
		
		_EAST = (_PANEL_WIDTH - (2 * max)) / 2;
		_WEST = _PANEL_WIDTH - (2 * max) - _EAST;
		
		if (_DEGREE % 90 >= 45){
			g.drawImage(base, (_PANEL_WIDTH / 2) - max, _PANEL_HEIGHT - _SOUTH, max - min, _SOUTH, this);
			g.drawImage(base, (_PANEL_WIDTH / 2) - min, _PANEL_HEIGHT - _SOUTH, max + min, _SOUTH, this);
		}
		else {
			g.drawImage(base, (_PANEL_WIDTH / 2) - max, _PANEL_HEIGHT - _SOUTH, max + min, _SOUTH, this);
			g.drawImage(base, (_PANEL_WIDTH / 2) + min, _PANEL_HEIGHT - _SOUTH, max - min, _SOUTH, this);
		}
		
		if (this.state != null && this.state.isFinished()){
			gameFinishedPresentation(this.state.getWinner(), g);
		} else _PRESENTATION_COUNTER = 0;
	}
	
	private void drawCell(int row, int col, Graphics g) {
		int p = board[row][col];
		if (p != -1){
			/*if (p == 0)
				g.setColor(Color.RED);
			else if (p == 1)
				g.setColor(Color.BLACK);*/
			/*switch (p){
			case 0: g.setColor(Color.MAGENTA); break;
			case 1: g.setColor(Color.YELLOW); break;
			case 2: g.setColor(Color.CYAN); break;
			case 3: g.setColor(Color.RED); break;
			case 4: g.setColor(Color.ORANGE); break;
			case 5: g.setColor(Color.BLUE); break;
			case 6: g.setColor(Color.PINK); break;
			case 7: g.setColor(Color.GREEN); break;
			case 8: g.setColor(Color.BLACK); break;
			}*/
			
			BufferedImage figure = null;
			if (p == 0) figure = tail;
			else if (p == 1) figure = head; 
			double proportion = (_MARGIN * 2) / (double) figure.getWidth();
			int width = (int) (figure.getWidth() * proportion);
			int height = (int) (figure.getHeight() * proportion);
			//width = figure.getWidth(); height = figure.getHeight();
			int point = getDistance(row + 0.5 - _CENTER, col + 0.5 - _CENTER, _DEGREE);
			g.drawImage(figure, (_PANEL_WIDTH / 2) - point - (width / 2), _PANEL_HEIGHT - height, width, height, this);
		
		}
	}
	
	public int getDistance(double x, double y, int degree){
		y *= -1;
		double radius = Math.sqrt((x * _CELL_DIM * x * _CELL_DIM) + (y * _CELL_DIM * y * _CELL_DIM));
		int degreee = (int) Math.toDegrees(Math.acos(((x * _CELL_DIM) / radius)));
		if (y < 0) degreee = 360 - degreee;
		return (int) (radius * Math.cos(Math.toRadians((degree + degreee) % 360)));
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
	
	public void gameFinishedPresentation(int status, Graphics g){
		BufferedImage win = getImage(PATH + "disney/win.png");
		BufferedImage lose = getImage(PATH + "disney/lose.png");
		BufferedImage draw = getImage(PATH + "disney/draw.png");
		
		if (_PRESENTATION_COUNTER < _COUNTER_LIMIT){
			BufferedImage castle = getImage(PATH + "disney/animation/castle" + String.format(PATTERN, _PRESENTATION_COUNTER % 4) + ".png");
			g.drawImage(castle, 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
			if (status == this.id) g.drawImage(win, 0, _PANEL_HEIGHT / 3 * 2, _PANEL_WIDTH, _PANEL_HEIGHT / 3, this);
			else if (status == -1) g.drawImage(draw, 0, _PANEL_HEIGHT / 3 * 2, _PANEL_WIDTH, _PANEL_HEIGHT / 3, this);
			else g.drawImage(lose, 0, _PANEL_HEIGHT / 3 * 2, _PANEL_WIDTH, _PANEL_HEIGHT / 3, this);
			++_PRESENTATION_COUNTER;
			this.repaint();
		}
		
	}
	
	private void keyTyped(char keyCode) {
		switch (keyCode){
		case '+': 
			if (_DEGREE == 0) {
				_DEGREE = 359;
				
			}
			else --_DEGREE;
			break;
		case '-': 
			if (_DEGREE == 359) {
				_DEGREE = 0;
			}
			else ++_DEGREE;
			break;
		case 'h':
	        for (int i = 0; i < board.length; i++) {
	           	System.out.print("|");
	            for (int j=0; j < board.length; j++) {
	            	System.out.print(" ");
	            	switch(board[i][j]){
	            	case -1: System.out.print(" "); break;
	            	case 0: System.out.print("O"); break;
	            	case 1: System.out.print("X"); break;
	            	}
	            	System.out.print(" |");
	            }
	            System.out.print("\n");
	        }
	        break;
		default:; break;
		}
		this.repaint();
	}
	
	private void setDrag(int x){
		_INIT_X = x;
	}
	
	private void mouseDragged(int x){
		double interval = _PANEL_WIDTH / 90.0;
		double degree = (x - _INIT_X) / interval;
		if (degree > -1.0 && degree < 0.0) degree = -1;
		else if (degree > 0 && degree < 1.0) degree = 1;
		_DEGREE = (int) (_DEGREE - degree);
		if (_DEGREE < 0) _DEGREE = 359 + _DEGREE;
		_DEGREE %= 360;
		_INIT_X = x;
		this.repaint();
	}
	
	private void mouseClicked(int row, int col, int clickCount, int mouseButton, Action whatUClicked){
		switch (whatUClicked){
		case BOARD:
			if (mouseButton == 1) _HEAD_ENUM = ((_HEAD_ENUM + _NUM_PIC_HEAD + 1) % _NUM_PIC_HEAD);
			else if (mouseButton == 3) _TAIL_ENUM = ((_TAIL_ENUM + _NUM_PIC_TAIL + 1) % _NUM_PIC_TAIL);
			break;
		case BASE:
			if (mouseButton == 3) _BASE_ENUM = ((_BASE_ENUM + _NUM_PIC_BASE + 1) % _NUM_PIC_BASE);
			break;
		case BACKGROUND:
			if (mouseButton == 3) _BACKGROUND_ENUM = ((_BACKGROUND_ENUM + _NUM_PIC_BACKGROUND + 1) % _NUM_PIC_BACKGROUND);
			break;
		}
		System.out.println("Mouse: " + clickCount + " clicks at " + whatUClicked.toString() + " with Button " + mouseButton);
		this.repaint();
	}
	
	public void update(Ttt2DState state) {
		this.state = state;
		this.board = state.getBoard();
		_DIMENSION = this.board.length;
		this.repaint();
	}
	
	public void setId(int id){
		this.id = id;
	}
}
