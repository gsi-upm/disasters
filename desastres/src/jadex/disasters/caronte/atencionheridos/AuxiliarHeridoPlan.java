package disasters.caronte.atencionheridos;

import disasters.*;
import disasters.caronte.*;
import java.util.*;

/**
 * Plan de recomendacion para atender a un herido.
 * 
 * @author Juan Luis Molina
 */
public class AuxiliarHeridoPlan extends CarontePlan{
	private final int MAX = 8;
	
	/**
	 * Cuerpo del plan AvisarAgentes.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		int idHerido = (Integer) getBeliefbase().getBelief("heridoActual").getFact();
		Integer[] epa = (Integer[]) getBeliefbase().getBeliefSet("epa").getFacts();
		HashMap<Integer,Integer> atendiendo = (HashMap<Integer,Integer>) getBeliefbase().getBelief("atendiendo").getFact();
		
		Disaster her = env.getEvent(idHerido);
		HashMap<Integer,Disaster> incendios = env.getEvents();
		
		if(incendios.containsKey(idHerido) == false){
			System.out.println("Mensaje de final de herido");
			enviarMensaje(Entorno.COORDINADOR, "finHerido", "fin", true);
			getBeliefbase().getBelief("numEpa").setFact(0);
			getBeliefbase().getBeliefSet("epa").removeFacts();
			getBeliefbase().getBelief("heridoActual").setFact(0);
		}else{
			if(atendiendo.keySet().containsAll(Arrays.asList(epa)) == false){
				for(Integer i : Arrays.asList(epa)){
					if(atendiendo.containsKey(i) == false){
						atendiendo.put(i, 0);
						env.printout("Auxiliar herido " + her.getName() + "(id:" + her.getId() + ") en la planta " +
								her.getFloor(), 1, i, true);
					}
				}
			}
			
			HashMap<Integer,Activity> actividades = env.getActivities();
			for(Integer i : actividades.keySet()){
				Activity act = actividades.get(i);
				if(act.getIdDisaster() == idHerido && atendiendo.containsKey(act.getIdUser())){
					atendiendo.put(act.getIdUser(), null);
					env.printout("--prueba-- Sigue con la actividad " + act.getType(), 1, act.getIdUser(), true);
				}
			}
			
			for(Integer i : atendiendo.keySet()){
				if(atendiendo.get(i) != null){
					if(atendiendo.get(i) == MAX){ // Si ha llegado al tope de veces sin avisar se elimina
						atendiendo.remove(i);
						env.printout("No es necesario que acudas al herido", 1, i, true);
						env.leaveResource(i);
						env.addInactiveResource(i);
						getBeliefbase().getBeliefSet("epa").removeFact(i);
					}else{
						atendiendo.put(i,atendiendo.get(i)+1); // Si no se va sumando una vez
					}
				}
			}
			
			getBeliefbase().getBelief("atendiendo").setFact(atendiendo);
			waitFor(2500);
		}
	}
}