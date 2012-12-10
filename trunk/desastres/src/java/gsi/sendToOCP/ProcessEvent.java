package gsi.sendToOCP;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Process event.
 * 
 * @author Lara Lorna Jim&eacute;nez
 */
public class ProcessEvent extends HttpServlet{

	private static boolean registered = false;

	/**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		try{
			String id = request.getParameter("id");
			String type = request.getParameter("type");
			String event = request.getParameter("event");
			String size = request.getParameter("size");
			String description = request.getParameter("description");
			String name = request.getParameter("name");
			String longitude = request.getParameter("longitude");
			String floor = request.getParameter("floor");
			String latitude = request.getParameter("latitude");
			String date = request.getParameter("date");

			String[] paramsPost = {id, type, event, size, description, name, longitude, floor, latitude, date};

			String remoteServletResponseData = "";
			String remoteServletResponseRegister = "";

			try{
				ProduceEvent p = new ProduceEvent();

				// type = produce --> Enviamos el evento generado
				remoteServletResponseData = BuildStringData.DataProduceBuilder(paramsPost); // Creamos el JSON data a enviar y lo enviamos, obteniendo la respuesta del servlet a nuestro POST.

				// Si estaba registrado a la entidad (en nuestro caso FirePlan) no hacer nada.
				if(!registered){
					// Si no estaba registrado, registrar!
					registered = true;
					// Enviamos mensaje para registrarnos a consumir un evento determinado
					remoteServletResponseRegister = BuildStringData.DataRegisterBuilder(paramsPost);
				}

			}catch(Exception e){
				System.err.println("\nError en invocacion a objeto de ProduceEvent que hace el POST al servlet remoto: " + e);
			}

			System.out.println("\nDatos recibidos en ProcessEvent desde AJAX mapa.js-->");
			System.out.println("El id del evento generado es: " + id);
			System.out.println("El tipo de situacion es: " + type);
			System.out.println("El evento es: " + event); //<-- esto lo he cambiado para enviar en el POST a servlet remoto FireEvent en vez de fire.
			System.out.println("El tamanno es: " + size);
			System.out.println("La descripcion es: " + description);
			System.out.println("El nombre es: " + name);
			System.out.println("La longitud es: " + longitude);
			System.out.println("La planta es: " + floor);
			System.out.println("La latitud es: " + latitude);
			System.out.println("La fecha es: " + date);

			System.out.println("\nEl resultado del POST produce hecho hacia servlet remoto desde ProduceEvent y devuelto a ProcessEvent es: " + remoteServletResponseData);
			System.out.println("\nEl resultado del POST register hecho hacia servlet remoto desde ProduceEvent y devuelto a ProcessEvent es: " + remoteServletResponseRegister); // si no devuelve nada es porque ya se ha registrado para FirePlan

			out.print(request.getParameterMap());
			out.flush();
		}catch(Exception e){
			System.err.println("Excepcion en ProcessEvent: " + e);
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