package es.ucm.fdi.tp.game.was3D;
/**
 * Enumerado que representa los movimientos de un lobo
 * @author Diego Acuña y Zihao Hong
 * 
 */
public enum Was3DWolfMovements {
	UP_LEFT(-1, -1), // arriba a la izquierda
	UP_RIGHT(1, -1), // arriba a la derecha
	DOWN_LEFT(-1, 1), // abajo a la izquierda
	DOWN_RIGHT(1, 1); // abajo a la derecha
	
	private int x; //fila
	private int y; //columna
	
	Was3DWolfMovements(int x, int y){
		this.setX(x);
		this.setY(y);
	}

	
	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}
}
