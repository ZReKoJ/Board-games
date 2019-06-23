package es.ucm.fdi.tp.view.chessAnimation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.view.GameWindow;

@SuppressWarnings("serial")
public class OnePieceInfo extends JFrame {

	private String PATH = GameWindow.RESOURCE;
	private OnePieceFigure figure;
	private JTextArea info;
	private BufferedImage image;
	
	OnePieceInfo(OnePieceFigure fig){
		this.figure = fig;
		initGUI();
	}

	private void initGUI() {
		this.setTitle(figure.toString());
		image = getImage(PATH + "onepiece/figure/" + figure.getName() + "/icon.png");
		this.setIconImage(image);
		JPanel mainPanel = new JPanel(new BorderLayout()) {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				fillBoard(g);
			}

			private void fillBoard(Graphics g) {
				image = getImage(PATH + "onepiece/infoBack.png");
				g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		
		info = new JTextArea() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				fillBoard(g);
			}

			private void fillBoard(Graphics g) {
				image = getImage(PATH + "onepiece/textArea.png");
				g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
				this.repaint();
			}
		};
		info.setEditable(false);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setText(figure.getDescription());
		info.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		Color color = Color.RED.darker().darker().darker();
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(color), figure.toString());
		title.setTitleColor(color);
		info.setBorder(title);
		
		JComponent comp = new JComponent(){
			{this.repaint();}
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				fillBoard(g);
			}

			private void fillBoard(Graphics g) {
				image = getImage(PATH + "onepiece/figure/" + figure.getName() + "/info.png");
				double proportion = this.getWidth() / (double) image.getWidth();
				int width = (int) (image.getWidth() * proportion);
				int height = (int) (image.getHeight() * proportion);
				g.drawImage(image, 0, this.getHeight() / 2 - height / 2, width, height, this);
			}
		};

		mainPanel.add(comp, BorderLayout.CENTER);
		mainPanel.add(info, BorderLayout.SOUTH);
		
		this.setUndecorated(true);
		this.setContentPane(mainPanel);
		this.setSize(350, 400);
		this.setVisible(true);
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
}
