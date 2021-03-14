import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class Log {	
	
	private String archivoLogGeneral;
	private String archivoLogDisparos;
	List<List<String>> logGeneral = new ArrayList<List<String>>();
	List<List<String>> logDisparos = new ArrayList<List<String>>();
	public int disparos = 0;		// variable que indica la cantidad de disparos total que se realizará en la ejecución del programa.
	
	public Log(String logGeneral,String logDisparos) {
		this.archivoLogGeneral = logGeneral;
		this.archivoLogDisparos = logDisparos;
		crearPlanilla();
	}
	
	void crearPlanilla() {
		logGeneral.add(new ArrayList<String>());	//creo la 1er sublista que representa la 1er columna (tiempo)
		logGeneral.add(new ArrayList<String>());	//creo la 2da sublista que representa la 2da columna (acción)
		logDisparos.add(new ArrayList<String>());
		logDisparos.add(new ArrayList<String>());
	}
	
	void escribir(Long tiempo, String accion) {
		logDisparos.get(0).add(Long.toString(tiempo));	//agrego el tiempo a la 1er columna
		logDisparos.get(1).add(accion);					//agrego la transicion disparada a la 2da columna
	}
	
	void finalizarPlanilla() {
		if (RedDePetri.salidasTotal == Ventana.cantidadAutos) {
			if(Ventana.generarLog){
				final String[][] data = new String[logDisparos.get(0).size()][2];
				for (int i = 0;i<logDisparos.get(0).size();i++) {
					data[i] = new String[] {logDisparos.get(0).get(i),logDisparos.get(1).get(i)};			
				}			
				try {
					String[] columnas = new String[] { "Tiempo", "T disparada" };
					TableModel model = new DefaultTableModel(data, columnas);
					final File file = new File(archivoLogDisparos);
					SpreadSheet.createEmpty(model).saveAs(file);
				}	catch (FileNotFoundException e) {
					//ErrorManager.showErrorMessage("createOds", e.toString());
					} catch (IOException e) {
						//ErrorManager.showErrorMessage("createOds", e.toString());
					} catch (IllegalArgumentException e) {
						//ErrorManager.showErrorMessage("createOds", e.toString());
					} catch (Exception e){
						//ErrorManager.showErrorMessage("createOds", e.toString());
					}
			}
			GestorDeMonitor.condicionHilo = false; //una vez finalizada la planilla, finalizan la ejecucion de los hilos.
		}
	}
}
