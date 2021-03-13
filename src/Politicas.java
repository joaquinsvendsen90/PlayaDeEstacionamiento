import java.util.ArrayList;
import java.util.List;

public class Politicas {						  	
	 					  //Transiciones: 1,10,11,12,13,14,15,16,19,2,20,21,23,24,3,4,5,6,7,8,9}
	//public static int prioridadesT_a[] = {1,1, 1, 1, 1, 1, 1, 1, 1, 1,1, 1, 2, 1, 1,1,1,1,1,1,1};		//Prioridad llenar de vehículos planta baja y luego habilitar el piso superior. Prioridad salida indistinta.																														
	//public static int prioridadesT_b[] = {1,1, 2, 1, 1, 1, 1, 1, 1, 1,1, 1, 1, 1, 1,1,1,1,1,1,1};		//Prioridad llenado indistinta. Prioridad salida a calle 2.
	
	public static int prioridadesT_a[] = {2,2, 2, 2, 2, 2, 2, 2, 2, 2,2, 2, 2, 2, 2,2,2,2,1,2,2};		//Prioridad llenar de vehículos planta baja y luego habilitar el piso superior. Prioridad salida indistinta.
	public static int prioridadesT_b[] = {2,2, 2, 1, 2, 2, 2, 2, 2, 2,2, 2, 2, 2, 2,2,2,2,2,2,2};		//Prioridad llenado indistinta. Prioridad salida a calle 2.
	public static int prioridadesT[] = prioridadesT_a;
	
	public Politicas() {}
	
	public static List<Integer> cual(List<Integer> hilosBloqueadosListos)  {		//este método es el encargado en decidir qué hilo será desbloqueado de la cola.
		List<Integer> decision = new ArrayList<Integer>();
		int comparador = 3, posicion = 0, cantHilosBloqueadosListos = 1;
		for (int i=0;i<hilosBloqueadosListos.size();i++) {
			if(hilosBloqueadosListos.get(i) == 1) {
				decision.add(i,0);
				if (prioridadesT[i] < comparador){
					comparador = prioridadesT[i];
					posicion = i;
				}		
				else if (prioridadesT[i] == comparador) {
						cantHilosBloqueadosListos += 1;
						decision.set(posicion,1);
						decision.set(i,1);
						posicion = i;
				}
			}
			else {
				decision.add(i,0);
			}	
		}
		if (cantHilosBloqueadosListos > 1) {
			List<Integer> decisionAleatoria = elegirAleatoriamente(decision);
			return decisionAleatoria;
		}
		else {
			decision.set(posicion,1);
			return decision;
		}

	}
	
	static public List<Integer> elegirAleatoriamente(List<Integer> decision) {	// ante igualdad de prioridades elegimos un hilo o transicion a disparar aleatoriamente.
		int comparador = 10, posicion = 0;
		for (int i=0; i<decision.size(); i++) {
			if (decision.get(i) == 1) {
				decision.set(i, 1 + (int)(Math.random()*9));  		//a cada hilo bloqueado le asigno un numero random para elegir de manera aleatoria a alguno de ellos.
			}
		}
		for (int i=0;i<decision.size();i++) {
			if(decision.get(i) >= 1) {
				if(decision.get(i) <= comparador) {
					comparador = decision.get(i); 
					decision.set(i,0);
					posicion = i;
				}
				else {
					decision.set(i, 0);
				}	
			}
		}
		decision.set(posicion,1); 
		return decision;
	}

}

	
