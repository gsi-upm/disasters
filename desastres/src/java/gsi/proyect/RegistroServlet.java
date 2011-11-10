package gsi.proyect;

import gsi.rest.Connection;
import java.io.*;
import java.security.MessageDigest;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.me.*;

/**
 *
 * @author Juan Luis Molina
 */
public class RegistroServlet extends HttpServlet{
	private static final String URL = Connection.getURL();

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String usuario = request.getParameter("user");
			String contraAux = request.getParameter("pass");
			String nombre = request.getParameter("nombre");
			String email = request.getParameter("email");
			String contra = MD5(contraAux);

			String rolAux = Connection.connect(URL + "userRole/" + usuario);
			JSONArray rol = new JSONArray(rolAux);

			if(rol.length() == 0){
				String registro = Connection.connect(URL + "registrar/" + usuario + "/" + contra + "/" + nombre + "/" + email);
				out.print("ok");
			}else{
				out.print("no");
			}
		} catch (JSONException ex) {
			System.out.println("Excepcion en RegistroServlet.processRequest(): " + ex);
		}finally{
			out.close();
		}
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

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
/**
 * Handles the HTTP <code>GET</code> method.
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
@Override
	protected

void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);


}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */

		@Override
		protected void

doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);


}

	/**
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */

		@Override
		public String

getServletInfo() {
		return "Short description";

}// </editor-fold>
}
