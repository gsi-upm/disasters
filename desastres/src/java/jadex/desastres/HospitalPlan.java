package jadex.desastres;

import jadex.runtime.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Plan de la AMBULANCIA para llevar a la ambulancia al hospital.
 * 
 * @author Olimpia Hernandez
 * 
 */
public class HospitalPlan extends Plan {

	/**
	 * Cuerpo del plan.
	 */
	public void body() {
		// Obtenemos un objeto de la clase entorno para poder usar sus métodos.
		//System.out.println("** ambulance: Hello world!  :D ");
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		
		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospitalMadrid").getFact();
		
		
		// Posicion actual de la ambulancia
		Position pos = (Position)getBeliefbase().getBelief("pos").getFact();
		
		
		
		
	

	}

		
}


