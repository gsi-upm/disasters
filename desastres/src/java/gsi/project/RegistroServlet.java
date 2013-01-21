package gsi.project;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import security.HashAlgorithm;

/**
 * Servlet para registrar un nuevo usuario.
 * 
 * @author Juan Luis Molina Nogales
 */
public class RegistroServlet extends HttpServlet{

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
		
		String usuario = request.getParameter("user");
		String pass = request.getParameter("pass");
		String contra = HashAlgorithm.SHA256(pass, usuario);
		String nombre = request.getParameter("nombre");
		String email = request.getParameter("email");
	
		String url = Constantes.DB_URL;
		if(Constantes.DB.equals("hsqldb")){
			url += getServletContext().getRealPath("/WEB-INF/db/" + Constantes.PROJECT);
		}
		try{
			Class.forName(Constantes.DB_DRIVER);
			Connection conexion = DriverManager.getConnection(url, Constantes.DB_USER, Constantes.DB_PASS);
			Statement s = conexion.createStatement();
			ResultSet rs = s.executeQuery(SQLQueries.userRole(usuario)); // para ver si existe
			if(rs.next()){
				out.print("no");
			}else{
				s.executeUpdate(SQLQueries.registrar(usuario, contra, nombre, email));
				out.print("ok");
			}
			conexion.close();
		}catch(ClassNotFoundException ex){
			System.out.println("ClassNotFoundExcepcion: " + ex);
		}catch(SQLException ex){
			System.out.println("SQLExcepcion: " + ex);
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo(){
		return "Short description";
	}
	// </editor-fold>
}