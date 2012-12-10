package disasters.caronte;

import jadex.bdi.runtime.Plan;

/**
 * Elimina un objeto del entorno.
 * 
 * @author Juan Luis Molina Nogales
 */
public class EliminarPlan extends Plan{
	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		String tipo = (String)getParameter("tipo").getValue();
		
		env.removeWorldObject(tipo, getComponentName());
	}
}