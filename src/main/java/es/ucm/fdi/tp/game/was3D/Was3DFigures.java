package es.ucm.fdi.tp.game.was3D;

/**
 * Enumerado que representa las figuras del juego Wolf And Sheep,
 * ya sean las casillas blancas, negras, el lobo y las ovejas, cada una
 * con su representacion como un numero entero y como caracter
 * @author Diego Acuña y Zihao Hong
 *
 */
public enum Was3DFigures {
	WHITE(-2, ' '), // casilla blanca
	BLACK(-1, '.'), // casilla negra
	WOLF(0, 'W', 1), // lobo
	SHEEP(1, 'S', 4); // oveja
	
	private int representation; // representacion en entero
	private char caracter; // representacion en caracter
	private int number; // numero de veces que esta dicha figura
	
	Was3DFigures(int representation, char caracter){
		this.setRepresentation(representation);
		this.setCaracter(caracter);
	}
	
	Was3DFigures(int representation, char caracter, int number){
		this.setRepresentation(representation);
		this.setCaracter(caracter);
		this.setNumber(number);
	}


	public int getRepresentation() {
		return representation;
	}

	
	public void setRepresentation(int representation) {
		this.representation = representation;
	}

	public char getCaracter(){
		return caracter;
	}
	
	public static char getCaracter(int repre) {
		char c = ' ';
		for (Was3DFigures fig : Was3DFigures.values()){
			if (fig.getRepresentation() == repre)
				c = fig.getCaracter();
		}
		return c;
	}
	
	public static int getNumber(int repre) {
		int n = BLACK.getRepresentation();
		for (Was3DFigures fig : Was3DFigures.values()){
			if (fig.getRepresentation() == repre)
				n = fig.getNumber();
		}
		return n;
	}


	public void setCaracter(char caracter) {
		this.caracter = caracter;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
