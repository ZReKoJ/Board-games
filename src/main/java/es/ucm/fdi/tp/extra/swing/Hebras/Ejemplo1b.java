package es.ucm.fdi.tp.extra.swing.Hebras;// Ejemplo mínimo de hebras, sin swing.
// Creación de varias hebras a partir de una misma clase.
public class Ejemplo1b {

	private static class MiThread extends Thread {
		private int num;
		public MiThread(int constante) {
			this.num = constante;
		}
		public void run() {
			while (true) {  // bucle infinito!
				System.out.print(num);
			}
		}
	}
	public static void main(String []args) {
		MiThread th0 = new MiThread(0);
		MiThread th1 = new MiThread(1);
		MiThread th2 = new MiThread(2);
		MiThread th3 = new MiThread(3);
		th0.start();
		th1.start();
		th2.start();
		th3.run();	//OJO: en el thread principal del 
					// programa!
	}
}
