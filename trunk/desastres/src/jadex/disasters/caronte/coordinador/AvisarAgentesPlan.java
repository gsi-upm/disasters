package disasters.caronte.coordinador;

import disasters.caronte.*;
import disasters.caronte.ontology.*;

/**
 * Plan para avisar a los agentes.
 * 
 * @author Juan Luis Molina
 */
public class AvisarAgentesPlan extends CarontePlan{
	/**
	 * Cuerpo del plan AvisarAgentes.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		int idEmergencia = ((Integer) getBeliefbase().getBelief("emergenciaActual").getFact()).intValue();
		System.out.println("Avisando agentes");
		Incendio inc = new Incendio(env.getEvent(idEmergencia), 0);
		enviarObjeto(Entorno.INTERVENCION_INCENDIOS, "atenderIncendio", inc, true);
		// Herido herido = new Herido(env.getPeople(idEmergencia));
		// enviarMensaje(Entorno.ATENCION_HERIDOS, "atenderHerido", msg, true);
	}
}