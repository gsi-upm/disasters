package gsi.proyect;

import jadex.desastres.Connection;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.me.*;

/**
 *
 * @author Juan Luis Molina
 */
public class MessagesServlet extends HttpServlet {

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
		int nivel = new Integer(request.getParameter("nivel"));
		int index = new Integer(request.getParameter("index"));
		try {
			String resultado;
			resultado = Connection.connect("http://localhost:8080/desastres/rest/messages/" + nivel + "/id/" + index);
			String idIni = "0";
			
			if(index == -1){
				JSONArray mensaje = new JSONArray(resultado);
				if(mensaje.length() > 0){
					idIni = mensaje.getJSONObject(0).getString("id");
				}

				String fecha = new Timestamp(new Date().getTime() - 300000).toString(); // 5 minutos antes
				resultado = Connection.connect("http://localhost:8080/desastres/rest/messages/" + nivel + "/date/" + fecha);
			}

			JSONArray mensajes = new JSONArray(resultado);

			for (int i = 0; i < mensajes.length(); i++) {
				JSONObject msg = mensajes.getJSONObject(i);
				out.println("<p>" + msg.getString("mensaje") + "</p>");
				
				if(i == (mensajes.length()-1)){
					if(index == -1){
						out.println("<hr>");
					}
					out.println("<span>" + msg.getString("id") + "</span>");
				}
			}

			if((mensajes.length()==0) && (index==-1)){
				out.println("<span>" + idIni + "</span>");
			}
			
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
