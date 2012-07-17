package disasters.caronte.entorno;

import disasters.*;
import disasters.caronte.*;
import java.sql.Timestamp;
import java.util.*;
import org.json.me.*;

/**
 * Clase para modelar el entorno, proporcionando metodos para interactuar con el.
 * 
 * @author aebeda
 * @author Juan Luis Molina
 */
public class ActualizarPlan extends CarontePlan{
	/**
	 * Cuerpo del plan.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		String ahora = (String) getBeliefbase().getBelief("fecha").getFact();
		try{
			String eventos = Connection.connect(Entorno.URL + "events/modified/" + ahora);
			JSONArray desastres = new JSONArray(eventos);

			String victimas = Connection.connect(Entorno.URL + "people/modified/" + ahora);
			JSONArray personas = new JSONArray(victimas);
			
			String recursos = Connection.connect(Entorno.URL + "resources/modified/" + ahora);
			JSONArray usuarios = new JSONArray(recursos);
			
			String asocs = Connection.connect(Entorno.URL + "associations/modified/" + ahora);
			JSONArray asociaciones = new JSONArray(asocs);
			
			String actis = Connection.connect(Entorno.URL + "activities/modified/" + ahora);
			JSONArray actividades = new JSONArray(actis);

			getBeliefbase().getBelief("fecha").setFact(new Timestamp(new Date().getTime()).toString());

			// Por cada desastre:
			for(int i = 0; i < desastres.length(); i++){
				JSONObject instancia = desastres.getJSONObject(i);
				Disaster nuevo = new Disaster(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("address"),
					instancia.getInt("floor"),
					instancia.getString("size"),
					instancia.getString("traffic"),
					instancia.getString("state"));
				
				// si ya existia actualizo el desastre existente
				if(env.getEvents().containsKey(nuevo.getId())){
					// si se ha eliminado lo borro directamente
					if(nuevo.getState().equals("erased")){
						env.removeEvent(nuevo.getId());
						System.out.println("- Emergencia eliminada: " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
					}else{
						env.putEvent(nuevo.getId(), nuevo);
						System.out.println("- Emergencia actualizada... " + nuevo.getName() + " - " + nuevo.getState() + " (id:" + nuevo.getId() + ")");
					}
				}else{
					env.putEvent(nuevo.getId(), nuevo);
					System.out.println("- Nueva emergencia: " + nuevo.getType() + " - " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
				}
			}
			// Por cada herido:
			for(int i = 0; i < personas.length(); i++){
				JSONObject instancia = personas.getJSONObject(i);
				People nuevo = new People(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getInt("quantity"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("adreess"),
					instancia.getInt("floor"),
					instancia.getString("size"),
					instancia.getString("traffic"),
					instancia.getString("state"),
					instancia.getInt("idAssigned"));
				
				if(env.getPeople().containsKey(nuevo.getId())){
					if(nuevo.getState().equals("erased")){
						env.removePeople(nuevo.getId());
						System.out.println("- Herido " + nuevo.getName() + " curado (id:" + nuevo.getId() + ")");
					}else{
						People antiguo = env.getPeople(nuevo.getId());
						env.putPeople(nuevo.getId(), nuevo);
						System.out.println("- Herido " + nuevo.getName() + " actualizado con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
						
						if(nuevo.getType().equals(antiguo.getType()) == false){
							ArrayList<Disaster> emergencias = new ArrayList<Disaster>();
							for(int j = 0; j < env.getAssociations().size(); j++){
								if(env.getAssociation(j).getIdInjured() == nuevo.getId()){
									emergencias.add(env.getEvent(env.getAssociation(j).getIdDisaster()));
								}
							}
							if(emergencias.isEmpty() == false){
								for(int j = 0; j < emergencias.size(); j++){
									if(antiguo.getType().equals("slight")){
										emergencias.get(j).removeSlight(antiguo);
									}else if(antiguo.getType().equals("serious")){
										emergencias.get(j).removeSerious(antiguo);
									}else if(antiguo.getType().equals("dead")){
										emergencias.get(j).removeDead(antiguo);
									}else if(antiguo.getType().equals("trapped")){
										emergencias.get(j).removeTrapped(antiguo);
									}
									if(nuevo.getType().equals("slight")){
										emergencias.get(j).addSlight(nuevo);
									}else if(nuevo.getType().equals("serious")){
										emergencias.get(j).addSerious(nuevo);
									}else if(nuevo.getType().equals("dead")){
										emergencias.get(j).addDead(nuevo);
									}else if(nuevo.getType().equals("trapped")){
										emergencias.get(j).addTrapped(nuevo);
									}
									env.putEvent(emergencias.get(j).getId(), emergencias.get(j));
								}
							}
						}
					}
				}else{
					env.putPeople(nuevo.getId(), nuevo);
					System.out.println("- Nuevo herido " + nuevo.getName() + " con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
				}
			}
			// Por cada usuario:
			for(int i = 0; i < usuarios.length(); i++){
				JSONObject instancia = usuarios.getJSONObject(i);
				Resource nuevo = new Resource(
					instancia.getInt("id"),
					instancia.getString("type"),
					instancia.getString("name"),
					instancia.getString("info"),
					instancia.getString("description"),
					new Double(instancia.getString("latitud")),
					new Double(instancia.getString("longitud")),
					instancia.getString("address"),
					instancia.getInt("floor"),
					instancia.getString("state"),
					instancia.getInt("idAssigned"));
				
				if(env.getResources().containsKey(nuevo.getId())){
					if(nuevo.getState().equals("erased")){
						env.removeResource(nuevo.getId());
						System.out.println("- El usuario " + nuevo.getName() + " ha cerrado sesion");
					}else{
						env.putResource(nuevo.getId(), nuevo);
						System.out.println("- El usuario " + nuevo.getName() + " ha actualizado sus datos");
					}
				}else{
					env.putResource(nuevo.getId(), nuevo);
					System.out.println("- Nuevo usuario: " + nuevo.getName() + " - " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
				}
			}
			// Por cada asociacion:
			for(int i = 0; i < asociaciones.length(); i++){
				JSONObject instancia = asociaciones.getJSONObject(i);
				Association nuevo = new Association(
					instancia.getInt("id"),
					instancia.getInt("idInjured"),
					instancia.getInt("idDisaster"),
					instancia.getString("state"));
				
				People herido = env.getPeople(nuevo.getIdInjured());
				Disaster emergencia = env.getEvent(nuevo.getIdDisaster());
				if(nuevo.getState().equals("erased")){
					env.removeAssociation(nuevo.getId());
					System.out.println("- Asociacion eliminada entre herido '" + herido.getName() + "' y emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
					if(emergencia != null){
						if(herido.getType().equals("slight")){
							emergencia.removeSlight(herido);
						}else if(herido.getType().equals("serious")){
							emergencia.removeSerious(herido);
						}else if(herido.getType().equals("dead")){
							emergencia.removeDead(herido);
						}else if(herido.getType().equals("trapped")){
							emergencia.removeTrapped(herido);
						}
						env.putEvent(emergencia.getId(), emergencia);
					}
				}else{
					env.putAssociation(nuevo.getId(), nuevo);
					System.out.println("- Nueva asociacion: herido '" + herido.getName() + "' con emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
					if(herido.getType().equals("slight")){
						emergencia.addSlight(herido);
					}else if(herido.getType().equals("serious")){
						emergencia.addSerious(herido);
					}else if(herido.getType().equals("dead")){
						emergencia.addDead(herido);
					}else if(herido.getType().equals("trapped")){
						emergencia.addTrapped(herido);
					}
					env.putEvent(emergencia.getId(), emergencia);
				}
			}
			// Por cada actividad:
			for(int i = 0; i < actividades.length(); i++){
				JSONObject instancia = actividades.getJSONObject(i);
				Activity nuevo = new Activity(
					instancia.getInt("id"),
					instancia.getInt("idUser"),
					instancia.getInt("idDisaster"),
					instancia.getString("type"),
					instancia.getString("state"));
				
				String nombre = "";
				if(env.getEvents().containsKey(nuevo.getIdDisaster())){
					nombre = "emergencia '" + env.getEvent(nuevo.getIdDisaster()).getName() + "'";
				}else if(env.getPeople().containsKey(nuevo.getIdDisaster())){
					nombre = "herido '" + env.getPeople(nuevo.getIdDisaster()).getName() + "'";
				}
				
				if(env.getActivities().containsKey(nuevo.getId())){
					env.removeActivity(nuevo.getId());
					System.out.println("- Actividad '" + nuevo.getType() + "' terminada en " + nombre + " (id:" + nuevo.getId() + ")");
				}else{
					if(nuevo.getState().equals("erased")){
						env.putActivity(nuevo.getId(), nuevo);
						System.out.println("- Actividad '" + nuevo.getType() + "' realizada en 'id:" + nuevo.getIdDisaster() + "' (id:" + nuevo.getId() + ")");
					}else{
						env.putActivity(nuevo.getId(), nuevo);
						System.out.println("- Nueva actividad '" + nuevo.getType() + "' en " + nombre + " (id:" + nuevo.getId() + ")");
					}
				}
			}
		}catch(JSONException e){
			System.out.println("Error con JSON: " + e);
		}
	}
}