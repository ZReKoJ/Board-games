package es.ucm.fdi.tp.extra.swing.Hebras;// Ejemplo mínimo de hebras, sin swing.
// Uso de la herencia de Thread y del método start().
public class Ejemplo1 {
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
		th0.start(); // Ejecución CONCURRENTE ¿Qué ocurre si se llama a run()?
		int contador = 0;
		while (true) {  
			System.out.print(contador);
			++contador;
		}
	}
}
