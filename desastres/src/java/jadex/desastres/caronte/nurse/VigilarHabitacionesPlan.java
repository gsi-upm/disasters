package jadex.desastres.caronte.nurse;

import jadex.bdi.runtime.Plan;
import jadex.desastres.Environment;
import jadex.desastres.Position;

/**
 * Plan de ENFERMERO
 * 
 * @author Juan Luis Molina
 * 
 */
public class VigilarHabitacionesPlan extends Plan {

	/**
	 * Cuerpo del plan
	 */
	public void body() {
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();

		// Posicion de la residencia que le corresponde
		Position residencia = (Position) getBeliefbase().getBelief("residencia").getFact();

		waitFor(1000);
	}
}
