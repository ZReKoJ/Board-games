package es.ucm.fdi.tp.view.chessAnimation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.concurrent.Callable;

public abstract class DrawImage implements Callable<Void> {

	protected Graphics2D g;
	
	public DrawImage(Graphics g){
		this.g = (Graphics2D) g;
		this.g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		this.g.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
	
	@Override
	public abstract Void call() throws Exception;

}
