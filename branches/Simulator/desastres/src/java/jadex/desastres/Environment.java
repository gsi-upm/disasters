package jadex.desastres;


import java.util.*;
import org.json.me.*;
import java.sql.Timestamp;
import java.lang.*;
import java.beans.PropertyChangeListener;

import jadex.util.collection.MultiCollection;
import jadex.util.SimplePropertyChangeSupport;
import jadex.runtime.Plan;

/**
 * 
 * Clase para modelar el entorno, proporcionando m�todos
 * para interactuar con �l.
 * 
 * @author aebeda
 * 
 */
public class Environment {
	//------constantes ----
	
	/** Los nombres de los agentes */
	public static final String AMBULANCIA="ambulance";
	public static final String BOMBERO="firemen";
	public static final String POLICIA="police";
	public static final String  CENTRAL="central";
	
	public static final String HERIDO_LEVE="slight";
	public static final String HERIDO_GRAVE="serious";
	public static final String HERIDO_MUERTO="dead";
	public static final String HERIDO_ATRAPADO="trapped";
	
		
	/** Los nombres de los eventos provocados sobre el mapa*/
	public static final String FUEGO="fire";
	public static final String TERREMOTO="collapse";
	public static final String INUNDACION="flood";
	
	public static final String DISASTERS = "http://localhost:8080/Disasters/rest/";    
	
	//------- atributos ---
	
	private final int tiempoJSON = 5000;
	private final int tiempoMove = 500;
	private TimerJSON temporizador;
	private TimerMove tempoMover;
	
	
	private String ahora;
	/** Agentes (nombre -> WorldObject)*/
	protected HashMap agentes;
	
	/** Numero de agentes creados, no tienen xq estar activos*/
	/** No ha sido usado*/
	protected Integer numAgentes;
	
	/** Eventos (id -> WorldObject)*/
	protected HashMap disasters;
	
	protected HashMap people;
	
	/** Numero de eventos creados, no tienen xq estar activos 
	 *  Usado para poder dar un nombre distinto a los eventos en las 
	 *  tablas Hash. 
	 */
	protected Integer numEventos;
	
	/** Agentes y Eventos (Pos -> WorldObject)*/
	protected MultiCollection objetos;
	
	/** Posici�n del centro de Madrid*/
	Position centroMadrid = new Position(40.45,-3.6);
	
	/** Generador aleatorio*/
	Random rnd = new Random();
	
	/** Objeto para notificar de cambios*/
	public SimplePropertyChangeSupport pcs;
	
	private int tablon;
	
	
	//---------------------
	
	/**
	 * Constructor
	 */
	public Environment() {
		this.agentes = new HashMap();
		this.disasters = new HashMap(); //fuegos,heridos,...
		this.people = new HashMap();
		this.objetos = new MultiCollection();
		this.pcs = new SimplePropertyChangeSupport(this);
		numAgentes = numEventos = 0;
		this.temporizador = new TimerJSON (tiempoJSON, this);
		this.tempoMover = new TimerMove (tiempoMove);
	//metemos los eventos y los heridos.
		// las ambulancias solo responden a desastres con heridos
		// bomberos responde a desastres y heridos ATRAPADOS
		// policia responde a desastres 
	
	
	
		 //Esto LA PRIMERA VEZ - recibo el json 
        
		try{
		String eventos = Connection.connect(DISASTERS+"events");
        JSONArray desastres = new JSONArray(eventos);

        String victimas = Connection.connect(DISASTERS+"people");
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
                    instancia.getString("traffic")
                    
                    );
       System.out.println("## ENV: New Disaster: "+nuevo.getType()+" - "+nuevo.getName()+" (id:"+nuevo.getId()+") ##");
       disasters.put(nuevo.getId(),nuevo);
       
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
                    instancia.getInt("quantity"),
                    instancia.getString("state")
                    );
            //si no est� asignado a nadie o a alguien que no existe pasa al siguiente
            if (nuevo.getIdAssigned()==0||!disasters.containsKey(nuevo.getIdAssigned())) continue;      
            
            people.put(nuevo.getId(),nuevo);
            Disaster dis = (Disaster)disasters.get(nuevo.getIdAssigned());
            System.out.println("## ENV: Updating Disaster Victims for "+dis.getName());
            
            if(nuevo.getType().equals("slight")){dis.setSlight(nuevo);}
            if(nuevo.getType().equals("serious")){dis.setSerious(nuevo);}
            if(nuevo.getType().equals("dead")){dis.setDead(nuevo);}
            if(nuevo.getType().equals("trapped")){dis.setTrapped(nuevo);}
          
            
       		}
       
		}
		catch(JSONException e){System.out.println("Error with JSON **** : "+e);}
		
		//Hacer una llamada cada pocos segundos al metodo actualiza()
		temporizador.start();
		
      }
	
	
	public void actualiza(){
		temporizador.reset();
		//ESTO PARA ACTUALIZAR - recibo el json actualizador
                   
        
       
       try{
       	String eventos = Connection.connect(DISASTERS+"events/modified/"+ahora);
        JSONArray desastres = new JSONArray(eventos);

        String victimas = Connection.connect(DISASTERS+"people/modified/"+ahora);
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
                    instancia.getString("traffic")
                     
                    );
          
            if(disasters.containsKey(nuevo.getId())){
            //si ya existia actualizo el desastre existente
                System.out.println("## ENV: Updating Disaster... "+nuevo.getName()+" - "+nuevo.getState());
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
               
                //si se ha eliminado lo borro directamente
                if(viejo.getState().equals("erased")){disasters.remove(viejo.getId());}
                                       
            }
            else{
            //System.out.println("### New Disaster: "+nuevo.getType()+" - "+nuevo.getName()+" (id:"+nuevo.getId()+") ##");
            disasters.put(nuevo.getId(),nuevo);
            
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
                    instancia.getInt("quantity"),
                    instancia.getString("state")
                    );
            if(nuevo.getState().equals("erased")){continue;}
            
            if (nuevo.getIdAssigned()==0) {
            	if(!people.containsKey(nuevo.getId())){continue;}
            		
            	//cuando ya existia y tengo que actualizar el desastres borrando sus heridos
            	else{
            		System.out.print("## ENV: Voy a desasociar herido cuya id es: "+nuevo.getId());
            		
            		People antiguo = (People)people.get(nuevo.getId());
            		int idDesastre = antiguo.getIdAssigned();
            		people.remove(antiguo.getId());
            		System.out.println(" del desastre: "+idDesastre);
            		Disaster extraido = (Disaster)disasters.get(idDesastre);
            		disasters.remove(idDesastre);
            		
            		 Disaster insertado = new Disaster(
                             extraido.getId(),
                             extraido.getType(),
                             extraido.getName(),
                             extraido.getInfo(),
                             extraido.getDescription(),
                             extraido.getAddress(),
                             extraido.getLongitud(),
                             extraido.getLatitud(),
                             extraido.getState(),
                             extraido.getSize(),
                             extraido.getTraffic()
                             );
            		 disasters.put(insertado.getId(),insertado);
            		 
            		
            		
            		}
            	
            	    
            	} 
             
            else{   
	            Disaster dis = (Disaster)disasters.get(nuevo.getIdAssigned());
	            System.out.println("## ENV: Updating Disaster Victims for "+dis.getName()+" -- "+nuevo.getQuantity()+nuevo.getType());
	            people.put(nuevo.getId(),nuevo);
	            if(nuevo.getType().equals("slight")){dis.setSlight(nuevo);}
	            if(nuevo.getType().equals("serious")){dis.setSerious(nuevo);}
	            if(nuevo.getType().equals("dead")){dis.setDead(nuevo);}
	            if(nuevo.getType().equals("trapped")){dis.setTrapped(nuevo);}
            	}
           
            
         }      
       
        }
        catch(JSONException e){System.out.println("Error with JSON *****"+e);}
		
        temporizador.run();
		
	}
	
	protected static Environment instance;
	
	
	/**
	 * Obtener una instancia del entorno, 
	 * para as� poder interactuar sobre �l.
	 * NOTA:se puede crear un parque de bomberos, desde el cual salgan, => Si es un
	 * bombero, tiene posici�n inicial fija.
	 */
	public static Environment getInstance(String tipo, String nombre,Position pos){
		//La primera vez que se llama a este m�todo (el agente Environment), 
		//instance vale null.
		if(instance == null){
			instance = new Environment();
		}
		if((tipo != null) && (nombre != null)){
			instance.addWorldObject(tipo,nombre,pos,null);
		}
		return instance;
	}
	
	/**
	 * Borra la instancia (Resetea el entorno).
	 */
	public static void clearInstance(){
		instance = null;
	}
	
	/**
	 * A�ade un objeto al entorno
	 */
	public void addWorldObject(String type, String name, Position position, String info){
		
		//System.out.println("Ejecuto addWorldObject ###" + position +" type: " + type + " nombre: "+name);
		//De momento no usamos el atributo info.
		//La posici�n siempre se especifica.
		WorldObject wo = new WorldObject(name,type,position,null);
		
		if(type.equals(AMBULANCIA)||type.equals(BOMBERO)||type.equals(POLICIA)){
			//REST -> cree el recurso
			String longitud = String.valueOf(position.getY());
			String latitud = String.valueOf(position.getX());
			System.out.println("LLamada a REST creando agente...");
			String resultado = Connection.connect(Environment.DISASTERS+"post/type="+type+
					"&name="+name+"&info="+info+"&latitud="+latitud+"&longitud="+longitud);
			
			try{
			//JSON -> guardo el id
			JSONObject idJson = new JSONObject(resultado);
			 int id = idJson.getInt("id");
			 wo.setId(id);
			System.out.println ("Id del objeto creado: "+id);
			agentes.put(name,wo);  //Si es un pir�namo, se a�ade a la tabla de agentes
			objetos.put(position,wo);   //Y tambi�n a la de elementos del mundo
			numAgentes++;
			}
			catch(Exception e){
				System.out.println("Error json: "+e);
			}
			
			
		}
		if(type.equals(CENTRAL)){
			agentes.put(name,wo);  //Si es una central, se a�ade a la tabla de agentes
			objetos.put(position,wo);   //Y tambi�n a la de elementos del mundo
			numAgentes++;
		}	
	}
	
	/**
	 * Cambia la posici�n de un agente.
	 * Los eventos no se mueven de posici�n.
	 */
	public void go(String name,Position pos){
		Position oldPos;
		
		WorldObject wo;
		synchronized(this){	//No deben varios agentes tocar las tablas Hash a la vez
			//Obtenemos el agente de la tabla Hash agentes, dado su nombre.
			 wo = getAgent(name);
			
			oldPos = wo.getPosition(); //Obtenemos la posici�n del agente antes de desplazarlo.
			objetos.remove(oldPos,wo); //Eliminamos el agente de su posici�n (de la colecci�n Objetos)
		}
			
			
			
			
		
		synchronized(this){
			
			
			wo.setPosition(pos);//Actualizamos la posici�n al objeto agente
			
			objetos.put(pos,wo); //A�adimos el objeto, con la posci�n renovada
			removeAgent(name);   //Actualizamos la tabla Hash de agentes.
			agentes.put(name,wo);
		
		}
		
		
		
		//Avisamos para el modo de evaluaci�n din�mico de posici�n
		//de que hemos variado una poscici�n.
		pcs.firePropertyChange("cambio_de_posicion",oldPos,pos);
	}
	
	
	public void andar (String name, Position inicial, Position dest, int desastre, int herido)throws InterruptedException{

		Double x1= inicial.getX();
		Double x2= dest.getX();
		Double y1= inicial.getY();
		Double y2= dest.getY();

		Double x=x2-x1;
		Double y=y2-y1;
		Double pendiente=y/x; 
		int velocidad = 50; //velocidad inversa, cuanto m�s grande m�s despacio
		
		Double pasoX=0.21/velocidad;
		Double pasoY=(-1)*(0.25/velocidad)*pendiente;
		
		Boolean derecha;
		Boolean arriba;
		
		
		//Tiempo entre cambios de posici�n
		Integer tiempo = 200;
		Position PosMedia;

		if(x>0){
		 	derecha = true;
		}else{
			derecha=false;
		}
		if(y>0){
		 	arriba = true;
		}else{
			arriba=false;
		}
		//System.out.println("Derecha "+derecha);
		//System.out.println("Arriba "+arriba);
		//El punto destino est� a la derecha del origen
		if(derecha){
			if (arriba){
				while ((x1<x2)|(y1<y2)){
					if((x1<x2)){
						x1=x1+pasoX;
						//System.out.println("va a la derecha ");
						}
						
					if((y1<y2)){
						y1=y1-pasoY;
						//System.out.println("sube ");
						}
					PosMedia=new Position(x1,y1);
					go(getAgent(name).getName(),PosMedia);
					//pinta agente
					//System.out.println("( "+x1+" , "+y1+" )/n");
	 				pinta(desastre, herido, x1, y1);
	 			
					
				}
				x1=x2;
				y1=y2;
				PosMedia=new Position(x1,y1);
				go(getAgent(name).getName(),PosMedia);
				//pinta agente
				//System.out.println("( "+x1+" , "+y1+" )/n");
 				pinta(desastre, herido, x1, y1);
			}else{
				while ((x1<x2)|(y1>y2)){
					if((x1<x2)){
						x1=x1+pasoX;
						//System.out.println("va a la derecha ");
						}
					if((y1>y2)){
						y1=y1-pasoY;
						//System.out.println("baja ");
						}
					PosMedia=new Position(x1,y1);
					go(getAgent(name).getName(),PosMedia);
					//pinta agente
					//System.out.println("( "+x1+" , "+y1+" )/n");
	 				pinta(desastre, herido, x1, y1);
					
				}
				x1=x2;
				y1=y2;
				PosMedia=new Position(x1,y1);
				go(getAgent(name).getName(),PosMedia);
				//pinta agente
				//System.out.println("( "+x1+" , "+y1+" )/n");
 				pinta(desastre, herido, x1, y1);
			}
				
		
		}else{ //El punto est� a la izquierda
			if (arriba){
				while ((x1>x2)|(y1<y2)){
					if((x1>x2)){
						x1=x1-pasoX;//System.out.println("va a la izquierda");
						}
					if((y1<y2)){
						y1=y1+pasoY;//System.out.println("sube ");
						}
					PosMedia=new Position(x1,y1);
					go(getAgent(name).getName(),PosMedia);
					//pinta agente
					//System.out.println("( "+x1+" , "+y1+" )/n");
	 				pinta(desastre, herido,  x1, y1);
					
				}
				x1=x2;
				y1=y2;
				PosMedia=new Position(x1,y1);
				go(getAgent(name).getName(),PosMedia);
				//pinta agente
				//System.out.println("( "+x1+" , "+y1+" )/n");
 				pinta(desastre, herido, x1, y1);
			}else{
				while ((x1>x2)|(y1>y2)){
					if((x1>x2)){
						x1=x1-pasoX;//System.out.println("va a la izquierda");
						}
					if((y1>y2)){
						y1=y1+pasoY;//System.out.println("baja ");
						}
					PosMedia=new Position(x1,y1);
					go(getAgent(name).getName(),PosMedia);
					//pinta agente
					//System.out.println("( "+x1+" , "+y1+" )/n");
	 				pinta(desastre, herido, x1, y1);
					
				}
				x1=x2;
				y1=y2;
				PosMedia=new Position(x1,y1);
				go(getAgent(name).getName(),PosMedia);
				//pinta agente
				//System.out.println("( "+x1+" , "+y1+" )/n");
 				pinta(desastre, herido, x1, y1);
			}
		}
				
		
}

	
	
	
	
	
	/**
	 * Pinta el movimiento de un agente en el mapa mediante REST
	 */
	public void pinta(int id, int idHerido, Double latitud, Double longitud)throws InterruptedException
	{
		
		//System.out.println("LLamada a REST modificando latitud y longitud...");
		String resultado1 = Connection.connect(Environment.DISASTERS+"put/"+id+"/latitud/"+latitud);
		if(idHerido!=0){String resultado3 = Connection.connect(Environment.DISASTERS+"put/"+idHerido+"/latitud/"+latitud);}
		String resultado2 = Connection.connect(Environment.DISASTERS+"put/"+id+"/longitud/"+longitud);
		if(idHerido!=0){String resultado4 = Connection.connect(Environment.DISASTERS+"put/"+idHerido+"/longitud/"+longitud);}
		//System.out.println("Resultado de la latitud: "+ resultado1);
		//System.out.println("Resultado de la longitud: "+ resultado2);
		Thread.sleep(2000);
		
	}
	
	
	
	
	/**
	 * Devuelve un agente
	 * dado su nombre (el nombre de los agentes es �nico).
	 */
	public synchronized WorldObject getAgent(String name){
		assert agentes.containsKey(name);
		
		return (WorldObject)agentes.get(name);
	}
	
	/**
	 * Elimina un agente
	 * dado su nombre (el nombre de los agentes es �nico).
	 */
	public synchronized WorldObject removeAgent(String name){
		assert agentes.containsKey(name);
		
		return (WorldObject)agentes.remove(name);
	}
	
	/**
	 * Devuelve la posici�n de un agente
	 * dado su nombre (el nombre de los agentes es �nico).
	 */
	public synchronized Position getAgentPosition(String name){
		assert agentes.containsKey(name);
		
		return ((WorldObject)agentes.get(name)).getPosition();
	}
	
	/**
	 * Devuelve un evento
	 * dado su id (el id de los eventos es �nico).
	 */
	public synchronized Disaster getEvent(int id){
		assert disasters.containsKey(id);
		
		return (Disaster)disasters.get(id);
	}
	
	/**
	 * Elimina un evento
	 * dado su nombre (el nombre de los eventos es �nico).
	 */
	public synchronized WorldObject removeEvent(String name){
		assert disasters.containsKey(name);
		
		return (WorldObject)disasters.remove(name);
	}	
	
	/**
	 * Devuelve la posici�n de un evento
	 * dado su nombre (concretamente su id).
	 */
	public synchronized Position getEventPosition(String name){
		assert disasters.containsKey(name);
		
		return ((WorldObject)disasters.get(name)).getPosition();
	}
	
	/**
	 * Devuelve todos los objetos que haya en una posici�n
	 */
	protected WorldObject[] getWorldObjects(Position pos){
	
		Collection col = (Collection)objetos.get(pos);
		return (WorldObject[])col.toArray(new WorldObject[col.size()]);
		
	}
	
	/**
	 * Devuelve todos los agentes
	 */
	protected WorldObject[] getAgentes(){
		Collection col = (Collection)agentes.values();
		return (WorldObject[])col.toArray(new WorldObject[col.size()]);
	}
	
	/**
	 * Devuelve todos los eventos
	 */
	protected WorldObject[] getEventos(){
		Collection col = (Collection)disasters.values();
		return (WorldObject[])col.toArray(new WorldObject[col.size()]);
	}
	
	
	/**
	 * Devuelve el n�mero total de agentes creados.
	 * @return
	 */
	public int getNumAgentes(){
		return numAgentes;
	}
	
	/**
	 * Devuelve el n�mero total de eventos creados.
	 * @return
	 */
	public int getNumEventos(){
		return numEventos;
	}
	
	/**
	 * Devuelve una posici�n aleatoria
	 * conociendo la ciudad.
	 * Puesto que de momento s�lo tenemos Madrid en la lista, 
	 * no hace falta especificar la ciudad.
	 */
	//protected Position getRandomPosition(Location loc){
	  protected Position getRandomPosition(){	
		//Las dos posiciones que se crean son las esquinas superior derecha e inferior izquierda 
		//del marco que contiene a Madrid.
		  
		Location location = new Location("Madrid",new Position(40.44,-3.66),new Position(40.39,-3.72));
		
		Position esd = (Position)location.getESD(); //Esquina superior Derecha
 		Position eii = (Position)location.getEII(); //Esquina inferior Izquierda.
		  
 		Double random1 = rnd.nextDouble();
		Double random2 = rnd.nextDouble();
		
		//Obtenemos la altura del marco, tomando la diferencia de latitudes.
		Double alturaMarco= Math.abs(esd.getY()-eii.getY() ); 
		//Obtenemos la anchura del marco, tomando la diferencia de longitudes.
		Double anchuraMarco= Math.abs(esd.getX()-eii.getX());
		
		//Obtenemos la latitud y longitud menor, para sumarles una fracci�n
		//aleatoria de la diferencia que las separa.
		Double marcoBottom = Math.min(esd.getY(), eii.getY());
		Double marcoLeft = Math.min(esd.getX(), eii.getX());
		
		Position nuevaAleatoria = new Position(marcoLeft + random1*anchuraMarco,marcoBottom + random2*alturaMarco);
		return nuevaAleatoria;
	}


	/**
	 * @return the tablon
	 */
	public int getTablon() {
		return tablon;
	}


	/**
	 * @param tablon the tablon to set
	 */
	public void setTablon(int tablon) {
		this.tablon = tablon;
	}


	
	  
	  
	  
}
