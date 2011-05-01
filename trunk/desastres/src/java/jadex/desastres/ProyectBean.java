package jadex.desastres;

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author Juan Luis Molina
 */
public class ProyectBean implements Serializable {

	public static final String PROYECT = "proyect";
	public static final String NUMBER = "number";

	private String proyect;
	private int number;

	private PropertyChangeSupport propertySupport;

	public ProyectBean() {
		propertySupport = new PropertyChangeSupport(this);
		proyect = "disasters";
	}

	public String getProyect() {
		return proyect;
	}

	public void setProyect(String value) {
		String oldValue = proyect;
		proyect = value;
		propertySupport.firePropertyChange(PROYECT, oldValue, proyect);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int value) {
		int oldValue = number;
		number = value;
		propertySupport.firePropertyChange(NUMBER, oldValue, number);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}
}
