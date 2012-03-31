package disasters.caronte.coordinador;

import disasters.*;
import disasters.caronte.*;
import jadex.bdi.runtime.IGoal;
import java.sql.Timestamp;
import java.util.Date;
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
		
		Disaster des = null;
		People[] per;
		while(des == null){
			des = buscarEventos(env);
		}
		env.setTablon(des.getId());
		
		Resource jefeIntervencion = jefeIntervencion(env, des);
		int idJI = jefeIntervencion.getId();
		getBeliefbase().getBelief("idJefeIntervencion").setFact(idJI);
		
		env.printout("Confirmar emergencia '" + des.getName() + "' (id:" + des.getId() + ") en la planta " + des.getFloor() + " creada por (id:" + des.getUser() + ")", 0, idJI);
		String fecha = new Timestamp(new Date().getTime()).toString();
		boolean confirmado = false;
		
		try{
			JSONArray respuesta = new JSONArray();
			while(confirmado == false){
				String respuestaAux = Connection.connect(Entorno.URL + "mensajes/coor/" + idJI + "/" + fecha);
				respuesta = new JSONArray(respuestaAux);
				if(respuesta.isNull(0) == false){
					confirmado = true;
				}else{
					waitFor(2000);
				}
			}
			
			String accion = respuesta.getJSONObject(0).getString("mensaje");
			if(accion.equals("OK")){
				IGoal avisarAgentes = createGoal("avisarAgentes");
				dispatchSubgoalAndWait(avisarAgentes);
			}
		}catch(JSONException ex){
			System.out.println(ex);
		}
	}
	
	/**
	 * Busca un evento para atender entre las emergencias y heridos.
	 * 
	 * @param env Entorno
	 */
	private Disaster buscarEventos(Entorno env){
		Disaster atender = null;
		Disaster[] emergencias = env.getEventos();
		
		if(emergencias.length > 0){
			atender = emergencias[0];
			People[] heridos = env.getHeridos();
			Association[] asociaciones = env.getAsociaciones();
			
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
	 * Busca al jefe de intervencion.
	 * 
	 * @param env Entorno
	 * @param des Emergencia
	 * @return Jefe de intervencion
	 */
	private Resource jefeIntervencion(Entorno env, Disaster des){
		Resource[] agentes = null;
		while(agentes == null){
			agentes = env.getRecursos();
			if(agentes == null){
				waitFor(2000);
			}
		}
		
		Resource jefeIntervencion = agentes[0];
		for(int i = 1; i < agentes.length; i++){
			double distJefe = distancia(jefeIntervencion.getLatitud(), jefeIntervencion.getLongitud(), des.getLatitud(), des.getLongitud());
			double distAgente = distancia(agentes[i].getLatitud(), agentes[i].getLongitud(), des.getLatitud(), des.getLongitud());
			boolean mejorPlanta = mejorPlanta(agentes[i].getFloor(), jefeIntervencion.getFloor(), des.getFloor());
			if((agentes[i].getType().equals("coordinator") && jefeIntervencion.getType().equals("coordinator") == false) ||
					(((agentes[i].getType().equals("coordinator") && jefeIntervencion.getType().equals("coordinator")) ||
					(agentes[i].getType().equals("coordinator") == false && jefeIntervencion.getType().equals("coordinator") == false)) &&
					distAgente < distJefe && mejorPlanta)){
				jefeIntervencion = agentes[i];
			}
		}
		return jefeIntervencion;
	}
}