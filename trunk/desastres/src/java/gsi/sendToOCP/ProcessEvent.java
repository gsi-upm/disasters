package gsi.sendToOCP;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Process event.
 * 
 * @author Lara Lorna Jim&eacute;nez
 */
public class ProcessEvent extends HttpServlet {
 
    private static boolean registered = false;
    String id = "1";
    
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

            //String id = request.getParameter("id");
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
            
            //Para obtener el url para acceder al archivo DB(path relativo)
            String urlDB = BuildStringData.DB_url();  
            
            //Obtenemos el ID asignado al evento en DB y lo asignamos antes de enviarlo.
            int i =0;
            i = IDFireQuery.getID_DB(urlDB, paramsPost);
            
            paramsPost[0] = Integer.toString(i);
                        
            String remoteServletResponseData = "";
            String remoteServletResponseRegister = "";

             try{
                
                //Si estaba registrado a la entidad (en nuestro caso FirePlan) no hacer nada.
                if(!registered){
                    //Si no estaba registrado, registrar!
                    registered = true; //--> Lo comento para que le llegue todo el rato(para pruebas)
                    // Enviamos mensaje para registrarnos a consumir un evento determinado
                    remoteServletResponseRegister = BuildStringData.DataRegisterBuilder(paramsPost, "FirePlan");
                    
                    //En periodo de prueba realizamos un registro a FireEvent para comprobar que lo que ellos reciben es lo mismo  que yo envio.
                    remoteServletResponseRegister = BuildStringData.DataRegisterBuilder(paramsPost, "FireEvent");
                    
                }
                 
                if(!(i==1)){ //Si ID ha mantenido su valor defecto, no nos interesa hacer el POST.
                    // type = produce-new --> Enviamos el evento generado
                    // type = produce-modify --> Enviamos el evento modificado y si size="-1" el evento eliminado
                    remoteServletResponseData = BuildStringData.DataProduceBuilder(paramsPost, urlDB); // Creamos el JSON data a enviar y lo enviamos, obteniendo la respuesta del OCP
                }
         
            }catch(Exception e){
                System.err.println("\nError en invocacion a objeto de ProduceEvent que hace el POST al servlet remoto: "+e);
            }

            /*//  TRAZAS  
            System.out.println("\nDatos recibidos en ProcessEvent desde AJAX mapa.js-->");
            System.out.println("El id del evento generado es: "+id);
            System.out.println("El tipo de situacion es: "+type);
            System.out.println("El evento es: "+event); //<--- esto lo he cambiado para enviar en el POST a servlet remoto FireEvent en vez de fire.
            System.out.println("El tamaño es: "+size);
            System.out.println("La descripcion es: "+description);
            System.out.println("El nombre es: "+name);
            System.out.println("La longitud es: "+longitude);
            System.out.println("La planta es: "+floor);
            System.out.println("La latitud es: "+latitude);
            System.out.println("La fecha es: "+date);
            

            System.out.println("\nEl resultado del POST produce hecho hacia servlet remoto desde ProduceEvent y devuelto a ProcessEvent es: "+remoteServletResponseData);
            System.out.println("\nEl resultado del POST register hecho hacia servlet remoto desde ProduceEvent y devuelto a ProcessEvent es: "+remoteServletResponseRegister);
            */
             
            out.print(request.getParameterMap());
            out.flush();
 
        }catch(Exception e){
            System.err.println("Excepcion en ProcessEvent: "+e);
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


            

            
