package es.ucm.fdi.tp.view.chessAnimation;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.view.GameWindow;

@SuppressWarnings("serial")
public abstract class GridButton extends JPanel{

	protected String PATH = GameWindow.RESOURCE;
	protected JButton[][] buttons;
	OnePieceInfo info;
	private int _ROWS;
	private int _COLS;
	private Dimension dim;
	protected Image image;
	
	GridButton(){
		this.setBorder(BorderFactory.createTitledBorder("Figures"));
		_ROWS = getNumFigures();
		_COLS = 1;
		this.setLayout(new GridLayout(_ROWS, _COLS));
		buttons = new JButton[_ROWS][_COLS];
		for (int i = _ROWS - 1; i >= 0; --i){
			for (int j = 0; j < _COLS; ++j){
				image = getImage(PATH + "onepiece/figure/" + getIconPath(i) + "/" + "icon.png");
				buttons[i][j] = new JButton(new ImageIcon(image));
				buttons[i][j].setToolTipText(getFigureName(i));
				final int identification = i, jdentification = j;
				buttons[i][j].addMouseListener(new MouseListener(){

					@Override
					public void mouseClicked(MouseEvent e) {
						if (SwingUtilities.isRightMouseButton(e)) {
							changeFigure(identification, identification, jdentification);
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {}

					@Override
					public void mouseExited(MouseEvent e) {}

					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isLeftMouseButton(e)) {
							playSound(identification);
							PointerInfo p = MouseInfo.getPointerInfo();
							dim = Toolkit.getDefaultToolkit().getScreenSize();
							info = new OnePieceInfo(getFigure(identification));
							int x = (int) p.getLocation().getX(), y = (int) p.getLocation().getY();
							if (x + info.getWidth() > dim.getWidth()) x -= info.getWidth();
							if (y + info.getHeight() > dim.getHeight() - 50) y -= info.getHeight();
							if (y < 0) y = 0;
							info.setLocation(x, y);
						}
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						if (SwingUtilities.isLeftMouseButton(e)) info.dispose();
					}
					
				});
				this.add(buttons[i][j]);
			}
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
	
	protected abstract int getNumFigures();
	
	protected abstract String getFigureName(int repre);
	
	protected abstract String getIconPath(int figure);
	
	protected abstract OnePieceFigure getFigure(int repre);
	
	protected abstract void changeFigure(int repre, int i, int j);
	
	protected abstract void playSound(int identification);
}
