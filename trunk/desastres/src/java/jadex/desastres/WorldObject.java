package jadex.desastres;

public class WorldObject {
	
	/** Nombre */
	protected String name;
	
	/** Tipo */
	protected String type;
	
	/** Posición */
	protected Position pos;
	
	/** Info */
	protected String info;
	
	/** id */
	protected int id;
	
	/**
	 * Constructor
	 */
	public WorldObject(String name, String type, Position pos, String info){
		
		//Comprobamos que el tipo del agente sea correcto.
		assert type.equals(Environment.AMBULANCIA)
						||type.equals(Environment.BOMBERO)
						||type.equals(Environment.POLICIA)
						||type.equals(Environment.INUNDACION)
						||type.equals(Environment.TERREMOTO)
						||type.equals(Environment.FUEGO)
						||type.equals(Environment.HERIDO_LEVE)
						||type.equals(Environment.HERIDO_GRAVE)
						||type.equals(Environment.HERIDO_ATRAPADO)
						||type.equals(Environment.HERIDO_MUERTO);
		
		this.name = name;
		this.type = type;
		this.pos = pos;
		this.info = info;
	}
	
	/**
	 * Devuelve el nombre
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Devuelve el tipo
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Devuelve la posición
	 */
	public Position getPosition(){
		return pos;
	}
	
	/**
	 * Devuelve la info
	 */
	public String getInfo(){
		return info;
	}
	
	/**
	 * Establece la posición
	 */
	public void setPosition(Position pos){
		this.pos = pos;
	}
	
	/**
	 * Establece la info
	 */
	public void setInfo(String info){
		this.info = info;
	}
	
	/**
	 * Establece el id
	 */
	public void setId(int id){
		this.id = id;
	}
	

	/**
	 * Devuelve el id
	 */
	public int getId(){
		return id;
	}
	
	
}