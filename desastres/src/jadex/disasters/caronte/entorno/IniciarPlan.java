package disasters.caronte.entorno;

import disasters.*;
import disasters.caronte.*;
import java.sql.Timestamp;
import java.util.Date;
import org.json.me.*;

/**
 * Clase para modelar el entorno, proporcionando metodos para interactuar con &eacute;l.
 * 
 * @author aebeda
 * @author Juan Luis Molina Nogales
 */
public class IniciarPlan extends CarontePlan{
	/**
	 * Cuerpo del plan Iniciar.
	 */
	public void body(){
		Entorno env = (Entorno) getBeliefbase().getBelief("env").getFact();
		try{
			String eventos = Connection.connect(Entorno.URL + "events");
			JSONArray desastres = new JSONArray(eventos);

			String victimas = Connection.connect(Entorno.URL + "people");
			JSONArray personas = new JSONArray(victimas);
			
			String recursos = Connection.connect(Entorno.URL + "resources");
			JSONArray usuarios = new JSONArray(recursos);
			
			String asocs = Connection.connect(Entorno.URL + "associations");
			JSONArray asociaciones = new JSONArray(asocs);
			
			String actis = Connection.connect(Entorno.URL + "activities");
			JSONArray actividades = new JSONArray(actis);

			getBeliefbase().getBelief("fecha").setFact(new Timestamp(new Date().getTime()));

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
				System.out.println("- Nueva emergencia: " + nuevo.getType() + " - " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
				env.putEvent(nuevo.getId(), nuevo);
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
					instancia.getString("address"),
					instancia.getInt("floor"),
					instancia.getString("size"),
					instancia.getString("traffic"),
					instancia.getString("state"),
					instancia.getInt("idAssigned"));
				env.putPeople(nuevo.getId(), nuevo);
				if(nuevo.getType().equals("healthy")){
					System.out.println("- Nueva persona: " + nuevo.getName() + " (id:" + nuevo.getId() + ")");
				}else{
					System.out.println("- Nuevo herido: " + nuevo.getName() + " con estado " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
				}
			}
			// Por cada usuario logueado:
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
				env.putResource(nuevo.getId(), nuevo);
				System.out.println("- Nuevo usuario: " + nuevo.getName() + " - " + nuevo.getType() + " (id:" + nuevo.getId() + ")");
			}
			// Por cada asociacion:
			for(int i = 0; i < asociaciones.length(); i++){
				JSONObject instancia = asociaciones.getJSONObject(i);
				Association nuevo = new Association(
					instancia.getInt("id"),
					instancia.getInt("idInjured"),
					instancia.getInt("idDisaster"),
					instancia.getString("state"));
				env.putAssociation(nuevo.getId(), nuevo);
				People herido = env.getPeople(nuevo.getIdInjured());
				Disaster emergencia = env.getEvent(nuevo.getIdDisaster());
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
			// Por cada actividad:
			for(int i = 0; i < actividades.length(); i++){
				JSONObject instancia = actividades.getJSONObject(i);
				Activity nuevo = new Activity(
					instancia.getInt("id"),
					instancia.getInt("idUser"),
					instancia.getInt("idDisaster"),
					instancia.getString("type"),
					instancia.getString("state"));
				env.putActivity(nuevo.getId(), nuevo);
				Resource usuario = env.getResource(nuevo.getIdUser());
				Disaster emergencia = env.getEvent(nuevo.getIdDisaster());
				System.out.println("- Nueva actividad '" + nuevo.getType() + "' realizada por '" + usuario.getName() +
					"' en emergencia '" + emergencia.getName() + "' (id:" + nuevo.getId() + ")");
			}
		}catch(JSONException e){
			System.out.println("Error con JSON: " + e);
		}
		
		waitFor(5000);
		getBeliefbase().getBelief("iniciado").setFact(true);
	}
}