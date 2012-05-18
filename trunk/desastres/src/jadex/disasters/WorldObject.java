package disasters;

import disasters.caronte.Entorno;
import disasters.desastres.Environment;
import jadex.bridge.IComponentIdentifier;

public class WorldObject{
	/** Nombre */
	protected String name;
	/** Tipo */
	protected String type;
	/** Posicion */
	protected Position pos;
	/** Info */
	protected String info;
	/** id */
	protected int id;
	/** componentID */
	protected IComponentIdentifier agentId;

	/**
	 * Constructor de objeto
	 * 
	 * @param name Nombre
	 * @param type Tipo
	 * @param pos Posicion
	 * @param info Informacion
	 */
	public WorldObject(String name, String type, Position pos, String info, IComponentIdentifier agentId){
		//Comprobamos que el tipo del agente sea correcto.
		boolean ent = false;
		boolean env = false;
		try{
			Class.forName("disasters.caronte.Entorno");
			ent = Entorno.AGENTES.contains(type);
		}catch(ClassNotFoundException ex){}
		try{
			Class.forName("disasters.desastres.Environment");
			env = Environment.AGENTES.contains(type) || Environment.EVENTOS.contains(type);
		}catch(ClassNotFoundException ex){}
		
		assert ent || env;
		this.name = name;
		this.type = type;
		this.pos = pos;
		this.info = info;
		this.agentId = agentId;
	}

	/**
	 * Devuelve el nombre
	 * 
	 * @return Nombre
	 */
	public String getName(){
		return name;
	}

	/**
	 * Devuelve el tipo
	 * 
	 * @return Tipo
	 */
	public String getType(){
		return type;
	}

	/**
	 * Devuelve la posicion
	 * 
	 * @return Posicion
	 */
	public Position getPosition(){
		return pos;
	}

	/**
	 * Establece la posicion
	 * 
	 * @param pos Posicion
	 */
	public void setPosition(Position pos){
		this.pos = pos;
	}

	/**
	 * Devuelve la info
	 * 
	 * @return Informacion
	 */
	public String getInfo(){
		return info;
	}

	/**
	 * 
	 * Establece la info
	 * 
	 * @param info Informacion
	 */
	public void setInfo(String info){
		this.info = info;
	}

	/**
	 * Devuelve el id
	 * 
	 * @return Identificador
	 */
	public int getId(){
		return id;
	}

	/**
	 * Establece el id
	 * 
	 * @param id Identificador
	 */
	public void setId(int id){
		this.id = id;
	}
	
	public IComponentIdentifier getAgentId(){
		return agentId;
	}

	public void setAgentId(IComponentIdentifier agentId){
		this.agentId = agentId;
	}
}