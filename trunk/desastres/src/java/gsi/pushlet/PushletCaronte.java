// Copyright (c) 2000 Just Objects B.V. <just@justobjects.nl>
// Distributable under LGPL license. See terms of license at gnu.org.
package gsi.pushlet;

import gsi.project.Constantes;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import nl.justobjects.pushlet.client.*;
import nl.justobjects.pushlet.core.*;
import nl.justobjects.pushlet.util.PushletException;
import org.json.me.*;

/**
 * PushletCaronte.
 * 
 * @author Juan Luis Molina Nogales
 **/
public class PushletCaronte extends Thread implements PushletClientListener, Protocol{
	private PushletClient pushletClient;
	private String host;
	private int port;
	private static final String SUBJECT = "/events";
	private static final long PUBLISH_INTERVAL = 2000;

	/**
	 * Constructor.
	 * 
	 * @param aHost host
	 * @param aPort port
	 */
	public PushletCaronte(String aHost, int aPort){
		host = aHost;
		port = aPort;
	}

	/**
	 * Run.
	 */
	public void run(){
		// Create and start a Pushlet client; we receive callbacks through onHeartbeat() and onData().
		try{
			pushletClient = new PushletClient(host, port);
			pushletClient.setDebug(true);
			pushletClient.join();
			pushletClient.listen(this, Protocol.MODE_STREAM);
			pushletClient.subscribe(SUBJECT);
			System.out.println("pushletClient started");
		}catch(PushletException pe){
			System.out.println("Error in setting up pushlet session pe=" + pe);
			return;
		}

		// Publish an event to the server every N seconds.
		HashMap<String,String> eventData = new HashMap<String,String>(2);
		String fecha = "1992-01-01 00:00:01.000";
		while(true){
			try{
				JSONArray array = getCatastrofes(fecha);
				fecha = new Timestamp(new Date().getTime()).toString();
				
				if(array.length() > 0){
					// Create event data
					eventData.put("fecha", fecha);
					eventData.put("datos", array.toString());
				
					// POST event to pushlet server
					pushletClient.publish(SUBJECT, eventData);
					System.out.println("publicado con " + array.length() + " datos");
				}
				
				Thread.sleep(PUBLISH_INTERVAL);
			}catch(PushletException ex){
				System.out.println("PushletException: " + ex);
			}catch(InterruptedException ex){
				System.out.println("InterruptedException: " + ex);
			}
		}
	}
	
	private JSONArray getCatastrofes(String fecha){
		JSONArray array = new JSONArray();
		try{
			Class.forName(Constantes.DB_DRIVER);
			Connection conexion = DriverManager.getConnection(Constantes.DB_URL, Constantes.DB_USER, Constantes.DB_PASS);
			Statement s = conexion.createStatement();
			String query = "SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, " +
				"LONGITUD, DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO, " +
				"USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO " +
				"FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E " +
				"WHERE MODIFICADO > '" + fecha + "' AND MARCADOR = M.ID AND TIPO = T.ID AND ESTADO = E.ID";
			ResultSet rs = s.executeQuery(query);
			
			if(rs.next()){
				JSONObject objeto = new JSONObject();
				objeto.put("id", rs.getInt(1));
				objeto.put("quantity", rs.getInt(2));
				objeto.put("name", rs.getString(3));
				objeto.put("description", rs.getString(4));
				objeto.put("info", rs.getString(5));
				objeto.put("latitud", rs.getFloat(6));
				objeto.put("longitud", rs.getFloat(7));
				objeto.put("address", rs.getString(8));
				objeto.put("size", rs.getString(9));
				objeto.put("traffic", rs.getString(10));
				objeto.put("floor", rs.getInt(11));
				objeto.put("idassigned", rs.getInt(12));
				objeto.put("date", rs.getString(13));
				objeto.put("modified", rs.getString(14));
				objeto.put("user", rs.getInt(15));
				objeto.put("item", rs.getString(16));
				objeto.put("type", rs.getString(17));
				objeto.put("state", rs.getString(18));
				array.put(objeto);
			}
			
			conexion.close();
		}catch(ClassNotFoundException ex){
			System.out.println("ClassNotFoundExcepcion: " + ex);
		}catch(SQLException ex){
			System.out.println("SQLExcepcion: " + ex);
		}catch(JSONException ex){
			System.out.println("JSONExcepcion: " + ex);
		}
		
		return array;
	}

	/**
	 * Error occurred.
	 * 
	 * @param message message
	 */
	public void onError(String message){
		System.out.println(message);
	}

	/**
	 * Abort event from server.
	 * 
	 * @param theEvent theEvent
	 */
	public void onAbort(Event theEvent){
		System.out.println("onAbort received: " + theEvent);
	}

	/**
	 * Data event from server.
	 * 
	 * @param theEvent theEvent
	 */
	public void onData(Event theEvent){
		// Calculate round trip delay
		String fecha = theEvent.getField("fecha");
		JSONArray datos = null;
		try{
			datos = new JSONArray(theEvent.getField("datos"));
		}catch(JSONException ex){
			System.out.println("JSONException: " + ex);
		}
		System.out.println("onData: tammano " + datos.length() + " a fecha " + fecha);
	}

	/**
	 * Heartbeat event from server.
	 * 
	 * @param theEvent theEvent
	 */
	public void onHeartbeat(Event theEvent){
		System.out.println("onHeartbeat received: " + theEvent);
	}

	/**
	 * Main program.
	 * 
	 * @param args [0]:host, [1]:port
	 */
	public static void main(String[] args){
		String servidor = (args.length > 0) ? args[0] : "localhost/caronte";
		int puerto = (args.length > 1) ? Integer.parseInt(args[1]) : 8080;
		new PushletCaronte(servidor, puerto).start();
	}
}