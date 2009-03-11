package jadex.desastres;


/**
 * Clase para definir localidades mediante dos puntos.
 * La esquina superior derecha e inferior izquierda.
 * 
 * @author aebeda
 * 
 */
public class Location {
	
	private static String name;
	private static Position esd; //Esquina superior derecha 
	private static Position eii; //Esquina inferior izquierda
	
	public Location(String name, Position esd, Position eii){
		this.name=name;
		this.esd=esd;
		this.eii=eii;
		
	}
	
	public String getName(){
		return name;
	}
	public Position getESD(){
		return esd;
	}
	public Position getEII(){
		return eii;
	}
	
	
}
