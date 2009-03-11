package gsi.disasters;

import java.io.PrintWriter;
import jess.*;
import java.util.*;
import org.json.me.*;
import java.sql.Timestamp;



/**
 * Launches jess and creates markers
 * @author julio camarero
 * @version 1.0
**/
public class ReteDisasterDB {
	/**
	 * Object rete
	**/
	private Rete rete;
	/**
	 * Marker in work memory
	**/
	private WorkingMemoryMarker marcador;
        
        /**
	 * Disasters registered
	**/
        private static Hashtable disasters;
	
        /**
	 * Resources registered
	**/
        private static Hashtable list;
        
        /**
	 * URL for Disasters2.0 application REST interface
	**/
        private final static String URL_BASE = "http://localhost:8080/Disasters/rest/";
        
         /**
	 * TimeStamp for the last JSON Request
	**/
        private String ahora;
        
        /**
	 * PrintWriter to show answers in web application
	**/
        private PrintWriter out;
        
        /**
	 * Private constructor
	 * @throws JessException if Jess fails
	**/
        public ReteDisasterDB(PrintWriter out) throws JessException {
	this.out= out;	
            rete = new Rete();
		
		// Carga reglas
              
                System.out.println("carga reglas");
		rete.batch("gsi/bc/evaluateDisaster2.clp");

		// Carga datos iniciales
                System.out.println("carga datos iniciales");
		try{getResources();}
                catch(JSONException e){System.out.println("JSON Error at ReteDisasterDB: "+e);}
                
                System.out.println("addAll");
                rete.addAll(list.values());
                
               
                rete.reset();
                rete.watchAll();
                
                
		// Marca para poder hacer reset a este punto
		//marcador = rete.mark();
                rete.run();
                //rete.runUntilHalt();
	}

	/**
	 * Creates a list of resources from a Database 
	 * @return ArraList with Resources
         * @throws JSONException if JSON data are not correct
	**/
	private Hashtable getResources() throws JSONException {
		list = new Hashtable();
		
                System.out.println("before URL_BASE");
                String freeResources = Connection.connect(URL_BASE+"free");
                System.out.println("URL_BASE: "+freeResources);
                JSONArray resources = new JSONArray(freeResources);
                System.out.println("freeResources");
                
                //Por cada recurso disponible:
               for (int i=0;i<resources.length();i++){
                    JSONObject instancia = resources.getJSONObject(i);
                    			
                    
                    Resource nuevo = new Resource(
                            instancia.getInt("id"),
                            instancia.getString("type"),
                            instancia.getString("name"),
                            instancia.getString("info"),
                            instancia.getString("description"),
                            instancia.getInt("idAssigned")
                            );
                System.out.println("## New Resource: "+nuevo.getType()+" - "+nuevo.getName()+" (id:"+nuevo.getId()+") assigned:"+nuevo.getIdAssigned()+"##");
                out.println("## New Resource: "+nuevo.getType()+" - "+nuevo.getName()+" (id:"+nuevo.getId()+") assigned:"+nuevo.getIdAssigned()+"##"+"<br>");
                out.flush();//ojo a esto!!
               list.put(nuevo.getId(),nuevo);
               
               }
                
		
		return list;
	}

	/**
	 * Stops the engine
	 * @throws JessException if Jess fails
	**/
        public void stop() throws JessException {
            rete.halt();
        
        }
        
        /**
	 * Evaluates a disaster
	 * @param disaster Disaster to evaluate
	 * @throws JessException if Jess fails
	**/
	private void evaluateDisaster(Disaster disaster) throws JessException{
		//rete.resetToMark(marcador);
		System.out.println ("Evaluamos desastre...");
                out.println ("Evaluamos desastre..."+"<br>");
                rete.add(disaster);
		
                rete.run();
                //rete.runUntilHalt();
		
                Iterator it =  rete.getObjects(new Filter() {
			public boolean accept(Object o){
				return (o instanceof Assignment);	
			}
		});
		while (it.hasNext()){
			Assignment eval = (Assignment) it.next();
			System.out.println("Assignement: " + eval.getMessage());
                        out.println("Assignement: " + eval.getMessage()+"<br>");
                        
		}	
                System.out.println ("Fin de evaluacion de desastre...");
                out.println ("Fin de evaluacion de desastre..."+"<br>");

	}
       
        
        /**
	 * Disassociates a resource from a disaster
	 * @param idResource the resource id in the application
         * @param idDisaster the disaster id in the application
                  **/
        public static void disAssociate (int idResource, int idDisaster){
            Disaster dis = (Disaster)disasters.get(idDisaster);
            Resource rec = (Resource)list.get(idResource);
            
            int marker =0;
            
            if(rec.getType().equals("police")){marker = dis.getPoliceMarker();}
            if(rec.getType().equals("firemen")){marker = dis.getFiremenMarker();}
            if(rec.getType().equals("ambulance")){marker = dis.getAmbulanceMarker();}
            if(marker!=0) {Connection.connect(URL_BASE+"delete/"+marker);
                if(rec.getType().equals("police")){dis.setPoliceMarker(0);}
                if(rec.getType().equals("firemen")){dis.setFiremenMarker(0);}
                if(rec.getType().equals("ambulance")){dis.setAmbulanceMarker(0);}

            }
              
        }
        
        /**
	 * Associates a resource to a disaster
	 * @param idResource the resource id in the application
         * @param idDisaster the disaster id in the application
         * @throws JSONException if JSON data are not correct
         **/
        public static void associate (int idResource, int idDisaster) throws JSONException{
            Disaster dis = (Disaster)disasters.get(idDisaster);
            Resource rec = (Resource)list.get(idResource);
            String tipo = rec.getType();
            
            Double latitud, longitud;
            
                     
            
            //Ahora hay que comprobar si ya había alguno puesto o no
            if (tipo.equals("police")){
                if(dis.getPoliceMarker()==0){
                    //añadir el marcador al mapa
                    latitud=dis.getLatitud()+0.0005;
                    longitud=dis.getLongitud();
                     
                    String resp = Connection.connect(URL_BASE+"post/type="+tipo+"&name="+rec.getName()+"&info="+rec.getInfo()+"&description="+rec.getDescription()+"&latitud="+latitud+"&longitud="+longitud+"&idAssigned="+idDisaster);
                    //recibir el id para actualizar el Marker
                    JSONObject instancia = new JSONObject (resp);
                    
                    int nuevoId = instancia.getInt("id");
                    dis.setPoliceMarker(nuevoId);
                    //System.out.println("Respuesta:"+nuevoId);
                   
                }
                else{
                    //modificar el existente para que sume 1  put/id/quantity/add
                    Connection.connect(URL_BASE+"put/"+dis.getPoliceMarker()+"/add");
                    
                }
            }
            if (rec.getType().equals("firemen")){
             if(dis.getFiremenMarker()==0){
                    //añadir el marcador al mapa
                    latitud=dis.getLatitud()+0.00025;
                     longitud=dis.getLongitud()-0.0005;
                     
                    String resp = Connection.connect(URL_BASE+"post/type="+tipo+"&name="+rec.getName()+"&info="+rec.getInfo()+"&description="+rec.getDescription()+"&latitud="+latitud+"&longitud="+longitud+"&idAssigned="+idDisaster);
                    //recibir el id para actualizar el Marker
                    JSONObject instancia = new JSONObject (resp);
                    
                    int nuevoId = instancia.getInt("id");
                    dis.setFiremenMarker(nuevoId);
                    //System.out.println("Respuesta:"+nuevoId);
                   
                }
                else{
                    //modificar el existente para que sume 1  put/id/quantity/add
                    Connection.connect(URL_BASE+"put/"+dis.getFiremenMarker()+"/add");
                    
                }
            
            
            }
            if (rec.getType().equals("ambulance")){
            
             if(dis.getAmbulanceMarker()==0){
                    //añadir el marcador al mapa
                    latitud=dis.getLatitud()+0.00025;
                    longitud=dis.getLongitud()+0.0005;
                     
                    String resp = Connection.connect(URL_BASE+"post/type="+tipo+"&name="+rec.getName()+"&info="+rec.getInfo()+"&description="+rec.getDescription()+"&latitud="+latitud+"&longitud="+longitud+"&idAssigned="+idDisaster);
                    //recibir el id para actualizar el Marker
                    JSONObject instancia = new JSONObject (resp);
                    
                    int nuevoId = instancia.getInt("id");
                    dis.setAmbulanceMarker(nuevoId);
                    //System.out.println("Respuesta:"+nuevoId);
                   
                }
                else{
                    //modificar el existente para que sume 1  put/id/quantity/add
                    Connection.connect(URL_BASE+"put/"+dis.getAmbulanceMarker()+"/add");
                    
                }
            
            }
            System.out.println (idResource+rec.getType()+" - associating - "+idDisaster+dis.getType());
            
            
            
        }
        
        
        /**
	 * Starts the engine. Find disasters and assign resources.
	 * @throws JessException if Jess fails
         * @throws JSONException if JSON data are not correct
         **/
        public void run() throws JSONException, JessException  {
        	
               
            
            //Esto LA PRIMERA VEZ
                disasters = new Hashtable();
		String ahora;
                //recibo el json 
                String eventos = Connection.connect(URL_BASE+"events");
                JSONArray desastres = new JSONArray(eventos);
		System.out.println(desastres);
                
                String victimas = Connection.connect(URL_BASE+"people");
                JSONArray personas = new JSONArray(victimas);
		
                ahora = new Timestamp(new Date().getTime()).toString();     
		
		//Por cada desastre:
               for (int i=0;i<desastres.length();i++){
                    JSONObject instancia = desastres.getJSONObject(i);
                    Disaster nuevo = new Disaster(
                            instancia.getInt("id"),
                            instancia.getString("type"),
                            instancia.getString("name"),
                            instancia.getString("info"),
                            instancia.getString("description"),
                            instancia.getString("address"),
                            new Double(instancia.getString("longitud")),
                            new Double (instancia.getString("latitud")),
                            instancia.getString("state"),
                            instancia.getString("size"),
                            instancia.getString("traffic"),
                            0,0,0,0 
                            );
               System.out.println("### New Disaster: "+nuevo.getType()+" - "+nuevo.getName()+" (id:"+nuevo.getId()+") ##");
	       out.println("### New Disaster: "+nuevo.getType()+" - "+nuevo.getName()+" (id:"+nuevo.getId()+") ##"+"<br>");
	       
               disasters.put(nuevo.getId(),nuevo);
               evaluateDisaster(nuevo);
               }
               
               //Por cada herido:
		for (int i=0;i<personas.length();i++){
                    JSONObject instancia = personas.getJSONObject(i);
                    People nuevo = new People(
                            
                            instancia.getInt("id"),
                            instancia.getString("type"),
                            instancia.getString("name"),
                            instancia.getString("info"),
                            instancia.getString("description"),
                            instancia.getInt("idAssigned"),
                            instancia.getInt("quantity")
                            );
                    //si no está asignado a nadie o a alguien que no existe se sale 
                    if (nuevo.getIdAssigned()==0||!disasters.containsKey(nuevo.getIdAssigned())) continue;      
                           
                    Disaster dis = (Disaster)disasters.get(nuevo.getIdAssigned());
                    System.out.println("*** Updating Disaster Victims for "+dis.getName());
                    out.println("*** Updating Disaster Victims for "+dis.getName()+"<br>");
                    
                    if(nuevo.getType().equals("slight")){dis.setSlight(nuevo.getQuantity());}
                    if(nuevo.getType().equals("serious")){dis.setSerious(nuevo.getQuantity());}
                    if(nuevo.getType().equals("dead")){dis.setDead(nuevo.getQuantity());}
                    if(nuevo.getType().equals("trapped")){dis.setTrapped(nuevo.getQuantity());}

                   
                    evaluateDisaster(dis);
               }
        }
        public void call() throws JSONException, JessException  {
                
                //ESTO PARA ACTUALIZAR
              
                    
                    
               
                
                //recibo el json actualizador
                            
                //aqui hay que recibirlos según la fehca....
                String eventos = Connection.connect(URL_BASE+"events/modified/"+ahora);
                JSONArray desastres = new JSONArray(eventos);
		
                String victimas = Connection.connect(URL_BASE+"people/modified/"+ahora);
                JSONArray personas = new JSONArray(victimas);
                
               ahora = new Timestamp(new Date().getTime()).toString();
               
                 for (int i=0;i<desastres.length();i++){
                    JSONObject instancia = desastres.getJSONObject(i);
                    
                    Disaster nuevo = new Disaster(
                            instancia.getInt("id"),
                            instancia.getString("type"),
                            instancia.getString("name"),
                            instancia.getString("info"),
                            instancia.getString("description"),
                            instancia.getString("address"),
                            new Double(instancia.getString("longitud")),
                            new Double (instancia.getString("latitud")),
                            instancia.getString("state"),
                            instancia.getString("size"),
                            instancia.getString("traffic"),
                            0,0,0,0 
                            );
                  
                    if(disasters.containsKey(nuevo.getId())){
                    //actualizo el desastre existente
                        System.out.println("*** Updating Disaster... "+nuevo.getName()+" - "+nuevo.getState());
                        out.println("*** Updating Disaster... "+nuevo.getName()+" - "+nuevo.getState()+"<br>");
                        Disaster viejo = (Disaster)disasters.get(nuevo.getId());
                        viejo.setType(nuevo.getType());
                        viejo.setName(nuevo.getName());
                        viejo.setInfo(nuevo.getInfo());
                        viejo.setDescription(nuevo.getDescription());
                        viejo.setAddress(nuevo.getAddress());
                        viejo.setLongitud(nuevo.getLongitud());
                        viejo.setLatitud(nuevo.getLatitud());
                        viejo.setState(nuevo.getState());
                        viejo.setSize(nuevo.getSize());
                        viejo.setTraffic(nuevo.getTraffic());
                        evaluateDisaster(viejo); //creo que no hace falta
                                               
                    }
                    else{
                    System.out.println("### New Disaster: "+nuevo.getType()+" - "+nuevo.getName()+" (id:"+nuevo.getId()+") ##");
	            out.println("### New Disaster: "+nuevo.getType()+" - "+nuevo.getName()+" (id:"+nuevo.getId()+") ##"+"<br>");
	            
                    disasters.put(nuevo.getId(),nuevo);
                    evaluateDisaster(nuevo);
                    }
                    
               }
                
                //Por cada herido:
		for (int i=0;i<personas.length();i++){
                    JSONObject instancia = personas.getJSONObject(i);
                    People nuevo = new People(
                            instancia.getInt("id"),
                            instancia.getString("type"),
                            instancia.getString("name"),
                            instancia.getString("info"),
                            instancia.getString("description"),
                            instancia.getInt("idAssigned"),
                            instancia.getInt("quantity")
                            );
                    if(nuevo.getState().equals("erased")){nuevo.setQuantity(0);}
                    if (nuevo.getIdAssigned()==0) continue;      
                     
                        
                    Disaster dis = (Disaster)disasters.get(nuevo.getIdAssigned());
                    System.out.println("*** Updating Disaster Victims for "+dis.getName()+" -- "+nuevo.getQuantity()+nuevo.getType());
                    out.println("*** Updating Disaster Victims for "+dis.getName()+" -- "+nuevo.getQuantity()+nuevo.getType()+"<br>");
                    
                    if(nuevo.getType().equals("slight")){dis.setSlight(nuevo.getQuantity());}
                    if(nuevo.getType().equals("serious")){dis.setSerious(nuevo.getQuantity());}
                    if(nuevo.getType().equals("dead")){dis.setDead(nuevo.getQuantity());}
                    if(nuevo.getType().equals("trapped")){dis.setTrapped(nuevo.getQuantity());}
                   
                   
                    evaluateDisaster(dis);
               }            
            
		
        
        }
	/**
	 * Main method
	 * @param args main programm parameters. (no expected)
	 * @throws JessException if Jess fails
	**/
	public static final void main(String [] args) throws JessException, JSONException {
		/*ReteDisasterDB reteBD = new ReteDisasterDB();
                reteBD.run();*/
		
		
	}
}


