
package gsi.envioOCP;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Lara Lorna Jiménez
 */
public class RecibeEvento extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            
            String type = request.getParameter("type");
            String event = request.getParameter("event");
            String size = request.getParameter("size");
            String description = request.getParameter("description");
            String name = request.getParameter("name");
            String longitude = request.getParameter("longitude");
            String floor = request.getParameter("floor");
            String latitude = request.getParameter("latitude");
            String date = request.getParameter("date");
            
            String[] paramsPost = {type, event, size, description, name, longitude, floor, latitude, date};
            
            String postResult = "";
            try{
                ProduceEvento p = new ProduceEvento();
                String urlPost = "http://localhost:8076/HTTPservidor/ConsumeEvento";
                postResult = p.postData(urlPost, paramsPost);
            }catch(Exception e){
                System.err.println("\nError en invocacion a objeto de ProduceEvento que hace el POST al servlet remoto: "+e);
            }
            
            System.out.println("\nDatos recibidos en RecibeEvento desde AJAX mapa.js-->");
            System.out.println("El tipo de situacion es: "+type);
            System.out.println("El evento es: "+event);
            System.out.println("El tamaño es: "+size);
            System.out.println("La descripcion es: "+description);
            System.out.println("El nombre es: "+name);
            System.out.println("La longitud es: "+longitude);
            System.out.println("La planta es: "+floor);
            System.out.println("La latitud es: "+latitude);
            System.out.println("La fecha es: "+date);
            
            
            System.out.println("\nEl resultado del POST hecho hacia servlet remoto desde ProduceEvento y devuelto a RecibeEvento es: "+postResult);
            
            out.print(request.getParameterMap());
            out.flush();
            
        }catch(Exception e){
            System.err.println("Excepcion en RecibeEventoServlet: "+e);
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
