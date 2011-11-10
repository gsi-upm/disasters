package gsi.proyect;

import gsi.rest.Connection;
import java.beans.*;
import java.io.Serializable;
import org.json.me.*;

/**
 *
 * @author Juan Luis Molina
 */
public class ProyectBean implements Serializable {
	public static final String NOMBRE_USUARIO = "nombreUsuario";
	public static final String ID = "id";
	public static final String ROL = "rol";
	public static final String NIVEL_MSG = "nivelMsg";
	public static final String PROYECT = "proyect";

	private static final String URL = Connection.getURL();
	private PropertyChangeSupport propertySupport;

	private String nombreUsuario, rol, proyect;
	private int id, nivelMsg;

	public ProyectBean() {
		propertySupport = new PropertyChangeSupport(this);
		nombreUsuario = "";
		id = 0;
		rol = "citizen";
		nivelMsg = 0;
		proyect = "caronte";
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String value) {
		String oldValue = nombreUsuario;
		nombreUsuario = value;
		propertySupport.firePropertyChange(NOMBRE_USUARIO, oldValue, nombreUsuario);
		if(value != null && value.equals("") == false){
			try {
				String proyectoAux = Connection.connect(URL + "userProyect/" + value);
				JSONArray proyecto = new JSONArray(proyectoAux);
				setId(proyecto.getJSONObject(0).getInt("id"));
				setRol(proyecto.getJSONObject(0).getString("rol"));
				setNivelMsg(proyecto.getJSONObject(0).getInt("level"));
				setProyect(proyecto.getJSONObject(0).getString("proyect"));
			} catch (JSONException ex) {
				System.out.println("Excepcion: " + ex);
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int value) {
		int oldValue = id;
		id = value;
		propertySupport.firePropertyChange(ID, oldValue, id);
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String value) {
		String oldValue = rol;
		rol = value;
		propertySupport.firePropertyChange(ROL, oldValue, rol);
	}

	public int getNivelMsg() {
		return nivelMsg;
	}

	public void setNivelMsg(int value) {
		int oldValue = nivelMsg;
		nivelMsg = value;
		propertySupport.firePropertyChange(NIVEL_MSG, oldValue, nivelMsg);
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
