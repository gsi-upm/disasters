package jadex.desastres.caronte.coordinadorEmergencias;

import jadex.bdi.runtime.*;
import jadex.desastres.*;

/**
 *
 * @author Juan Luis Molina
 */
public class AvisarAgentesPlan extends EnviarMensajePlan {

	public void body(){
		// Obtenemos un objeto de la clase Environment para poder usar sus metodos
		Environment env = (Environment) getBeliefbase().getBelief("env").getFact();
		
		int idDes = env.getTablon();
		Disaster des = env.getEvent(idDes);
		
		People herido = null;

		if (des.getSlight() != null) {
			herido = des.getSlight();
		}
		if (des.getSerious() != null) {
			herido = des.getSerious();
		}
		if (des.getDead() != null) {
			herido = des.getDead();
		}

		if (herido != null) {
			Environment.printout("OO coordinador: He encontrado un herido cuya id es: " + herido.getId() + "!!",0);
			herido.setAtendido(true);
		} else {
			Environment.printout("OO coordinador: La emergencia no tiene heridos!!",0);
		}

		Environment.printout("OO coordinador: Avisando al enfermero...",0);
		String resultado1 = enviarMensaje("nurse", "aviso_geriatrico", "go");
		Environment.printout("OO coordinador: Avisando al gerocultor...",0);
		String resultado2 = enviarMensaje("gerocultor", "aviso_geriatrico", "go");

		if ((des.getSize().equals("small") == false) ||
				((herido != null) && (herido.getType().equals("slight") == false))) {
			Environment.printout("OO coordinador: Avisando la central... (en espera)...",0);
			String resultado = enviarMensaje("centralEmergencias", "aviso_geriatrico", "go");
			Environment.printout("OO coordinador: Respuesta recibida de central: " + resultado,0);
		}

		//Creamos un nuevo objetivo.
		IGoal esperaSolucion = createGoal("esperarSolucion");
		dispatchSubgoalAndWait(esperaSolucion);
	}
}
