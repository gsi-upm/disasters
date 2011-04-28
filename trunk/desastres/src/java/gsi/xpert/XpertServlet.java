package gsi.xpert;
 
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import jess.JessException;
import org.json.me.JSONException;




/**
 * Servlet used to start and stop the Expert System
 * @author julio camarero
 * @version 1.0
 */
public class XpertServlet extends HttpServlet {
   
    
    private ReteDisasterDB reteBD;
    
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException  {
        response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
        
       String action = request.getParameter("action");
        
       //Boolean parar = new Boolean(request.getParameter("stop"));
       
        if(action.equals("start")){    
                try{
                    reteBD = new ReteDisasterDB(out);
                    reteBD.run();

                 

                }
                catch(JessException e){out.println("Jess Exception: "+e);}
                catch(JSONException e){out.println("JSON Exception: "+e);}


                  try {

                    out.println("<p>Servlet XpertServlet at " + request.getContextPath () + "</p>");
                    out.flush();

                } finally { 
                    out.close();
                }
        }
       
        else if (action.equals("call")){
                 try{
                    
                    reteBD.call();

                 

                }
                catch(JessException e){out.println("Jess Exception: "+e);}
                catch(JSONException e){out.println("JSON Exception: "+e);}
        
        }
       
       else if (action.equals("stop")) {
            
                try{
                   

                    reteBD.stop();


                }
                catch(JessException e){out.println("Jess Exception: "+e);}
                


                  try {

                    out.println("<p>Servlet XpertServlet stopped</p>");
                    out.flush();

                } 
                  finally { 
                    out.close();
                }
        }
                
                
        }
     

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
    * Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
    * Returns a short description of the servlet.
    */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
