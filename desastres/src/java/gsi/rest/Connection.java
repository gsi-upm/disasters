package gsi.rest;

import java.net.*;
import java.io.*;

/**
 * Class used to connect to websites
 * @author julio camarero
 * @version 1.0
 */
public class Connection {
	private static final String PROYECTO = "caronte";
	private static final String URL_BASE = "http://localhost:8080/" + PROYECTO + "/rest/";

	/**
	 * Getter
	 * @return String with the URL
	 */
	public static String getURL(){
		return URL_BASE;
	}

	/**
	 * Getter
	 * @return String with the Proyect name
	 */
	public static String getProyecto(){
		return PROYECTO;
	}

    /**
     * @param source URL to connect
     * @return String with the response from the URL
     *
     * Makes a connection
     */
    public static String connect(String source) {
        try{
            URL direction = new URL(DisasterApplication.removeBlanks(source));
            URLConnection directionConnection = direction.openConnection();
            //DataInputStream dis = new DataInputStream(yahooConnection.getInputStream());
            String inputLine;
            StringBuffer buff = new StringBuffer();
            BufferedReader dis = new BufferedReader(new InputStreamReader(directionConnection.getInputStream()));

            while((inputLine = dis.readLine()) != null){
                buff.append(inputLine);
				// System.out.println(inputLine);
            }

            String a = buff.toString();

            dis.close();
            return a;
        }catch (MalformedURLException me){
            return ("MalformedURLException: " + me);
        }catch (IOException ioe){
            return ("IOException: " + ioe);
        }
    }

    /**

     *
     * Test to try this class
     */
    public static void main(String[] args) {
        try {
            URL yahoo = new URL("http://www.google.es"); //funciona
            URL yahoo1 = new URL(URL_BASE + "post/type=dead&name=Avio&info=Avion&description=ningun&quantity=5&address=Aeropuerto+Barajas,+madrid&size=big&traffic=low");
            URL yahoo2 = new URL(URL_BASE + "year/2001"); //funciona
            URL yahoo3 = new URL(URL_BASE + "events"); //funciona
            URL yahoo5 = new URL(URL_BASE + "resources"); //funciona
            URL yahoo6 = new URL(URL_BASE + "people"); //funciona
            URL yahoo4 = new URL(URL_BASE + "delete/id/4"); //funciona
            URL yahoo7 = new URL(URL_BASE + "put/5/quantity/1"); //funciona
            URL yahoo8 = new URL(URL_BASE + "post/type=ambulance&name=SAMUR&info=Coche+115-F&description=Servicio+de+Urgencia&address=Plaza+del+Encuentro,1,madrid&latitud=40.416878&longitud=-3.703748");
            URL yahoo9 = new URL(URL_BASE + "post/type=ambulance&address=burgos");
            URLConnection yahooConnection = yahoo8.openConnection();
            //DataInputStream dis = new DataInputStream(yahooConnection.getInputStream());
            String inputLine;
            StringBuffer buff = new StringBuffer();

            BufferedReader dis = new BufferedReader(new InputStreamReader(yahooConnection.getInputStream()));

            while((inputLine = dis.readLine()) != null){
                buff.append(inputLine);
				// System.out.println(inputLine);
            }

            String a = buff.toString();
            System.out.println(a);
            dis.close();
        }catch (MalformedURLException me){
            System.out.println("MalformedURLException: " + me);
        }catch (IOException ioe){
            System.out.println("IOException: " + ioe);
        }
    }
}