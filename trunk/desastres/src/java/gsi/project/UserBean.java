package gsi.project;

import gsi.rest.Connection;
import java.beans.*;
import java.io.Serializable;
import java.sql.*;
import org.json.me.*;

/**
 * JavaBean del usuario.
 * 
 * @author Juan Luis Molina Nogales
 */
public class UserBean implements Serializable{
	/** Nombre. */
	public static final String NOMBRE = "nombre";
	/** Identificador. */
	public static final String ID = "id";
	/** Rol. */
	public static final String ROL = "rol";
	/** Nivel de mensaje. */
	public static final String NIVEL_MSG = "nivelMsg";
	
	private PropertyChangeSupport propertySupport;

	private String nombre, rol;
	private int id, nivelMsg;

	/**
	 * Constructor.
	 */
	public UserBean(){
		propertySupport = new PropertyChangeSupport(this);
		nombre = "";
		id = 0;
		rol = "citizen";
		nivelMsg = 0;
	}

	/**
	 * Getter de nombre.
	 * 
	 * @return nombre
	 */
	public String getNombre(){
		return nombre;
	}

	/**
	 * Setter de nombre.
	 * 
	 * @param value nombre
	 */
	public void setNombre(String value){
		String oldValue = nombre;
		nombre = value;
		propertySupport.firePropertyChange(NOMBRE, oldValue, nombre);
		if(value != null && value.equals(oldValue) == false){
			if(Constantes.DB.equals("hsqldb")){
				try{
					String url = Connection.URL_BASE;
					String proyectoAux = Connection.connect(url + "userRole/" + value);
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
					java.sql.Connection conexion = DriverManager.getConnection(Constantes.DB_URL,
							Constantes.DB_USER, Constantes.DB_PASS);
					java.sql.Statement s = conexion.createStatement();
					ResultSet rs = s.executeQuery(SQLQueries.userRole(value));
					if(rs.next()){
						setId(rs.getInt(1));
						setRol(rs.getString(2));
						setNivelMsg(rs.getInt(3));
					}
					conexion.close();
				}catch(ClassNotFoundException ex){
					System.out.println("ClassNotFoundExcepcion: " + ex);
				}catch(SQLException ex){
					System.out.println("SQLExcepcion: " + ex);
				}
			}
		}
	}

	/**
	 * Getter de id
	 * 
	 * @return id
	 */
	public int getId(){
		return id;
	}

	/**
	 * Setter de id.
	 * 
	 * @param value id 
	 */
	public void setId(int value){
		int oldValue = id;
		id = value;
		propertySupport.firePropertyChange(ID, oldValue, id);
	}

	/**
	 * Getter de rol.
	 * 
	 * @return rol
	 */
	public String getRol(){
		return rol;
	}

	/**
	 * Setter de rol.
	 * 
	 * @param value rol
	 */
	public void setRol(String value){
		String oldValue = rol;
		rol = value;
		propertySupport.firePropertyChange(ROL, oldValue, rol);
	}

	/**
	 * Getter de nivel de mensaje.
	 * 
	 * @return nivel de mensaje
	 */
	public int getNivelMsg(){
		return nivelMsg;
	}

	/**
	 * Setter de nivel de mensaje.
	 * 
	 * @param value nivel de mensaje
	 */
	public void setNivelMsg(int value){
		int oldValue = nivelMsg;
		nivelMsg = value;
		propertySupport.firePropertyChange(NIVEL_MSG, oldValue, nivelMsg);
	}

	/**
	 * Adds a PropertyChangeListener.
	 * 
	 * @param listener listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener){
		propertySupport.addPropertyChangeListener(listener);
	}

	/**
	 * Removes a PropertyChangeListener.
	 * 
	 * @param listener listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener){
		propertySupport.removePropertyChangeListener(listener);
	}
}