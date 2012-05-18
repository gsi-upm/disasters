package disasters.caronte.coordinador;

import disasters.Association;
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
		int idEmergencia = ((Integer) getBeliefbase().getBelief("emergencia_actual").getFact());
		String tipoEmergencia = ((String) getBeliefbase().getBelief("tipo_emergencia").getFact());
		System.out.println("Avisando agentes");
		if(tipoEmergencia.equals("incendio")){
			Incendio inc = new Incendio(env.getEvent(idEmergencia), 0);
			System.out.println("Hay un incendio...");
			enviarObjeto(Entorno.INTERVENCION_INCENDIOS, "atender_incendio", inc, true);
			if(env.getEvent(idEmergencia).hasInjured()){
				System.out.println("con heridos!!");
				enviarObjeto(Entorno.ATENCION_HERIDOS, "atender_herido", inc, true);
			}
		}else if(tipoEmergencia.equals("herido")){
			Herido herido = new Herido(env.getPeople(idEmergencia));
			enviarObjeto(Entorno.ATENCION_HERIDOS, "atender_herido", herido, true);
		}
	}
}