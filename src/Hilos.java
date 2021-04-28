
public class Hilos extends Thread {
	private GestorDeMonitor monitor;
	private int[] transicionesHilo;
	private SensibilizadoConTiempo sensibilizadoConTiempo;
	static String[] nombre = {"Hilo 0 - \"Ingreso de Vehículos por barrera 1\" T:19,1,4","Hilo 1 - \"Ingreso de Vehículos por barrera 2\" T:20,2,5","Hilo 2 - \"Ingreso de Vehículos por barrera 3\" T:21,3,6","Hilo 3 - \"Entrada al piso 1\" T:7","Hilo 4 - \"Salida del piso 1\" T:9","Hilo 5 - \"Subir al piso 2\" T:23,8","Hilo 6 - \"Bajar del piso 2\" T:10,24","Hilo 7 - \"Salida por barrera 1\" T:11,13,15","Hilo 8 - \"Salida por barrera 2\" T:12,14,16"};
	//static String[] nombre = {"Hilo 0 - \"Ingreso de Vehículos por barrera 1\" T:19,1,4","Hilo 1 - \"Ingreso de Vehículos por barrera 2\" T:20,2,5","Hilo 2 - \"Ingreso de Vehículos por barrera 3\" T:21,3,6","Hilo 3 - \"Entrada al piso 1\" T:7","Hilo 4 - \"Salida del piso 1\" T:9","Hilo 5 - \"Subir al piso 2\" T:23,8","Hilo 6 - \"Bajar del piso 2\" T:10,24","Hilo 7 - \"Llegada a la barrera de salida 1\" T:11","Hilo 8 - \"Salida por barrera 1\" T:13,15","Hilo 9 - \"LLegada a la barrera de salida 2\" T:12","Hilo 10 - \"Salida por barrera 2\" T:14,16"};
	
	private int vinoDeSleep = 0;
	boolean finalizado =  false;
	public static boolean condicionHilo = true;
	public static long tiempoInicio, tiempoFinal;
	
	public Hilos(GestorDeMonitor monitor,int[] transicionesHilo, SensibilizadoConTiempo sensibilizadoConTiempo) {
		this.monitor = monitor;
		this.transicionesHilo = transicionesHilo;
		this.sensibilizadoConTiempo = sensibilizadoConTiempo;
	}
	
	static public void nombreDelHilo(int id) {
		System.out.println(nombre[id]);
	}
	
	
	public void run() {
		while(condicionHilo == true) {
			for (int i=0; i < this.transicionesHilo.length; i++) {
			//System.out.println("El hilo que esta ahora es "+Thread.currentThread().getName()+ " e i= "+i+" y viene con la T "+this.transicionesHilo[i]);
				if (sensibilizadoConTiempo.vieneDelSleep[Integer.parseInt(Thread.currentThread().getName())] == true) {
					if((this.transicionesHilo.length > 1)) {
						i--;
						//System.out.println("SOY HILO "+Thread.currentThread().getName()+" Y ENTRE A RESTAR EL i! Y SU VALOR ES: i = "+i);
					}
					sensibilizadoConTiempo.vieneDelSleep[Integer.parseInt(Thread.currentThread().getName())] = false; //reseteo la bandera.	
					int transicion = sensibilizadoConTiempo.cualTransicion(Integer.parseInt(Thread.currentThread().getName()));
					//System.out.println("SOY "+Thread.currentThread().getName()+" QUIERO DISPARAR LA TRANS: "+transicion);
					monitor.dispararTransicion(RedDePetri.transiciones[transicion]);
				}
				else{
					monitor.dispararTransicion(this.transicionesHilo[i]);
				}
				if(condicionHilo == false && finalizado ==  false) {
					finalizado = true;
					tiempoFinal  = System.currentTimeMillis();
					System.out.println("Ejecución finalizada. Se tardó "+((tiempoFinal - tiempoInicio)) +" ms para que salgan " +Ventana.cantidadAutos +" autos del estacionamiento." );
					break;
				}
			}
		}	
	}	
}