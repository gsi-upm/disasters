package jadex.desastres;
import java.util.Iterator;
import java.util.Map;

import jadex.runtime.*;

/**
 * Plan de BOMBEROS para llevar al bombero a la estación de bomberos.
 * 
 * @author Iván Rojo
 * 
 */
public class EstacionBomberosPlan extends Plan {

	/**
	 * Cuerpo del plan.
	 */
	public void body() {
		// Obtenemos un objeto de la clase entorno para poder usar sus métodos.
		//System.out.println("** firemen: Hello world!  :D ");
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		// Posicion actual del bombero
		Position pos = (Position) getBeliefbase().getBelief("pos").getFact();

		// Identifica la posición del desastre
		Position destino = null;

		
		waitFor(500);
		
	}

}