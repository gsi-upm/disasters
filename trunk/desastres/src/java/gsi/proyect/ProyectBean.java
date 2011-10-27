package gsi.proyect;

import jadex.desastres.Connection;
import java.beans.*;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;

/**
 *
 * @author Juan Luis Molina
 */
public class ProyectBean implements Serializable {
	public static final String PROYECT = "proyect";
	public static final String ROL = "rol";
	public static final String NIVEL_MSG = "nivelMsg";
	public static final String NOMBRE_USUARIO = "nombreUsuario";
	public static final String ID = "id";

	private String proyect;
	private String rol;
	private int nivelMsg;
	private String nombreUsuario;
	private int id;

	private PropertyChangeSupport propertySupport;

	public ProyectBean() {
		propertySupport = new PropertyChangeSupport(this);
		proyect = "caronte";
		rol = "citizen";
		nivelMsg = 0;
		nombreUsuario = "";
		id = 0;
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
	}

	public int getNivelMsg() {
		return nivelMsg;
	}

	public void setNivelMsg(int value) {
		int oldValue = nivelMsg;
		nivelMsg = value;
		propertySupport.firePropertyChange(NIVEL_MSG, oldValue, nivelMsg);
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
				String proyectoAux = Connection.connect("http://localhost:8080/desastres/rest/userProyect/" + value);
				JSONArray proyecto = new JSONArray(proyectoAux);
				setProyect(proyecto.getJSONObject(0).getString("proyect"));
				setRol(proyecto.getJSONObject(0).getString("rol"));
				setNivelMsg(proyecto.getJSONObject(0).getInt("level"));
				setId(proyecto.getJSONObject(0).getInt("id"));
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

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}
}
