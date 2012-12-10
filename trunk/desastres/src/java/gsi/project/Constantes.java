package gsi.project;

/**
 * Constantes del proyecto, servidor y base de datos
 * 
 * @author Juan Luis Molina Nogales
 */
public class Constantes {
	/** Proyecto (caronte o desastres). */
	public static final String PROJECT = "caronte";
	/** Puerto de Tomcat. */
	public static final int SERVER_PORT = 8080;
	/** Base de datos (hsqldb o mysql). */
	public static final String DB = "hsqldb";
	/** Puerto de la base de datos. */
	public static final int DB_PORT = 3306;
	/** (local o remote). */
	private static final String DB_MODE = "local";
	
	private static final String HSQLDB_DRIVER = "org.hsqldb.jdbcDriver";
	private static final String HSQLDB_URL = "jdbc:hsqldb:file:";
	private static final String HSQLDB_USER = "sa";
	private static final String HSQLDB_PASS = "";
	
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String MYSQL_LOCAL_URL = "jdbc:mysql://localhost:" + DB_PORT + "/" + PROJECT;
	private static final String MYSQL_REMOTE_URL = "jdbc:mysql://shannon.gsi.dit.upm.es:" + DB_PORT + "/" + PROJECT + "_db";
	private static final String MYSQL_LOCAL_USER = "root";
	private static final String MYSQL_REMOTE_USER = "jlmolina";
	private static final String MYSQL_LOCAL_PASS = "";
	private static final String MYSQL_REMOTE_PASS = "NafDuJ4VhG6dcmtP";
	private static final String MYSQL_URL = DB_MODE.equals("local") ? MYSQL_LOCAL_URL : MYSQL_REMOTE_URL;
	private static final String MYSQL_USER = DB_MODE.equals("local") ? MYSQL_LOCAL_USER : MYSQL_REMOTE_USER;
	private static final String MYSQL_PASS = DB_MODE.equals("local") ? MYSQL_LOCAL_PASS : MYSQL_REMOTE_PASS;
	
	/** Driver de la base de datos. */
	public static final String DB_DRIVER = DB.equals("hsqldb") ? HSQLDB_DRIVER : MYSQL_DRIVER;
	/** URL de la base de datos. */
	public static final String DB_URL = DB.equals("hsqldb") ? HSQLDB_URL : MYSQL_URL;
	/** Usuario de la base de datos. */
	public static final String DB_USER = DB.equals("hsqldb") ? HSQLDB_USER : MYSQL_USER;
	/** Contrase&ntilde;a de la base de datos. */
	public static final String DB_PASS = DB.equals("hsqldb") ? HSQLDB_PASS : MYSQL_PASS;
}