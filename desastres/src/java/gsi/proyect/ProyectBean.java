package gsi.proyect;

import java.beans.*;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Juan Luis Molina
 */
public class ProyectBean implements Serializable {

	public static final String PROYECT = "proyect";
	public static final String ROL = "rol";
	public static final String NIVEL_MSG = "nivelMsg";

	private String proyect;
	private String rol;
	private int nivelMsg;

	private PropertyChangeSupport propertySupport;

	private HashMap<String,Integer> relaciones = new HashMap<String,Integer>();

	public ProyectBean() {
		propertySupport = new PropertyChangeSupport(this);
		proyect = "caronte";
		nivelMsg = 0;
		
		String roles[][] = {{"citizen","1"},{"agent","4"},{"administrator","5"},
			{"otherStaff","3"},{"nurse","4"},{"relative","2"},{"assistant","4"},{"gerocultor","4"}};
		for(int i=0; i<roles.length; i++){
			relaciones.put(roles[i][0],Integer.parseInt(roles[i][1]));
		}
	}

	public String getProyect() {
		return proyect;
	}

	public void setProyect(String value) {
		String oldValue = proyect;
		proyect = value;
		propertySupport.firePropertyChange(PROYECT, oldValue, proyect);
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String value) {
		String oldValue = rol;
		rol = value;
		propertySupport.firePropertyChange(ROL, oldValue, rol);

		setNivelMsg(nivel(rol));
	}

	public int getNivelMsg() {
		return nivelMsg;
	}

	public void setNivelMsg(int value) {
		int oldValue = nivelMsg;
		nivelMsg = value;
		propertySupport.firePropertyChange(NIVEL_MSG, oldValue, nivelMsg);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}

	private int nivel(String rol){
		int nivel = 0;
		if(relaciones.containsKey(rol)){
			nivel = relaciones.get(rol);
		}
		return nivel;
	}
}
