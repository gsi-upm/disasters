package jadex.desastres.sanitarios.medicoCACH;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 * Primeros auxilios sobre aquellas victimas que necesiten de una accion inmediata.
 * En cuanto se estabilicen, se debe producir su traslado inmediato
 *
 * @author Juan Luis Molina
 */
public class EstabilizacionVictimasPlan extends EnviarMensajePlan {
	
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
		System.out.println("** Medico CACH: victimas estabilizadas");
		
		System.out.println("** Medico CACH: avisando a la ambulancia");
		enviarMensaje("ambulancia","traslado","go");
	}
}
