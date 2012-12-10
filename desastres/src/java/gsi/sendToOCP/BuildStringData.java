package gsi.sendToOCP;

import java.net.URLEncoder;

/**
 * Build String data.
 *
 * @author Lara Lorna Jim&eacute;nez
 */
public class BuildStringData{

	private static final String URLRemoteServlet = "http://localhost:8090/HTTPservidor/ConsumeEvento";
	// Ahora es una URL en un servidor de prueba pero mas tarde
	// debera ser la URL del sevlet remoto (el que conecta con OCP) que recibe estos POST -->
	// Suponiendo que su servlet para procesar register y produce sea uno y no dos distintos 
	private static final String URLServletConsume = "URL de nuestro servlet, el que procesa el POST de los partners";
	// Este es el servlet local con el que nosotros consumimos los FirePlan que ellos nos manden.
	// Se envia para que ellos sepan donde tienen que mandar el POST
	private static final String charset = "UTF-8";

	/**
	 * Data produce builder.
	 * 
	 * @param param [0]:id, [1]:type, [2]:event
	 * @return response
	 * @throws Exception 
	 */
	public static String DataProduceBuilder(String param[]) throws Exception{
		// param[2] es event --> viene como fire pero queremos que sea FireEvent, asi que lo cambio lo primero.
		param[2] = "FireEvent";

		// no modificamos param[1] <-- variable type. Viene desde mapa.js con el valor que 
		// necesitamos en este metodo: 'produce'

		String dataToSend = "{\'" + param[2] + "-" + Integer.parseInt(param[0]) + "\':" // Concatenamos la entidad y el id (ej: FireEvent-2)
			+ "{'type':\'" + param[1] + "\'" // Establece la funcion del POST. Toma dos valores "REGISTRAR" o "PRODUCIR".
			// "register": Para registrarse a consumir un evento OCP. En este caso solo se tendra en cuenta el parametro event.
			// "produce": Se esta produciendo un evento.
			+ ",'size':\'" + param[3] + "\','description':\'" + param[4] + "\','name':\'" + param[5] + "\','longitude':"
			+ Float.parseFloat(param[6]) + ",'floor':" + Integer.parseInt(param[7]) + ",'latitude':" + Float.parseFloat(param[8])
			+ ",'date':\'" + param[9] + "\'}}";

		System.out.println("\nSaca: " + dataToSend);

		/* Codificacion URL para el mensaje en formato JSON. */
		String queryString = "param=" + URLEncoder.encode(dataToSend, charset);

		// Realizamos el POST para informar de evento a OCP
		String servletResponse = ProduceEvent.postData(URLRemoteServlet, queryString);

		return servletResponse;
	}

	/**
	 * Data register builder.
	 * 
	 * @param param [0]:id, [1]:type, [2]:event
	 * @return response
	 * @throws Exception 
	 */
	public static String DataRegisterBuilder(String param[]) throws Exception {
		// param[2] es event --> viene como fire pero queremos que sea FireEvent, asi que lo cambio lo primero.
		param[2] = "FirePlan"; //Puesto que nos registramos a cualquier FirePlan

		// cambiamos la variable que viene como 'produce' desde mapa.js a 'register'
		param[1] = "register";

		// "id=" + Integer.parseInt(param[0]) --> En principio como nos queremos registrar a una entidad
		//  en general (FirePlan) y no a la de un evento en particular (FirePlan-2) no hace falta mandar la id.  
		String dataToRegister = "type=" + param[1] + "&entity=" + param[2] + "&url=" + URLServletConsume;

		System.out.println("\nSaca: " + dataToRegister);

		// Realizamos el POST para registrarnos a entidad
		String servletResponse = ProduceEvent.postData(URLRemoteServlet, dataToRegister);

		return servletResponse;
	}

	/**
	 * Main.
	 * 
	 * @param args no
	 */
	public static void main(String[] args){}
}
