package gsi.proyect;

import jadex.desastres.Connection;
import jadex.desastres.Environment;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.me.*;

/**
 *
 * @author Juan Luis Molina
 */
public class ProyectServlet extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			//String proyecto = getServletContext().getInitParameter("proyect");
			String usuario = request.getParameter("user");
			String proyectoAux = Connection.connect(Environment.URL + "userProyect/" + usuario);
			JSONArray proyecto = new JSONArray(proyectoAux);
			String proyect = proyecto.getJSONObject(0).getString("proyect");
			String rol = proyecto.getJSONObject(0).getString("rol");
			response.sendRedirect("/desastres/index.jsp?proyect=" + proyect + "&rol=" + rol);
		} catch (JSONException ex) {
			System.out.println("Excepcion en ProyectServlet.processRequest(): " + ex);
		}finally{
			out.close();
		}
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
