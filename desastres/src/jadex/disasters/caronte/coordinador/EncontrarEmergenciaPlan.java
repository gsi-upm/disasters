package disasters.caronte.coordinador;

import disasters.*;
import disasters.caronte.*;
import jadex.bdi.runtime.IGoal;
import java.sql.Timestamp;
import java.util.*;
import org.json.me.*;

/**
 * Plan del coordinador de emergencias para encontrar emergencias.
 *
 * @author Juan Luis Molina
 *
 */
public class EncontrarEmergenciaPlan extends CarontePlan{
	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		Disaster des = buscarEventos(env);
		
		if(des != null){
			env.setTablonEventos(des.getId());
			getBeliefbase().getBelief("emergencia_actual").setFact(des.getId());
			getBeliefbase().getBelief("tipo_emergencia").setFact("incendio");
			
			Resource directorActuacion = directorActuacion(env, des);
			int idDA = directorActuacion.getId();
			
			env.printout("Confirmar emergencia '" + des.getName() + "' (id:" + des.getId() + ") en la planta " +
					des.getFloor() + " creada por (id:" + des.getUser() + ")", 0, idDA, true);
			String fecha = new Timestamp(new Date().getTime()).toString();
			boolean confirmado = false;
			boolean cancelado = false;
		
			try{
				JSONArray respuesta = new JSONArray();
				int contador = 0;
				while(confirmado == false && cancelado == false){
					String respuestaAux = Connection.connect(Entorno.URL + "mensajes/coor/" + idDA + "/" + fecha);
					respuesta = new JSONArray(respuestaAux);
					if(respuesta.isNull(0) == false){
						confirmado = true;
					}else{
						contador++;
						waitFor(2500);
					}
					
					if(contador == 8){ // Mucho tiempo sin contestar
						cancelado = true;
						env.printout("CANCEL", 0, idDA, true);
						env.leaveResource(idDA);
						env.addInactiveResource(idDA);
						getBeliefbase().getBelief("id_director_actuacion").setFact(0);
						env.removeTablonEventos(des.getId());
						getBeliefbase().getBelief("emergencia_actual").setFact(0);
					}
				}
			
				if(confirmado){
					String accion = respuesta.getJSONObject(0).getString("mensaje");
					if(accion.equals("OK")){
						IGoal avisarAgentes = createGoal("avisar_agentes");
						dispatchSubgoalAndWait(avisarAgentes);
					}else if(accion.equals("NO")){
						env.leaveResource(idDA);
						env.addInactiveResource(idDA);
						getBeliefbase().getBelief("id_director_actuacion").setFact(0);
						env.removeTablonEventos(des.getId());
						getBeliefbase().getBelief("emergencia_actual").setFact(0);
					}
				}
			}catch(JSONException ex){
				System.out.println(ex);
			}
		}else{
			People her = buscarHeridos(env);
			if(her != null){
				env.setTablonEventos(her.getId());
				getBeliefbase().getBelief("emergencia_actual").setFact(her.getId());
				getBeliefbase().getBelief("tipo_emergencia").setFact("herido");
			}
		}
		waitFor(2500);
	}
	
	/**
	 * Busca un evento para atender entre las emergencias y heridos.
	 * 
	 * @param env Entorno
	 */
	private Disaster buscarEventos(Entorno env){
		Disaster atender = null;
		Disaster[] emergencias = env.getEventosSin();
		
		if(emergencias.length > 0){
			atender = emergencias[0];
			Association[] asociaciones = env.getAssociationsArray();
			
			for(int i = 1; i < emergencias.length; i++){
				if(masGrave(emergencias[i], atender)){ // la nueva tiene mas gravedad
					atender = emergencias[i];
				}else if(emergencias[i].getSize().equals(atender.getSize())){
					int num1 = 0;
					int num2 = 0;
					
					for(int j = 0; j < asociaciones.length; j++){
						if(asociaciones[j].getIdDisaster() == emergencias[i].getId()){
							num1++;
						}
						if(asociaciones[j].getIdDisaster() == atender.getId()){
							num2++;
						}
					}
					if(num1 > num2){ // la nueva tiene mas heridos
						atender = emergencias[i];
					}
				}
			}
		}
		
		return atender;
	}
	
	/**
	 * Busca un evento para atender entre las emergencias y heridos.
	 * 
	 * @param env Entorno
	 */
	private People buscarHeridos(Entorno env){
		People atender = null;
		People[] heridos = env.getHeridosSin();
		
		if(heridos.length > 0){
			atender = heridos[0];
			
			for(int i = 1; i < heridos.length; i++){
				if(masGrave(heridos[i], atender)){ // el nuevo tiene mas gravedad
					atender = heridos[i];
				}else if(heridos[i].getType().equals(atender.getType())){
					if(heridos[i].getQuantity() > atender.getQuantity()){ // el nuevo tiene mas heridos
						atender = heridos[i];
					}
				}
			}
		}
		
		return atender;
	}
	
	/**
	 * Comprueba si un nuevo desastre es mas grave que el anterior guardado.
	 * 
	 * @param compruebo Desastre que compruebo
	 * @param atendiendo Desastre que hasta ahora era el mas grave
	 * @return True si es mas grave 
	 */
	private boolean masGrave(Disaster compruebo, Disaster atendiendo){
		boolean masGrave = false;
		String size1 = compruebo.getSize();
		String size2 = atendiendo.getSize();
		if((size1.equals("huge") && (size2.equals("big") || size2.equals("medium") || size2.equals("small"))) ||
				(size1.equals("big") && (size2.equals("medium") || size2.equals("small"))) ||
				(size1.equals("medium") && size2.equals("small"))){
			masGrave = true;
		}
		return masGrave;
	}
	
	/**
	 * Comprueba si un nuevo herido es mas grave que el anterior guardado.
	 * 
	 * @param compruebo Heridos que compruebo
	 * @param atendiendo Heridos que hasta ahora era el mas grave
	 * @return True si es mas grave 
	 */
	private boolean masGrave(People compruebo, People atendiendo){
		boolean masGrave = false;
		String type1 = compruebo.getType();
		String type2 = atendiendo.getType();
		if((type1.equals("serious") && (type2.equals("slight") || type2.equals("healthy"))) ||
				((type1.equals("slight") && type2.equals("healthy")))){
			masGrave = true;
		}
		return masGrave;
	}
	
	/**
	 * Busca al director de actuacion.
	 * 
	 * @param env Entorno
	 * @param des Emergencia
	 * @return Director de actuacion
	 */
	private Resource directorActuacion(Entorno env, Disaster des){
		Resource directorActuacion = null;
		Resource[] agentes = env.getFreeResources();
		
		if(agentes.length > 0){
			directorActuacion = agentes[0];
			for(int i = 1; i < agentes.length; i++){
				double distDirector = distancia(directorActuacion.getLatitud(), directorActuacion.getLongitud(), des.getLatitud(), des.getLongitud());
				double distAgente = distancia(agentes[i].getLatitud(), agentes[i].getLongitud(), des.getLatitud(), des.getLongitud());
				boolean mejorPlanta = mejorPlanta(agentes[i].getFloor(), directorActuacion.getFloor(), des.getFloor());
				if((agentes[i].getType().equals("coordinator") && directorActuacion.getType().equals("coordinator") == false) ||
						(((agentes[i].getType().equals("coordinator") && directorActuacion.getType().equals("coordinator")) ||
						(agentes[i].getType().equals("coordinator") == false && directorActuacion.getType().equals("coordinator") == false)) &&
						distAgente < distDirector && mejorPlanta)){
					directorActuacion = agentes[i];
				}
			}
			env.useResource(directorActuacion.getId(), "DA");
			getBeliefbase().getBelief("id_director_actuacion").setFact(directorActuacion.getId());
		}
		
		return directorActuacion;
	}
}