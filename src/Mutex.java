import java.util.concurrent.Semaphore;

public class Mutex{			//clase que genera los semaforos.
	
	private Semaphore semaforo;
	private Log log;
	
	public Mutex(int i, Log log) {	// i = cantidad de elementos que permite el acceso al semáforo.
		semaforo = new Semaphore(i,true); // los argumentos son: la cantidad de elementos que permite ingresar al monitor.
										  //					 true para activar el fairness (FIFO).	 
		this.log = log;
	}
	
	public void acquire(){
		try {
			semaforo.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		} 	
	}
	
	void release() {
		semaforo.release();
	}
	
}