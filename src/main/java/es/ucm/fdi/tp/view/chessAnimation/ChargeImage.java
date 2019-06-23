package es.ucm.fdi.tp.view.chessAnimation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

public class ChargeImage implements Callable<BufferedImage> {

	private String path;
	
	ChargeImage(String path){
		this.path = path;
	}
	
	@Override
	public BufferedImage call() throws Exception {
		BufferedImage image = null;
		if (!Thread.interrupted()){
			try {
				 image = ImageIO.read(new File(path));
			} catch (IOException ex) {
				System.err.println(path);
				System.err.println(ex);
			}
		}
		return image;
	}

}
