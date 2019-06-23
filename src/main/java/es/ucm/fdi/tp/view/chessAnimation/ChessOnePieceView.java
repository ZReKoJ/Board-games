package es.ucm.fdi.tp.view.chessAnimation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.game.chess.ChessBoard.Piece;
import es.ucm.fdi.tp.view.GameWindow;

@SuppressWarnings("serial")
public abstract class ChessOnePieceView extends JComponent {

	private int _PANEL_WIDTH = this.getWidth();
	private int _PANEL_HEIGHT = this.getHeight();
	private int _CELL_HEIGHT = 50;
	private int _CELL_WIDTH = 50;
	protected int _ROWS = 0;
	protected int _COLS = 0;
	protected int _SELECTED_ROW = -1;
	protected int _SELECTED_COL = -1;
	
	protected int _BACKGROUND_ENUM = 0;
	protected int _NUM_PIC_BACKGROUND = 28;
	
	public static String PATH = GameWindow.RESOURCE;
	public static String PATTERN = "%0" + 3 + "d";
	private String PATH_FIGURE = "onepiece/figure/";
	private String PATH_BACKGROUND = "onepiece/background/background";
	
	protected int _POINTER = 0;
	private int _PRESENTATION_COUNTER = 0;
	private int _COUNTER_LIMIT = 80;
	
	private Clip clip = null;
	private BufferedImage background, figure, animation;
	private OnePieceFigure op;

	protected byte showOnePieceFigure = 0x10;
	
	private int id;
	private int win = -1;
	
	public ChessOnePieceView() {
		initGUI();
	}

	private void initGUI() {
		setBorder(BorderFactory.createRaisedBevelBorder());

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == ' '){
					if (clip.isRunning()) clip.stop();
					else clip.loop(Clip.LOOP_CONTINUOUSLY);
				}
				else if (e.getKeyChar() == '\n'){
					try {
						clip.close();
						File soundFile = new File(GameWindow.RESOURCE + "onepiece/music/music (" + (((int) (Math.random() * 100)) % 38 + 1) + ").wav");
						AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						double gain = 0.1; // number between 0 and 1 (loudest)
					    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
						((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(dB);
						clip.loop(Clip.LOOP_CONTINUOUSLY);
					}
					catch (Exception e1) { e1.printStackTrace(); }
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}
			
		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				ChessOnePieceView.this.requestFocus();
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

				ChessOnePieceView.this.mouseClicked(row, col, e.getClickCount(), mouseButton);
				}
		});

		
		try {
			File soundFile = new File(GameWindow.RESOURCE + "onepiece/music/music (" + (((int) (Math.random() * 100)) % 38 + 1) + ").wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			double gain = 0.1; // number between 0 and 1 (loudest)
		    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
			((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(dB);
		} catch (Exception e1) { e1.printStackTrace(); }
		
		this.setPreferredSize(new Dimension(400, 400));
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		fillBoard(g);
	}

	private void fillBoard(Graphics g) {
		_PANEL_WIDTH = this.getWidth();
		_PANEL_HEIGHT = this.getHeight();
		_ROWS = getNumRows();
		_COLS = getNumCols();
		
		if (_ROWS <= 0 || _COLS <= 0) {
			BufferedImage screenProtector = getImage(PATH + "onepiece/screenProtector" + ".jpg");
			g.drawImage(screenProtector, 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
			return;
		}

		background = getImage(PATH + PATH_BACKGROUND + String.format(PATTERN, _BACKGROUND_ENUM % _NUM_PIC_BACKGROUND) + ".jpg");
		g.drawImage(makeImageTranslucent(background, 0.5), 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);

		_CELL_WIDTH = this.getWidth() / _COLS;
		_CELL_HEIGHT = this.getHeight() / _ROWS;

		if (win < 0 || _PRESENTATION_COUNTER >= _COUNTER_LIMIT){
			for (int i = 0; i < _ROWS; i++)
				for (int j = 0; j < _COLS; j++)
					drawCell(i, j, g);
			if (_SELECTED_ROW != -1 && _SELECTED_COL != -1) drawCell(_SELECTED_ROW, _SELECTED_COL, g);
		}
		
		Piece pi = Piece.valueOf(showOnePieceFigure);
		if (pi != Piece.Empty && pi != Piece.Outside){
			op = getFigure(showOnePieceFigure);
			if (_POINTER == op.getAnimation() - 1) showOnePieceFigure = 0x10;
			animation = getImage(PATH + PATH_FIGURE + op.getName() + "/animation/animation (" + (_POINTER % op.getAnimation() + 1) + ").png");
			double proportion = _PANEL_HEIGHT / (double) animation.getHeight();
			int width = (int) (animation.getWidth() * proportion);
			int height = (int) (animation.getHeight() * proportion);
			g.drawImage(animation, _PANEL_WIDTH / 2 - width / 2, 0, width, height, this);
		}
		
		if (win >= 0){
			gameFinishedPresentation(win, g);
		} else _PRESENTATION_COUNTER = 0;
		
		++_POINTER;
		this.repaint();
	}

	private void drawCell(int row, int col, Graphics g) {
		int x = col * _CELL_WIDTH;
		int y = row * _CELL_HEIGHT;

		Graphics2D g2 = (Graphics2D)(g);
		g2.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		if (_SELECTED_ROW == row && _SELECTED_COL == col){
			g.setColor(Color.GREEN);
			for (int i = 0; i < 3; ++i)
				g.drawRect(x + i, y + i, _CELL_WIDTH - 2 * i, _CELL_HEIGHT - 2 * i);
		}
		else {
			g.setColor(Color.LIGHT_GRAY);
			g.drawRect(x, y, _CELL_WIDTH, _CELL_HEIGHT);
		}

		byte p = getValue(invertValue(row, col, true), invertValue(row, col, false));
		Piece pi = Piece.valueOf(p);
		
		if (pi != Piece.Empty && pi != Piece.Outside) {
			String colorOrGray, path;
			op = getFigure(p);
			if (p / 8 == this.id) colorOrGray = "color";
			else colorOrGray = "gray";
			if (_SELECTED_ROW == row && _SELECTED_COL == col)
				path = PATH + PATH_FIGURE + op.getName() + "/dinamic/" + colorOrGray + String.format(PATTERN, _POINTER % op.getDinamic()) + ".png";
			else path = PATH + PATH_FIGURE + op.getName() + "/static/" + colorOrGray + String.format(PATTERN, _POINTER % op.getStatic()) + ".png";
			figure = getImage(path);
			double proportion = _CELL_HEIGHT / (double) figure.getHeight();
			int width = (int) (figure.getWidth() * proportion);
			int height = (int) (figure.getHeight() * proportion);
			g2.drawImage(figure, x + _CELL_WIDTH / 2 - width / 2, y + _CELL_HEIGHT - height, width, height, this);

		}
	}
	
	public void gameFinishedPresentation(int status, Graphics g){
		BufferedImage win = getImage(PATH + "onepiece/win.png");
		BufferedImage lose = getImage(PATH + "onepiece/lose.png");
		
		if (_PRESENTATION_COUNTER < _COUNTER_LIMIT){
			BufferedImage animation = getImage(PATH + "onepiece/animation/animation (" + (_PRESENTATION_COUNTER % _COUNTER_LIMIT + 1) + ").png");
			g.drawImage(animation, 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this); // No Thread here pls !!!
		}
		else if (_PRESENTATION_COUNTER == _COUNTER_LIMIT) {
			if (status == this.id) g.drawImage(win, 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
			else g.drawImage(lose, 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
		}
		else {
			if (status == this.id) g.drawImage(makeImageTranslucent(win, 0.3), 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
			else g.drawImage(makeImageTranslucent(lose, 0.3), 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
		}
		++_PRESENTATION_COUNTER;
	}
	
	public void drawImage(Graphics g, BufferedImage im, int x, int y, int width, int height, ChessOnePieceView v){
		Utils.worker.submit(new DrawImage(g){

			@Override
			public Void call() throws Exception {
				this.g.drawImage(im, x, y, width, height, v);
				return null;
			}
			
		});
	}
	
	public BufferedImage getImage(String path){
		BufferedImage image = null;
		Future<BufferedImage> bi = Utils.worker.submit(new ChargeImage(path));
		try { image = bi.get(); }
		catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
		return image;
	}
	
	public BufferedImage makeImageTranslucent(BufferedImage source, double alpha) {
	    BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(), java.awt.Transparency.TRANSLUCENT);
	    // Get the images graphics
	    Graphics2D g = target.createGraphics();
	    // Set the Graphics composite to Alpha
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
	    // Draw the image into the prepared receiver image
	    g.drawImage(source, null, 0, 0);
	    // let go of all system resources in this Graphics
	    g.dispose();
	    // Return the image
	    return target;
	}
	
	public int invertValue(int row, int col, boolean rowOrCol){
		if (rowOrCol) {
			if (this.id == 1) return col;
			else if (this.id == 0) return _COLS - 1 - col;
			else return row;
		}
		else return row;
	}
	
	protected abstract int getNumRows();
	
	protected abstract int getNumCols();
	
	protected abstract byte getValue(int row, int col);
	
	protected abstract OnePieceFigure getFigure(byte representation);

	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setWin(int win){
		this.win = win;
	}
}
