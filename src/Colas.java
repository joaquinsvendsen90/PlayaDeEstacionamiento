import java.util.*;

public class Colas {
	
	private Mutex mutex;
	private Log log;
	List<Integer> hilosBloqueados = new ArrayList<Integer>(Collections.nCopies(RedDePetri.transiciones.length, 0)); 			//array que determina mediante un 1, qué hilos están bloqueados en la cola. lo inicializo en cero.
	List<Integer> hilosBloqueadosListos = new ArrayList<Integer>(Collections.nCopies(RedDePetri.transiciones.length, 0));		//array que determina mediante un 1, qué hilos bloqueados pueden disparar alguna transición sensibilizada.	 													
	Mutex colasCondicion[] = new Mutex[RedDePetri.transiciones.length];	
	
	public Colas(Mutex mutex,Mutex colasCondicion[],Log log) {	// se tiene que pasar como argumentos un semaforo de tipo Mutex
		this.mutex = mutex;
		this.colasCondicion = colasCondicion;
		this.log = log;
		for (int i = 0;i<RedDePetri.transiciones.length;i++) {
			colasCondicion[i] = new Mutex(0,log);
		}
	}						 
	
	void delay(int transicion) {	
		hilosBloqueados.set(RedDePetri.obtenerPosicionT(transicion),1);		//pongo en uno el elemento del array correspondiente a dicho hilo, en representacion de que está bloqueado.	
		System.out.println("El "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]+" se bloquea en la cola dentro del monitor");
		colasCondicion[RedDePetri.obtenerPosicionT(transicion)].acquire();	//se bloquea el hilo en la cola de variable de condicion correspondiente a la transición que desea disparar.
	}
	
	void resume(List<Integer> decision) {	
		for (int i = 0; i < RedDePetri.transiciones.length;i++) {
			if (decision.get(i) == 1) {
				hilosBloqueados.set(i,0);										//marco como liberado dicho hilo
				hilosBloqueadosListos.set(i,0);	
				colasCondicion[i].release();								//libero el unico hilo bloqueado que tiene transiciones sensibilizadas listas para disparar.
				System.out.println("El hilo bloqueado en la transición "+RedDePetri.transiciones[i] +" fué desbloqueado.");
			}	
		}	
	}
	
	public List<Integer> quienesEstan() {
		return hilosBloqueados;
	}
	
	void mostrarHilosBloqueados() {
		int suma1 = 0,suma2 = 0;
		String hBloqueados = "";
		String hBloqueadosListos = "";
		for (int i = 0;i<RedDePetri.transiciones.length;i++) {
			if (hilosBloqueados.get(i) == 1) {
				hBloqueados += " " +Integer.toString(RedDePetri.transiciones[i]);
				suma1++; 
			}
		}
		if (suma1 == 0) {
			System.out.println("No hay hilos bloqueados en la cola.");
		}
		else if (suma1 == 1) {
			System.out.println("Hay un hilo bloqueado esperando por disparar la transicion "+ hBloqueados);
		}
		else {
			System.out.println("Hay "+suma1+" hilos bloqueados esperando por disparar las transiciones "+hBloqueados);
		}
		for (int i = 0;i<RedDePetri.transiciones.length;i++) {
			if (hilosBloqueadosListos.get(i) == 1) {
				hBloqueadosListos += " " + Integer.toString(RedDePetri.transiciones[i]);
				suma2++; 
			}
		}
		if (suma2 == 0) {
			System.out.println("No hay hilos bloqueados listos para ser desbloqueados.");
		}
		else if (suma2 == 1) {
			System.out.println("El hilo bloqueado en la transición "+ hBloqueadosListos +" está listo para ser desbloqueado.");
		}
		else {
			System.out.println("los hilos bloqueados en las transiciones "+ hBloqueadosListos +" están listos para ser desbloqueados");
		}
	}
}

