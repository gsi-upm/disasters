package gsi.consumeFromOCP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author lara
 */
public class HTTPToFile {
    
    private String charset = "UTF-8";
    
    public String sendJSONData(String url, String query) throws Exception{

        String queryString = "Param="+query;

        /** Parametros HTTP de conexion */
        HttpURLConnection urlc = (HttpURLConnection) (new URL(url)).openConnection();
        urlc.setRequestMethod("POST");
        urlc.setDoOutput(true);
        urlc.setDoInput(true);
        urlc.setUseCaches(false);
        urlc.setAllowUserInteraction(false);
        urlc.setRequestProperty("Accept-Charset", charset);
        urlc.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=" + charset);

        /** Hacer el POST */
        OutputStream out = null;
        out = urlc.getOutputStream();
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        writer.write(queryString);
        writer.flush();
        writer.close();
        out.close();

        /** Leer el Response Code y el Response Message del servidor al que hemos hecho el request*/
        String respMessage = urlc.getResponseMessage();
        int respCode = urlc.getResponseCode();

        System.out.println("\nMensaje en HTTPToFile");
        System.out.println("Response Code - Response Code Message enviado por servlet remoto HTTPServidor en Fano: "+Integer.toString(respCode)+" - "+respMessage);

        /** Leer la respuesta del servidor a la peticion POST mandada */
        BufferedReader bfreader = new BufferedReader(new InputStreamReader(urlc.getInputStream())); 
        StringBuilder builder = new StringBuilder(100);
        String line = "";
        while ((line = bfreader.readLine()) != null) {
            builder.append(line);
        }
        bfreader.close();

        /** hacer return con la respuesta del servidor */
        return builder.toString();

    } 
    
    public static void main(String[] args){}
    
}


