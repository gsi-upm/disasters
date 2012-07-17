package gsi.project;

/**
 * Queries SQL usadas en la autenticacion y en el registro de usuarios
 * 
 * @author Juan Luis Molina
 */
public class SQLQueries{
	/**
	 * 
	 * @param username
	 * @param pass
	 * @return 
	 */
	public static String user(String username, String pass){
		return "SELECT U.ID, TIPO, NOMBRE_REAL, CORREO, LATITUD, LONGITUD, PLANTA FROM USUARIOS U, TIPOS_USUARIOS T " +
			"WHERE NOMBRE_USUARIO = '" + username + "' AND PASSWORD = '" + pass + "' AND TIPO_USUARIO = T.ID";
	}
	
	/**
	 * 
	 * @param username
	 * @return 
	 */
	public static String preInsertar(String username){
		return "SELECT ID, ESTADO FROM CATASTROFES WHERE NOMBRE = '" + username + "' AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')";
	}
	
	/**
	 * 
	 * @param date
	 * @param id
	 * @return 
	 */
	public static String postPreInsertar(String date, int id){
		return "UPDATE CATASTROFES SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'), MODIFICADO = '" + date + "' WHERE ID = " + id;
	}
	
	/**
	 * 
	 * @param tipoUsuario
	 * @param username
	 * @param descripcion
	 * @param informacion
	 * @param latitud
	 * @param longitud
	 * @param estado
	 * @param date
	 * @param planta
	 * @return 
	 */
	public static String insertar(String tipoUsuario, String username, String descripcion, String informacion, float latitud, float longitud, int estado, String date, int planta){
		return "INSERT INTO CATASTROFES(MARCADOR, TIPO, CANTIDAD, NOMBRE, DESCRIPCION, INFO, LATITUD, LONGITUD, DIRECCION, ESTADO, SIZE, TRAFFIC, IDASSIGNED, FECHA, USUARIO, PLANTA) " +
			"VALUES((SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource'), (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = '" + tipoUsuario + "'), 1, '" +
			username + "', '" + descripcion + "', '" + informacion + "', " + latitud + ", " + longitud + ", '', " + estado + ", '', '', 0, '" + date + "', 1, " + planta + ")";
	}
	
	/**
	 * 
	 * @param username
	 * @return 
	 */
	public static String userRole(String username){
		return "SELECT U.ID, TIPO, NIVEL FROM USUARIOS U, TIPOS_USUARIOS T WHERE NOMBRE_USUARIO = '" + username + "' AND TIPO_USUARIO = T.ID";
	}
	
	/**
	 * 
	 * @param usuario
	 * @param contra
	 * @param nombre
	 * @param email
	 * @return 
	 */
	public static String registrar(String usuario, String contra, String nombre, String email){
		return "INSERT INTO USUARIOS(NOMBRE_USUARIO, PASSWORD, TIPO_USUARIO, NOMBRE_REAL, CORREO, LATITUD, LONGITUD, LOCALIZACION, PROYECTO) " +
			"VALUES('" + usuario + "', '" + contra + "', (SELECT ID FROM TIPOS_USUARIOS WHERE TIPO = 'citizen'), '" + nombre + "', '" + email + "', 0.0, 0.0, FALSE, 'caronte')";
	}
}