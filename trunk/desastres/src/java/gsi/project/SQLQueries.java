package gsi.project;

/**
 * Queries SQL usadas en la autenticaci&oacute;n y en el registro de usuarios.
 * 
 * @author Juan Luis Molina Nogales
 */
public class SQLQueries{
	/**
	 * Devuelve los datos de un usuario.
	 * 
	 * @param username nombre de usuario
	 * @param pass contrase&ntilde;a
	 * @return resultado
	 */
	public static String user(String username, String pass){
		return "SELECT U.ID, TIPO, NOMBRE_REAL, CORREO, LATITUD, LONGITUD, PLANTA FROM USUARIOS U, TIPOS_USUARIOS T " +
			"WHERE NOMBRE_USUARIO = '" + username + "' AND PASSWORD = '" + pass + "' AND TIPO_USUARIO = T.ID";
	}
	
	/**
	 * Selecciona el id de un usuario.
	 * 
	 * @param username nombre de usuario
	 * @return resultado
	 */
	public static String preInsertar(String username){
		return "SELECT ID, ESTADO FROM CATASTROFES WHERE NOMBRE = '" + username + "' AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')";
	}
	
	/**
	 * Actualiza el estado de un usuario.
	 * 
	 * @param date fecha
	 * @param id identificador
	 * @return resultado
	 */
	public static String postPreInsertar(String date, int id){
		return "UPDATE CATASTROFES SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'), MODIFICADO = '" + date + "' WHERE ID = " + id;
	}
	
	/**
	 * Guarda un marcador en la base de datos.
	 * 
	 * @param tipoUsuario tipo de usuario
	 * @param username nombre de usuario
	 * @param descripcion descripci&oacute;n
	 * @param informacion informaci&oacute;n
	 * @param latitud latitud
	 * @param longitud longitud
	 * @param estado estado
	 * @param date fecha
	 * @param planta planta
	 * @return resultado
	 */
	public static String insertar(String tipoUsuario, String username, String descripcion, String informacion, float latitud, float longitud, int estado, String date, int planta){
		return "INSERT INTO CATASTROFES(MARCADOR, TIPO, CANTIDAD, NOMBRE, DESCRIPCION, INFO, LATITUD, LONGITUD, DIRECCION, ESTADO, SIZE, TRAFFIC, IDASSIGNED, FECHA, USUARIO, PLANTA) " +
			"VALUES((SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource'), (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = '" + tipoUsuario + "'), 1, '" +
			username + "', '" + descripcion + "', '" + informacion + "', " + latitud + ", " + longitud + ", '', " + estado + ", '', '', 0, '" + date + "', 1, " + planta + ")";
	}
	
	/**
	 * Devuelve el rol de un usuario.
	 * 
	 * @param username nombre de usuario
	 * @return resultado
	 */
	public static String userRole(String username){
		return "SELECT U.ID, TIPO, NIVEL FROM USUARIOS U, TIPOS_USUARIOS T WHERE NOMBRE_USUARIO = '" + username + "' AND TIPO_USUARIO = T.ID";
	}
	
	/**
	 * Guarda un usuario en la base de datos.
	 * 
	 * @param usuario nombre de usuario
	 * @param contra contrase&ntilde;a
	 * @param nombre nombre real
	 * @param email correo electronico
	 * @return resultado
	 */
	public static String registrar(String usuario, String contra, String nombre, String email){
		return "INSERT INTO USUARIOS(NOMBRE_USUARIO, PASSWORD, TIPO_USUARIO, NOMBRE_REAL, CORREO, LATITUD, LONGITUD, LOCALIZACION, PROYECTO) " +
			"VALUES('" + usuario + "', '" + contra + "', (SELECT ID FROM TIPOS_USUARIOS WHERE TIPO = 'citizen'), '" + nombre + "', '" + email + "', 0.0, 0.0, FALSE, 'caronte')";
	}
}