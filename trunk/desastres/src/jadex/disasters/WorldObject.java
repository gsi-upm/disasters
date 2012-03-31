package disasters;

import disasters.caronte.Entorno;
import disasters.desastres.Environment;

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

	/**
	 * Constructor de objeto
	 * 
	 * @param name Nombre
	 * @param type Tipo
	 * @param pos Posicion
	 * @param info Informacion
	 */
	public WorldObject(String name, String type, Position pos, String info){
		//Comprobamos que el tipo del agente sea correcto.
		assert type.equals(Entorno.ENFERMERO) || type.equals(Entorno.CELADOR) ||
				type.equals(Entorno.GEROCULTOR) || type.equals(Entorno.AUXILIAR) ||
				type.equals(Entorno.RECEPCIONISTA) || type.equals(Entorno.OTRO_PERSONAL) ||
				type.equals(Entorno.CIUDADANO) || type.equals(Entorno.AMBULANCIA) ||
				type.equals(Entorno.BOMBERO) || type.equals(Entorno.POLICIA) ||
				type.equals(Environment.AMBULANCIA) || type.equals(Environment.BOMBERO) ||
				type.equals(Environment.POLICIA) || type.equals(Environment.AMBULANCIA2) ||
				type.equals(Environment.INUNDACION) || type.equals(Environment.TERREMOTO) || type.equals(Environment.FUEGO) ||
				type.equals(Environment.HERIDO_LEVE) || type.equals(Environment.HERIDO_GRAVE) ||
				type.equals(Environment.HERIDO_ATRAPADO) || type.equals(Environment.HERIDO_MUERTO);
		this.name = name;
		this.type = type;
		this.pos = pos;
		this.info = info;
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
}