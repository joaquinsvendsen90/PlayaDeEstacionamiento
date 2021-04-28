import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Ventana extends JFrame implements ActionListener {
	public static int cantidadAutos = 0;			// variable que contiene la cantidad de autos seleccionada por el usuario
	public static boolean generarLog = false; 		// variable que se pone en true cuando se produce una seleccion de SI o NO en la ventana, condicion necesaria junto con cantidadAutos.
	public static boolean activarTiempo = false;	// variable que se pone en true cuando se produce selecciona Activar el tiempo en la ventana grafica
	private int tildeLog = 0; 						// variable para el checkbox de generar log. tilde = 0 -> generarLog = false, tilde = 1 -> generarLog = true 
	private int tildeTiempo = 0;					// variable para el checkbox de activar tiempos. 	
	private static String salidasTotal, entrada1, entrada2, entrada3, entradaTotal, piso1, piso2, salida1, salida2, marcaPiso1, marcaPiso2;							// variables que muestran los autos que entran y salen, convertidas a String
	
	private Politicas politicas;
	private JLabel Etiqueta;			//Etiquetas de comentarios que estan dentro de la ventana
	private JLabel Etiqueta2;
	private JLabel Etiqueta3;
	private JLabel Etiqueta4;		
	private JLabel Etiqueta5;				//Etiqueta: Autos totales saliendo de la playa	
	private static JLabel Etiqueta6;	
	private JLabel Etiqueta7;				//Etiqueta: Autos entrando por barrera 1
	private static JLabel Etiqueta8;
	private JLabel Etiqueta9;				//Etiqueta: Autos entrando por barrera 2
	private static JLabel Etiqueta10;
	private JLabel Etiqueta11;				//Etiqueta: Autos entrando por barrera 3
	private static JLabel Etiqueta12;
	private JLabel Etiqueta13;				//Etiqueta: Autos totales entrando a la playa
	private static JLabel Etiqueta14;
	private JLabel Etiqueta15;				//Autos estacionados piso 1
	private static JLabel Etiqueta16;
	private JLabel Etiqueta17;				//Autos estacionados piso 2
	private static JLabel Etiqueta18;
	private JLabel Etiqueta19;				//Autos saliendo por barrera 1
	private static JLabel Etiqueta20;
	private JLabel Etiqueta21;				//Autos saliendo por barrera 2
	private static JLabel Etiqueta22;
	private JLabel Etiqueta23; 				//Etiqueta: Marcado piso 1
	private static JLabel Etiqueta24;
	private JLabel Etiqueta25; 				//Etiqueta: Marcado piso 2
	private static JLabel Etiqueta26;
	
	private JRadioButton opcion1; 		//boton para la primera opcion
	private JRadioButton opcion2;		//boton para la segunda opcion
	private JButton	botonComenzar;		//boton de para comenzar la ejecucion
	private JTextField campoAutos;		//campo de texto para escribir la cantidad de Autos
	private String campo;				//variable que usamos para guardar lo seleccionado en campoAutos
	private JCheckBox checkbox1;		//botones tipo caja de seleccion, para generar log y activar tiempos
	private	JCheckBox checkbox2;
	
	public Ventana(Politicas politicas){	//VENTANA PRINCIPAL
		this.politicas = politicas;	
		//instanciamos los objetos para crear las etiquetas
		Etiqueta  = new JLabel();
		Etiqueta2 = new JLabel();
		Etiqueta3 = new JLabel();
		Etiqueta4 = new JLabel();
		
		//Seteamos la Etiqueta
		Etiqueta.setText("Seleccione la politica deseada: ");
		Etiqueta.setBounds(10,5,500,15); // x,y, ancho, alto 
		add(Etiqueta);		
		//Seteamos la Etiqueta2
		// perteneciente a la opcion 1
		Etiqueta2.setText("Prioridad llenar de vehículos planta baja y luego habilitar el piso superior. Prioridad salida indistinta.");
		Etiqueta2.setBounds(30,25,800,15); // x,y, ancho, alto 
		add(Etiqueta2);	
		//Seteamos la Etiqueta3
		// perteneciente a la opcion 2
		Etiqueta3.setText("Prioridad llenado indistinta. Prioridad salida a calle 2.");
		Etiqueta3.setBounds(30,45,500,15); // x,y, ancho, alto 
		add(Etiqueta3);	
		//Seteamos la Etiqueta4
		// perteneciente al campo de texto para rellenar
		Etiqueta4.setText("Elija la cantidad de Autos que van a ingresar: ");
		Etiqueta4.setBounds(10,85,500,15); // x,y, ancho, alto 
		add(Etiqueta4);
		//Seteamos la opcion 1
		opcion1 = new JRadioButton();
		opcion1.setBounds(5, 25, 20, 15);
		add(opcion1);
		//Seteamos la opcion 2
		opcion2 = new JRadioButton();
		opcion2.setBounds(5, 45, 20, 15);
		add(opcion2);
		//Seteamos el campo de texto que sirve para indicar la cantidad de autos que ingresarán
		campoAutos = new JTextField();
		campoAutos.setBounds(350, 83, 50, 20);
		add(campoAutos);	
		//Boton tipo caja de seleccion para la generacion del log
		checkbox1 = new JCheckBox("Generar log");
		checkbox1.setBounds(495,74,130,15);
		add(checkbox1);
		//Boton tipo caja de seleccion para la activacion de los tiempos
		checkbox2 = new JCheckBox("Activar Transic. temporales");
		checkbox2.setBounds(495,89,220,15);
		add(checkbox2);    
		//Seteamos el boton Comenzar
		botonComenzar = new JButton();
		botonComenzar.setBounds(325, 130, 110, 20);
		botonComenzar.setText("Comenzar");
		add(botonComenzar);	
		//Creamos un grupo de botones, para que cuando se seleccione uno, los demas de deseleccionen solos.
		ButtonGroup grupoBotones = new ButtonGroup();
		grupoBotones.add(opcion1);
		grupoBotones.add(opcion2);
		//Aqui se queda esperando que se seleccione una opcion o un boton para luego realizar una accion
		opcion1.addActionListener(this);
		opcion2.addActionListener(this);
		checkbox1.addActionListener(this);
		checkbox2.addActionListener(this);
		botonComenzar.addActionListener(this);
			
		//inicializo la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //al cerrar la ventana se detiene el programa
		setSize(760,210); //seteamos el tamaño de la ventana
		setLocationRelativeTo(null); //centra la ventana en la pantalla
		setLayout(null); //elimina toda plantilla
		setResizable(false); // evita modificar el tamaño de ventana
		setTitle("*--Playa de estacionamiento--*"); //Titulo de la ventana
		setVisible(true); //hace visible la ventana
	}	
			
	public Ventana() {	//VENTANA SECUNDARIA - Muestra la cantidad de autos que van saliendo.
		//Configuracion de las etiquetas
		Etiqueta5 = new JLabel();
		Etiqueta6 = new JLabel();
		Etiqueta7 = new JLabel();
		Etiqueta8 = new JLabel();
		Etiqueta9 = new JLabel();
		Etiqueta10 = new JLabel();
		Etiqueta11 = new JLabel();
		Etiqueta12 = new JLabel();
		Etiqueta13 = new JLabel();
		Etiqueta14 = new JLabel();
		Etiqueta15 = new JLabel();
		Etiqueta16 = new JLabel();
		Etiqueta17 = new JLabel();
		Etiqueta18 = new JLabel();
		Etiqueta19 = new JLabel();
		Etiqueta20 = new JLabel();
		Etiqueta21 = new JLabel();
		Etiqueta22 = new JLabel();
		Etiqueta23 = new JLabel();
		Etiqueta24 = new JLabel();
		Etiqueta25 = new JLabel();
		Etiqueta26 = new JLabel();
		
		Etiqueta5.setText("Cantidad de autos que salieron: ");
		Etiqueta5.setBounds(20,235,600,15); // x,y, ancho, alto 
		Etiqueta6.setBounds(375,235,200,15); // x,y, ancho, alto
		Etiqueta7.setText("Autos que ingresaron por barrera 1: ");
		Etiqueta7.setBounds(20,15,600,15); // x,y, ancho, alto
		Etiqueta8.setBounds(375,15,200,15); // x,y, ancho, alto
		Etiqueta9.setText("Autos que ingresaron por barrera 2: ");
		Etiqueta9.setBounds(20,35,600,15); // x,y, ancho, alto
		Etiqueta10.setBounds(375,35,200,15); // x,y, ancho, alto
		Etiqueta11.setText("Autos que ingresaron por barrera 3: ");
		Etiqueta11.setBounds(20,55,600,15); // x,y, ancho, alto
		Etiqueta12.setBounds(375,55,200,15); // x,y, ancho, alto
		Etiqueta13.setText("Cantidad de autos que ingresaron: ");
		Etiqueta13.setBounds(20,215,600,15); // x,y, ancho, alto
		Etiqueta14.setBounds(375,215,200,15); // x,y, ancho, alto
		Etiqueta15.setText("Autos estacionados en piso 1: ");
		Etiqueta15.setBounds(20,95,600,15); // x,y, ancho, alto
		Etiqueta16.setBounds(375,95,200,15); // x,y, ancho, alto
		Etiqueta17.setText("Autos estacionados en piso 2: ");
		Etiqueta17.setBounds(20,115,600,15); // x,y, ancho, alto
		Etiqueta18.setBounds(375,115,200,15); // x,y, ancho, alto
		Etiqueta19.setText("Autos que salieron por barrera 1: ");
		Etiqueta19.setBounds(20,155,600,15); // x,y, ancho, alto
		Etiqueta20.setBounds(375,155,200,15); // x,y, ancho, alto
		Etiqueta21.setText("Autos que salieron por barrera 2: ");
		Etiqueta21.setBounds(20,175,600,15); // x,y, ancho, alto
		Etiqueta22.setBounds(375,175,200,15); // x,y, ancho, alto
		Etiqueta23.setText("Piso 1: ");
		Etiqueta23.setBounds(20,260,600,15); // x,y, ancho, alto
		Etiqueta24.setBounds(375,260,200,15); // x,y, ancho, alto
		Etiqueta25.setText("Piso 2: ");
		Etiqueta25.setBounds(20,275,600,15); // x,y, ancho, alto
		Etiqueta26.setBounds(375,275,200,15); // x,y, ancho, alto
		
		add(Etiqueta5);
		add(Etiqueta6);
		add(Etiqueta7);
		add(Etiqueta8);
		add(Etiqueta9);
		add(Etiqueta10);
		add(Etiqueta11);
		add(Etiqueta12);
		add(Etiqueta13);
		add(Etiqueta14);
		add(Etiqueta15);
		add(Etiqueta16);
		add(Etiqueta17);
		add(Etiqueta18);
		add(Etiqueta19);
		add(Etiqueta20);
		add(Etiqueta21);
		add(Etiqueta22);
		add(Etiqueta23);
		add(Etiqueta24);
		add(Etiqueta25);
		add(Etiqueta26);
		
		//inicializo la Subventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //al cerrar la ventana se detiene el programa
		setSize(450,350); //seteamos el tamaño de la ventana
		setLocationRelativeTo(null); //centra la ventana en la pantalla
		setLayout(null); //elimina toda plantilla
		setResizable(false); // evita modificar el tamaño de ventana
		setTitle("-Autos saliendo-"); //Titulo de la ventana
		setVisible(false); //hace visible la ventana	
	}
	
	public static void mostrarVentanaAutos() { //Metodo que es llamado en la clase RedDePetri, luego de actualizar marcado. Muestra en la ventana los autos que van saliendo.
		salidasTotal = Integer.toString(RedDePetri.salidasTotal);	
		Etiqueta6.setText(salidasTotal);
		entrada1 = Integer.toString(RedDePetri.entradaBarrera1);
		Etiqueta8.setText(entrada1);
		entrada2 = Integer.toString(RedDePetri.entradaBarrera2);
		Etiqueta10.setText(entrada2);
		entrada3 = Integer.toString(RedDePetri.entradaBarrera3);
		Etiqueta12.setText(entrada3);
		entradaTotal = Integer.toString(RedDePetri.entradasTotal);
		Etiqueta14.setText(entradaTotal);
		piso1 = Integer.toString(RedDePetri.estacionadosPiso1);
		Etiqueta16.setText(piso1);
		piso2 = Integer.toString(RedDePetri.estacionadosPiso2);
		Etiqueta18.setText(piso2);
		salida1 = Integer.toString(RedDePetri.salidaBarrera1);
		Etiqueta20.setText(salida1);
		salida2 = Integer.toString(RedDePetri.salidaBarrera2);
		Etiqueta22.setText(salida2);
		marcaPiso1 = Integer.toString(RedDePetri.marcadoPiso1);
		Etiqueta24.setText(marcaPiso1);
		marcaPiso2 = Integer.toString(RedDePetri.marcadoPiso2);
		Etiqueta26.setText(marcaPiso2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			//Realiza la Accion correspondiente al seleccionar cada opcion
			if(e.getSource().equals(opcion1)){
				politicas.prioridadesT = politicas.prioridadesT_a;
			}
			if(e.getSource().equals(opcion2)){
				politicas.prioridadesT = politicas.prioridadesT_b;
			}
			if(e.getSource().equals(botonComenzar)) {
				campo = campoAutos.getText();				//guardamos en la variable String llamada campo, la cantidad de autos seleccionados
				cantidadAutos = Integer.parseInt(campo);	//convertimos la variable anterior a una variable de tipo entero
			}
			if(e.getSource().equals(checkbox1)) {
				if(tildeLog == 0) {					//logica para la activacion y desactivacion del boton con tilde de Generacion del log
					tildeLog = 1;
				}
				else {
					tildeLog = 0;
				}
				if(tildeLog == 1) {
				generarLog = true;
				}
				if(tildeLog == 0) {
					generarLog = false;
				}
			}
			if(e.getSource().equals(checkbox2)) {
					if(tildeTiempo == 0) {			//logica para la activacion y desactivacion del boton con tilde referido a los tiempos
						tildeTiempo = 1;
					}
					else {
						tildeTiempo = 0;
					}
					if(tildeTiempo== 1) {
						activarTiempo = true;
					}
					if(tildeTiempo == 0) {
						activarTiempo = false;
					}
				}
	}
}