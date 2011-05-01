package jadex.desastres;

import java.net.*;
import java.io.*;

/**
 * Clase para conectarse mediante una URL a Desastres 2.0
 * 
 * @author juliocamarero
 */
public class Connection {

	public static String connect(String source) {
		try {
			URL direccion = new URL(removeBlanks(source));
			URLConnection direccionConnection = direccion.openConnection();

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
		} catch (MalformedURLException me) {
			return ("MalformedURLException: " + me);
		} catch (IOException ioe) {
			return ("IOException: " + ioe);
		}
	}

	public static String removeBlanks(String cadena) {
		String nueva = "";
		String caracter;
		for (int i = 0; i < cadena.length(); i++) {
			caracter = cadena.substring(i, i + 1);

			if (caracter.equals(" ")) {
				nueva = nueva.concat("+");
			} else {
				nueva = nueva.concat(caracter);
			}
		}
		return nueva;
	}
}