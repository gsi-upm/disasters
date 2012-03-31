package disasters.caronte.coordinador;

import disasters.*;
import disasters.caronte.Entorno;
import jadex.bdi.runtime.IGoal;

/**
 * Plan para avisar a los agentes.
 * 
 * @author Juan Luis Molina
 */
public class AvisarAgentesPlan extends EnviarMensajePlan{
	/**
	 * Cuerpo del plan AvisarAgentes.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		System.out.println("Avisando agentes");
		enviarMensaje(Entorno.INTERVENCION_INCENDIOS, "atenderIncendio", String.valueOf(env.getTablon()));
		// enviarMensaje(Entorno.ATENCION_HERIDOS, "atenderHerido", String.valueOf(env.getTablon()));
	}
}