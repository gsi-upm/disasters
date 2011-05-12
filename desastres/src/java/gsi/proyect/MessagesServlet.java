package gsi.proyect;

import jadex.desastres.Connection;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.me.*;

/**
 *
 * @author Juan Luis Molina
 */
public class MessagesServlet extends HttpServlet {

	//private final Timestamp fechaIni = new Timestamp(new Date().getTime());
	//private static Timestamp fecha;
	private static int index;

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
		int action = new Integer(request.getParameter("action"));
		
		try {
			String resultado;
			if (index == 0) {
				resultado = Connection.connect("http://localhost:8080/desastres/rest/messages");
			} else {
				resultado = Connection.connect("http://localhost:8080/desastres/rest/messages/id/" + index);
			}
			JSONArray mensajes = new JSONArray(resultado);

			//if (mensajes.length() != 0) {
				out.println("</div>");
				for (int i = 0; i < mensajes.length(); i++) {
					JSONObject msg = mensajes.getJSONObject(i);
					out.println("<p>" + msg.getString("mensaje") + "</p>");
				}
				out.println("<div id=\"messages" + (action + 1) + "\">");
				index += mensajes.length();
			//}
		} catch (Exception ex) {
			System.out.println("Excepcion en MessageServlet: " + ex);
		} finally {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);


	}

	/**
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";

	}// </editor-fold>
}
