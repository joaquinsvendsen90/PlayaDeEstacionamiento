import java.util.List;
import java.util.ArrayList;



public class RedDePetri {
	
	private Politicas politicas;
	private SensibilizadoConTiempo sensibilizadoConTiempo;
	private Mutex mutex;
	private Log log;
	
	//creación de matrices (son listas que contienen sublistas).
	public List<List<Integer>> matrizIncidencia = new ArrayList<List<Integer>>(); 
	public List<Integer> marcado = new ArrayList<Integer>();
	public List<Integer> T_sensibilizadas = new ArrayList<Integer>();
	public static int transiciones[] = {1,10,11,12,13,14,15,16,19,2,20,21,23,24,3,4,5,6,7,8,9};
	
	public static int marcadoPiso1 = 0, marcadoPiso2 = 0;
	int transicionADisparar;
	public static int entradaBarrera1 = 0, entradaBarrera2 = 0, entradaBarrera3 = 0, entradasTotal = 0;
	public static int estacionadosPiso1 = 0, estacionadosPiso2 = 0, salidaBarrera1 = 0, salidaBarrera2 = 0, salidasTotal = 0;
	
	public RedDePetri(Politicas politicas,SensibilizadoConTiempo sensibilizadoConTiempo,Mutex mutex,Log log) {
		this.politicas = politicas;
		this.sensibilizadoConTiempo = sensibilizadoConTiempo;
		this.mutex = mutex;
		this.log = log;
	}

	public void sensibilizadas(int transicionADisparar) {
		List<Integer> T_sensibilizadasNuevas = new ArrayList<Integer>();
		int cantPlazasI = matrizIncidencia.get(0).size();
		int cantTransicionesI = matrizIncidencia.size()/cantPlazasI;
		int sensibilizado = 1;
		for (int i = 0;i<cantTransicionesI;i++) {
			for (int j = 0;j<cantPlazasI;j++) {
				if ((matrizIncidencia.get(i).get(j) + marcado.get(j)) < 0  ) {	//si la suma es menor que cero
					sensibilizado = 0;	//bandera que indica que la transición no está sensibilizada.
					break;
				}
			}
			if(sensibilizado == 1) {
				T_sensibilizadasNuevas.add(transiciones[i]);					//marco como sensibilizada dicha transicion.
			}
			sensibilizado = 1; //reseteo la bandera para la proxima iteración. (por si sensibilizado vale 0)
		}
		if (T_sensibilizadasNuevas.contains(transicionADisparar)) {	//si la transicion disparada sigue estando sensibilizada
			sensibilizadoConTiempo.setNuevoTimeStamp(obtenerPosicionT(transicionADisparar));		//entonces hay que setearle el nuevo timeStamp de sensibilizado
		}
		for(int i = 0;i < T_sensibilizadasNuevas.size();i++) {
			if((T_sensibilizadas.contains(T_sensibilizadasNuevas.get(i))) == false) {								//si las nuevas transiciones no estaban antes sensibilizadas 
				sensibilizadoConTiempo.setNuevoTimeStamp(obtenerPosicionT(T_sensibilizadasNuevas.get(i)));		//entonces marcar el nuevo timeStamp
			}
		}
		T_sensibilizadas = T_sensibilizadasNuevas;
	}
	
	public boolean disparar(int transicionADisparar) {
		boolean sensibilizada = false;
		System.out.println("el "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]+" Intenta disparar la transicion " +transicionADisparar);
		for(int i=0;i<T_sensibilizadas.size();i++) {			//corroboro si la transicion que quiere disparar el hilo está sensibilizada.
			if(T_sensibilizadas.get(i) == transicionADisparar) {	
				sensibilizada = true;
			}
		}	
		mostrarSensibilizadas();
		if (sensibilizada == true) {	
			boolean ventana = sensibilizadoConTiempo.testVentantaTiempo(obtenerPosicionT(transicionADisparar));
			if (ventana == true) {
				if (sensibilizadoConTiempo.esperando[obtenerPosicionT(transicionADisparar)] == false) {					
					actualizaMarcado(transicionADisparar);		//llamo a la funcion que genera el nuevo marcado (disparo la transicion)
					ContadorDeAutos(transicionADisparar);	// Me fijo si salio algun auto de la playa de estacionamiento y mantengo un conteo de los mismos.
					marcadoPiso1 = marcado.get(3);			//guardo los marcados de las plazas que representan los pisos 1 y 2. para luego mostraarlos en la ventana grafica. 
					marcadoPiso2 = marcado.get(4);
					Ventana.mostrarVentanaAutos();	
					return true;					//quiere decir que pudo disparar la transicion.
				}
				else {		//esperando == true. es decir hay un hilo durmiendo esperando por disparar dicha transicion.
					if (Integer.parseInt(Thread.currentThread().getName()) == sensibilizadoConTiempo.IDHiloSleep[obtenerPosicionT(transicionADisparar)]) { // si el hilo que quiere disparar a la transicion es el mismo que hizo el sleep esperandola.
						System.out.println("El hilo "+Integer.parseInt(Thread.currentThread().getName())+" viene de un sleep y ahora quiere disparar la transición "+transicionADisparar);
						sensibilizadoConTiempo.resetEsperando(obtenerPosicionT(transicionADisparar));		//ponemos esperando = false.
						actualizaMarcado(transicionADisparar);		//llamo a la funcion que genera el nuevo marcado (disparo la transicion)
						ContadorDeAutos(transicionADisparar);
						marcadoPiso1 = marcado.get(3);
						marcadoPiso2 = marcado.get(4);
						Ventana.mostrarVentanaAutos();	//Muestra en la ventana la cantidad de autos que van saliendo
						return true;					//quiere decir que pudo disparar la transicion.
					}
					else {			//el id del hilo que quiere disparar no corresponde con el id del hilo que estuvo en el sleep.
						System.out.println("Ya hay un hilo esperando a disparar dicha transición.");
						return false;
					} 
				}
			}
			else {			//ventana == false.
				System.out.println("La transición "+transicionADisparar+" está sensibilizada pero aún no se cumplió el tiempo alfa");
				boolean antes = sensibilizadoConTiempo.antesDeLaVentana(obtenerPosicionT(transicionADisparar));  // esto indica si ya hay alguien durmiendo esperando disparar la transicion.
				if (antes = true) {			// si no hay nadie durmiendo
					System.out.println("no hay ningún hilo esperando para disparar dicha transición por lo tanto el hilo se va a dormir esperando a que se cumpla el tiempo alfa");
					mutex.release(); 	//libero el mutex antes de mandar a dormir al hilo.
					sensibilizadoConTiempo.setEsperando(obtenerPosicionT(transicionADisparar));
					try {
						Thread.currentThread().sleep(sensibilizadoConTiempo.timeStamp[obtenerPosicionT(transicionADisparar)] + sensibilizadoConTiempo.alfa[obtenerPosicionT(transicionADisparar)] - System.currentTimeMillis());
					}
					catch (Exception e) {
					}
					System.out.println("El hilo "+Integer.parseInt(Thread.currentThread().getName()) +" Acaba de despertarse ya que se cumplió el tiempo alfa");
					sensibilizadoConTiempo.vieneDelSleep[Integer.parseInt(Thread.currentThread().getName())] = true;
					return false;
				}
				else {		//si ya hay un hilo durmiendo.
					System.out.println("Ya hay un hilo durmiendo esperando por disparar la transición.");
					return false;
				}		
			}
		}	
		else {	//si no hay transiciones sensibilizadas
			System.out.println("No hay transiciones sensibilizadas que pueda disparar este hilo.");
			return false;					//quiere decir que no pudo disparar ninguna transicion
		}	
	}
	
	public void actualizaMarcado(int disparo) {			//Método encargado de hacer el nuevo marcado que sería Mi+1 = Mi + I.U 
		for (int j =0;j<matrizIncidencia.get(0).size();j++) {
					marcado.set(j,(marcado.get(j)+matrizIncidencia.get(obtenerPosicionT(disparo)).get(j)));		
		}
		log.escribir(System.nanoTime(),"T"+disparo);
		System.out.println("transición " +"T"+disparo +" disparada. :)");
		sensibilizadoConTiempo.resetEsperando(obtenerPosicionT(disparo));
	}
	
	public static int obtenerPosicionT(int t) {			//este metodo le pasamos como argumento el numero de transicion y devuelve la posicion de dicha transicion en el vector de transiciones.
		for (int i=0;i<transiciones.length;i++) {
			if (transiciones[i] == t) {
				return i;
			}
		}
		return -1;
	}
	
	public void  ContadorDeAutos(int t) {		//metodo para analizar y llevar una cuenta de la cantidad de autos que salen y se tiene que corresponder con la cantidad de autos seleccionados por el usuario que ingresaron a la playa
		switch(t) {
			case 19: {
				entradaBarrera1++;
				entradasTotal++;
				break;
			}
			case 20: {
				entradaBarrera2++;
				entradasTotal++;
				break;
			}
			case 21: {
				entradaBarrera3++;
				entradasTotal++;
				break;
			}
			case 7: {
				estacionadosPiso1++;
				break;
			}
			case 8: {
				estacionadosPiso2++;
				break;
			}
			case 15: {
				salidaBarrera1++;
				salidasTotal++;
				break;
			}
			case 16: {
				salidaBarrera2++;
				salidasTotal++;
				break;
			}
		}
		if(entradasTotal == Ventana.cantidadAutos) {
			marcado.set(16,0);
		}
	}
	
	void mostrarMatrizIncidencia() {
		// IMPRESION DE LAS MATRICES GUARDADAS EN LAS LISTAS
		int cantPlazasI = matrizIncidencia.get(0).size();
		int cantTransicionesI = matrizIncidencia.size()/cantPlazasI;
		System.out.println("\n");
	    System.out.print("La matriz de incidencia es: ");
	    for(int j=0; j<cantPlazasI; j++){ // filas
	    	System.out.print("\n");
	    	for(int k=0; k<cantTransicionesI; k++){ //columnas
	    	System.out.print(" "+matrizIncidencia.get(k).get(j));
	    	}
	    }
	    System.out.println("\n");
	}    
	
	void mostrarMarcado() {   
		String marca = "";
	    for(int k=0; k<marcado.size(); k++){ //filas
	    	marca += "     " +Integer.toString(marcado.get(k));
	    }
	    System.out.println("                    Plaza: 0     1     10     11   12     13     14     15   16     17    18    19    2     20    21    22    26    29    3     30    31    4     5     6     7     8     9");
	    System.out.println("el marcado actual es: " +marca);
	}    
	
	void mostrarSensibilizadas() {    
		String sensi = "";
		for(int k=0; k<T_sensibilizadas.size(); k++){ //filas
			sensi += " "+Integer.toString(T_sensibilizadas.get(k)); 	
	    }
		System.out.println("\"La transiciones " + sensi +" están sensibilizadas");
	}
	
	public void aserciones() {
		int cantTokens1 = 0,cantTokens2 = 0,cantTokens3 = 0,cantTokens4 = 0,cantTokens5 = 0,cantTokens6 = 0,cantTokens7 = 0,cantTokens8 = 0,cantTokens9 = 0,cantTokens10 = 0;
		int[] invariante1 = {0,1,2,3,4,12,17,18,20,21,22,26}; //estas son las plazas correspondiente a cada invariante.
		int[] invariante2 = {3,5};     int[] invariante3 = {4,6,17,20}; int[] invariante4 = {8,10,13}; 								
		int[] invariante5 = {9,11,14}; int[] invariante6 = {13,14,15};  int[] invariante7 = {17,19,20};
		int[] invariante8 = {18,23};   int[] invariante9 = {21,24};     int[] invariante10 = {22,25};
		
		for (int i=0;i<invariante1.length;i++) {
			cantTokens1 += marcado.get(invariante1[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		for (int i=0;i<invariante2.length;i++) { //contabiliziamos todos los invariantes del mismo "length" para no hacer tantos bucles for.
			cantTokens2 += marcado.get(invariante2[i]); 
			cantTokens8 += marcado.get(invariante8[i]);
			cantTokens9 += marcado.get(invariante9[i]);
			cantTokens10 += marcado.get(invariante10[i]);
		}
		for (int i=0;i<invariante4.length;i++) {
			cantTokens4 += marcado.get(invariante4[i]);
			cantTokens5 += marcado.get(invariante5[i]);
			cantTokens6 += marcado.get(invariante6[i]);
			cantTokens7 += marcado.get(invariante7[i]);
		}
		for (int i=0;i<invariante3.length;i++) {
			cantTokens3 += marcado.get(invariante3[i]);
		}	
		
		assert cantTokens1 == 60 : "\n El invariante M(P0) + M(P1) + M(P10) + M(P11) + M(P12) + M(P2) + M(P29) + M(P3) + M(P31) + M(P4) + M(P5) + M(P9) = 60 No se cumple.";
		assert cantTokens2 == 30 : "\n El invariante M(P11) + M(P13) = 30 No se cumple.";
		assert cantTokens3 == 30 : "\n El invariante M(P12) + M(P14) + M(P29) + M(P31) = 30 No se cumple.";
		assert cantTokens4 == 1  : "\n El invariante M(P16) + M(P18) + M(P20) = 1 No se cumple.";	
		assert cantTokens5 == 1  : "\n El invariante M(P17) + M(P19) + M(P21) = 1 No se cumple.";
		assert cantTokens6 == 1  : "\n El invariante M(P20) + M(P21) + M(P22) = 1 No se cumple.";
		assert cantTokens7 == 1  : "\n El invariante M(P29) + M(P30) + M(P31) = 1 No se cumple.";
		assert cantTokens8 == 1  : "\n El invariante M(P3)  +  M(P6) = 1  No se cumple.";
		assert cantTokens9 == 1  : "\n El invariante M(P4)  +  M(P7) = 1  No se cumple.";
		assert cantTokens10 == 1 : "\n El invariante M(P5)  +  M(P8) = 1  No se cumple.";
	}
}				
