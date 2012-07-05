/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gsi.envioOCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.*;


/**
 *
 * @author Lara Lorna Jiménez
 */
public class ProduceEvento {
   
    private String charset = "UTF-8";
   
    /**
     * 
     * @param url, data
     * @throws MalformedURLException
     * @throws IOException
     * @throws ProtocolException
     * 
     * @author Lara Lorna Jiménez Jiménez
     */
    public String postData(String url,String param[]) throws MalformedURLException,IOException,ProtocolException{
        
        /** Codificacion URL para los parametros. */
        String queryString = "type=" + URLEncoder.encode(param[0], charset);
        queryString += "&event=" + URLEncoder.encode(param[1], charset);
        queryString += "&size=" + URLEncoder.encode(param[2], charset);
        queryString += "&description=" + URLEncoder.encode(param[3], charset);
        queryString += "&name=" + URLEncoder.encode(param[4], charset);
        queryString += "&longitude=" + URLEncoder.encode(param[5], charset);
        queryString += "&floor=" + URLEncoder.encode(param[6], charset);
        queryString += "&latitude=" + URLEncoder.encode(param[7], charset);
        queryString += "&date=" + URLEncoder.encode(param[8], charset);
        
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
        
        System.out.println("\nMensaje en ProduceEvento");
        System.out.println("Response Code - Response Code Message enviado por servlet remoto OCP: "+Integer.toString(respCode)+" - "+respMessage);
        
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
        
    
    public static void main(String []args){}
    
    
}

