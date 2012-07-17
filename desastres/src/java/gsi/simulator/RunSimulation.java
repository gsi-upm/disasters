/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsi.simulator;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import gsi.simulator.rest.*;

/**
 *
 * @author Sergio
 */
public class RunSimulation extends HttpServlet{

	private static final String URL_BASE = "/desastres/";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        Simulator sim = new Simulator();

        if(request.getParameter("sim").equals("run")){
            try{
                int victims0 = Integer.parseInt(request.getParameter("victims0"));
                int fires0 = Integer.parseInt(request.getParameter("fires0"));

                if(victims0 == 0 || fires0 == 0){
                    sim.runSimulation();
                }else{
                    sim.runSimulation(victims0, fires0);
                }
            }catch(NumberFormatException e){
                sim.runSimulation();
            }finally{
                response.sendRedirect(URL_BASE + "index.jsp?alert=true");
            }
        }

        else if (request.getParameter("sim").equals("restart")) {
            for (int i = 0; i < 400 ; i++) { // Should be changed to the highest id number
                EventsManagement.delete(i);
            }
            response.sendRedirect(URL_BASE + "index.jsp");
        }
        else if (request.getParameter("sim").equals("pause")) {
            Simulator.pauseSimulation(); // Not working
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
