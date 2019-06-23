package es.ucm.fdi.tp.view.was3D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.game.was3D.Was3DAction;
import es.ucm.fdi.tp.game.was3D.Was3DFigures;
import es.ucm.fdi.tp.game.was3D.Was3DState;
import es.ucm.fdi.tp.mvc.GameView;
import es.ucm.fdi.tp.view.GameWindow;

@SuppressWarnings("serial")
public class Was3DView extends GameView<Was3DState,Was3DAction> {
	
	public enum Action {
		PLAYER, PLAYER_POKEMON, POKEBALL, BOY, GIRL, SMART, RAND, BATTLE_BOX, BOARD, BACKGROUND
	}

	public String[] texts = { "bag", "console", "autoRandom", "fight", "restart", "player",
			"pokemon", "rand", "run", "autoSmart", "sheep", "smart", "wolf", "return", "manual", "null" };
	
	// Console image panels
	private Action _CONSOLE_AT = Action.BATTLE_BOX;
	private boolean _POKEBALL_ZONE = false;
	private boolean _SHOW_MESSAGE = false;
	private int _PRESENTATION_COUNTER = 0;
	private int _COUNTER_LIMIT = 10;
	// Panel characteriscs
	private int _PANEL_WIDTH = this.getWidth();
	private int _PANEL_HEIGHT = this.getHeight();
	// Cell characteristics
	private int _CELL_WIDTH = 40;
	private int _CELL_HEIGHT = _CELL_WIDTH / 2;
	// Cell characteristics
	private int _SEPARATOR = 0;
	private int _INCLINATION = _CELL_WIDTH;
	private int _DEPTH = _CELL_WIDTH / 10;
	// Cell characteristics
	private int _CELL_ROW = -1;
	private int _CELL_COL = -1;
	private int _SELECTED_ROW = -1;
	private int _SELECTED_COL = -1;
	// Panel margins
	private int _NORTH = 0;
	private int _EAST = 0;
	private int _SOUTH = 0;
	private int _WEST = 0;
	// Board dimensions
	private int _ROWS = 0;
	private int _COLS = 0;
	private int _START_ROW = 0;
	private int _START_COL = 0;
	// Board dimensions
	private int _BOARD_WIDTH = _CELL_WIDTH * _COLS + _INCLINATION * _ROWS;
	private int _BOARD_HEIGHT = _CELL_HEIGHT * _ROWS;
	// Player
	private int _PLAYER_WIDTH = 0;
	private int _PLAYER_HEIGHT = 0;
	private int _PLAYER_POK_WIDTH = 0;
	private int _PLAYER_POK_HEIGHT = 0;
	// Manual
	private int _BOY_WIDTH = 0;
	private int _BOY_HEIGHT = 0;
	private int _GIRL_WIDTH = 0;
	private int _GIRL_HEIGHT = 0;
	// Smart
	private int _SMART_WIDTH = 0;
	private int _SMART_HEIGHT = 0;
	// Rand
	private int _RAND_WIDTH = 0;
	private int _RAND_HEIGHT = 0;
	// Battle Box
	private int _BATTLE_BOX_X = 0;
	private int _BATTLE_BOX_Y = _PANEL_HEIGHT - _SOUTH;
	private int _BATTLE_BOX_WIDTH = 0;
	private int _BATTLE_BOX_HEIGHT = 0;
	private int _BATTLE_BOX_CELLS = 7;
	private int _BATTLE_BOX_CELL_WIDTH = _BATTLE_BOX_WIDTH / _BATTLE_BOX_CELLS;
	private int _BATTLE_BOX_CELL_HEIGHT = _BATTLE_BOX_HEIGHT / _BATTLE_BOX_CELLS;
	// Pictures count
	private int _NUM_PIC_WOLF = 18;
	private int _NUM_PIC_SHEEP = 16;
	private int _NUM_PIC_PLAYER = 11;
	private int _NUM_PIC_BOY = 11;
	private int _NUM_PIC_GIRL = 11;
	private int _NUM_PIC_SMART = 4;
	private int _NUM_PIC_RAND = 1;
	private int _NUM_PIC_BACKGROUND = 29;
	private int _NUM_PIC_GROUND = 32;
	//private int _NUM_PIC_WORD = texts.length;
	// Pictures enumeration
	private int _WOLF_ENUM = 0;
	private int _SHEEP_ENUM = 0;
	private int _BACKGROUND_ENUM = 0;
	private int _PLAYER_ENUM;
	private int _BOY_ENUM = 0;
	private int _GIRL_ENUM = 0;
	private int _SMART_ENUM = 0;
	private int _RAND_ENUM = 0;
	private int _GROUND_ENUM = 0;
	private int[] _WORD_ENUM = { 3, 0, 6, 8 };
	private String[] _MODE_ENUM = { "girl", "smart", "rand" };
	private int _ARROW_POINTER = 0;
	// Paths
	public String PATH = GameWindow.RESOURCE;
	public String PATTERN = "%0" + 3 + "d";
	public String PATH_WOLF = "pokemon/figure/wolf/wolf";
	public String PATH_SHEEP = "pokemon/figure/sheep/sheep";
	public String PATH_PLAYER = "pokemon/player/boy/boy";
	public String PATH_BOY = "pokemon/player/boy/boy";
	public String PATH_GIRL = "pokemon/player/girl/girl";
	public String PATH_SMART = "pokemon/player/akinator/akinator";
	public String PATH_RAND = "pokemon/player/bender/bender";
	public String PATH_BACKGROUND = "pokemon/background/background";
	public String PATH_WORD = "pokemon/text/";
	public String PATH_GROUND = "pokemon/ground/ground";
	// Images
	private BufferedImage sheep, wolf, player, pokemon, boy, girl, smart, rand, background, text, ground;

	private Was3DState state;
	private int[][] board;
	private int[][] presentationBoard;
	private GameWindow<Was3DState, Was3DAction> gameWindow;
	private JTextArea textArea = new JTextArea();
	private boolean block;
	private boolean init = false;

	public Was3DView() {
		initGUI();
	}

	private void initGUI() {
		setBorder(BorderFactory.createRaisedBevelBorder());

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				Was3DView.this.keyTyped(e.getKeyChar());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Was3DView.this.keyReleased(e.getKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				Was3DView.this.keyPressed(e.getKeyCode());
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
				Was3DView.this.requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX(), y = e.getY();
			
				int mouseButton = 0;
				if (SwingUtilities.isLeftMouseButton(e)) mouseButton = 1;
				else if (SwingUtilities.isMiddleMouseButton(e)) mouseButton = 2;
				else if (SwingUtilities.isRightMouseButton(e)) mouseButton = 3;
				else return; // Unknown button, don't know if it is possible!
				
				int row, col;
				Action zone = setZone(x, y);
				switch (zone) {
				case BATTLE_BOX:
					row = (y - (_PANEL_HEIGHT - _BATTLE_BOX_HEIGHT)) / _BATTLE_BOX_CELL_HEIGHT;
					col = x / _BATTLE_BOX_CELL_WIDTH;
					break;
				case BOARD:
					row = (y - _NORTH) / (_CELL_HEIGHT + (_SEPARATOR / 2));
					int west = _EAST; // Margen de la esquina inferior izquierda
					west += ((_ROWS - row - 1) * _INCLINATION); // Voy a la esquina inferior del row
					west += ((_CELL_HEIGHT - ((y - _NORTH) % (_CELL_HEIGHT + (_SEPARATOR / 2)))) * 2); // Me centro en el pixel del row
					col = (x - west) / (_CELL_WIDTH + _SEPARATOR);
					break;
				default:
					row = y;
					col = x;
					break;
				}
				Was3DView.this.mouseClicked(row, col, e.getClickCount(), mouseButton, zone);
			}
		});

		addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e){
				int x = e.getX(), y = e.getY();
				if (setZone(x, y) == Action.BOARD)
					Was3DView.this.mouseWheelMoved(e.getWheelRotation());
			}
			
		});
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(Color.WHITE);
		textArea.setForeground(Color.DARK_GRAY);
		textArea.setFont(new Font("Serif", Font.BOLD, 10));
		this.add(textArea);
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
		textArea.setVisible(false);
		
		if ( _SEPARATOR < 0 || _SEPARATOR > 50) _SEPARATOR = 0; // Hay que poner un maximo limite de sep pero no se cual
		_SEPARATOR /= 2;
		
		if (_ROWS <= 0 || _COLS <= 0) {
			background = getImage(PATH + "pokemon/screenProtector" + ".jpg");
			g.drawImage(background, 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
			return;
		}
		
		_CELL_WIDTH = ((_PANEL_WIDTH / 2) - (_COLS * _SEPARATOR)) / _COLS;
		_CELL_HEIGHT = _CELL_WIDTH / 2;
		_INCLINATION = _CELL_WIDTH;
		_DEPTH = _CELL_WIDTH / 10;
		_BOARD_WIDTH = _CELL_WIDTH * _COLS + _INCLINATION * _ROWS + _SEPARATOR * _COLS;
		_BOARD_HEIGHT = _CELL_HEIGHT * _ROWS + (_SEPARATOR / 2) * _ROWS;
		while (_BOARD_WIDTH < _PANEL_WIDTH || _BOARD_HEIGHT < this.getHeight()){
			++_CELL_WIDTH;
			_CELL_HEIGHT = _CELL_WIDTH / 2;
			_INCLINATION = _CELL_WIDTH;
			_DEPTH = _CELL_WIDTH / 10;
			_BOARD_WIDTH = _CELL_WIDTH * _COLS + _INCLINATION * _ROWS + _SEPARATOR * _COLS;
			_BOARD_HEIGHT = _CELL_HEIGHT * _ROWS + _DEPTH + (_SEPARATOR / 2) * _ROWS;
		}
		while (_BOARD_WIDTH >= _PANEL_WIDTH || _BOARD_HEIGHT >= this.getHeight()){
			--_CELL_WIDTH;
			_CELL_HEIGHT = _CELL_WIDTH / 2;
			_INCLINATION = _CELL_WIDTH;
			_DEPTH = _CELL_WIDTH / 10;
			_BOARD_WIDTH = _CELL_WIDTH * _COLS + _INCLINATION * _ROWS + _SEPARATOR * _COLS;
			_BOARD_HEIGHT = _CELL_HEIGHT * _ROWS + _DEPTH + (_SEPARATOR / 2) * _ROWS;
		}
		_NORTH = (_PANEL_HEIGHT - _BOARD_HEIGHT) / 2;
		_WEST = (_PANEL_WIDTH - _BOARD_WIDTH) / 2;
		_SOUTH = _PANEL_HEIGHT - _BOARD_HEIGHT - _NORTH;
		_EAST = _PANEL_WIDTH - _BOARD_WIDTH - _WEST;
		
		background = getImage(PATH + PATH_BACKGROUND + String.format(PATTERN, _BACKGROUND_ENUM) + ".jpg");
		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

		BufferedImage pokeball;
		double height = _NORTH / 3.0, proportion;
		boy = getImage(PATH + PATH_BOY + String.format(PATTERN, _BOY_ENUM) + ".png");
		proportion = height / (double) boy.getHeight();
		_BOY_WIDTH = (int) (boy.getWidth() * proportion); _BOY_HEIGHT = (int) (boy.getHeight() * proportion);
		girl = getImage(PATH + PATH_GIRL + String.format(PATTERN, _GIRL_ENUM) + ".png");
		proportion = height / (double) girl.getHeight();
		_GIRL_WIDTH = (int) (girl.getWidth() * proportion); _GIRL_HEIGHT = (int) (girl.getHeight() * proportion);
		smart = getImage(PATH + PATH_SMART + String.format(PATTERN, _SMART_ENUM) + ".png");
		proportion = height / (double) smart.getHeight();
		_SMART_WIDTH = (int) (smart.getWidth() * proportion); _SMART_HEIGHT = (int) (smart.getHeight() * proportion);
		rand = getImage(PATH + PATH_RAND + String.format(PATTERN, _RAND_ENUM) + ".png");
		proportion = height / (double) rand.getHeight();
		_RAND_WIDTH = (int) (rand.getWidth() * proportion); _RAND_HEIGHT = (int) (rand.getHeight() * proportion);
		
		if (_POKEBALL_ZONE){
			pokeball = getImage(PATH + "pokemon/pokeball_opened.png");
			g.drawImage(pokeball, 0, 0, _NORTH, _NORTH, this);
			for (int i = 0; i < _MODE_ENUM.length; ++i){
				switch(_MODE_ENUM[i]){
				case "boy":
					g.drawImage(boy, _NORTH, (int) (i * height), _BOY_WIDTH, _BOY_HEIGHT, this);
					text = getImage(PATH + "pokemon/text/manual" + ".png");
					break;
				case "girl":
					g.drawImage(girl, _NORTH, (int) (i * height), _GIRL_WIDTH, _GIRL_HEIGHT, this);
					text = getImage(PATH + "pokemon/text/manual" + ".png");
					break;
				case "smart":
					g.drawImage(smart, _NORTH, (int) (i * height), _SMART_WIDTH, _SMART_HEIGHT, this);
					text = getImage(PATH + "pokemon/text/smart" + ".png");
					break;
				case "rand":
					g.drawImage(rand, _NORTH, (int) (i * height), _RAND_WIDTH, _RAND_HEIGHT, this);
					text = getImage(PATH + "pokemon/text/rand" + ".png");
					break;
				}

				g.drawImage(text, _NORTH + _NORTH / 2, (int) ((i * height) + (height / 4)), _NORTH / 2, (int) (height / 2), this);
			}
		}
		else {
			pokeball = getImage(PATH + "pokemon/pokeball_closed.png");
			g.drawImage(pokeball, 0, 0, _NORTH, _NORTH, this);
		}

		if (!init){
			switch(this.gameWindow.getPlayerMode()){
			case AI:
				PATH_PLAYER = PATH_SMART;
				_PLAYER_ENUM = _SMART_ENUM;
				_NUM_PIC_PLAYER = _NUM_PIC_SMART;
				_MODE_ENUM[0] = "boy"; _MODE_ENUM[1] = "girl"; _MODE_ENUM[2] = "rand";
				break;
			case RANDOM:
				PATH_PLAYER = PATH_RAND;
				_PLAYER_ENUM = _RAND_ENUM;
				_NUM_PIC_PLAYER = _NUM_PIC_RAND;
				_MODE_ENUM[0] = "boy"; _MODE_ENUM[1] = "girl"; _MODE_ENUM[2] = "smart";
				break;
			default: break;
			}
			init = true;
		}
		
		player = getImage(PATH + PATH_PLAYER + String.format(PATTERN, _PLAYER_ENUM) + ".png");
		_PLAYER_WIDTH = player.getWidth(); _PLAYER_HEIGHT = player.getHeight();
		g.drawImage(player, _PANEL_WIDTH - _PLAYER_WIDTH, _PANEL_HEIGHT - _PLAYER_HEIGHT, _PLAYER_WIDTH, _PLAYER_HEIGHT, this);
		
		if (this.gameWindow.getPlayerId() == Was3DFigures.WOLF.getRepresentation())
			pokemon = getImage(PATH + PATH_WOLF + "F" + String.format(PATTERN, _WOLF_ENUM) + ".png");
		else if (this.gameWindow.getPlayerId() == Was3DFigures.SHEEP.getRepresentation())
			pokemon = getImage(PATH + PATH_SHEEP + "F" + String.format(PATTERN, _SHEEP_ENUM) + ".png");
		if (state != null && state.isFinished() && state.getWinner() == gameWindow.getPlayerId()){
			if (this.gameWindow.getPlayerId() == Was3DFigures.WOLF.getRepresentation())
				pokemon = getImage(PATH + PATH_WOLF + "F" + String.format(PATTERN, _WOLF_ENUM + 1) + ".png");
			else if (this.gameWindow.getPlayerId() == Was3DFigures.SHEEP.getRepresentation())
				pokemon = getImage(PATH + PATH_SHEEP + "F" + String.format(PATTERN, _SHEEP_ENUM + 1) + ".png");
		}
		_PLAYER_POK_WIDTH = pokemon.getWidth() / 2; _PLAYER_POK_HEIGHT = pokemon.getHeight() / 2;
		g.drawImage(pokemon, _PANEL_WIDTH - _PLAYER_WIDTH - _PLAYER_POK_WIDTH, _PANEL_HEIGHT - _PLAYER_POK_HEIGHT, _PLAYER_POK_WIDTH, _PLAYER_POK_HEIGHT, this);
		
		text = getImage(PATH + "pokemon/text/battleBox" + ".png");
		
		_BATTLE_BOX_X = 0;
		_BATTLE_BOX_Y = _PANEL_HEIGHT - _SOUTH;
		_BATTLE_BOX_WIDTH = _PANEL_WIDTH - _PLAYER_WIDTH - _PLAYER_POK_WIDTH;
		_BATTLE_BOX_HEIGHT = _SOUTH;
		g.drawImage(text, _BATTLE_BOX_X, _BATTLE_BOX_Y, _BATTLE_BOX_WIDTH, _BATTLE_BOX_HEIGHT, this);
		_BATTLE_BOX_CELL_WIDTH = _BATTLE_BOX_WIDTH / _BATTLE_BOX_CELLS;
		_BATTLE_BOX_CELL_HEIGHT = _BATTLE_BOX_HEIGHT / _BATTLE_BOX_CELLS;
		
		for (int i = 0; i < 4; ++i){
			text = getImage(PATH + PATH_WORD + texts[_WORD_ENUM[i]] + ".png");
			switch (i){
			case 0: g.drawImage(text, _BATTLE_BOX_X + _BATTLE_BOX_CELL_WIDTH, _BATTLE_BOX_Y + _BATTLE_BOX_CELL_HEIGHT * 2, _BATTLE_BOX_CELL_WIDTH * 2, _BATTLE_BOX_CELL_HEIGHT, this); break;
			case 1: g.drawImage(text, _BATTLE_BOX_X + _BATTLE_BOX_CELL_WIDTH * 4, _BATTLE_BOX_Y + _BATTLE_BOX_CELL_HEIGHT * 2, _BATTLE_BOX_CELL_WIDTH * 2, _BATTLE_BOX_CELL_HEIGHT, this); break;
			case 2: g.drawImage(text, _BATTLE_BOX_X + _BATTLE_BOX_CELL_WIDTH, _BATTLE_BOX_Y + _BATTLE_BOX_CELL_HEIGHT * 4, _BATTLE_BOX_CELL_WIDTH * 2, _BATTLE_BOX_CELL_HEIGHT, this); break;
			case 3: g.drawImage(text, _BATTLE_BOX_X + _BATTLE_BOX_CELL_WIDTH * 4, _BATTLE_BOX_Y + _BATTLE_BOX_CELL_HEIGHT * 4, _BATTLE_BOX_CELL_WIDTH * 2, _BATTLE_BOX_CELL_HEIGHT, this);break;
			}
		}
		
		text = getImage(PATH + "pokemon/text/arrow" + ".png");
		switch (_ARROW_POINTER){
		case 0: g.drawImage(text, _BATTLE_BOX_X, _BATTLE_BOX_Y + _BATTLE_BOX_CELL_HEIGHT * 2, _BATTLE_BOX_CELL_WIDTH, _BATTLE_BOX_CELL_HEIGHT, this); break;
		case 1: g.drawImage(text, _BATTLE_BOX_X + _BATTLE_BOX_CELL_WIDTH * 3, _BATTLE_BOX_Y + _BATTLE_BOX_CELL_HEIGHT * 2, _BATTLE_BOX_CELL_WIDTH, _BATTLE_BOX_CELL_HEIGHT, this); break;
		case 2: g.drawImage(text, _BATTLE_BOX_X, _BATTLE_BOX_Y + _BATTLE_BOX_CELL_HEIGHT * 4, _BATTLE_BOX_CELL_WIDTH, _BATTLE_BOX_CELL_HEIGHT, this); break;
		case 3: g.drawImage(text, _BATTLE_BOX_X + _BATTLE_BOX_CELL_WIDTH * 3, _BATTLE_BOX_Y + _BATTLE_BOX_CELL_HEIGHT * 4, _BATTLE_BOX_CELL_WIDTH, _BATTLE_BOX_CELL_HEIGHT, this);break;
		}
		
		char wLetter, sLetter;
		if (this.gameWindow.getPlayerId() == Was3DFigures.WOLF.getRepresentation()){
			wLetter = 'B'; sLetter = 'F';
		}
		else if (this.gameWindow.getPlayerId() == Was3DFigures.SHEEP.getRepresentation()){
			wLetter = 'F'; sLetter = 'B';
		}
		else {
			wLetter = ' '; sLetter = ' ';
			System.err.println("The id is incorrect");
		}
		
		wolf = getImage(PATH + PATH_WOLF + wLetter + String.format(PATTERN, _WOLF_ENUM)+ ".png");
		sheep = getImage(PATH + PATH_SHEEP + sLetter + String.format(PATTERN, _SHEEP_ENUM)+ ".png");
		ground = getImage(PATH + PATH_GROUND + String.format(PATTERN, _GROUND_ENUM)+ ".png");
		
		for (int i =_START_ROW; i < _START_ROW + _ROWS; i++)
			for (int j = _START_COL; j < _START_COL + _COLS; j++)
				if ((i + j) % 2 == 1)
					drawCell(i - _START_ROW, j - _START_COL, g);
		
		if (this.state != null && this.state.isFinished()){
			gameFinishedPresentation(this.gameWindow.getPlayerId() == this.state.getWinner(), g);
		} else _PRESENTATION_COUNTER = 0;

		if (_SHOW_MESSAGE){
			Color line = Color.YELLOW;
			Color back = Color.ORANGE;
			Color txt = Color.BLUE;
			g.setColor(line);
			g.drawOval(_PANEL_WIDTH - _PLAYER_WIDTH * 2, _PANEL_HEIGHT - _PLAYER_HEIGHT - _PLAYER_HEIGHT / 2, 2 * _PLAYER_WIDTH, _PLAYER_HEIGHT / 2);
			g.drawOval(_PANEL_WIDTH - _PLAYER_WIDTH - _PLAYER_WIDTH / 4, _PANEL_HEIGHT - _PLAYER_HEIGHT + _PLAYER_WIDTH / 20, _PLAYER_WIDTH / 8, _PLAYER_WIDTH / 8);
			g.drawOval(_PANEL_WIDTH - _PLAYER_WIDTH - _PLAYER_WIDTH / 8, _PANEL_HEIGHT - _PLAYER_HEIGHT + _PLAYER_WIDTH / 20 + _PLAYER_WIDTH / 8, _PLAYER_WIDTH / 10, _PLAYER_WIDTH / 10);
			g.setColor(back);
			g.fillOval(_PANEL_WIDTH - _PLAYER_WIDTH * 2, _PANEL_HEIGHT - _PLAYER_HEIGHT - _PLAYER_HEIGHT / 2, 2 * _PLAYER_WIDTH, _PLAYER_HEIGHT / 2);
			g.fillOval(_PANEL_WIDTH - _PLAYER_WIDTH - _PLAYER_WIDTH / 4, _PANEL_HEIGHT - _PLAYER_HEIGHT + _PLAYER_WIDTH / 20, _PLAYER_WIDTH / 8, _PLAYER_WIDTH / 8);
			g.fillOval(_PANEL_WIDTH - _PLAYER_WIDTH - _PLAYER_WIDTH / 8, _PANEL_HEIGHT - _PLAYER_HEIGHT + _PLAYER_WIDTH / 20 + _PLAYER_WIDTH / 8, _PLAYER_WIDTH / 10, _PLAYER_WIDTH / 10);
			textArea.setBackground(back);
			textArea.setForeground(txt);
			textArea.setBounds(_PANEL_WIDTH - (_PLAYER_WIDTH * 2) + _PLAYER_WIDTH / 2, _PANEL_HEIGHT - _PLAYER_HEIGHT - _PLAYER_HEIGHT / 2 + _PLAYER_HEIGHT / 8, _PLAYER_WIDTH, _PLAYER_HEIGHT / 4);
			textArea.setVisible(true);
			_SHOW_MESSAGE = false;
		}
	}

	private void drawCell(int row, int col, Graphics g) {
		int x[] = {
				_WEST + ((_ROWS - row) * _INCLINATION) + col * _CELL_WIDTH + col * _SEPARATOR,
				_WEST + ((_ROWS - row) * _INCLINATION) + col * _CELL_WIDTH + _CELL_WIDTH + col * _SEPARATOR,
				_WEST + ((_ROWS - row - 1) * _INCLINATION) + col * _CELL_WIDTH + _CELL_WIDTH + col * _SEPARATOR,
				_WEST + ((_ROWS - row - 1) * _INCLINATION) + col * _CELL_WIDTH + col * _SEPARATOR
				};
		int y[] = {
				_NORTH + row * _CELL_HEIGHT + row * (_SEPARATOR / 2),
				_NORTH + row * _CELL_HEIGHT + row * (_SEPARATOR / 2),
				_NORTH + row * _CELL_HEIGHT + _CELL_HEIGHT + row * (_SEPARATOR / 2),
				_NORTH + row * _CELL_HEIGHT + _CELL_HEIGHT + row * (_SEPARATOR / 2)
				};

		if (row == _SELECTED_ROW && col == _SELECTED_COL) g.setColor(Color.RED);
		else if (row == _CELL_ROW && col == _CELL_COL) g.setColor(Color.GREEN);
		else g.setColor(Color.GRAY);
		
		for (int i = 0; i < y.length; i++)
			y[i] += _DEPTH;
		for (int j = 0; j < _DEPTH; ++j){
			for (int i = 0; i < y.length; i++)
				--y[i];
			g.fillPolygon(x, y, 4);
		}
		g.drawImage(ground, x[3], y[0], x[1] - x[3], y[2] - y[0], this);
		
		int p;
		if (this.presentationBoard == null) p = -1;
		else  p = presentationBoard[row + _START_ROW][col + _START_COL];

		if (p >= 0) {
			int i = ((x[0] + x[3]) / 2) - (_CELL_WIDTH / 8);
			int j = ((y[0] + y[3]) / 2) - _CELL_HEIGHT + (_CELL_HEIGHT / 4);
			
			if (p == Was3DFigures.WOLF.getRepresentation())
				g.drawImage(wolf, i, j, _CELL_WIDTH, _CELL_HEIGHT, this);
			else if (p == Was3DFigures.SHEEP.getRepresentation())
				g.drawImage(sheep, i, j, _CELL_WIDTH, _CELL_HEIGHT, this);
		}
	}
	
	public BufferedImage getImage(String path){
		BufferedImage image = null;
		try {
			 image = ImageIO.read(new File(path));
		} catch (IOException ex) {
			System.err.println(path);
			System.err.println(ex);
		}
		return image;
	}
	
	public BufferedImage thresholdImage(BufferedImage image, int threshold) {
	    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	    result.getGraphics().drawImage(image, 0, 0, null);
	    WritableRaster raster = result.getRaster();
	    int[] pixels = new int[image.getWidth()];
	    for (int y = 0; y < image.getHeight(); y++) {
	        raster.getPixels(0, y, image.getWidth(), 1, pixels);
	        for (int i = 0; i < pixels.length; i++) {
	            if (pixels[i] < threshold) pixels[i] = 0;
	            else pixels[i] = 255;
	        }
	        raster.setPixels(0, y, image.getWidth(), 1, pixels);
	    }
	    return result;
	}
	
	public Action setZone(int x, int y){
		if (0 <= x && x < _NORTH && 0 <= y && y < _NORTH)
			return Action.POKEBALL;
		else if (_NORTH <= x && x < _NORTH + _NORTH && 0 <= y && y < _NORTH && _POKEBALL_ZONE){
			switch (_MODE_ENUM[(int) (y / (_NORTH / 3.0))]){
			case "boy": return Action.BOY;
			case "girl": return Action.GIRL;
			case "smart": return Action.SMART;
			case "rand": return Action.RAND;
			default: return null;
			}
		}
		else if ((_PANEL_WIDTH - _PLAYER_WIDTH) <= x && x < _PANEL_WIDTH && (_PANEL_HEIGHT - _PLAYER_HEIGHT) <= y && y < _PANEL_HEIGHT)
			return Action.PLAYER;
		else if ((_PANEL_WIDTH - _PLAYER_WIDTH - _PLAYER_POK_WIDTH) <= x && x < (_PANEL_WIDTH - _PLAYER_WIDTH) && (_PANEL_HEIGHT - _PLAYER_POK_HEIGHT) <= y && y < _PANEL_HEIGHT)
			return Action.PLAYER_POKEMON;
		else if (0 <= x && x < _BATTLE_BOX_WIDTH && (_PANEL_HEIGHT - _BATTLE_BOX_HEIGHT) <= y && y < _PANEL_HEIGHT)
			return Action.BATTLE_BOX;
		else {
			if (0 <= (y - _NORTH) && (y - _NORTH) < _BOARD_HEIGHT){
				int row = (y - _NORTH) / (_CELL_HEIGHT + (_SEPARATOR / 2));
				int west = _EAST; // Margen de la esquina inferior izquierda
				west += ((_ROWS - row - 1) * _INCLINATION); // Voy a la esquina inferior del row
				west += ((_CELL_HEIGHT - ((y - _NORTH) % (_CELL_HEIGHT + (_SEPARATOR / 2)))) * 2); // Me centro en el pixel del row
				if (0 <= (x - west) && (x - west) < ((_CELL_WIDTH + _SEPARATOR) * _COLS) && (((y - _NORTH) % (_CELL_HEIGHT + (_SEPARATOR / 2))) < _CELL_HEIGHT) && (((x - west) % (_CELL_WIDTH + _SEPARATOR)) < _CELL_WIDTH)){
					return Action.BOARD;
				}
				else return Action.BACKGROUND;
			}
			else return Action.BACKGROUND;
		}
	}
	
	public void battleBoxMove(String text){
		switch (text){
		case "bag":
			if (!block){
				Was3DAction wa = this.gameWindow.getSmartPlayer().requestAction(this.state);
				_SELECTED_ROW = invertValue(wa.getRowIni() - _START_ROW, true);
				_SELECTED_COL = invertValue(wa.getColIni() - _START_COL, false);
				_CELL_ROW = invertValue(wa.getRowFin() - _START_ROW, true);
				_CELL_COL = invertValue(wa.getColFin() - _START_COL, false);
				_CONSOLE_AT = Action.BOARD;
			}
			else showInfoMessage("It isn't your turn yet");
		break;
		case "console": _CONSOLE_AT = Action.BOARD; _CELL_ROW = _ROWS - 1; _CELL_COL = 0; break;
		case "autoRandom": 
			_PLAYER_ENUM = _RAND_ENUM;
			_NUM_PIC_PLAYER = _NUM_PIC_RAND;
			PATH_PLAYER = PATH_RAND;
			_MODE_ENUM[0] = "boy"; _MODE_ENUM[1] = "girl"; _MODE_ENUM[2] = "smart";
			switch(this.gameWindow.getPlayerMode()){
			case AI: _WORD_ENUM[_ARROW_POINTER] = 9; break;
			case MANUAL: _WORD_ENUM[_ARROW_POINTER] = 14; break;
			case RANDOM: _WORD_ENUM[_ARROW_POINTER] = 2; break;
			}
			_POKEBALL_ZONE = false;
			this.gameWindow.changePlayerMode("Random");
			showInfoMessage(randModeMessage());
			break;
		case "fight": _WORD_ENUM[0] = 1; _WORD_ENUM[1] = 11; _WORD_ENUM[2] = 7; _WORD_ENUM[3] = 13; break;
		case "autoSmart": 
			_PLAYER_ENUM = _SMART_ENUM;
			_NUM_PIC_PLAYER = _NUM_PIC_SMART;
			PATH_PLAYER = PATH_SMART;
			_MODE_ENUM[0] = "boy"; _MODE_ENUM[1] = "girl"; _MODE_ENUM[2] = "rand";
			switch(this.gameWindow.getPlayerMode()){
			case AI: _WORD_ENUM[_ARROW_POINTER] = 9; break;
			case MANUAL: _WORD_ENUM[_ARROW_POINTER] = 14; break;
			case RANDOM: _WORD_ENUM[_ARROW_POINTER] = 2; break;
			}
			_POKEBALL_ZONE = false;
			this.gameWindow.changePlayerMode("Smart");
			showInfoMessage(smartModeMessage());
			break;
		case "player": _PLAYER_ENUM = ((_PLAYER_ENUM + _NUM_PIC_PLAYER + 1) % _NUM_PIC_PLAYER); break; 
		case "pokemon": _WORD_ENUM[0] = 12; _WORD_ENUM[1] = 10; _WORD_ENUM[2] = 5; _WORD_ENUM[3] = 13; break;
		case "rand": 
			if (!block) this.gameWindow.makeRandMove();
			else showInfoMessage("It isn't your turn yet");
			break;
		case "run": 
			switch(this.gameWindow.getPlayerMode()){
			case AI: _WORD_ENUM[0] = 14; _WORD_ENUM[1] = 2; _WORD_ENUM[2] = 4; _WORD_ENUM[3] = 13; break;
			case MANUAL: _WORD_ENUM[0] = 9; _WORD_ENUM[1] = 2; _WORD_ENUM[2] = 4; _WORD_ENUM[3] = 13; break;
			case RANDOM: _WORD_ENUM[0] = 14; _WORD_ENUM[1] = 9; _WORD_ENUM[2] = 4; _WORD_ENUM[3] = 13; break;
			}
			break;
		case "restart": this.gameWindow.getGameCtrl().startGame();  break;
		case "sheep": _SHEEP_ENUM = ((_SHEEP_ENUM + _NUM_PIC_SHEEP + 2) % _NUM_PIC_SHEEP); break;
		case "smart": 
			if (!block) this.gameWindow.makeSmartMove();
			else showInfoMessage("It isn't your turn yet");
			break;
		case "wolf": _WOLF_ENUM = ((_WOLF_ENUM + _NUM_PIC_WOLF + 2) % _NUM_PIC_WOLF); break;
		case "return": _WORD_ENUM[0] = 3; _WORD_ENUM[1] = 0; _WORD_ENUM[2] = 6; _WORD_ENUM[3] = 8; break;
		case "manual": 
			if (Math.random() < 0.5){
				_PLAYER_ENUM = _BOY_ENUM;
				_NUM_PIC_PLAYER = _NUM_PIC_BOY;
				PATH_PLAYER = PATH_BOY;
				_MODE_ENUM[0] = "girl"; _MODE_ENUM[1] = "smart"; _MODE_ENUM[2] = "rand";
			}
			else {
				_PLAYER_ENUM = _GIRL_ENUM;
				_NUM_PIC_PLAYER = _NUM_PIC_GIRL;
				PATH_PLAYER = PATH_GIRL;
				_MODE_ENUM[0] = "boy"; _MODE_ENUM[1] = "smart"; _MODE_ENUM[2] = "rand";
			}
			_POKEBALL_ZONE = false;
			switch(this.gameWindow.getPlayerMode()){
			case AI: _WORD_ENUM[_ARROW_POINTER] = 9; break;
			case MANUAL: _WORD_ENUM[_ARROW_POINTER] = 14; break;
			case RANDOM: _WORD_ENUM[_ARROW_POINTER] = 2; break;
			}
			this.gameWindow.changePlayerMode("Manual");
			showInfoMessage(manualModeMessage());
			break;
		case "null": /* No hace nada */ break;
		}
	}
	
	public int invertValue(int value, boolean rowOrCol){
		if (this.gameWindow.getPlayerId() == Was3DFigures.WOLF.getRepresentation())
			return value;
		else if (this.gameWindow.getPlayerId() == Was3DFigures.SHEEP.getRepresentation())
			if (rowOrCol) // its row
				return _ROWS - 1 - value;
			else return _COLS - 1 - value;
		else return value;
	}
	
	public String manualModeMessage(){
		String message = 
				"Pikachu, i choose u\n" + 
				"I'm ur clone, hehehe\n" + 
				"C'mon, dont choose me, this is not POKEMON!\n" +
				"I'm gonna denounce u for copyright issues\n" +
				"Manual mode"
				;
		String[] messages = message.split("\\n");
		int i = (int) (Math.random() * 100);
		i %= messages.length;
		return messages[i];
	}
	
	public String smartModeMessage(){
		String message = 
				"I'm the genius Akinator\n" +
				"U know u are winning cuz u chose me\n" +
				"U r so noob for calling me\n" + 
				"I know all u think :3\n" + 
				"I'll do everything u want ..., senpaaisama\n" +
				"You have 3 wishes, but idc\n" +
				"I can see ur future and it's so DAAAARK\n" +
				"Smart mode"
				;
		String[] messages = message.split("\\n");
		int i = (int) (Math.random() * 100);
		i %= messages.length;
		return messages[i];
	}
	
	public String randModeMessage(){
		String message = 
				"Hahaha, u alrdy lose\n" +
				"Damn, I'm drunk\n" + 
				"Sorry, I have no brain\n" +
				"I have no f*** idea about dis\n" +
				"Wanna some beer?\n" +
				"Random mode"
				;
		String[] messages = message.split("\\n");
		int i = (int) (Math.random() * 100);
		i %= messages.length;
		return messages[i];
	}
	
	public void gameFinishedPresentation(boolean winner, Graphics g){
		BufferedImage win = getImage(PATH + "pokemon/win.png"), lose = getImage(PATH + "pokemon/lose.png");
		BufferedImage unevo, evo;
		if (this.gameWindow.getPlayerId() == Was3DFigures.WOLF.getRepresentation()){
			unevo = getImage(PATH + PATH_WOLF + 'F' + String.format(PATTERN, _WOLF_ENUM) + ".png");
			evo = getImage(PATH + PATH_WOLF + 'F' + String.format(PATTERN, _WOLF_ENUM + 1) + ".png");
			if (winner) wolf = getImage(PATH + PATH_WOLF + 'B' + String.format(PATTERN, _WOLF_ENUM + 1) + ".png");
		}
		else if (this.gameWindow.getPlayerId() == Was3DFigures.SHEEP.getRepresentation()){
			unevo = getImage(PATH + PATH_SHEEP + 'F' + String.format(PATTERN, _SHEEP_ENUM) + ".png");
			evo = getImage(PATH + PATH_SHEEP + 'F' + String.format(PATTERN, _SHEEP_ENUM + 1) + ".png");
			if (winner) sheep = getImage(PATH + PATH_SHEEP + 'B' + String.format(PATTERN, _SHEEP_ENUM + 1) + ".png");
		}
		else {
			unevo = null;
			evo = null;
			System.err.println("The id is incorrect");
		}
		
		if (_PRESENTATION_COUNTER >= _COUNTER_LIMIT){
			for (int i =_START_ROW; i < _START_ROW + _ROWS; i++)
				for (int j = _START_COL; j < _START_COL + _COLS; j++)
					if ((i + j) % 2 == 1)
						drawCell(i - _START_ROW, j - _START_COL, g);
			if (winner) g.drawImage(win, _NORTH, 0, _PANEL_WIDTH - _NORTH, _NORTH, this);
			else g.drawImage(lose, _NORTH, 0, _PANEL_WIDTH - _NORTH, _NORTH, this);
			if (_PRESENTATION_COUNTER == _COUNTER_LIMIT) { 
				_SHOW_MESSAGE = true;
				try { Thread.sleep(1000); } catch (InterruptedException e) {}
			}
		}
		else if (_PRESENTATION_COUNTER == _COUNTER_LIMIT - 1){
			if (winner) {
				g.drawImage(getImage(PATH + "pokemon/twinkleHappy.jpg"), 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
				g.drawImage(evo, _PANEL_WIDTH / 2 - evo.getWidth() / 2, _PANEL_HEIGHT / 2 - evo.getHeight() / 2, evo.getWidth(), evo.getHeight(), this);
				g.drawImage(win, 0, 0, _PANEL_WIDTH, _NORTH, this);
			}
			else {
				g.drawImage(getImage(PATH + "pokemon/twinkleSad.jpg"), 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
				g.drawImage(unevo, _PANEL_WIDTH / 2 - unevo.getWidth() / 2, _PANEL_HEIGHT / 2 - unevo.getHeight() / 2, unevo.getWidth(), unevo.getHeight(), this);
				g.drawImage(lose, 0, 0, _PANEL_WIDTH, _NORTH, this);
			}
			this.repaint();
		}
		else {
			_SHOW_MESSAGE = false;
			g.drawImage(getImage(PATH + "pokemon/black.jpg"), 0, 0, _PANEL_WIDTH, _PANEL_HEIGHT, this);
			if (_PRESENTATION_COUNTER % 2 == 0)
				g.drawImage(thresholdImage(unevo, 1), _PANEL_WIDTH / 2 - unevo.getWidth() / 2, _PANEL_HEIGHT / 2 - unevo.getHeight() / 2, unevo.getWidth(), unevo.getHeight(), this);
			else g.drawImage(thresholdImage(evo, 1), _PANEL_WIDTH / 2 - evo.getWidth() / 2, _PANEL_HEIGHT / 2 - evo.getHeight() / 2, evo.getWidth(), evo.getHeight(), this);
			this.repaint();
		}
		++_PRESENTATION_COUNTER;
	}
	
	private void mouseClicked(int row, int col, int clickCount, int mouseButton, Action whatUClicked) {
		_CONSOLE_AT = whatUClicked;
		if (mouseButton == 1){
			switch (_CONSOLE_AT){
			case PLAYER:
				System.out.println("Mouse: " + clickCount + " clicks at PLAYER with Button " + mouseButton);
				break;
			case PLAYER_POKEMON:
				System.out.println("Mouse: " + clickCount + " clicks at PLAYER POKEMON with Button " + mouseButton);
				break;
			case POKEBALL: 
				if (!_POKEBALL_ZONE) _POKEBALL_ZONE = true;
				else _POKEBALL_ZONE = false;
				break;
			case BOY:
				_PLAYER_ENUM = _BOY_ENUM;
				_NUM_PIC_PLAYER = _NUM_PIC_BOY;
				PATH_PLAYER = PATH_BOY;
				this.gameWindow.changePlayerMode("Manual");
				_POKEBALL_ZONE = false;
				_MODE_ENUM[0] = "girl"; _MODE_ENUM[1] = "smart"; _MODE_ENUM[2] = "rand";
				_WORD_ENUM[0] = 3; _WORD_ENUM[1] = 0; _WORD_ENUM[2] = 6; _WORD_ENUM[3] = 8;
				showInfoMessage(manualModeMessage());
				System.out.println("Mouse: " + clickCount + " clicks at BOY with Button " + mouseButton);
				break;
			case GIRL:
				_PLAYER_ENUM = _GIRL_ENUM;
				_NUM_PIC_PLAYER = _NUM_PIC_GIRL;
				PATH_PLAYER = PATH_GIRL;
				this.gameWindow.changePlayerMode("Manual");
				_POKEBALL_ZONE = false;
				_MODE_ENUM[0] = "boy"; _MODE_ENUM[1] = "smart"; _MODE_ENUM[2] = "rand";
				_WORD_ENUM[0] = 3; _WORD_ENUM[1] = 0; _WORD_ENUM[2] = 6; _WORD_ENUM[3] = 8;
				showInfoMessage(manualModeMessage());
				System.out.println("Mouse: " + clickCount + " clicks at GIRL POKEMON with Button " + mouseButton);
				break;
			case SMART:
				_PLAYER_ENUM = _SMART_ENUM;
				_NUM_PIC_PLAYER = _NUM_PIC_SMART;
				PATH_PLAYER = PATH_SMART;
				this.gameWindow.changePlayerMode("Smart");
				_POKEBALL_ZONE = false;
				_MODE_ENUM[0] = "boy"; _MODE_ENUM[1] = "girl"; _MODE_ENUM[2] = "rand";
				_WORD_ENUM[0] = 3; _WORD_ENUM[1] = 0; _WORD_ENUM[2] = 6; _WORD_ENUM[3] = 8;
				showInfoMessage(smartModeMessage());
				System.out.println("Mouse: " + clickCount + " clicks at SMART with Button " + mouseButton);
				break;
			case RAND:
				_PLAYER_ENUM = _RAND_ENUM;
				_NUM_PIC_PLAYER = _NUM_PIC_RAND;
				PATH_PLAYER = PATH_RAND;
				this.gameWindow.changePlayerMode("Random");
				_POKEBALL_ZONE = false;
				_MODE_ENUM[0] = "boy"; _MODE_ENUM[1] = "girl"; _MODE_ENUM[2] = "smart";
				_WORD_ENUM[0] = 3; _WORD_ENUM[1] = 0; _WORD_ENUM[2] = 6; _WORD_ENUM[3] = 8;
				showInfoMessage(randModeMessage());
				System.out.println("Mouse: " + clickCount + " clicks at RAND with Button " + mouseButton);
				break;
			case BATTLE_BOX:
				if (row == 2 && (col == 1 || col == 2)) {
					_ARROW_POINTER = 0;
					battleBoxMove(texts[_WORD_ENUM[_ARROW_POINTER]]);
					System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX [0, 0] with Button " + mouseButton);
				}
				else if (row == 2 && (col == 4 || col == 5)) {
					_ARROW_POINTER = 1;
					battleBoxMove(texts[_WORD_ENUM[_ARROW_POINTER]]);
					System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX [1, 0] with Button " + mouseButton);
				}
				else if (row == 4 && (col == 1 || col == 2)) {
					_ARROW_POINTER = 2;
					battleBoxMove(texts[_WORD_ENUM[_ARROW_POINTER]]);
					System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX [0, 1] with Button " + mouseButton);
				}
				else if (row == 4 && (col == 4 || col == 5)) {
					_ARROW_POINTER = 3;
					battleBoxMove(texts[_WORD_ENUM[_ARROW_POINTER]]);
					System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX [1, 1] with Button " + mouseButton);
				}
				else System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX (" + col + ", " + row + ") with Button " + mouseButton);
				break;
			case BOARD:
				if (_SELECTED_ROW == -1 || _SELECTED_COL == -1){
					if (board[invertValue(row, true)][invertValue(col, false)] == this.gameWindow.getPlayerId()){
						_SELECTED_ROW = row - _START_ROW; _SELECTED_COL = col - _START_COL;
					}
					_CELL_ROW = row - _START_ROW; _CELL_COL = col - _START_COL;
				}
				else {
					if (_SELECTED_ROW == row && _SELECTED_COL == col){
						_SELECTED_ROW = -1; _SELECTED_COL = -1;
					}
					else if (board[invertValue(row, true)][invertValue(col, false)] != this.gameWindow.getPlayerId()){
						_CELL_ROW = row - _START_ROW; _CELL_COL = col - _START_COL;
						Was3DAction wa = new Was3DAction(board[invertValue(_SELECTED_ROW + _START_ROW, true)][invertValue(_SELECTED_COL + _START_COL, false)], invertValue(_SELECTED_ROW + _START_ROW, true), invertValue(_SELECTED_COL + _START_COL, false), invertValue(_CELL_ROW + _START_ROW, true), invertValue(_CELL_COL + _START_COL, false));
						if (state.isValid(wa, board)){
							if (!block) {
								gameWindow.getGameCtrl().makeMove(wa);
								showInfoMessage("You moved from [" + _SELECTED_ROW + ", " + _SELECTED_COL + "] to [" + _CELL_ROW + ", " + _CELL_COL + "]");
								_SELECTED_ROW = -1; _SELECTED_COL = -1;
							}
							else showInfoMessage("It isn't your turn yet");
						}
						else showInfoMessage("This movement isn't valid");
					}
					else {
						_SELECTED_ROW = row - _START_ROW; _SELECTED_COL = col - _START_COL;
					}
				}
				break;
			case BACKGROUND:
				System.out.println("Mouse: " + clickCount + " clicks at BACKGROUND with Button " + mouseButton);
				break;
			default:
				System.out.println("Mouse: " + clickCount + " clicks at NOWHERE AREA with Button " + mouseButton);
				break;
			}
		}
		else if (mouseButton == 3){
			int cambio = 1;
			switch (_CONSOLE_AT){
			case PLAYER:
				_PLAYER_ENUM = ((_PLAYER_ENUM + _NUM_PIC_PLAYER + cambio) % _NUM_PIC_PLAYER);
				System.out.println("Mouse: " + clickCount + " clicks at PLAYER with Button " + mouseButton);
				break;
			case PLAYER_POKEMON:
				if (this.gameWindow.getPlayerId() == Was3DFigures.WOLF.getRepresentation())
					_WOLF_ENUM = ((_WOLF_ENUM + _NUM_PIC_WOLF + cambio * 2) % _NUM_PIC_WOLF);
				else if (this.gameWindow.getPlayerId() == Was3DFigures.SHEEP.getRepresentation())
					_SHEEP_ENUM = ((_SHEEP_ENUM + _NUM_PIC_SHEEP + cambio * 2) % _NUM_PIC_SHEEP);
				System.out.println("Mouse: " + clickCount + " clicks at PLAYER POKEMON with Button " + mouseButton);
				break;
			case BOY:
				_BOY_ENUM = ((_BOY_ENUM + _NUM_PIC_BOY + cambio) % _NUM_PIC_BOY);
				System.out.println("Mouse: " + clickCount + " clicks at BOY with Button " + mouseButton);
				break;
			case GIRL:
				_GIRL_ENUM = ((_GIRL_ENUM + _NUM_PIC_GIRL + cambio) % _NUM_PIC_GIRL);
				System.out.println("Mouse: " + clickCount + " clicks at GIRL with Button " + mouseButton);
				break;
			case SMART:
				_SMART_ENUM = ((_SMART_ENUM + _NUM_PIC_SMART + cambio) % _NUM_PIC_SMART);
				System.out.println("Mouse: " + clickCount + " clicks at SMART with Button " + mouseButton);
				break;
			case RAND:
				_RAND_ENUM = ((_RAND_ENUM + _NUM_PIC_RAND + cambio) % _NUM_PIC_RAND);
				System.out.println("Mouse: " + clickCount + " clicks at RAND with Button " + mouseButton);
				break;
			case BATTLE_BOX:
				if (row == 2 && (col == 1 || col == 2)) {
					_ARROW_POINTER = 0;
					System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX [0, 0] with Button " + mouseButton);
				}
				else if (row == 2 && (col == 4 || col == 5)) {
					_ARROW_POINTER = 1;
					System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX [1, 0] with Button " + mouseButton);
				}
				else if (row == 4 && (col == 1 || col == 2)) {
					_ARROW_POINTER = 2;
					System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX [0, 1] with Button " + mouseButton);
				}
				else if (row == 4 && (col == 4 || col == 5)) {
					_ARROW_POINTER = 3;
					System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX [1, 1] with Button " + mouseButton);
				}
				else System.out.println("Mouse: " + clickCount + " clicks at BATTLE BOX (" + col + ", " + row + ") with Button " + mouseButton);
				break;
			case BOARD:
				_CELL_ROW = row - _START_ROW; _CELL_COL = col - _START_COL;
				if (board[invertValue(row, true)][invertValue(col, false)] == Was3DFigures.WOLF.getRepresentation())
					_WOLF_ENUM = ((_WOLF_ENUM + _NUM_PIC_WOLF + cambio * 2) % _NUM_PIC_WOLF);
				else if (board[invertValue(row, true)][invertValue(col, false)] == Was3DFigures.SHEEP.getRepresentation())
					_SHEEP_ENUM = ((_SHEEP_ENUM + _NUM_PIC_SHEEP + cambio * 2) % _NUM_PIC_SHEEP);
				else 
					_GROUND_ENUM = ((_GROUND_ENUM + _NUM_PIC_GROUND + cambio) % _NUM_PIC_GROUND);
				System.out.println("Mouse: " + clickCount + " clicks at CELL [" + row + "," + col + "] with Button " + mouseButton);
				break;
			case BACKGROUND:
				_BACKGROUND_ENUM = ((_BACKGROUND_ENUM + _NUM_PIC_BACKGROUND + cambio) % _NUM_PIC_BACKGROUND);
				System.out.println("Mouse: " + clickCount + " clicks at BACKGROUND with Button " + mouseButton);
				break;
			default:
				System.out.println("Mouse: " + clickCount + " clicks at NOWHERE AREA with Button " + mouseButton);
				break;
			}
		}
		this.repaint();
	}
	
	private void mouseWheelMoved(int movement){
		_CONSOLE_AT = Action.BOARD;
		if (_CELL_ROW == -1) _CELL_ROW = _ROWS - 1;
		if (_CELL_COL == -1) _CELL_COL = 0;
		if(movement == -1){
			acercar();
			System.out.println("Mouse: Wheel rolled up");
		}
		else if (movement == 1){
			alejar();
			System.out.println("Mouse: Wheel rolled down");
		}
		this.repaint();
	}
	
	private void keyTyped(char keyCode) {
		switch (keyCode){
		case '+': acercar(); break;
		case '-': alejar(); break;
		}
		System.out.println("Key [" + keyCode + "] pressed ..");
		this.repaint();
	}
	
	private void keyReleased(int keyCode) {
		switch (_CONSOLE_AT){
		case BATTLE_BOX:
			switch (keyCode){
			case KeyEvent.VK_ENTER:
				battleBoxMove(texts[_WORD_ENUM[_ARROW_POINTER]]);
				break;
			case KeyEvent.VK_UP:
				switch (_ARROW_POINTER){
				case 0: _ARROW_POINTER = 0; break;
				case 1: _ARROW_POINTER = 1; break;
				case 2: _ARROW_POINTER = 0; break;
				case 3: _ARROW_POINTER = 1; break;
				}
				break;
			case KeyEvent.VK_DOWN:
				switch (_ARROW_POINTER){
				case 0: _ARROW_POINTER = 2; break;
				case 1: _ARROW_POINTER = 3; break;
				case 2: _ARROW_POINTER = 2; break;
				case 3: _ARROW_POINTER = 3; break;
				}
				break;
			case KeyEvent.VK_LEFT:
				switch (_ARROW_POINTER){
				case 0: _ARROW_POINTER = 0; break;
				case 1: _ARROW_POINTER = 0; break;
				case 2: _ARROW_POINTER = 2; break;
				case 3: _ARROW_POINTER = 2; break;
				}
				break;
			case KeyEvent.VK_RIGHT:
				switch (_ARROW_POINTER){
				case 0: _ARROW_POINTER = 1; break;
				case 1: _ARROW_POINTER = 1; break;
				case 2: _ARROW_POINTER = 3; break;
				case 3: _ARROW_POINTER = 3; break;
				}
				break;
			case KeyEvent.VK_ESCAPE:
				_WORD_ENUM[0] = 3; _WORD_ENUM[1] = 0; _WORD_ENUM[2] = 6; _WORD_ENUM[3] = 8;
				break;
			}
			break;
		case BOARD:
			if (_CELL_ROW == -1) _CELL_ROW = _ROWS - 1;
			if (_CELL_COL == -1) _CELL_COL = 0;
			switch (keyCode){
			case KeyEvent.VK_ENTER: 
				int row = _CELL_ROW + _START_ROW, col = _CELL_COL + _START_COL;
				if (_SELECTED_ROW == -1 || _SELECTED_COL == -1){
					if (board[invertValue(row, true)][invertValue(col, false)] == this.gameWindow.getPlayerId()){
						_SELECTED_ROW = row - _START_ROW; _SELECTED_COL = col - _START_COL;
					}
					_CELL_ROW = row - _START_ROW; _CELL_COL = col - _START_COL;
				}
				else {
					if (_SELECTED_ROW == row && _SELECTED_COL == col){
						_SELECTED_ROW = -1; _SELECTED_COL = -1;
					}
					else if (board[invertValue(row, true)][invertValue(col, false)] != this.gameWindow.getPlayerId()){
						_CELL_ROW = row - _START_ROW; _CELL_COL = col - _START_COL;
						Was3DAction wa = new Was3DAction(board[invertValue(_SELECTED_ROW + _START_ROW, true)][invertValue(_SELECTED_COL + _START_COL, false)], invertValue(_SELECTED_ROW + _START_ROW, true), invertValue(_SELECTED_COL + _START_COL, false), invertValue(_CELL_ROW + _START_ROW, true), invertValue(_CELL_COL + _START_COL, false));
						if (state.isValid(wa, board)){
							if (!block){
								gameWindow.getGameCtrl().makeMove(wa);
								showInfoMessage("You moved from [" + _SELECTED_ROW + ", " + _SELECTED_COL + "] to [" + _CELL_ROW + ", " + _CELL_COL + "]");
								_SELECTED_ROW = -1; _SELECTED_COL = -1;
							}
							else showInfoMessage("It isn't your turn yet");
						}
						else showInfoMessage("This movement isn't valid");
					}
					else {
						_SELECTED_ROW = row - _START_ROW; _SELECTED_COL = col - _START_COL;
					}
				}
			break;
			case KeyEvent.VK_UP: --_CELL_ROW; if (_CELL_ROW < 0) _CELL_ROW = 0; break;
			case KeyEvent.VK_DOWN: ++_CELL_ROW; if (_CELL_ROW >= _ROWS) _CELL_ROW = _ROWS - 1; break;
			case KeyEvent.VK_LEFT: --_CELL_COL; if (_CELL_COL < 0) _CELL_COL = 0; break;
			case KeyEvent.VK_RIGHT: ++_CELL_COL; if (_CELL_COL >= _COLS) _CELL_COL = _COLS - 1; break;
			case KeyEvent.VK_ESCAPE: _CONSOLE_AT = Action.BATTLE_BOX;
			_CELL_ROW = -1; _CELL_COL = -1; 
			_SELECTED_ROW = -1; _SELECTED_COL = -1;
			break;
			}
			break;
		case BACKGROUND:
			switch (keyCode){
			case KeyEvent.VK_ENTER: _CONSOLE_AT = Action.BATTLE_BOX; break;
			case KeyEvent.VK_UP: _CONSOLE_AT = Action.BATTLE_BOX; break;
			case KeyEvent.VK_DOWN: _CONSOLE_AT = Action.BATTLE_BOX; break;
			case KeyEvent.VK_LEFT: _CONSOLE_AT = Action.BATTLE_BOX; break;
			case KeyEvent.VK_RIGHT: _CONSOLE_AT = Action.BATTLE_BOX; break;
			case KeyEvent.VK_ESCAPE: _CONSOLE_AT = Action.BATTLE_BOX; break;
			}
			break;
		default:
			_CONSOLE_AT = Action.BATTLE_BOX;
			break;
		}
		System.out.println("Key [" + keyCode + "] desactivated ..");
		this.repaint();
	}
	
	private void keyPressed(int keyCode) {
		System.out.println("Key [" + keyCode + "] activated ..");
		this.repaint();
	}
	
	public void acercar() {
		if (_CONSOLE_AT == Action.BOARD){
			if (_ROWS > 1){
				if (_CELL_ROW != 0){
					++_START_ROW;
					--_CELL_ROW;
				}
				--_ROWS;
			}
			if (_COLS > 1){
				if (_CELL_COL == _COLS - 1){
					++_START_COL;
					--_CELL_COL;
				}
				--_COLS;
			}
		}
	}
	
	public void alejar(){
		if (_CONSOLE_AT == Action.BOARD){
			if (_ROWS < board.length){
				if (_START_ROW != 0){
					--_START_ROW;
					++_CELL_ROW;
				}
				++_ROWS;
			}
			if (_COLS < board[0].length){
				if (_START_COL != 0){
					--_START_COL;
					++_CELL_COL;
				}
				++_COLS;
			}
		}
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
	public void update(Was3DState state) {
		this.state = state;
		this.board = state.getBoard();
		_ROWS = this.board.length;
		_COLS = this.board.length;
		this.presentationBoard = new int[_ROWS][_COLS];
		for (int i = 0; i < _ROWS; ++i)
			for(int j = 0; j < _COLS; ++j)
				this.presentationBoard[i][j] = this.board[invertValue(i, true)][invertValue(j, false)];
		if (!state.isFinished() && gameWindow.getPlayerId() == state.getTurn()) this.showInfoMessage("It's your turn");
		this.repaint();
		if (this.state.isFinished()) if (!block) gameWindow.getGameCtrl().stopGame();;
	}

	@Override
	public void showInfoMessage(String msg) {
		textArea.setText(msg);
		_SHOW_MESSAGE = true;
		this.repaint();
	}

	@Override
	public void setGameViewCtrl(GameWindow<Was3DState, Was3DAction> gameWindow) {
		this.gameWindow = gameWindow;
	}
	
	
}
