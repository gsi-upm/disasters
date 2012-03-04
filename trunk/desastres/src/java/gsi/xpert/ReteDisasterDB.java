package gsi.xpert;

import gsi.disasters.*;
import gsi.rest.Connection;
import gsi.simulator.*;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;
import jess.*;
import org.json.me.*;

/**
 * Launches jess and creates markers
 * @author julio camarero
 * @version 1.0
 */
public class ReteDisasterDB {
	/** Object rete */
	private Rete rete;
	/** Marker in work memory */
	private WorkingMemoryMarker marker;
	/** Disasters registered */
	private static Hashtable<Integer,Disaster> disastersHash;
	/** Resources registered */
	private static Hashtable<Integer,Resource> resourcesHash;
	/** URL for Disasters2.0 application REST interface */
	private static final String URL_BASE = Connection.getURL();
	/** TimeStamp for the last JSON Request */
	private String now;
	/** PrintWriter to show answers in web application */
	private PrintWriter out;
	/** Quantity to add to calculate the latitude of the police marker */
	private final static double POLICE_MARKER_LATITUDE_DIFFERENCE = 0.0005;
	/** Quantity to add to calculate the longitude of the police marker */
	private final static double POLICE_MARKER_LONGITUDE_DIFFERENCE = 0;
	/** Quantity to add to calculate the latitude of the fireman marker */
	private final static double FIREMEN_MARKER_LATITUDE_DIFFERENCE = 0.00025;
	/** Quantity to add to calculate the longitude of the fireman marker */
	private final static double FIREMEN_MARKER_LONGITUDE_DIFFERENCE = -0.0005;
	/** Quantity to add to calculate the latitude of the ambulance marker */
	private final static double AMBULANCE_MARKER_LATITUDE_DIFFERENCE = 0.00025;
	/** Quantity to add to calculate the longitude of the ambulance marker */
	private final static double AMBULANCE_MARKER_LONGITUDE_DIFFERENCE = 0.0005;

	/**
	 * Constructor
	 * @throws JessException if Jess fails
	 */
	public ReteDisasterDB(PrintWriter out) throws JessException {
		this.out = out;
		rete = new Rete();
		out.println("Escribo en " + out);

		//Loads rules
		System.out.println("Loads rules");
		rete.batch("gsi/bc/evaluateDisaster2.clp");

		//Loads initial data
		System.out.println("Loads initial data");
		try {
			getResources();
		} catch (JSONException e) {
			System.out.println("JSON Error at ReteDisasterDB: " + e);
		}

		//AddAll
		System.out.println("addAll");
		rete.addAll(resourcesHash.values());
		rete.reset();
		rete.watchAll();

		//Mark to be able to reset to this point
		//marcador = rete.mark();
		rete.run();
		//rete.runUntilHalt();
	}

	/**
	 * Creates a list of resources from a Database
	 * @return ArraList with Resources
	 * @throws JSONException if JSON data are not correct
	 */
	private Hashtable<Integer,Resource> getResources() throws JSONException {
		resourcesHash = new Hashtable<Integer,Resource>();
		String freeResources = Connection.connect(URL_BASE + "free");
		JSONArray resources = new JSONArray(freeResources);

		//For each resource
		for (int i = 0; i < resources.length(); i++) {
			JSONObject JSONObject = resources.getJSONObject(i);
			Resource newResource = new Resource(
					JSONObject.getInt("id"),
					ResourceType.getType(JSONObject.getString("type")),
					JSONObject.getString("name"),
					JSONObject.getString("info"),
					JSONObject.getString("description"),
					JSONObject.getInt("idAssigned"));

			System.out.println("## New Resource: " + newResource.getType()
					+ " - " + newResource.getName() + " (id:"
					+ newResource.getId() + ") assigned:"
					+ newResource.getIdAssigned() + "##");
			out.println("## New Resource: " + newResource.getType() + " - "
					+ newResource.getName() + " (id:" + newResource.getId()
					+ ") assigned:" + newResource.getIdAssigned() + "##<br/>");
			out.flush();//be careful with this!
			resourcesHash.put(newResource.getId(), newResource);
		}
		return resourcesHash;
	}

	/**
	 * Stops the engine
	 * @throws JessException if Jess fails
	 */
	public void stop() throws JessException {
		rete.halt();
	}

	/**
	 * Evaluates a disaster
	 * @param disaster Disaster to evaluate
	 * @throws JessException if Jess fails
	 */
	private void evaluateDisaster(Disaster disaster) throws JessException {
		//rete.resetToMark(marcador);
		System.out.println("Evaluamos desastre...");
		out.println("Evaluamos desastre..." + "<br/>");
		rete.add(disaster); // LANZA EXCEPCION!!! Jess reported an error in routine DefinstanceList.updateMultipleSlots. Message: Called method threw an exception.

		rete.run();
		//rete.runUntilHalt();

		//Only iterates the Assingment objects
		Iterator it = rete.getObjects(new Filter() {

			public boolean accept(Object o) {
				return (o instanceof Assignment);
			}
		});
		while (it.hasNext()) {
			Assignment eval = (Assignment) it.next();
			System.out.println("Assignement: " + eval.getMessage());
			out.println("Assignement: " + eval.getMessage() + "<br/>");
		}
		System.out.println("Fin de evaluacion de desastre...");
		out.println("Fin de evaluacion de desastre..." + "<br/>");
	}

	/**
	 * Disassociates a resource from a disaster
	 * @param idResource the resource id in the application
	 * @param idDisaster the disaster id in the application
	 */
	public static void disAssociate(int idResource, int idDisaster) {
		Disaster dis = disastersHash.get(idDisaster);
		Resource res = resourcesHash.get(idResource);
		int marker = 0;

		if (res.getType() == ResourceType.POLICE) {
			marker = dis.getPoliceMarker();
		}
		if (res.getType() == ResourceType.FIREMEN) {
			marker = dis.getFiremenMarker();
		}
		if (res.getType() == ResourceType.AMBULANCE) {
			marker = dis.getAmbulanceMarker();
		}
		if (marker != 0) {
			Connection.connect(URL_BASE + "delete/" + marker);
			if (res.getType() == ResourceType.POLICE) {
				dis.setPoliceMarker(0);
			}
			if (res.getType() == ResourceType.FIREMEN) {
				dis.setFiremenMarker(0);
			}
			if (res.getType() == ResourceType.AMBULANCE) {
				dis.setAmbulanceMarker(0);
			}
		}
	}

	/**
	 * Associates a resource to a disaster
	 * @param idResource the resource id in the application
	 * @param idDisaster the disaster id in the application
	 * @throws JSONException if JSON data are not correct
	 */
	public static void associate(int idResource, int idDisaster) throws JSONException {
		Disaster dis = disastersHash.get(idDisaster);
		Resource res = resourcesHash.get(idResource);
		double latitude;
		double longitude;

		//We must check if there is already markers of the same type
		if (res.getType() == ResourceType.POLICE) {
			if (dis.getPoliceMarker() == 0) {
				//Add the marker to the map
				latitude = dis.getLatitude() + POLICE_MARKER_LATITUDE_DIFFERENCE;
				longitude = dis.getLongitude() + POLICE_MARKER_LONGITUDE_DIFFERENCE;

				String resp = Connection.connect(URL_BASE + "post/type=police"
						+ "&name=" + res.getName() + "&info=" + res.getInfo()
						+ "&description=" + res.getDescription() + "&latitud="
						+ latitude + "&longitud=" + longitude + "&idAssigned="
						+ idDisaster);
				//get the id to update the marker
				JSONObject JSONObject = new JSONObject(resp);
				int newId = JSONObject.getInt("id");
				dis.setPoliceMarker(newId);
				//System.out.println("Response:"+nuevoId);
			} else {
				//Modify the existing to add 1: put/id/quantity/add
				Connection.connect(URL_BASE + "put/" + dis.getPoliceMarker() + "/add");
			}
		}
		if (res.getType() == ResourceType.FIREMEN) {
			if (dis.getFiremenMarker() == 0) {
				//Add the marker to the map
				latitude = dis.getLatitude() + FIREMEN_MARKER_LATITUDE_DIFFERENCE;
				longitude = dis.getLongitude() - FIREMEN_MARKER_LONGITUDE_DIFFERENCE;

				String resp = Connection.connect(URL_BASE + "post/type=firemen"
						+ "&name=" + res.getName() + "&info=" + res.getInfo()
						+ "&description=" + res.getDescription() + "&latitud="
						+ latitude + "&longitud=" + longitude + "&idAssigned="
						+ idDisaster);
				//get the id to update the marker
				JSONObject JSONObject = new JSONObject(resp);

				int newId = JSONObject.getInt("id");
				dis.setFiremenMarker(newId);
				//System.out.println("Response:"+nuevoId);
			} else {
				//Modify the existing to add 1: put/id/quantity/add
				Connection.connect(URL_BASE + "put/" + dis.getFiremenMarker() + "/add");
			}
		}
		if (res.getType() == ResourceType.AMBULANCE) {
			if (dis.getAmbulanceMarker() == 0) {
				//Add the marker to the map
				latitude = dis.getLatitude() + AMBULANCE_MARKER_LATITUDE_DIFFERENCE;
				longitude = dis.getLongitude() + AMBULANCE_MARKER_LONGITUDE_DIFFERENCE;

				String resp = Connection.connect(URL_BASE + "post/type=ambulance"
						+ "&name=" + res.getName() + "&info=" + res.getInfo()
						+ "&description=" + res.getDescription() + "&latitud="
						+ latitude + "&longitud=" + longitude + "&idAssigned="
						+ idDisaster);
				//get the id to update the marker
				JSONObject JSONObject = new JSONObject(resp);

				int newId = JSONObject.getInt("id");
				dis.setAmbulanceMarker(newId);
				//System.out.println("Response:"+nuevoId);
			} else {
				//Modify the existing to add 1: put/id/quantity/add
				Connection.connect(URL_BASE + "put/" + dis.getAmbulanceMarker() + "/add");
			}
		}
		System.out.println(idResource + ". " + res.getType().toString()
				+ " - associating - " + ". " + idDisaster + dis.getType().toString());
	}

	/**
	 * Starts the engine. Find disasters and assign resources.
	 * @throws JessException if Jess fails
	 * @throws JSONException if JSON data are not correct
	 */
	public void run() throws JSONException, JessException {
		//Only the first time
		disastersHash = new Hashtable<Integer,Disaster>();

		//All the initial disasters from JSON
		String events = Connection.connect(URL_BASE + "events");
		JSONArray disastersJSON = new JSONArray(events);
		System.out.println(disastersJSON);

		//All the initial victims from JSON
		String people = Connection.connect(URL_BASE + "people");
		JSONArray victims = new JSONArray(people);

		//For each disaster
		for (int i = 0; i < disastersJSON.length(); i++) {
			JSONObject JSONObject = disastersJSON.getJSONObject(i);
			Disaster newDisaster = new Disaster(
					JSONObject.getInt("id"),
					DisasterType.getType(JSONObject.getString("type")),
					JSONObject.getString("name"),
					JSONObject.getString("info"),
					JSONObject.getString("description"),
					JSONObject.getString("address"),
					new Double(JSONObject.getString("longitud")),
					new Double(JSONObject.getString("latitud")),
					StateType.getType(JSONObject.getString("state")),
					SizeType.getType(JSONObject.getString("size")),
					DensityType.getType(JSONObject.getString("traffic")),
					null, null, null, null, 0, 0, 0, 0);

			System.out.println("### New Disaster: " + newDisaster.getType()
					+ " - " + newDisaster.getName() + " (id:"
					+ newDisaster.getId() + ") ##");
			out.println("### New Disaster: " + newDisaster.getType() + " - "
					+ newDisaster.getName() + " (id:" + newDisaster.getId()
					+ ") ##<br/>");

			disastersHash.put(newDisaster.getId(), newDisaster);
			evaluateDisaster(newDisaster);
		}

		//For each victim
		for (int i = 0; i < victims.length(); i++) {
			JSONObject JSONObject = victims.getJSONObject(i);
			People newPeople = new People(
					JSONObject.getInt("id"),
					InjuryDegree.getType(JSONObject.getString("type")),
					JSONObject.getString("name"),
					JSONObject.getString("info"),
					JSONObject.getString("description"),
					JSONObject.getInt("idAssigned"),
					JSONObject.getInt("quantity"));
			//Does nothing if it is not assigned to a disaster o it is incorrect
			if (newPeople.getIdAssigned() == 0
					|| !disastersHash.containsKey(newPeople.getIdAssigned())) {
				continue;
			}

			Disaster dis = disastersHash.get(newPeople.getIdAssigned());
			System.out.println("*** Updating Disaster Victims for " + dis.getName());
			out.println("*** Updating Disaster Victims for " + dis.getName() + "<br/>");

			/* TODO: Temporary solution until individual persons can be saved in
			 * the database. Victims don't have to be default but must have the
			 * lifepoints saved. */
			if (newPeople.getType().equals(InjuryDegree.SLIGHT)) {
				for (int j = 0; j < newPeople.getQuantity(); j++) {
					//TODO: All the Person from the same People will have the same id
					dis.addSlight(VictimManager.generateSlightVictim(newPeople.getId(), new Parameters()));
				}
			}
			if (newPeople.getType().equals(InjuryDegree.SERIOUS)) {
				for (int j = 0; j < newPeople.getQuantity(); j++) {
					dis.addSerious(VictimManager.generateSeriousVictim(newPeople.getId(), new Parameters()));
				}
			}
			if (newPeople.getType().equals(InjuryDegree.DEAD)) {
				for (int j = 0; j < newPeople.getQuantity(); j++) {
					dis.addDead(VictimManager.generateDefaultDead(newPeople.getId()));
				}
			}
			if (newPeople.getType().equals(InjuryDegree.TRAPPED)) {
				for (int j = 0; j < newPeople.getQuantity(); j++) {
					dis.addTrapped(VictimManager.generateDefaultTrapped(newPeople.getId()));
				}
			}
			evaluateDisaster(dis);
		}
	}

	/**
	 * Updates the application with the modifications in the JSON
	 * @throws org.json.me.JSONException
	 * @throws jess.JessException
	 */
	public void call() throws JSONException, JessException {

		//Disasters JSON since the last modification
		String events = Connection.connect(URL_BASE + "events/modified/" + now);
		JSONArray disasters = new JSONArray(events);

		//Victims JSON since the last modification
		String people = Connection.connect(URL_BASE + "people/modified/" + now);
		JSONArray victims = new JSONArray(people);

		for (int i = 0; i < disasters.length(); i++) {
			JSONObject JSONObject = disasters.getJSONObject(i);
			Disaster newDisaster = new Disaster(
					JSONObject.getInt("id"),
					DisasterType.getType(JSONObject.getString("type")),
					JSONObject.getString("name"),
					JSONObject.getString("info"),
					JSONObject.getString("description"),
					JSONObject.getString("address"),
					new Double(JSONObject.getString("longitud")),
					new Double(JSONObject.getString("latitud")),
					StateType.getType(JSONObject.getString("state")),
					SizeType.getType(JSONObject.getString("size")),
					DensityType.getType(JSONObject.getString("traffic")),
					null, null, null, null, 0, 0, 0, 0);

			//It's not a new Disaster, it has just been modified
			if (disastersHash.containsKey(newDisaster.getId())) {
				System.out.println("*** Updating Disaster... "
						+ newDisaster.getName() + " - " + newDisaster.getState());
				out.println("*** Updating Disaster... " + newDisaster.getName()
						+ " - " + newDisaster.getState() + "<br/>");

				Disaster old = (Disaster) disasters.get(newDisaster.getId());
				old.setType(newDisaster.getType());
				old.setName(newDisaster.getName());
				old.setInfo(newDisaster.getInfo());
				old.setDescription(newDisaster.getDescription());
				old.setAddress(newDisaster.getAddress());
				old.setLongitude(newDisaster.getLongitude());
				old.setLatitude(newDisaster.getLatitude());
				old.setState(newDisaster.getState());
				old.setSize(newDisaster.getSize());
				old.setTraffic(newDisaster.getTraffic());
				evaluateDisaster(old); //I think it's not necessary

				//It's a completely new disaster
			} else {
				System.out.println("### New Disaster: " + newDisaster.getType()
						+ " - " + newDisaster.getName() + " (id:"
						+ newDisaster.getId() + ") ##");
				out.println("### New Disaster: " + newDisaster.getType() + " - "
						+ newDisaster.getName() + " (id:" + newDisaster.getId()
						+ ") ##<br/>");

				disasters.put(newDisaster.getId(), newDisaster);
				evaluateDisaster(newDisaster);
			}
		}

		//For each NEW victim
		for (int i = 0; i < victims.length(); i++) {
			JSONObject JSONObject = victims.getJSONObject(i);
			People newPeople = new People(
					JSONObject.getInt("id"),
					InjuryDegree.getType(JSONObject.getString("type")),
					JSONObject.getString("name"),
					JSONObject.getString("info"),
					JSONObject.getString("description"),
					JSONObject.getInt("idAssigned"),
					JSONObject.getInt("quantity"));
			//TODO: What should we do with the erased people??
			if (newPeople.getState().equals(StateType.ERASED)) {
				newPeople.setQuantity(0);
			}
			if (newPeople.getIdAssigned() == 0) {
				continue;
			}

			Disaster dis = (Disaster) disasters.get(newPeople.getIdAssigned());
			System.out.println("*** Updating Disaster Victims for "
					+ dis.getName() + " -- " + newPeople.getQuantity()
					+ newPeople.getType());
			out.println("*** Updating Disaster Victims for " + dis.getName()
					+ " -- " + newPeople.getQuantity()
					+ newPeople.getType() + "<br/>");

			/* TODO: Temporary solution until individual persons can be saved in
			 * the database. Victims don't have to be default but must have the
			 * lifepoints saved. */
			if (newPeople.getType().equals(InjuryDegree.SLIGHT)) {
				for (int j = 0; j < newPeople.getQuantity(); j++) {
					//TODO: All the Person from the same People will have the same id
					dis.addSlight(VictimManager.generateSlightVictim(newPeople.getId(), new Parameters()));
				}
			}
			if (newPeople.getType().equals(InjuryDegree.SERIOUS)) {
				for (int j = 0; j < newPeople.getQuantity(); j++) {
					dis.addSerious(VictimManager.generateSeriousVictim(newPeople.getId(), new Parameters()));
				}
			}
			if (newPeople.getType().equals(InjuryDegree.DEAD)) {
				for (int j = 0; j < newPeople.getQuantity(); j++) {
					dis.addDead(VictimManager.generateDefaultDead(newPeople.getId()));
				}
			}
			if (newPeople.getType().equals(InjuryDegree.TRAPPED)) {
				for (int j = 0; j < newPeople.getQuantity(); j++) {
					dis.addTrapped(VictimManager.generateDefaultTrapped(newPeople.getId()));
				}
			}
			evaluateDisaster(dis);
		}
		//Refreshes the time for the next update
		now = new Timestamp(new Date().getTime()).toString();
	}

	/**
	 * Main method
	 * @param args main programm parameters. (no expected)
	 * @throws JessException if Jess fails
	 */
	public static void main(String[] args) throws JessException, JSONException {
		/*ReteDisasterDB reteBD = new ReteDisasterDB();
		reteBD.run();*/
	}
}