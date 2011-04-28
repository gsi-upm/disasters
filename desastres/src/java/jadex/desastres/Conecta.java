package jadex.desastres;

import java.net.*;
import java.io.*;
import java.lang.*;


/**
 * Clase para conectarse mediante una URL a Desastres 2.0
 * 
 * @author aebeda
 */
public class Conecta {
	
	/**
	 * Creamos evento/agente teniendo du tipo, nombre, informacion y posicion.
	 */
	public Conecta(Position position,String type,String name,String info){
		//Pasamos a String la latitud y la longitud
		String longitud = String.valueOf(position.getY());
		String latitud = String.valueOf(position.getX());
		//Crea un fuego en las coordenadas dadas.
		try{
			URL url0 = new URL(Environment.URL + "post/type="+type+"&name="+name+"&info=''&latitud="+latitud+"&longitud="+longitud);
			//Nos conectamos a la URL
			conectaWeb(url0);
		}catch(MalformedURLException me){
			System.err.println("MalformedURLException: " + me);
		}
	}
	
	/**
	 * Conecta a la URL dada, y devuelve en un String el resultado dado
	 * por el servidor.
	 * @param url
	 */
	public String conectaWeb(URL url){
		try{
			URLConnection urlConnection = url.openConnection();
		    BufferedReader dis = new BufferedReader(
		    		new InputStreamReader(urlConnection.getInputStream()));
		    
		    String inputLine;
		    StringBuffer buff = new StringBuffer();
		    
	         while ((inputLine = dis.readLine()) != null) {
	             buff.append(inputLine);
	         }
	         //System.out.println(buff);
	         dis.close();
	         return buff.toString();
		}catch (MalformedURLException me) {
			System.err.println("MalformedURLException: " + me);
		}catch (IOException ioe) {
			System.err.println("IOException: " + ioe);
		}
		return "error";
	}
}