/*
 * $Header: /cvsroot/securityfilter/securityfilter/src/example/org/securityfilter/example/realm/TrivialSecurityRealm.java,v 1.3 2003/10/25 10:49:04 maxcooper Exp $
 * $Revision: 1.3 $
 * $Date: 2003/10/25 10:49:04 $
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

package org.securityfilter.example.realm;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.securityfilter.example.Constants;
import org.securityfilter.realm.SimpleSecurityRealmBase;
import jadex.desastres.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Calendar;
import org.json.me.*;

/**
 * Trivial implementation of the SecurityRealmInterface.
 *
 * There is one user: username is 'username', password is 'password'
 * And this user is in one role: 'inthisrole'
 *
 * @author Max Cooper (max@maxcooper.com)
 * @version $Revision: 1.3 $ $Date: 2003/10/25 10:49:04 $
 */
public class TrivialSecurityRealm extends SimpleSecurityRealmBase {

   private String exampleProperty;

   /**
    * Authenticate a user.
    *
    * Implement this method in a subclass to avoid dealing with Principal objects.
    *
    * @param username a username
    * @param password a plain text password, as entered by the user
    *
    * @return null if the user cannot be authenticated, otherwise a Pricipal object is returned
    */
	public boolean booleanAuthenticate(String username, String password) {
		boolean autenticado = false;
		try {
			String pass = MD5(password);
			String usuarioAux = Connection.connect("http://localhost:8080/desastres/rest/user/" + username + "/" + pass);
			JSONArray usuario = new JSONArray(usuarioAux);
			
			if(usuario.length() == 1) {
				autenticado = true;
				Calendar cal = Calendar.getInstance();
				String date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH)
						+ " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
				Connection.connect("http://localhost:8080/desastres/rest/post/type=user&name=" + username + "&quantity=1&latitud=0&longitud=0" + "&fecha=" + date + "&idAssigned=0");
			}
		} catch (Exception ex) {
			System.out.println("Excepcion en TrivialSecurityRealm.booleanAuthenticate(): " + ex);
		}

	   /*return (
         (Constants.VALID_USERNAME.equals(username) && Constants.VALID_PASSWORD.equals(password)) ||
         (Constants.VALID_USERNAME2.equals(username) && Constants.VALID_PASSWORD.equals(password)) ||
         (Constants.VALID_USERNAME3.equals(username) && Constants.VALID_PASSWORD.equals(password)) ||
         (Constants.VALID_USERNAME4.equals(username) && Constants.VALID_PASSWORD.equals(password))
      );*/
	   return autenticado;
   }

	private String MD5(String valor){
		String hash = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(valor.getBytes("UTF-8"));
			byte[] valorHash = md5.digest();
			int[] valorHash2 = new int[16];
			for (int i = 0; i < valorHash.length; i++) {
				valorHash2[i] = new Integer(valorHash[i]);
				if (valorHash2[i] < 0) {
					valorHash2[i] += 256;
				}
				hash += (Integer.toHexString(valorHash2[i]));
			}
		} catch (Exception ex) {}

		return hash;
	}

   /**
    * Test for role membership.
    *
    * Implement this method in a subclass to avoid dealing with Principal objects.
    *
    * @param username The name of the user
    * @param role name of a role to test for membership
    * @return true if the user is in the role, false otherwise
    */
   public boolean isUserInRole(String username, String role) {
	   boolean rol = false;
	   try {
			String rolUsuarioAux = Connection.connect("http://localhost:8080/desastres/rest/userRole/" + username);
			JSONArray rolUsuario = new JSONArray(rolUsuarioAux);
			
			if(rolUsuario.getJSONObject(0).getString("user_type").equals(role)) {
				rol = true;
			}
		} catch (JSONException ex) {
			System.out.println("Excepcion: " + ex);
		}

	   /*return (
         (Constants.VALID_USERNAME.equals(username) && Constants.VALID_ROLE.equals(role)) ||
		 (Constants.VALID_USERNAME2.equals(username) && Constants.VALID_ROLE.equals(role)) ||
		 (Constants.VALID_USERNAME3.equals(username) && Constants.VALID_ROLE2.equals(role)) ||
		 (Constants.VALID_USERNAME4.equals(username) && Constants.VALID_ROLE3.equals(role))
      );*/
	   return rol;
   }

   /**
    * Setter for exampleProperty to deomonstrate setting realm properties from config file.
    *
    * This has no effect other than printing a message when the property is set.
    *
    * @param value example property value
    */
   public void setExampleProperty(String value) {
      exampleProperty = value;
      System.out.println(this.getClass().getName() + ": exampleProperty set to \'" + value + "\'");
   }

   /**
    * Getter for exampleProperty.
    *
    * @return the value of exampleProperty
    */
   public String getExampleProperty() {
      return exampleProperty;
   }
}

// ----------------------------------------------------------------------------
// EOF
