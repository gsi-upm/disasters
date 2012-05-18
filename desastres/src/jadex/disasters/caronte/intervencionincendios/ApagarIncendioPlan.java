package disasters.caronte.intervencionincendios;

import disasters.*;
import disasters.caronte.*;
import java.util.*;

/**
 * Plan de recomendacion para apagar un incendio.
 * 
 * @author Juan Luis Molina
 */
public class ApagarIncendioPlan extends CarontePlan{
	private final int MAX = 8;
	
	/**
	 * Cuerpo del plan AvisarAgentes.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		int idIncendio = (Integer) getBeliefbase().getBelief("incendio_actual").getFact();
		List<Integer> epi = Arrays.asList((Integer[]) getBeliefbase().getBeliefSet("epi").getFacts());
		List<Integer> esi = Arrays.asList((Integer[]) getBeliefbase().getBeliefSet("esi").getFacts());
		HashMap<Integer,Integer> apagando = (HashMap<Integer,Integer>) getBeliefbase().getBelief("apagando").getFact();
		
		List<Integer> equipos = epi;
		equipos.addAll(esi);
		
		Disaster des = env.getEvent(idIncendio);
		HashMap<Integer,Disaster> incendios = env.getEvents();
		
		if(incendios.containsKey(idIncendio) == false){
			System.out.println("Mensaje de final de incendio");
			enviarMensaje(Entorno.COORDINADOR, "fin_incendio", "fin", true);
			getBeliefbase().getBelief("numero_epi").setFact(0);
			getBeliefbase().getBelief("numero_esi").setFact(0);
			getBeliefbase().getBeliefSet("epi").removeFacts();
			getBeliefbase().getBeliefSet("esi").removeFacts();
			getBeliefbase().getBelief("incendio_actual").setFact(0);
		}else{
			if(apagando.keySet().containsAll(equipos) == false){
				for(Integer i : equipos){
					if(apagando.containsKey(i) == false){
						apagando.put(i, 0);
						env.printout("Atiende incendio " + des.getName() + "(id:" + des.getId() + ") en la planta " +
								des.getFloor(), 1, i, true);
					}
				}
			}
			
			HashMap<Integer,Activity> actividades = env.getActivities();
			for(Integer i : actividades.keySet()){
				Activity act = actividades.get(i);
				if(act.getIdDisaster() == idIncendio && apagando.containsKey(act.getIdUser())){
					apagando.put(act.getIdUser(), null);
					env.printout("--prueba-- Sigue con la actividad " + act.getType(), 1, act.getIdUser(), true);
				}
			}
			
			for(Integer i : apagando.keySet()){
				if(apagando.get(i) != null){
					if(apagando.get(i) == MAX){ // Si ha llegado al tope de veces sin avisar se elimina
						apagando.remove(i);
						env.printout("No es necesario que acudas al indencio", 1, i, true);
						env.leaveResource(i);
						env.addInactiveResource(i);
						if(epi.contains(i)){
							getBeliefbase().getBeliefSet("epi").removeFact(i);
						}else{
							getBeliefbase().getBeliefSet("esi").removeFact(i);
						}
						
					}else{
						apagando.put(i,apagando.get(i)+1); // Si no se va sumando una vez
					}
				}
			}
			
			getBeliefbase().getBelief("apagando").setFact(apagando);
			waitFor(2500);
		}
	}
}