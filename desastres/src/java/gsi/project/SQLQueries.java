package gsi.project;

/**
 *
 * @author Juan Luis Molina
 */
public class SQLQueries{
	public static String user(String username, String pass){
		return "SELECT u.id, t.tipo, u.nombre_real, u.correo, u.latitud, u.longitud, u.planta FROM usuarios u, tipos_usuarios t " +
			"WHERE u.nombre_usuario = '" + username + "' AND u.password = '" + pass + "' AND u.tipo_usuario = t.id";
	}
	
	public static String preInsertar(String username){
		return "SELECT id, estado FROM catastrofes WHERE nombre = '" + username + "' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')";
	}
	
	public static String postPreInsertar(String date, int id2){
		return "UPDATE catastrofes SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased'), modificado = '" + date + "' WHERE id = " + id2;
	}
	
	public static String insertar(String tipoUsuario, String username, String descripcion, String informacion, float latitud, float longitud, int estado, String date, int planta){
		return "INSERT INTO catastrofes(marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud, direccion, estado, size, traffic, idAssigned, fecha, usuario, planta) " +
			"VALUES((SELECT id FROM tipos_marcadores WHERE tipo_marcador = 'resource'), (SELECT id FROM tipos_catastrofes WHERE tipo_catastrofe = '" + tipoUsuario + "'), 1, '" +
			username + "', '" + descripcion + "', '" + informacion + "', " + latitud + ", " + longitud + ", '', " + estado + ", '', '', 0, '" + date + "', 1, " + planta + ")";
	}
	
	public static String userRole(String username){
		return "SELECT u.id, t.tipo FROM usuarios u, tipos_usuarios t WHERE u.nombre_usuario = '" + username + "' AND u.tipo_usuario = t.id";
	}
	
	public static String userProject(String username){
		return "SELECT u.id, tipo, nivel FROM usuarios u, tipos_usuarios t WHERE nombre_usuario = '" + username + "' AND tipo_usuario = t.id";
	}
	
	public static String registrar(String usuario, String contra, String nombre, String email){
		return "INSERT INTO usuarios(nombre_usuario, password, tipo_usuario, nombre_real, correo, latitud, longitud, localizacion, proyecto) " +
			"VALUES('" + usuario + "', '" + contra + "', (SELECT id FROM tipos_usuarios WHERE tipo = 'citizen'), '" + nombre + "', '" + email + "', 0.0, 0.0, FALSE, 'caronte')";
	}
}