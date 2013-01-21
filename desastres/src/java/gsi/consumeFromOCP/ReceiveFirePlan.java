package gsi.consumeFromOCP;

import gsi.project.Constantes;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author lara
 */
public class ReceiveFirePlan extends HttpServlet {

    private final String URL_HTTPServidor = "http://strauss.gsi.dit.upm.es:8090/HTTPservidor/ReceiveJSONToFile";

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
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

            String param = request.getParameter("Param");
            System.out.println("Param= " + param);

            out.write("JSON recibido: " + param);
            out.flush();

            String newParam = param.replace("\'", "\"");

            String url = Constantes.DB_URL; // Al usar hsqldb como es le caso, sera: HSQLDB_URL
            if (Constantes.DB.equals("hsqldb")) {
                url += getServletContext().getRealPath("/WEB-INF/db/" + Constantes.PROJECT);
            }
            
            JSONToDB json = new JSONToDB();
            
            if(newParam.startsWith("{\"FireEvent")){
                json.insertEvent(newParam, url,"FireEvent");
            }else{
                json.insertEvent(newParam, url,"FirePlan");
                JSONparser.produce(newParam, url);
 
            }
            //Thread.sleep(10000);

        } catch (Exception excep) {
            System.err.println("Error en ReceiveFirePlan: " + excep);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
