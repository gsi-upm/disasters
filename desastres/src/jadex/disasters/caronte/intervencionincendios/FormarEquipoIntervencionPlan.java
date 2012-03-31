package disasters.caronte.intervencionincendios;

import disasters.*;
import disasters.caronte.*;

/**
 * Plan para formar el equipo de intervencion.
 * 
 * @author Juan Luis Molina
 */
public class FormarEquipoIntervencionPlan extends CarontePlan{
	/**
	 * Cuerpo del plan AvisarAgentes.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		System.out.println("Formando equipos");
		String recibido = enviarRespuesta("ack_atenderIncendio", "OK");
		Disaster des = env.getEvent(env.getTablon());
		
		Resource[] agentes = null;
		while(agentes == null){
			agentes = env.getRecursos();
			if(agentes == null){
				waitFor(2000);
			}
		}
		
		Resource atiende = agentes[0];
		for(int i = 1; i < agentes.length; i++){
			double distAtiende = distancia(atiende.getLatitud(), atiende.getLongitud(), des.getLatitud(), des.getLongitud());
			double distAgente = distancia(agentes[i].getLatitud(), agentes[i].getLongitud(), des.getLatitud(), des.getLongitud());
			boolean mejorPlanta = mejorPlanta(agentes[i].getFloor(), atiende.getFloor(), des.getFloor());
			if((agentes[i].getType().equals("gerocultor") && distAgente < distAtiende && mejorPlanta) ||
					(agentes[i].getType().equals("gerocultor") == false && atiende.getType().equals("gerocultor") == false && distAgente < distAtiende && mejorPlanta)){
				atiende = agentes[i];
			}
		}
		
		env.printout("Atiende incendio " + des.getName() + "(id:" + des.getId() + ") en la planta " + des.getFloor(), 1, atiende.getId());
		
	}
}