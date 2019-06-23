package es.ucm.fdi.tp.extra.hebras;// Ejemplo de uso de join().
// Cálculo de números primos en tres hebras distintas.
// La hebra de main espera a la finalización de las tres.
public class EjemploPrimos1 {
	public static final int NUM = 4000000;

	public static void main(String[] args) {
		System.out.println("inicio");
		long startTime = System.currentTimeMillis();
		HebraPrimos hebra1 = new HebraPrimos("uno ",1,NUM);
		HebraPrimos hebra2 = new HebraPrimos("dos ",NUM+1,2*NUM);
		HebraPrimos hebra3 = new HebraPrimos("tres",2*NUM+1,3*NUM);
		HebraPrimos hebra4 = new HebraPrimos("cuatro",3*NUM+1,4*NUM);
		hebra1.start();
		hebra2.start();
		hebra3.start();
		hebra4.start();
		try {
			hebra1.join();
			hebra2.join();
			hebra3.join();
			hebra4.join();
		} catch (InterruptedException e) {}
		long endTime = System.currentTimeMillis();
		System.out.println("Tiempo total: " + (endTime-startTime) + " ms.");
	}
	
}
