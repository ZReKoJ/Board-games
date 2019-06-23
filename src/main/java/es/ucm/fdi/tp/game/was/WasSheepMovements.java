package es.ucm.fdi.tp.game.was;
/**
 * Enumerado que representa los movimientos de las ovejas
 * @author Diego Acuña y Zihao Hong
 *
 */
public enum WasSheepMovements {
	DOWN_LEFT(-1, 1), //movimiento abajo y a la izquierda
	DOWN_RIGHT(1, 1); // movimiento abajo y a la derecha
	
	private int x; // fila
	private int y; // columna
	
	WasSheepMovements(int x, int y){
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
