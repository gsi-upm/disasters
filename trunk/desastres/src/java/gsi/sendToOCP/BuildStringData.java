package gsi.sendToOCP;

import gsi.consumeFromOCP.JSONToDB;
import java.net.URLEncoder;

/**
 * Build String data.
 *
 * @author Lara Lorna Jim&eacute;nez
 */
public class BuildStringData {
    
    /*// En casa:
    private static final String URLRemoteServlet = "http://localhost:8090/HTTPservidor/ConsumeEvento";
    private static final String URLServletConsume = "http://apps.gsi.dit.upm.es/caronte/ReceiveFirePlan";
    private static final String URLconnectDB = "http://localhost:8080/caronte/PathServlet";
    */
    
    /*//Las URL de prueba es decir las que uso con mi HTTPservidor en la beca:
    private static final String URLRemoteServlet = "http://strauss.gsi.dit.upm.es:8090/HTTPservidor/ConsumeEvento";
    private static final String URLServletConsume = "http://apps.gsi.dit.upm.es/caronte/ReceiveFirePlan";
    private static final String URLconnectDB = "http://strauss.gsi.dit.upm.es:8080/caronte/PathServlet";
    */
    
    //URL's a usar los partners:
    private static final String URLRemoteServlet = "http://155.54.95.129:8080/"; // URL del sevlet remoto(el que conecta con OCP) que recibe estos POST --> Suponiendo que su$
    private static final String URLServletConsume = "http://apps.gsi.dit.upm.es/caronte/ReceiveFirePlan"; // Este es el servlet local con el que nosotros con$
                                           // Se envia para que ellos sepan donde tienen que mandar el POST
    private static final String URLconnectDB = "http://apps.gsi.dit.upm.es/caronte/PathServlet";
    
    
    private static final String charset = "UTF-8";
    
   
    public static String DataProduceBuilder(String param[], String urlDB) throws Exception{

        // param[2] es event --> viene como fire pero queremos que sea FireEvent, asi que lo cambio lo primero.
        param[2] = "FireEvent";

        // no modificamos param[1]<-- variable type. Viene desde mapa.js con el valor que 
        // necesitamos en este metodo: 'produce-new' o 'produce-modify'
        
        String dataToSendType = param[1];// Establece la funcion del POST. 
                                         // Toma tres valores: 'produce-new', 'produce-modify' y 'register'
                                         // En este caso solo: 'produce-new' y 'produce-modify'
        
        String dataToSendParam = "{\'"+param[2]+"-"+Integer.parseInt(param[0])+"\':" // Concatenamos la entidad y el id (ej: FireEvent-2)
                                        + "{"
                                            + "'severity':"+"\'"+param[3]+"\'"   
                                            +",'description':"+"\'"+param[4]+"\'"
                                            +",'name':"+"\'"+param[5]+"\'"
                                            //+",'date':"+"\'"+param[9]+"\'" --> Los partners no lo necesitan.
                                            +",'position':"
                                                     + "{\'Position-"+Integer.parseInt(param[0])+"\':"
                                                              +"{'longitude':"+Float.parseFloat(param[6])
                                                              +",'floor':"+Integer.parseInt(param[7])
                                                              +",'latitude':"+Float.parseFloat(param[8])
                                                              + "}"
                                                     + "}"
                                        + "}"
                                 +"}";

        //System.out.println("\nSaca: "+dataToSendParam);
        
        //Almacenamos en la tabla FireEvent_To_OCP los FireEvents que generamos para en jsonOCP.jsp
        //verlos dinamicamente.
        JSONToDB js = new JSONToDB();
        js.insertEvent(dataToSendParam, urlDB, "FireEvent hacia OCP");

        /** Codificacion URL para el mensaje en formato JSON. */
        String queryString = "param=" + URLEncoder.encode(dataToSendParam, charset)+"&type="+URLEncoder.encode(dataToSendType, charset);

        //Realizamos el POST para informar de evento a OCP
        String servletResponse = ProduceEvent.postData(URLRemoteServlet, queryString);
        
        return servletResponse;

    }
    
    
    public static String DataRegisterBuilder(String param[], String registro) throws Exception{
        
        // param[2] es event --> viene como fire pero queremos que sea FireEvent, asi que lo cambio lo primero.
        param[2] = registro; //Puesto que nos registramos a cualquier FirePlan y en pruebas tambien a FireEvent
 
        // cambiamos la variable que viene como 'produce' desde mapa.js a 'register'
        param[1] = "register"; 

        // "id="+Integer.parseInt(param[0]) --> En principio como nos queremos registrar a una entidad en genera (FirePlan) y no a la de un evento en particu$
        String dataToRegister = "type="+param[1]+"&entity="+param[2]+"&url="+URLServletConsume+"&id="; 

        //System.out.println("\nSaca: "+dataToRegister);

        //Realizamos el POST para registrarnos a entidad
        String servletResponse = ProduceEvent.postData(URLRemoteServlet, dataToRegister);

        return servletResponse;

    }
    
    public static String DB_url() throws Exception{
       
        String postData = "nada=";
        String servletRes = ProduceEvent.postData(URLconnectDB, postData);
        return servletRes;
        
    }
    
    public static void main(String[] args){

        /* Para comprobar corecto fuincionamiento de métodos
        String param[] = {"1", "produce", "FireEvent", "big", "description", "Fire", "38.3245", "-2", "45.3423"};
        try{
            BuildStringData.DataProduceBuilder(param);
        }catch(Exception e){
            System.err.println("Error: "+e);
        }*/
    }
    
}
