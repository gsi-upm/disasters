package jadex.desastres.disasters.sanitarios.grupoSanitarioOperativo;

import jadex.bdi.runtime.*;
import jadex.base.fipa.SFipa;
import jadex.desastres.Disaster;
import jadex.desastres.Environment;
import jadex.desastres.People;
import jadex.desastres.Position;

/**
 * Siguiendo los procesos de prioridad que marque el triage
 * 
 * @author Juan Luis Molina
 */
public class ReanimacionPlan extends Plan {
	
	/**
	 * Cuerpo del plan
	 */
	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar susmetodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		// Posicion actual que le permite recoger al herido.
		Position posicionActual = (Position) getBeliefbase().getBelief("pos").getFact();
		// Posicion del hospital que le corresponde
		Position posicionHospital = (Position) getBeliefbase().getBelief("hospitalMadrid").getFact();

		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);
		
		People herido = null;
		if (des.getSlight() != null){
			herido = des.getSlight();
		}else if( des.getSerious() != null){
			herido = des.getSerious();
		}else if(des.getDead() != null){
			herido = des.getDead();
		}

		
	}
}