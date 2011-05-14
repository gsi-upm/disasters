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
		
		int id = env.getTablon();
		String idDes = new Integer(id).toString();
		
		Environment.printout("OO coordinador: Avisando al gerocultor...",0);
		String resultado2 = enviarMensaje("gerocultor", "aviso_geriatrico", idDes);

		Environment.printout("OO coordinador: Avisando al enfermero...",0);
		String resultado1 = enviarMensaje("nurse", "aviso_geriatrico", idDes);

		String emergencia = esperarYEnviarRespuesta("estadoEmergencia","ok");
		Environment.printout("OO coordinador: estado de la emergencia: " + emergencia, 0);

		String heridos = esperarYEnviarRespuesta("estadoHeridos","ok");
		if (!heridos.equals("null")) {
			Environment.printout("OO coordinador: estado del herido: " + heridos, 0);
		} else {
			Environment.printout("OO coordinador: La emergencia no tiene heridos!!",0);
		}

		if (!emergencia.equals("small") || (!heridos.equals("null") && !heridos.equals("slight"))) {
			Environment.printout("OO coordinador: Avisando la central... (en espera)...",0);
			String resultado = enviarMensaje("centralEmergencias", "aviso_geriatrico", idDes);
			Environment.printout("OO coordinador: Respuesta recibida de central: " + resultado,0);
		}

		//Creamos un nuevo objetivo.
		IGoal esperaSolucion = createGoal("esperarSolucion");
		dispatchSubgoalAndWait(esperaSolucion);
	}
}
