package gsi.project;

import gsi.rest.Connection;
import java.beans.*;
import java.io.Serializable;
import java.sql.*;
import org.json.me.*;

/**
 *
 * @author Juan Luis Molina
 */
public class UserBean implements Serializable{
	public static final String NOMBRE = "nombre";
	public static final String ID = "id";
	public static final String ROL = "rol";
	public static final String NIVEL_MSG = "nivelMsg";
	
	private PropertyChangeSupport propertySupport;

	private String nombre, rol;
	private int id, nivelMsg;

	public UserBean(){
		propertySupport = new PropertyChangeSupport(this);
		nombre = "";
		id = 0;
		rol = "citizen";
		nivelMsg = 0;
	}

	public String getNombre(){
		return nombre;
	}

	public void setNombre(String value){
		String oldValue = nombre;
		nombre = value;
		propertySupport.firePropertyChange(NOMBRE, oldValue, nombre);
		if(value != null && value.equals("") == false && value.equals(oldValue) == false){
			if(Constantes.DB.equals("hsqldb")){
				try{
					String url = Connection.getURL();
					String proyectoAux = Connection.connect(url + "userProject/" + value);
					JSONArray proyecto = new JSONArray(proyectoAux);
					setId(proyecto.getJSONObject(0).getInt("id"));
					setRol(proyecto.getJSONObject(0).getString("rol"));
					setNivelMsg(proyecto.getJSONObject(0).getInt("level"));
				}catch(JSONException ex){
					System.out.println("Excepcion: " + ex);
				}
			}else{
				try{
					Class.forName(Constantes.DB_DRIVER);
					java.sql.Connection conexion = DriverManager.getConnection(Constantes.DB_URL,Constantes.DB_USER,Constantes.DB_PASS);
					java.sql.Statement s = conexion.createStatement();
					ResultSet rs = s.executeQuery(SQLQueries.userProject(value));
					if(rs.next()){
						setId(rs.getInt(1));
						setRol(rs.getString(2));
						setNivelMsg(rs.getInt(3));
					}
					conexion.close();
				}catch(Exception ex){
					System.out.println("Excepcion: " + ex);
				}
			}
		}
	}

	public int getId(){
		return id;
	}

	public void setId(int value){
		int oldValue = id;
		id = value;
		propertySupport.firePropertyChange(ID, oldValue, id);
	}

	public String getRol(){
		return rol;
	}

	public void setRol(String value){
		String oldValue = rol;
		rol = value;
		propertySupport.firePropertyChange(ROL, oldValue, rol);
	}

	public int getNivelMsg(){
		return nivelMsg;
	}

	public void setNivelMsg(int value){
		int oldValue = nivelMsg;
		nivelMsg = value;
		propertySupport.firePropertyChange(NIVEL_MSG, oldValue, nivelMsg);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener){
		propertySupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener){
		propertySupport.removePropertyChangeListener(listener);
	}
}