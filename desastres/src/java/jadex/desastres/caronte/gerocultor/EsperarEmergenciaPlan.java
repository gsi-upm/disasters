package jadex.desastres.caronte.gerocultor;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Plan de GEROCULTOR
 * 
 * @author Lorena Lopez Lebon
 * 
 */
public class EsperarEmergenciaPlan extends Plan {

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
