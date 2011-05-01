package jadex.desastres.disasters.sanitarios.grupoSanitarioOperativo;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Primeros auxilios sobre aquellas victimas que necesiten de una accion inmediata antes de poder ser trasladados al Area de Rescate
 * 
 * @author Juan Luis Molina
 */
public class PrimerosAuxiliosPlan extends EnviarMensajePlan {
	
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

		System.out.println("** Grupo sanitario: realizando primeros auxilios");

		People herido = null;
		if (des.getSlight() != null){
			herido = des.getSlight();
		}else if( des.getSerious() != null){
			herido = des.getSerious();
		}else if(des.getDead() != null){
			herido = des.getDead();
		}
		
		if(herido.getType().equals("serious")){
			herido.setType("slight");
		}

		System.out.println("** Grupo sanitario: heridos estabilizados");
		enviarMensaje("medicoCACH","triage","go");
	}
}
