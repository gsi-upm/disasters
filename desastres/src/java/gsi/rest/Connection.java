package gsi.rest;

import gsi.project.Constantes;
import java.io.*;
import java.net.*;

/**
 * Class used to connect to websites.
 * 
 * @author Julio Camarero
 * @version 1.0
 */
public class Connection{
	/** URL base. */
	public static final String URL_BASE = "http://localhost:" + Constantes.SERVER_PORT + "/" + Constantes.PROJECT + "/rest/";

	/**
     * Makes a connection.
	 * 
     * @param source URL to connect
     * @return String with the response from the URL
     */
    public static String connect(String source){
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
     * Test to try this class.
	 * 
	 * @param args argumentos (no se usa)
     */
    public static void main(String[] args){
        try{
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
            // DataInputStream dis = new DataInputStream(yahooConnection.getInputStream());
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