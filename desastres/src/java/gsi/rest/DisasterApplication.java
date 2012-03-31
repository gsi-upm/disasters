package gsi.rest;

import gsi.project.Constantes;
import java.sql.Timestamp;
import java.util.Date;
import org.restlet.*;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

/**
 * Class used to define routes and Restlets
 * @author julio camarero
 * @version 1.0
 */
public class DisasterApplication extends Application {

	private static final String URL_BASE = "/" + Constantes.PROJECT + "/RESTFUL/";

	/**
	 * Constructor (not used)
	 */
	public DisasterApplication(){}
	
	/**
	 * Method used to remove the blanks in a String (useful for URLs)
	 * @param cadena with the original text
	 * @return the String without blanks
	 */
	public static String removeBlanks(String cadena){
		String nueva = "";
		String caracter;
		for(int i = 0; i < cadena.length(); i++){
			caracter = cadena.substring(i, i + 1);

			if(caracter.equals(" ")){
				nueva = nueva.concat("+");
			}else{
				nueva = nueva.concat(caracter);
			}
		}
		return nueva;
	}

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public Restlet createInboundRoot(){
		// Create a root router
		Router router = new Router(getContext());
		
		// Getting data by Id
		Restlet id = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?action=id&id=" + request.getAttributes().get("id");
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all data for a year
		Restlet year = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int year = Integer.parseInt((String) request.getAttributes().get("year"));
				String redirector = URL_BASE + "get.jsp?action=year&year1="
						+ year + "-01-01 00:00:01" + "&year2=" + (year + 1) + "-01-01 00:00:01";
				//response.setEntity(redirector_year, MediaType.TEXT_PLAIN);
				//response.redirectPermanent(redirectorYear);
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get events by year
		Restlet eventYear = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int year = Integer.parseInt((String) request.getAttributes().get("year"));
				String redirector = URL_BASE + "get.jsp?action=eventsByYear&year1="
						+ year + "-01-01 00:00:01" + "&year2=" + (year + 1) + "-01-01 00:00:01";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get resources by year
		Restlet resourceYear = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int year = Integer.parseInt((String) request.getAttributes().get("year"));
				String redirector = URL_BASE + "get.jsp?action=resourcesByYear&year1="
						+ year + "-01-01 00:00:01" + "&year2=" + (year + 1) + "-01-01 00:00:01";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get injured people by year
		Restlet peopleYear = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int year = Integer.parseInt((String) request.getAttributes().get("year"));
				String redirector = URL_BASE + "get.jsp?action=peopleByYear&year1="
						+ year + "-01-01 00:00:01" + "&year2=" + (year + 1) + "-01-01 00:00:01";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// DELETE data by id
		Restlet delId = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "delete.jsp?action=id&id=" + request.getAttributes().get("id");
				//response.setEntity(redirector_year, MediaType.TEXT_PLAIN);
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// DELETE events
		Restlet delEvents = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "delete.jsp?action=events";
				//response.setEntity(redirector_year, MediaType.TEXT_PLAIN);
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// DELETE all
		Restlet delAll = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "delete.jsp?action=all";
				//response.setEntity(redirector_year, MediaType.TEXT_PLAIN);
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		//Post Events in the database
		Restlet post = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "postFul.jsp?" + request.getAttributes().get("params") + "&date="
						+ new Timestamp(new Date().getTime()).toString() + "&state=active&user=1";
				//default anonymous user id is 1
				//default state created is active
				//date is right now!
				//Hallar latitud y longitud en el jsp
				//response.setEntity(redirector_year, MediaType.TEXT_PLAIN);
				response.redirectPermanent(removeBlanks(redirector));
			}
		};

		//Updating Events in the database
		Restlet put = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String parameter = (String) request.getAttributes().get("parameter");
				int id = Integer.parseInt((String) request.getAttributes().get("id"));
				String value = (String) request.getAttributes().get("value");
				String redirector = URL_BASE + "put.jsp?action=" + parameter + "&id=" + id + "&" + "value=" + value;
				if (parameter.equals("state")){
					if (!(value.equals("controlled") || value.equals("active") || value.equals("erased"))){
						response.setEntity("State format is not correct. (active, controlled or erased)", MediaType.TEXT_PLAIN);
					}
				}
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		//Updating Events in the database
		Restlet putLatLong = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int id = Integer.parseInt((String) request.getAttributes().get("id"));
				String valueLat = (String) request.getAttributes().get("latitud");
				String valueLong = (String) request.getAttributes().get("longitud");
				String redirector = URL_BASE + "put.jsp?action=latlong&id=" + id + "&" + "latitud=" + valueLat + "&" + "longitud=" + valueLong;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		//Increments 1 the quantity
		Restlet putAdd = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int id = Integer.parseInt((String) request.getAttributes().get("id"));
				String action = (String) request.getAttributes().get("action");
				String redirector = URL_BASE + "put.jsp?action=" + action + "&id=" + id;
				if (!(action.equals("add") || action.equals("remove"))){
					response.setEntity("Action is not correct (add or remove).", MediaType.TEXT_PLAIN);
				}
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		//Increments latitud and longitud, used due to limitations to add float numbers in mobile
		Restlet putInc = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int id = Integer.parseInt((String) request.getAttributes().get("id"));
				String parameter = (String) request.getAttributes().get("parameter");
				if (!(parameter.equals("latitud") || parameter.equals("longitud"))){
					response.setEntity("Parameter is not correct (latitud or longitud).", MediaType.TEXT_PLAIN);
				}
				Double valueParameter = Double.parseDouble((String) request.getAttributes().get("valueParameter"));
				Double increment = Double.parseDouble((String) request.getAttributes().get("valueIncrement"));
				valueParameter += increment;
				String redirector = URL_BASE + "put.jsp?action=" + parameter + "&id=" + id + "&value=" + valueParameter;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get Events
		Restlet evento = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String event = (String) request.getAttributes().get("event");
				String redirector;
				if(event != null){
					redirector = URL_BASE + "get.jsp?action=type&type=" + event;
				}else{
					redirector = URL_BASE + "get.jsp?action=events"; // action=item&item=event
				}
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get Resources
		Restlet recurso = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String event = (String) request.getAttributes().get("resource");
				String redirector;
				if(event != null){
					redirector = URL_BASE + "get.jsp?action=type&type=" + event;
				}else{
					redirector = URL_BASE + "get.jsp?action=resources"; // action=item&item=resource
				}
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get injured people
		Restlet persona = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String event = (String) request.getAttributes().get("people");
				String redirector;
				if(event != null){
					redirector = URL_BASE + "get.jsp?action=type&type=" + event;
				}else{
					redirector = URL_BASE + "get.jsp?action=people"; // action=item&item=people
				}
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all data from a date
		Restlet fecha = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				boolean fecha_correcta = true;
				String redirector_date = (String) request.getAttributes().get("date");
				if (redirector_date == null){
					int year = Integer.parseInt((String) request.getAttributes().get("year"));
					int month = Integer.parseInt((String) request.getAttributes().get("month"));
					int day = Integer.parseInt((String) request.getAttributes().get("day"));
					redirector_date = year + "-" + month + "-" + day;
					//IMPORTANTE LA ESTRUCTURA DE LA FECHA!!!
					// mensaje = day + "-" + month + "-" + year;
					if (year < 0 || month > 12 || day > 31){
						fecha_correcta = false;
					}
				}
				//En caso de fecha no correcta pasando los 3 parametors, imprime un mensaje de error
				//si se mete entera mal hace lo que puede...
				if (!fecha_correcta){
					response.setEntity("Date format is not correct. (YYYY-MM-DD)", MediaType.TEXT_PLAIN);
				} else {
					String redirector = URL_BASE + "get.jsp?action=all&fecha=" + redirector_date + " 00:00:01";
					response.redirectTemporary(removeBlanks(redirector));
				}
			}
		};

		// Get all events from a date
		Restlet eventDate = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				boolean fecha_correcta = true;
				String redirector_date = (String) request.getAttributes().get("date");
				if (redirector_date == null){
					int year = Integer.parseInt((String) request.getAttributes().get("year"));
					int month = Integer.parseInt((String) request.getAttributes().get("month"));
					int day = Integer.parseInt((String) request.getAttributes().get("day"));
					redirector_date = year + "-" + month + "-" + day;
					//IMPORTANTE LA ESTRUCTURA DE LA FECHA!!!
					// mensaje = day + "-" + month + "-" + year;
					if (year < 0 || month > 12 || day > 31){
						fecha_correcta = false;
					}
				}
				//En caso de fecha no correcta pasando los 3 parametors, imprime un mensaje de error
				//si se mete entera mal hace lo que puede...
				if (!fecha_correcta){
					response.setEntity("Date format is not correct. (YYYY-MM-DD)", MediaType.TEXT_PLAIN);
				} else {
					String redirector = URL_BASE + "get.jsp?action=events&fecha=" + redirector_date + " 00:00:01";
					response.redirectTemporary(removeBlanks(redirector));
				}
			}
		};

		// Get all resources from a date
		Restlet resourceDate = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				boolean fecha_correcta = true;
				String redirector_date = (String) request.getAttributes().get("date");
				if (redirector_date == null){
					int year = Integer.parseInt((String) request.getAttributes().get("year"));
					int month = Integer.parseInt((String) request.getAttributes().get("month"));
					int day = Integer.parseInt((String) request.getAttributes().get("day"));
					redirector_date = year + "-" + month + "-" + day;
					//IMPORTANTE LA ESTRUCTURA DE LA FECHA!!!
					// mensaje = day + "-" + month + "-" + year;
					if (year < 0 || month > 12 || day > 31){
						fecha_correcta = false;
					}
				}
				//En caso de fecha no correcta pasando los 3 parametors, imprime un mensaje de error
				//si se mete entera mal hace lo que puede...
				if (!fecha_correcta){
					response.setEntity("Date format is not correct. (YYYY-MM-DD)", MediaType.TEXT_PLAIN);
				} else {
					String redirector = URL_BASE + "get.jsp?action=resources&fecha=" + redirector_date + " 00:00:01";
					response.redirectTemporary(removeBlanks(redirector));
				}
			}
		};

		// Get all injured people from a date
		Restlet peopleDate = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				boolean fecha_correcta = true;
				String redirector_date = (String) request.getAttributes().get("date");
				if (redirector_date == null){
					int year = Integer.parseInt((String) request.getAttributes().get("year"));
					int month = Integer.parseInt((String) request.getAttributes().get("month"));
					int day = Integer.parseInt((String) request.getAttributes().get("day"));
					redirector_date = year + "-" + month + "-" + day;
					//IMPORTANTE LA ESTRUCTURA DE LA FECHA!!!
					// mensaje = day + "-" + month + "-" + year;
					if (year < 0 || month > 12 || day > 31){
						fecha_correcta = false;
					}
				}
				//En caso de fecha no correcta pasando los 3 parametors, imprime un mensaje de error
				//si se mete entera mal hace lo que puede...
				if (!fecha_correcta){
					response.setEntity("Date format is not correct. (YYYY-MM-DD)", MediaType.TEXT_PLAIN);
				} else {
					String redirector = URL_BASE + "get.jsp?action=people&fecha=" + redirector_date + " 00:00:01";
					response.redirectTemporary(removeBlanks(redirector));
				}
			}
		};

		// Get Resources from a disaster
		Restlet resourcesAssociated = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int id = Integer.parseInt((String) request.getAttributes().get("id"));
				String redirector = URL_BASE + "get.jsp?action=associated&type=resource&id=" + id;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get Victims from a disaster
		Restlet peopleAssociated = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				int id = Integer.parseInt((String) request.getAttributes().get("id"));
				String redirector = URL_BASE + "get.jsp?action=associated&type=people&id=" + id;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get real Resources from another database
		Restlet freeResources = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String event = (String) request.getAttributes().get("resource");
				String redirector =URL_BASE + "get.jsp?action=free";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the modified events from a timestamp
		Restlet eventsModified = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector_date = (String) request.getAttributes().get("dateTime");
				String redirector = URL_BASE + "get.jsp?action=eventsModified&fecha=" + redirector_date;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the modified victims from a timestamp
		Restlet peopleModified = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector_date = (String) request.getAttributes().get("dateTime");
				String redirector = URL_BASE + "get.jsp?action=peopleModified&fecha=" + redirector_date;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the modified resources from a timestamp
		Restlet resourcesModified = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector_date = (String) request.getAttributes().get("dateTime");
				String redirector = URL_BASE + "get.jsp?action=resourcesModified&fecha=" + redirector_date;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the modified events from a timestamp in XML
		Restlet eventsModifiedXML = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector_date = (String) request.getAttributes().get("dateTime");
				String redirector = URL_BASE + "get.jsp?mode=xml&action=eventsModified&fecha=" + redirector_date;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the modified victims from a timestamp in XML
		Restlet peopleModifiedXML = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector_date = (String) request.getAttributes().get("dateTime");
				String redirector = URL_BASE + "get.jsp?mode=xml&action=peopleModified&fecha=" + redirector_date;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the modified resources from a timestamp in XML
		Restlet resourcesModifiedXML = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector_date = (String) request.getAttributes().get("dateTime");
				String redirector = URL_BASE + "get.jsp?mode=xml&action=resourcesModified&fecha=" + redirector_date;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all  events from a timestamp in XML
		Restlet eventsXML = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?mode=xml&action=item&item=event";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the modified victims in XML
		Restlet peopleXML = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?mode=xml&action=item&item=people";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the modified resources in XML
		Restlet resourcesXML = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?mode=xml&action=item&item=resource";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};
                
		// Get all the healthy people in the center
		Restlet healthy = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?action=healthy";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the unhealthy people in the center
		Restlet unhealthy = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?action=unhealthy";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};
                
		// 
		Restlet updateHealthy = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String id = (String) request.getAttributes().get("id");
				String redirector = URL_BASE + "delete.jsp?action=healthy&id=" + id;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get all the slight people in the center
		Restlet slight = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?action=slight";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get the person
		Restlet person = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String id = (String) request.getAttributes().get("id");
				String redirector = URL_BASE + "get.jsp?action=person&id=" + id;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Introducir mensaje
		Restlet message = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String tipo_receptor = (String) request.getAttributes().get("tipo_receptor");
				String receptor = (String) request.getAttributes().get("receptor");
				String valor = (String) request.getAttributes().get("valor");
				String redirector = URL_BASE + "put.jsp?action=message&tipo_receptor=" + tipo_receptor +
					"&receptor=" + receptor + "&mensaje=" + valor;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};
		
		// Get all the associations
		Restlet associations = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?action=associations";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};
		
		// Get all the activities
		Restlet activities = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector = URL_BASE + "get.jsp?action=activities";
				response.redirectTemporary(removeBlanks(redirector));
			}
		};
		
		// Get all the modified associations from a timestamp
		Restlet associationsModified = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector_date = (String) request.getAttributes().get("dateTime");
				String redirector = URL_BASE + "get.jsp?action=associationsModified&fecha=" + redirector_date;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};
		
		// Get all the modified activities from a timestamp
		Restlet activitiesModified = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String redirector_date = (String) request.getAttributes().get("dateTime");
				String redirector = URL_BASE + "get.jsp?action=activitiesModified&fecha=" + redirector_date;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};
		
		// Lee mensajes
		Restlet mensajes = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String creador = (String) request.getAttributes().get("creador");
				String fecha = (String) request.getAttributes().get("fecha");
				String redirector = URL_BASE + "get.jsp?action=messages&creador=" + creador + "&fecha=" + fecha;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Attach the handlers to the root router
		router.attach("/id/{id}", id);
		router.attach("/year/{year}", year);

		router.attach("/date/{date}", fecha);
		router.attach("/date/{year}/{month}/{day}", fecha);

		router.attach("/events", evento);
		router.attach("/events/{event}", evento);

		router.attach("/events/{id}/people", peopleAssociated);
		router.attach("/events/{id}/resources", resourcesAssociated);

		router.attach("/events/modified/{dateTime}", eventsModified);
		router.attach("/people/modified/{dateTime}", peopleModified);
		router.attach("/resources/modified/{dateTime}", resourcesModified);

		//PARA el XML
		router.attach("/xml/events/modified/{dateTime}", eventsModifiedXML);
		router.attach("/xml/people/modified/{dateTime}", peopleModifiedXML);
		router.attach("/xml/resources/modified/{dateTime}", resourcesModifiedXML);

		router.attach("/xml/events", eventsXML);
		router.attach("/xml/people", peopleXML);
		router.attach("/xml/resources", resourcesXML);


		router.attach("/events/year/{year}", eventYear);
		router.attach("/resources/year/{year}", resourceYear);
		router.attach("/people/year/{year}", peopleYear);

		router.attach("/events/date/{date}", eventDate);


		router.attach("/resources/date/{date}", resourceDate);
		router.attach("/people/date/{date}", peopleDate);
		router.attach("/events/date/{year}/{month}/{day}", eventDate);
		router.attach("/resources/date/{year}/{month}/{day}", resourceDate);
		router.attach("/people/date/{year}/{month}/{day}", peopleDate);

		router.attach("/free", freeResources);
		router.attach("/resources", recurso);
		router.attach("/resources/{resource}", recurso);

		router.attach("/people", persona);
		router.attach("/people/{people}", persona);


		router.attach("/delete/id/{id}", delId);
		router.attach("/delete/events", delEvents);
		router.attach("/delete/all", delAll);

		router.attach("/post/{params}", post);

		router.attach("/put/{id}/{parameter}/{valueParameter}/inc/{valueIncrement}", putInc);
		router.attach("/put/{id}/{parameter}/{value}", put);
		router.attach("/put/{id}/{action}", putAdd);
		router.attach("/put/{id}/latlong/{latitud}/{longitud}", putLatLong);
		
		router.attach("/healthy", healthy);
		router.attach("/unhealthy", unhealthy);
		router.attach("/slight", slight);
		router.attach("/healthy/id/{id}", updateHealthy);
		router.attach("/person/{id}", person);

		router.attach("/message/{tipo_receptor}/{receptor}/{valor}", message);
		
		router.attach("/associations", associations);
		router.attach("/activities", activities);
		router.attach("/associations/modified/{dateTime}", associationsModified);
		router.attach("/activities/modified/{dateTime}", activitiesModified);
		
		router.attach("/mensajes/coor/{creador}/{fecha}", mensajes);

		// Get the user
		Restlet user = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String user_name = (String) request.getAttributes().get("nombre_usuario");
				String user_pass = (String) request.getAttributes().get("password");
				String redirector = URL_BASE + "temporal.jsp?action=user&nombre_usuario="+ user_name + "&password=" + user_pass;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get the user projects
		Restlet userProject = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String user_name = (String) request.getAttributes().get("nombre_usuario");
				String redirector = URL_BASE + "temporal.jsp?action=userProject&nombre_usuario=" + user_name;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		// Get the user role
		Restlet userRole = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String user_name = (String) request.getAttributes().get("nombre_usuario");
				String redirector = URL_BASE + "temporal.jsp?action=userRole&nombre_usuario=" + user_name;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		Restlet registrar = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String user = (String) request.getAttributes().get("user");
				String pass = (String) request.getAttributes().get("pass");
				String nombre = (String) request.getAttributes().get("nombre");
				String email = (String) request.getAttributes().get("email");
				String redirector = URL_BASE + "temporal.jsp?action=registrar&user=" + user + "&pass=" + pass +
					"&nombre=" + nombre + "&email=" + email;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};

		Restlet insertar = new Restlet(getContext()){
			@Override
			public void handle(Request request, Response response){
				String type = (String) request.getAttributes().get("type");
				String name = (String) request.getAttributes().get("name");
				String description = (String) request.getAttributes().get("description");
				String info = (String) request.getAttributes().get("info");
				String latitud = (String) request.getAttributes().get("latitud");
				String longitud = (String) request.getAttributes().get("longitud");
				String planta = (String) request.getAttributes().get("planta");
				String redirector = URL_BASE + "temporal.jsp?action=insertar&type=" + type + "&name=" + name + "&description=" +
					description + "&info=" + info + "&latitud=" + latitud + "&longitud=" + longitud + "&planta=" + planta;
				response.redirectTemporary(removeBlanks(redirector));
			}
		};
		
		router.attach("/user/{nombre_usuario}/{password}", user);
		router.attach("/userProject/{nombre_usuario}", userProject);
		router.attach("/userRole/{nombre_usuario}", userRole);
		router.attach("/registrar/{user}/{pass}/{nombre}/{email}", registrar);
		router.attach("/insertar/{type}/{name}/{description}/{info}/{latitud}/{longitud}/{planta}", insertar);
		
		//Redirector inicial = new Redirector (getContext(), "index.jsp", Redirector.MODE_CLIENT_TEMPORARY);
		//router.attachDefault(inicial);

		// Return the root router
		return router;
	}
}