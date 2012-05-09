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
		int idIncendio = (Integer) getBeliefbase().getBelief("incendioActual").getFact();
		int numEpi = (Integer) getBeliefbase().getBelief("numEpi").getFact();
		//int numEsi = (Integer) getBeliefbase().getBelief("numEsi").getFact();
		Integer[] epi = (Integer[]) getBeliefbase().getBeliefSet("epi").getFacts();
		//Integer[] esi = (Integer[]) getBeliefbase().getBeliefSet("esi").getFacts();
		
		Disaster des = env.getEvent(idIncendio);
		
		Resource[] agentes = env.getFreeResources();
		if(agentes.length > 0){
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
			
			if(numEpi - epi.length > 0){
				env.useResource(atiende.getId(), "EPI");
				getBeliefbase().getBeliefSet("epi").addFact(atiende.getId());
			}else{
				env.useResource(atiende.getId(), "ESI");
				getBeliefbase().getBeliefSet("esi").addFact(atiende.getId());
			}
			
		}else{
			waitFor(2500);
		}
	}
}