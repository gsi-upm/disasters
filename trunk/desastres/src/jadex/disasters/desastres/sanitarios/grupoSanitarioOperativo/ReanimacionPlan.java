package disasters.desastres.sanitarios.grupoSanitarioOperativo;

import disasters.*;
import jadex.bdi.runtime.Plan;

/**
 * Siguiendo los procesos de prioridad que marque el triage
 * 
 * @author Juan Luis Molina
 */
public class ReanimacionPlan extends Plan{
	
	/**
	 * Cuerpo del plan
	 */
	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar susmetodos
		Environment env = (Environment)getBeliefbase().getBelief("env").getFact();

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);
		
		People herido = null;
		if(des.getSlight() != null){
			herido = des.getSlight();
		}else if(des.getSerious() != null){
			herido = des.getSerious();
		}else if(des.getDead() != null){
			herido = des.getDead();
		}
	}
}