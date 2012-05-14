/*
 * $Header: /cvsroot/securityfilter/securityfilter/src/share/org/securityfilter/realm/SimpleSecurityRealmBase.java,v 1.7 2003/01/06 00:17:25 maxcooper Exp $
 * $Revision: 1.7 $
 * $Date: 2003/01/06 00:17:25 $
 *
 * ====================================================================
 * The SecurityFilter Software License, Version 1.1
 *
 * (this license is derived and fully compatible with the Apache Software
 * License - see http://www.apache.org/LICENSE.txt)
 *
 * Copyright (c) 2002 SecurityFilter.org. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by
 *        SecurityFilter.org (http://www.securityfilter.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "SecurityFilter" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact license@securityfilter.org .
 *
 * 5. Products derived from this software may not be called "SecurityFilter",
 *    nor may "SecurityFilter" appear in their name, without prior written
 *    permission of SecurityFilter.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE SECURITY FILTER PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
package security;

import gsi.project.*;
import gsi.rest.Connection;
import java.security.*;
import java.sql.*;
import java.util.Date;
import org.json.me.*;
import org.securityfilter.realm.*;

/**
 * Security realm base class.
 * This class insulates you from having to create or process Principal objects.
 * You can implement a realm by overriding the two methods that neither take or
 * return a Principal object and this class does the conversions for you.
 *
 * @author Max Cooper (max@maxcooper.com)
 * @author Juan Luis Molina
 * @version $Revision: 1.7 $ $Date: 2003/01/06 00:17:25 $
 */
public class MySecurityRealm implements SecurityRealmInterface{
	
	/**
	 * Authenticate a user.
	 *
	 * @param username a username
	 * @param password a plain text password, as entered by the user
	 * @return true if the username/password combination is valid, false otherwise
	 */
	public boolean booleanAuthenticate(String username, String password){
		boolean autenticado = false;
		String pass = MD5(password);
		
		if(Constantes.DB.equals("hsqldb")){
			try{
				String url = Connection.getURL();
				String usuarioAux = Connection.connect(url + "user/" + username + "/" + pass);
				JSONArray usuario = new JSONArray(usuarioAux);
				
				if(usuario.length() == 1){
					autenticado = true;
					String tipoUsuario = usuario.getJSONObject(0).getString("user_type");
					String latitud = usuario.getJSONObject(0).getString("latitud");
					String longitud = usuario.getJSONObject(0).getString("longitud");
					String descripcion = usuario.getJSONObject(0).getString("real_name");
					String informacion = usuario.getJSONObject(0).getString("email");
					String planta = usuario.getJSONObject(0).getString("planta");
					Connection.connect(url + "insertar/" + tipoUsuario + "/" + username + "/" +
						descripcion + "/" + informacion + "/" + latitud + "/" + longitud + "/" + planta);
				}
			}catch(Exception ex){
				System.out.println("Excepcion: " + ex);
			}
		}else{
			try{
				Class.forName(Constantes.DB_DRIVER);
				java.sql.Connection conexion = DriverManager.getConnection(Constantes.DB_URL,Constantes.DB_USER,Constantes.DB_PASS);
				Statement s = conexion.createStatement();
				ResultSet rs = s.executeQuery(SQLQueries.user(username, pass));
				if(rs.next()){
					autenticado = true;
					String date = new java.sql.Timestamp(new Date().getTime()).toString();
					String tipoUsuario = rs.getString(2);
					String descripcion = rs.getString(3);
					String informacion = rs.getString(4);
					float latitud = rs.getFloat(5);
					float longitud = rs.getFloat(6);
					int planta = rs.getInt(7);
					
					ResultSet rs2 = s.executeQuery(SQLQueries.preInsertar(username));
					int estado = 1;
					if(rs2.next()){
						int id2 = rs2.getInt(1);
						estado = rs2.getInt(2);
						s.executeUpdate(SQLQueries.postPreInsertar(date, id2));
					}
					s.executeUpdate(SQLQueries.insertar(tipoUsuario, username, descripcion, informacion, latitud, longitud, estado, date, planta));
				}
				conexion.close();
			}catch(Exception ex){
				System.out.println("Excepcion: " + ex);
			}
		}
		return autenticado;
	}
	
	/**
	 * Devuelde el hash MD5 de la contrasenna del usuario
	 * 
	 * @param valor Contrasenna del usuario
	 * @return Contrasenna codificada
	 */
	private String MD5(String valor){
		String hash = "";
		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(valor.getBytes("UTF-8"));
			byte[] valorHash = md5.digest();
			int[] valorHash2 = new int[16];
			for(int i = 0; i < valorHash.length; i++){
				valorHash2[i] = new Integer(valorHash[i]);
				if(valorHash2[i] < 0){
					valorHash2[i] += 256;
				}
				hash += (Integer.toHexString(valorHash2[i]));
			}
		}catch(Exception ex){}

		return hash;
	}

	/**
	 * Test for role membership.
     *
     * @param username The name of the user
     * @param rolename name of a role to test for membership
     * @return true if the user is in the role, false otherwise
     */
	public boolean isUserInRole(String username, String rolename){
		boolean rol = false;
		
		if(Constantes.DB.equals("hsqldb")){
			try{
				String url = Connection.getURL();
				String rolUsuarioAux = Connection.connect(url + "userRole/" + username);
				JSONArray rolUsuario = new JSONArray(rolUsuarioAux);
				
				if(rolUsuario.getJSONObject(0).getString("user_type").equals(rolename)){
					rol = true;
				}
			}catch(JSONException ex){
				System.out.println("Excepcion: " + ex);
			}
		}else{
			try{
				Class.forName(Constantes.DB_DRIVER);
				java.sql.Connection conexion = DriverManager.getConnection(Constantes.DB_URL,Constantes.DB_USER,Constantes.DB_PASS);
				Statement s = conexion.createStatement();
				ResultSet rs = s.executeQuery(SQLQueries.userRole(username));
				if(rs.next()){
					if(rs.getString(2).equals(rolename)){
						rol = true;
					}
				}
				conexion.close();
			}catch(Exception ex){
				System.out.println("Excepcion: " + ex);
			}
		}
		return rol;
	}

	/**
	 * Authenticate a user.
	 *
	 * @param username a username
	 * @param password a plain text password, as entered by the user
	 * @return a Principal object representing the user if successful, false otherwise
	 */
	public Principal authenticate(String username, String password){
		if(booleanAuthenticate(username, password)){
			return new SimplePrincipal(username);
		}else{
			return null;
		}
	}
	
	/**
	 * Test for role membership.
	 *
	 * Use Principal.getName() to get the username from the principal object.
	 *
	 * @param principal Principal object representing a user
	 * @param rolename name of a role to test for membership
	 * @return true if the user is in the role, false otherwise
	 */
	public boolean isUserInRole(Principal principal, String rolename){
		String username = null;
		if(principal != null){
			username = principal.getName();
		}
		return isUserInRole(username, rolename);
	}
}

// ----------------------------------------------------------------------------
// EOF