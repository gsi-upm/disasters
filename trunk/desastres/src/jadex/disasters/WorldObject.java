package disasters;

import disasters.caronte.Entorno;
import disasters.desastres.Environment;
import jadex.bridge.IComponentIdentifier;

/**
 * WorldObject.
 */
public class WorldObject{
	/** Nombre. */
	protected String name;
	/** Tipo. */
	protected String type;
	/** Posici&oacute;n. */
	protected Position pos;
	/** Info. */
	protected String info;
	/** ID. */
	protected int id;
	/** ComponentID. */
	protected IComponentIdentifier agentId;

	/**
	 * Constructor de objeto.
	 * 
	 * @param name nombre
	 * @param type tipo
	 * @param pos posici&oacute;n
	 * @param info informaci&oacute;n
	 * @param agentId ID del agente
	 */
	public WorldObject(String name, String type, Position pos, String info, IComponentIdentifier agentId){
		// Comprobamos que el tipo del agente sea correcto.
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
	 * Devuelve el nombre.
	 * 
	 * @return nombre
	 */
	public String getName(){
		return name;
	}

	/**
	 * Devuelve el tipo.
	 * 
	 * @return tipo
	 */
	public String getType(){
		return type;
	}

	/**
	 * Devuelve la posici&oacute;n.
	 * 
	 * @return posici&oacute;n
	 */
	public Position getPosition(){
		return pos;
	}

	/**
	 * Establece la posici&oacute;n.
	 * 
	 * @param pos posici&oacute;n
	 */
	public void setPosition(Position pos){
		this.pos = pos;
	}

	/**
	 * Devuelve la info.
	 * 
	 * @return informaci&oacute;n
	 */
	public String getInfo(){
		return info;
	}

	/**
	 * Establece la info.
	 * 
	 * @param info informaci&oacute;n
	 */
	public void setInfo(String info){
		this.info = info;
	}

	/**
	 * Devuelve el id.
	 * 
	 * @return identificador
	 */
	public int getId(){
		return id;
	}

	/**
	 * Establece el id.
	 * 
	 * @param id identificador
	 */
	public void setId(int id){
		this.id = id;
	}
	
	/**
	 * Devuelve el id del agente.
	 * 
	 * @return ID del agente
	 */
	public IComponentIdentifier getAgentId(){
		return agentId;
	}
	
	/**
	 * Establece el id del agente.
	 * 
	 * @param agentId ID del agente
	 */
	public void setAgentId(IComponentIdentifier agentId){
		this.agentId = agentId;
	}
}