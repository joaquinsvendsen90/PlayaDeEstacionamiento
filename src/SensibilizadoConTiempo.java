
public class SensibilizadoConTiempo {
	
		  //transición: 1,10,11,12,13,14,15,16,19,2,20,21,23,24,3,4,5,6,7,8,9   T 13 y 14 son las transic. de la caja
	long timeStamp[] = {0,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0,0 ,0 ,0 ,0 ,0,0,0,0,0,0,0};	//tiempo de sensibilizado
	long[] alfa;	//tiempo alfa de cada transición. (en milisegundos)
	boolean primero[] = {false,false,false ,false ,false ,false ,false ,false ,false ,false ,false,false ,false ,false ,false ,false,false,false,false,false,false}; 
	boolean esperando[] = {false,false,false ,false ,false ,false ,false ,false ,false ,false ,false,false ,false ,false ,false ,false,false,false,false,false,false};
	boolean vieneDelSleep[] = {false,false,false ,false ,false ,false ,false ,false,false}; //uno por hilo.
	int 	IDHiloSleep[] = {10,10,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10,10 ,10 ,10 ,10 ,10,10,10,10,10,10};  // como usamos el hilo 0, el 10 indica como vacío.
				
	public SensibilizadoConTiempo(boolean tiempoActivado) {
		if (tiempoActivado == true) {
			     //transición: 1,10, 11,12,13,14,15,16,19,2,20, 21, 23,24,3,4,5,6,7,8,9
			alfa = new long[] {0,100,0, 0, 0 ,0 ,3 ,3,120,0,120,120,0, 0, 0,1,1,1,0,0,100};
		}
		else {
			alfa = new long[] {0,0,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0,0 ,0 ,0 ,0 ,0,0,0,0,0,0,0}; //sirve para quitar los tiempos a la hora de hacer los tests.
		}
	}
	
	boolean testVentantaTiempo(int transicionADisparar) {
		boolean ventana;
		if (alfa[transicionADisparar] <= (System.currentTimeMillis() - timeStamp[transicionADisparar])) {
			return ventana = true;
		}
		else {
			return ventana = false;
		}
	}
	
	void setNuevoTimeStamp(int transicion){
		timeStamp[transicion] = System.currentTimeMillis();
	}	
	
	void resetTimeStamp(int transicion){
		timeStamp[transicion] = 0;
	}

	boolean antesDeLaVentana(int transicion) {	
		if (primero[transicion] == false) {
			primero[transicion] = true;
			return true;
		}
		else {
			return false;
		}
	}
	
	void setEsperando(int transicion) {
		IDHiloSleep[transicion] = Integer.parseInt(Thread.currentThread().getName()); 
		esperando[transicion] = true;
	}
	
	void resetEsperando(int transicion) {
		IDHiloSleep[transicion] = 10; // el 10 indica que no hay hilos durmiendo esperando por disparar la transicion.
		esperando[transicion] = false;
	}
	
	int cualTransicion(int hilo) {		//con este método se qué transicion quiere disparar un hilo si viene de  un sleep
		for(int i = 0; i < IDHiloSleep.length ; i++) {
			if (IDHiloSleep[i] == hilo) {
				return i;
			}		
		}
		return -1;			//si el hilo no estuvo esperando dormido por disparar una transicion, retorna -1
	}	
}
