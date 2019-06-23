package es.ucm.fdi.tp.extra.hebras;// Ejemplo de uso de join() (segunda parte).
// Ejemplo de cálculo de números primos en una sola hebra.
public class EjemploPrimos2 {
	public static final int NUM = 4000000;

	public static void main(String[] args) {
		System.out.println("inicio");
		long startTime = System.currentTimeMillis();
		HebraPrimos hebra1 = new HebraPrimos("uno ",1,4*NUM);
		hebra1.start();
		try {
			hebra1.join();
		} catch (InterruptedException e) {}
		long endTime = System.currentTimeMillis();
		System.out.println("Tiempo total: " + (endTime-startTime) + " ms.");
	}
	
}
