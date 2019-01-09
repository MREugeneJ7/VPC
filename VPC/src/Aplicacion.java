import java.util.logging.Logger;

/**
 * VPC Practica imagen
 * Aplicacion.java
 * Purpose: Clase que contiene el metodo main y enlaza la interfaz grafica con el algoritmo.
 *
 * @author Eugenio José González Luis
 * @version 0.1 04/10/2018
 */
public class Aplicacion {
	
	public static final Logger logger = Logger.getLogger("log");
	
	/**
	 * Constructor de la clase aplicación
	 */
	public Aplicacion() {
		new VentanaEntorno(new Entorno());
	}
	public static void main(String args[]) {
		new Aplicacion();
	}

}