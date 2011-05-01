package jadex.desastres.disasters.sanitarios.ambulancia;

import jadex.desastres.ambulance.*;
import jadex.bdi.runtime.*;
import jadex.desastres.Environment;
import jadex.desastres.Position;
import jadex.desastres.WorldObject;

/**
 * Plan de la AMBULANCIA para llevar a la ambulancia al hospital.
 *
 * @author Olimpia Hernandez y Juan Luis Molina Nogales
 *
 */
public class HospitalPlan extends Plan {

	/**
	 * Cuerpo del plan.
	 */
	public void body() {
		// Obtenemos un objeto de la clase entorno para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospitalMadrid").getFact();

		// Posicion actual de la ambulancia
		//Position pos = (Position) getBeliefbase().getBelief("pos").getFact();
		WorldObject agente = (WorldObject)getBeliefbase().getBelief("agente").getFact();
		Position pos = agente.getPosition();

		waitFor(1000);
	}
}
