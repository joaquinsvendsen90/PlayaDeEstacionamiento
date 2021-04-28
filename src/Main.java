import java.io.*;

public class Main {
	
	public static void main (String args[]) throws InterruptedException{
		
		//vectores que contienen las transiciones que quieren disparar cada uno de los 8 hilos definidos en el sistema.
		int TransicionesHilo0[]  = {19,1,4}; 	// Ingreso de Vehículos por barrera 1
		int TransicionesHilo1[]  = {20,2,5}; 	// Ingreso de Vehículos por barrera 2
		int TransicionesHilo2[]  = {21,3,6};	// Ingreso de Vehículos por barrera 3
		int TransicionesHilo3[]  = {7}; 		// Entrada al piso 1
		int TransicionesHilo4[]  = {9}; 		// salida del piso 1
		int TransicionesHilo5[]  = {23,8}; 		// Subir al piso 2
		int TransicionesHilo6[]  = {10,24}; 	// Bajar del piso 2
		int TransicionesHilo7[]  = {11,13,15};  // Salida por barrera 1
		int TransicionesHilo8[]  = {12,14,16}; 	// Salida por barrera 2
		
		
		int[][] transicionesPorHilo = {TransicionesHilo0,TransicionesHilo1,TransicionesHilo2,TransicionesHilo3,TransicionesHilo4,TransicionesHilo5,TransicionesHilo6,TransicionesHilo7,TransicionesHilo8};
		final int cantHilos = transicionesPorHilo.length;
		int cantidadAutosInicial;
		
		//politicas
		Politicas politicas = new Politicas();
		Ventana ventanaMostrarAutos = new Ventana(); //ventana para mostrar la cantidad de autos saliendo
		Ventana ventana = new Ventana(politicas);	 //ventana principal con todas las opciones del programa
		
		while(true) {			//en este bucle seteamos a traves de la ventana la cantidad de disparos que realizará cada hilo.
			Thread.sleep(1000);
			if (ventana.cantidadAutos > 0) {
				cantidadAutosInicial = Ventana.cantidadAutos;
				ventana.setVisible(false);							//se cierra la ventana principal
				ventanaMostrarAutos.setVisible(true);				//se activa la ventana secundaria
				break;
			}
		}
		
		//creación de arrays
		Thread threads[] = new Thread[cantHilos];
		Thread.State status[] = new Thread.State[cantHilos];
		
		String Path = new File("").getAbsolutePath(); //contiene el path de la carpeta del proyecto para abrir y guardar los archivos externos necesarios
		
		//Archivo Log
		Log log = new Log(Path+"/LogGeneral.ods",Path+"/LogDisparos.ods");
		
		//semaforo de acceso al monitor
		Mutex mutex = new Mutex(1,log);		
		
		//Creación de Colas
		Mutex colasCondicion[] = new Mutex[RedDePetri.transiciones.length];				//colas de variable de condicion para cada hilo.
		Colas colas = new Colas(mutex,colasCondicion,log);		
		
		SensibilizadoConTiempo sensibilizadoConTiempo = new SensibilizadoConTiempo(ventana.activarTiempo); //activarTiempo: true = transiciones con tiempo, false = transiciones inmediatas
		//instancia de la Red de Petri
		RedDePetri redDePetri = new RedDePetri(politicas,sensibilizadoConTiempo,mutex,log);
		String ruta = Path+"/matricest0.html";						//ruta donde se encuentra el archivo html que contiene las matrices exportadas del pipe
		Matrices matrices = new Matrices(ruta,redDePetri);			//levanta las matrices del archivo exportado por el pipe.
		//instancia del monitor
		GestorDeMonitor monitor = new GestorDeMonitor(redDePetri,transicionesPorHilo,politicas,sensibilizadoConTiempo,mutex,colas,log);
		
		Hilos hilo0 = new Hilos(monitor,TransicionesHilo0,sensibilizadoConTiempo);			//Hilo es quien tiene el método run(). le pasamos el objeto monitor y también
		Hilos hilo1 = new Hilos(monitor,TransicionesHilo1,sensibilizadoConTiempo);			//las transiciones que quiere disparar cada hilo.
		Hilos hilo2 = new Hilos(monitor,TransicionesHilo2,sensibilizadoConTiempo);
		Hilos hilo3 = new Hilos(monitor,TransicionesHilo3,sensibilizadoConTiempo);
		Hilos hilo4 = new Hilos(monitor,TransicionesHilo4,sensibilizadoConTiempo);
		Hilos hilo5 = new Hilos(monitor,TransicionesHilo5,sensibilizadoConTiempo);
		Hilos hilo6 = new Hilos(monitor,TransicionesHilo6,sensibilizadoConTiempo);
		Hilos hilo7 = new Hilos(monitor,TransicionesHilo7,sensibilizadoConTiempo);
		Hilos hilo8 = new Hilos(monitor,TransicionesHilo8,sensibilizadoConTiempo);
		
		//Creación de Hilos:
		
		threads[0]=new Thread(hilo0,"0");
		threads[1]=new Thread(hilo1,"1");
		threads[2]=new Thread(hilo2,"2");
		threads[3]=new Thread(hilo3,"3");
		threads[4]=new Thread(hilo4,"4");
		threads[5]=new Thread(hilo5,"5");
		threads[6]=new Thread(hilo6,"6");
		threads[7]=new Thread(hilo7,"7");
		threads[8]=new Thread(hilo8,"8");
		
		redDePetri.marcado.set(16, cantidadAutosInicial); //seteamos la plaza 26 (vehiculos por acceder) de la red con la cantidad de autos seleccionada.
		//redDePetri.marcado.set(19,0);		//con este marcado anulo el 2do piso para realizar tests de tiempo.
		redDePetri.mostrarMarcado();
		//ejecucion de hilos	
		Hilos.tiempoInicio = System.currentTimeMillis();
		for (int i=0; i<cantHilos; i++){
			threads[i].start();
		}
	}
}
