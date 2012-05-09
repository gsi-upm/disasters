package disasters.caronte.atencionheridos;

import disasters.*;
import disasters.caronte.*;

/**
 * Plan para formar el equipo de primeros auxilios.
 * 
 * @author Juan Luis Molina
 */
public class FormarEquipoAuxilioPlan extends CarontePlan{
	/**
	 * Cuerpo del plan AvisarAgentes.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		int idHerido = (Integer) getBeliefbase().getBelief("heridoActual").getFact();
		int numEpa = (Integer) getBeliefbase().getBelief("numEpa").getFact();
		Integer[] epa = (Integer[]) getBeliefbase().getBeliefSet("epa").getFacts();
		
		Disaster her = env.getEvent(idHerido);
		
		Resource[] agentes = env.getFreeResources();
		if(agentes.length > 0){
			Resource atiende = agentes[0];
			for(int i = 1; i < agentes.length; i++){
				double distAtiende = distancia(atiende.getLatitud(), atiende.getLongitud(), her.getLatitud(), her.getLongitud());
				double distAgente = distancia(agentes[i].getLatitud(), agentes[i].getLongitud(), her.getLatitud(), her.getLongitud());
				boolean mejorPlanta = mejorPlanta(agentes[i].getFloor(), atiende.getFloor(), her.getFloor());
				if((agentes[i].getType().equals("nurse") && distAgente < distAtiende && mejorPlanta) ||
						(agentes[i].getType().equals("nurse") == false && atiende.getType().equals("nurse") == false && distAgente < distAtiende && mejorPlanta)){
					atiende = agentes[i];
				}
			}
			
			env.useResource(atiende.getId(), "EPA");
			getBeliefbase().getBeliefSet("epa").addFact(atiende.getId());
			
			
		}else{
			waitFor(2500);
		}
	}
}