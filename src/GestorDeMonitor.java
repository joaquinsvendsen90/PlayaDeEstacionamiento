import java.util.List;

public class GestorDeMonitor{
	
	private RedDePetri redDePetri;
	private Politicas politicas;
	private SensibilizadoConTiempo sensibilizadoConTiempo;
	private Mutex mutex;
	private Colas colas;
	private Log log;
	
	int[][] transicionesPorHilo;
	List<Integer> decision;
	long tiempo;
	
	public static boolean condicionHilo = true;
	public static long tiempoInicio, tiempoFinal;

	//constructor
	public GestorDeMonitor(RedDePetri redDePetri,int[][] transicionesPorHilo,Politicas politicas,SensibilizadoConTiempo sensibilizadoContiempo,Mutex mutex,Colas colas,Log log) {
		this.redDePetri = redDePetri;
		this.transicionesPorHilo = transicionesPorHilo;
		this.politicas = politicas;
		this.sensibilizadoConTiempo = sensibilizadoContiempo;
		this.mutex = mutex;
		this.colas = colas;
		this.log = log;
	}
	
	//metodo de exportación para acceder al monitor
	void dispararTransicion(int transicionADisparar){
		boolean k = false;
		int cantHilosBloqueados = 0;

		mutex.acquire();
		tiempo = System.nanoTime();
		System.out.println("Adentro del monitor "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]);
		boolean adentro = true;
		while (adentro == true) { //quiere decir que está dentro del monitor.
			if (condicionHilo == false) {
				tiempoFinal  = System.currentTimeMillis();
				System.out.println("Ejecución finalizada. Se tardó "+((tiempoFinal - tiempoInicio)) +" ms para que salgan " +Ventana.cantidadAutos +" autos del estacionamiento." );
				//System.exit(1);
			}
			k = redDePetri.disparar(transicionADisparar);
			redDePetri.mostrarMarcado();
			if (k == true) {											//pudo disparar la transicion!
				redDePetri.sensibilizadas(transicionADisparar);							//actualizo el vector de transiciones sensibilizadas.
				redDePetri.aserciones();								//funcion encargada de ejecutar las aserciones.
				log.finalizarPlanilla();
				List<Integer> hilosBloqueados = colas.quienesEstan();					//pregunto por los hilos que están bloqueados en la colas de variable de condición.			
				for (int i = 0; i < colas.hilosBloqueadosListos.size();i++) {			//reinicio el arraylist que indica los hilos bloqueados que están listos para desbloquearse.
					colas.hilosBloqueadosListos.set(i,0);
				}
				
				for (int i = 0;i < redDePetri.T_sensibilizadas.size();i++) {
					int PosicionSensibilizada = RedDePetri.obtenerPosicionT(redDePetri.T_sensibilizadas.get(i));	//obtenemos la posicion en el vector de hilos bloqueados correspondiente con la transicion sensibilizada.
					if (hilosBloqueados.get(PosicionSensibilizada) == 1){	//preguntamos si hay un hilo bloqueado en la transicion sensibilizada.
						colas.hilosBloqueadosListos.set(PosicionSensibilizada, 1);
					}
				}	
				colas.mostrarHilosBloqueados();
				for (int i=0;i<RedDePetri.transiciones.length;i++) {
					cantHilosBloqueados += colas.hilosBloqueadosListos.get(i);		//sumo la cantidad de hilos bloqueados que pueden ser desbloqueados.
				}
				if (cantHilosBloqueados >= 1) {		//si hay hilos bloqueados listos: // se puede cambiar qué prioridad elegimos.
					decision = politicas.cual(colas.hilosBloqueadosListos);// hay que elegir a qué hilo desbloquear, si hay uno solo, se desbloqueará ese.
					if (cantHilosBloqueados > 1) {
						int transicion=0;
						for (int i = 0; i < decision.size();i++) {		//recorro el vector desicion para poder imprimir en pantalla que hilo desbloquea la política.
							if (decision.get(i) == 1) {
								transicion = i;
								break;
							}
						}
						System.out.println("Por política, se decide desbloquear el hilo bloqueado en la transición "+RedDePetri.transiciones[transicion]);
					}
					colas.resume(decision);	
				}		
				adentro = false;				//El hilo ya disparó su transición y liberó o no a los hilos bloqueados listos para continuar.
				if (cantHilosBloqueados == 0) {
					mutex.release();									//libero el mutex de acceso al monitor						
				}
			}
			else {	//(K=false)						si no puedo disparar la transicion entonces... 		
				if (sensibilizadoConTiempo.vieneDelSleep[Integer.parseInt(Thread.currentThread().getName())] == true) {	//si viene del sleep
					System.out.println("El Hilo "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]+" que viene del sleep, abandona el monitor.");
					adentro = false;			//es un hilo que viene del sleep por lo q se tiene que ir del monitor.
					
				}
				else {
					mutex.release();					//libero el mutex de acceso al monitor
					colas.delay(transicionADisparar);	//si no pudo disparar la transicion entonces que se vaya a la cola de variable de condicion correspondiente a dicho hilo.					
				}
			}
				
		}			
	}
}
