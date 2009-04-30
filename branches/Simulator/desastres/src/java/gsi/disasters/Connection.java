package gsi.disasters;
import gsi.rest.DisasterApplication;
import java.net.*;
import java.io.*;

/**
 * Class used to connect to websites
 * @author julio camarero
 * @version 1.0
 */
class Connection {
   
    
    /**
	 * @param source URL to connect 
         * @return String with the response from the URL
	 * 
	 * Makes a connection
	 */
    public static String connect (String source){
    try {
         URL direccion = new URL(DisasterApplication.removeBlanks(source));
          URLConnection direccionConnection = direccion.openConnection();
         //DataInputStream dis = new
         //DataInputStream(yahooConnection.getInputStream());
         String inputLine;
         StringBuffer buff = new StringBuffer();
         BufferedReader dis = new BufferedReader(new InputStreamReader(direccionConnection.getInputStream()));
         
         while ((inputLine = dis.readLine()) != null) {

             buff.append(inputLine);
            // System.out.println(inputLine);
         }
         
         String a = buff.toString();
         
         dis.close();
         return a;
      }
      catch (MalformedURLException me) {
        return("MalformedURLException: " + me);
      }
      catch (IOException ioe) {
         return("IOException: " + ioe);
      }
   }
    
    /**
	
	 * 
	 * Test to try this class
	 */
    public static void main(String[] args) {
      try {
         URL yahoo = new URL("http://www.google.es"); //funciona
         URL yahoo1 = new URL("http://localhost:8080/Disasters/rest/post/type=dead&name=Avio&info=Avion&description=ningun&quantity=5&address=Aeropuerto+Barajas,+madrid&size=big&traffic=low");
         URL yahoo2 = new URL("http://localhost:8080/Disasters/rest/year/2001");//funciona
         URL yahoo3 = new URL("http://localhost:8080/Disasters/rest/events");//funciona
         URL yahoo5 = new URL("http://localhost:8080/Disasters/rest/resources");//funciona
         URL yahoo6 = new URL("http://localhost:8080/Disasters/rest/people");//funciona
         URL yahoo4 = new URL("http://localhost:8080/Disasters/rest/delete/id/4");//funciona
         URL yahoo7 = new URL("http://localhost:8080/Disasters/rest/put/5/quantity/1");//funciona
         
         URL yahoo8 = new URL("http://localhost:8080/Disasters/rest/post/type=ambulance&name=SAMUR&info=Coche+115-F&description=Servicio+de+Urgencia&address=Plaza+del+Encuentro,1,madrid&latitud=40.416878&longitud=-3.703748");
         URL yahoo9 = new URL("http://localhost:8080/Disasters/RESTFUL/prePost.jsp?type=ambulance&address=burgos&date='2008-04-14+13:06:09.109'&state=active&user=1");
         URLConnection yahooConnection = yahoo8.openConnection();
         //DataInputStream dis = new
         //DataInputStream(yahooConnection.getInputStream());
         String inputLine;
         StringBuffer buff = new StringBuffer();
         
         BufferedReader dis = new BufferedReader(new InputStreamReader(yahooConnection.getInputStream()));
         
         
         while ((inputLine = dis.readLine()) != null) {

             buff.append(inputLine);
            // System.out.println(inputLine);
         }
         
         String a = buff.toString();
         System.out.println(a);
         dis.close();
      }
      catch (MalformedURLException me) {
        System.out.println("MalformedURLException: " + me);
      }
      catch (IOException ioe) {
         System.out.println("IOException: " + ioe);
      }
   }
}