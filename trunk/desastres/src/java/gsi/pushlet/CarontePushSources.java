// Copyright (c) 2000 Just Objects B.V. <just@justobjects.nl>
// Distributable under LGPL license. See terms of license at gnu.org.
package gsi.pushlet;

import gsi.project.Constantes;
import java.sql.*;
import java.util.Date;
import nl.justobjects.pushlet.core.*;
import org.json.me.*;

/**
 * 
 *
 * @author Juan Luis Molina
 */
public class CarontePushSources{
	/**
	 * Produces events from Caronte.
	 */
	static public class CaronteEventPushSource implements EventSource, Runnable{
		/**
		 * Here we get our stocks from.
		 */
		Thread thread = null;
		volatile boolean active = false;

		private int restarts = 1;
		private static final String SUBJECT = "/events";
		private static final int PUBLISH_INTERVAL = 2000;
		
		private String fecha;

		public CaronteEventPushSource(){
			fecha = "1992-01-01 00:00:01.000";
		}

		/**
		 * Activate the event source.
		 */
		synchronized public void activate(){
			System.out.println("activating...");
			stopThread();
			thread = new Thread(this, "CarontePublisher-" + (restarts++));
			active = true;
			thread.start();
			System.out.println("activated");
		}

		/**
		 * Deactivate the event source.
		 */
		synchronized public void passivate(){
			System.out.println("passivating...");
			active = false;
			stopThread();
			System.out.println("passivated");
		}

		/**
		 * Deactivate the event source.
		 */
		synchronized public void stop(){
			active = false;
		}

		public void run(){
			while(active){
				try{
					JSONArray array = getCatastrofes(fecha);
					fecha = new Timestamp(new Date().getTime()).toString();
					
					if(array.length() > 0){
						publish(fecha, array);
						System.out.println("publicacion (fecha=" + fecha + ")");
					}
					
					// If we were interrupted just return.
					if(thread == null || thread.isInterrupted()){
						break;
					}
					
					thread.sleep(PUBLISH_INTERVAL);
				}catch(InterruptedException e){
					System.out.println("InterruptedException: " + e);
					System.exit(-1);
				}
			}
			
			// Loop terminated: reset vars
			thread = null;
			active = false;
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

		private void publish(String fecha, JSONArray datos){
			Event event = Event.createDataEvent(SUBJECT);
			event.setField("fecha", fecha);
			event.setField("datos", datos.toString());
			Dispatcher.getInstance().multicast(event);
		}

		private void stopThread(){
			if(thread != null){
				thread.interrupt();
				thread = null;
			}
		}
	}
	
	public static void main(String[] args){
		// new CarontePushSources$CaronteEventPushSource();
	}
}