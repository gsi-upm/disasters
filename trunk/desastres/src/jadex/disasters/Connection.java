package disasters;

import java.io.*;
import java.net.*;

/**
 * Clase para conectarse mediante una URL a Desastres 2.0.
 * 
 * @author Julio Camarero
 */
public class Connection{
	/**
	 * Conecta a la URL dada y devuelve en un String el resultado dado por el servidor.
	 * 
	 * @param source URL a la que realiza la conexi&oacute;n
	 * @return respuesta del servidor
	 */
	public static String connect(String source){
		try{
			URL direccion = new URL(removeBlanks(source));
			URLConnection direccionConnection = direccion.openConnection();

			String inputLine;
			StringBuffer buff = new StringBuffer();
			BufferedReader dis = new BufferedReader(new InputStreamReader(direccionConnection.getInputStream()));

			while((inputLine = dis.readLine()) != null){
				buff.append(inputLine);
			}

			String a = buff.toString();
			dis.close();
			return a;
		}catch(MalformedURLException me){
			return ("MalformedURLException: " + me);
		}catch(IOException ioe) {
			return ("IOException: " + ioe);
		}
	}

	/**
	 * Elimina los espacios cambiandolos por '+'.
	 * 
	 * @param cadena texto a formatear
	 * @return texto formateado
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
}