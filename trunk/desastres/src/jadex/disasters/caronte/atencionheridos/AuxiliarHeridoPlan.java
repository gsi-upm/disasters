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
		int idHerido = (Integer) getBeliefbase().getBelief("herido_actual").getFact();
		String tipoEmergencia = (String) getBeliefbase().getBelief("tipo_emergencia").getFact();
		Integer[] epa = (Integer[]) getBeliefbase().getBeliefSet("epa").getFacts();
		HashMap<Integer,Integer> atendiendo = (HashMap<Integer,Integer>) getBeliefbase().getBelief("atendiendo").getFact();
		
		if(tipoEmergencia.equals("incendio")){
			boolean hayHerido = false;
			Association[] asociaciones = env.getAssociationsArray();
			for(Association i : asociaciones){
				if(i.getIdDisaster() == idHerido){
					idHerido = i.getIdInjured();
					hayHerido = true;
					break;
				}
			}
			
			if(hayHerido == false){
				idHerido = 0;
			}
			getBeliefbase().getBelief("herido_actual").setFact(idHerido);
			getBeliefbase().getBelief("tipo_emergencia").setFact("herido");
		}
		
		People her = env.getPeople(idHerido);
		HashMap<Integer,People> heridos = env.getPeople();
		
		if(heridos.containsKey(idHerido) == false){
			System.out.println("Mensaje de final de herido");
			enviarMensaje(Entorno.COORDINADOR, "fin_herido", "fin", true);
			getBeliefbase().getBelief("numero_epa").setFact(0);
			getBeliefbase().getBeliefSet("epa").removeFacts();
			getBeliefbase().getBelief("herido_actual").setFact(0);
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