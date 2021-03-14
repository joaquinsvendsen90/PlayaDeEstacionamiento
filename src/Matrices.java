import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Matrices {
	private RedDePetri redDePetri;
	
	public Matrices(String ruta,RedDePetri redDePetri) {
		this.redDePetri = redDePetri;
		generacionMatrices(ruta);
	}

	void generacionMatrices(String ruta) {
		// LECTURA DEL ARCHIVO HTML 
		File file = new File(ruta);
		ArrayList<String> fichero = new ArrayList<String>();
		
		try (Scanner s = new Scanner(file)) {		//GUARDO EN UN FICHERO AL ARCHIVO LEIDO
			while (s.hasNext()) {
				fichero.add(s.nextLine());
			}
		}	
		catch(IOException e){
	 
		}	
		for(int x=0;x<fichero.size();x++) {		//ELIMINO LOS ESPACIOS EN BLANCO
			fichero.set(x,fichero.get(x).replace(" ", ""));
		}
		fichero.remove(fichero.indexOf("P0")); //elimino los primeros 2 "P0" para ubicarme en el tercero que equivale al inicio de la matriz de incidencia
		fichero.remove(fichero.indexOf("P0"));
		
		//MATRIZ DE INCIDENCIA
		int finMatrizI = 0;
		int i = 0;
		int ubicacion = (fichero.indexOf("P0")+2);	//me ubico 2 posiciones despues del P0 de la matriz i
	    
		//--------- Guardo los valores de la matriz de incidencia en la lista
		while( finMatrizI == 0 ){		//mientras queden elementos por recorrer en la tabla...
			if (!fichero.get(ubicacion).equals("</tr>")){	//si la posicion actual no es "</tr>	
				redDePetri.matrizIncidencia.add(new ArrayList<Integer>());	//creo una sublista que representa una columna para meter el primer valor
				ubicacion = ubicacion + 1;	//guardar valor que esta almacenado en una posicion siguiente a la actual
				String numCadena = fichero.get(ubicacion); 
				int numEntero = Integer.parseInt(numCadena);	//paso el valor del numero que estaba en string a int
				redDePetri.matrizIncidencia.get(i).add(numEntero);	//agrego el valor a la lista
				i++;
				ubicacion = ubicacion + 2;
			}
			else{ // quiere decir que encontro un </tr>
				if(fichero.get(ubicacion+1).equals("</table>")){ //si encontro un </tr> y lo que le sigue es un </table> entonces terminar.
					finMatrizI = 1;								//quiere  decir que llego al final de la matriz
				} // si encontro un </tr> y no le sigue un </table> entonces seguir. (ACA LLEGO AL FINAL DE UNA FILA)
				ubicacion = ubicacion + 5;
				i = 0;
			}
		}
	    
		//MATRIZ DE MARCADO INICIAL
		int finMatrizM = 0; 
		ubicacion = fichero.indexOf("Initial")+2;
		while(finMatrizM == 0){
			if (!fichero.get(ubicacion).equals("</tr>")){
				ubicacion = ubicacion + 1;
		    	String numCadena2 = fichero.get(ubicacion); 
		    	int numEntero2 = Integer.parseInt(numCadena2);  //paso el valor del numero que estaba en string a int
		    	redDePetri.marcado.add(numEntero2); // a esa columna unica le voy agregando todos los datos (osea las fila)
		    	ubicacion = ubicacion + 2;
		    }
		    else{ //quiere decir que encontro un </tr>. TERMINA!. (LA MATRIZ DE EL MARCADO INICIAL ES UNA SOLA FILA)
		    	finMatrizM=1;
		    }
		}
		
		//MATRIZ DE TRANSICIONES SENSIBILIZADAS 
	    int finMatrizSens = 1;   
	    List<Integer> sensibilizadas = new ArrayList<Integer>();
	    ubicacion = fichero.indexOf("Enabledtransitions");
	    while(finMatrizSens == 1){	
	    	if(fichero.get(ubicacion).equals("yes")){	// si desde la ubicacion actual encuentro un yes, entonces me guarda un 1 en la lista
	    		sensibilizadas.add(1);
	    		ubicacion++;
	    	}
	    	else if(fichero.get(ubicacion).equals("no")){	// si desde la ubicacion actual encuentro un no, entonces me guarda un 0 en la lista
	    		sensibilizadas.add(0);
	    		ubicacion++;
	    	}
	    	else if(fichero.get(ubicacion).equals("</html>")){ //acá termina el archivo
	    		finMatrizSens = 0;	
	    	}
	    	else {
	    		ubicacion++;
	    	}
	    }
	    for (int j=0;j < sensibilizadas.size(); j++) {		//convierto el vector de sensibilizas a numueros que representan las transiciones.
	    	if (sensibilizadas.get(j) == 1) {
	    		redDePetri.T_sensibilizadas.add(RedDePetri.transiciones[j]);
	    	}	
	    }
	}

}