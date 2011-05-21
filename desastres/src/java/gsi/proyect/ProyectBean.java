package gsi.proyect;

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author Juan Luis Molina
 */
public class ProyectBean implements Serializable {

	public static final String PROYECT = "proyect";

	private String proyect;

	private PropertyChangeSupport propertySupport;

	public ProyectBean() {
		propertySupport = new PropertyChangeSupport(this);
		proyect = "caronte";
	}

	public String getProyect() {
		return proyect;
	}

	public void setProyect(String value) {
		String oldValue = proyect;
		proyect = value;
		propertySupport.firePropertyChange(PROYECT, oldValue, proyect);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}
}
