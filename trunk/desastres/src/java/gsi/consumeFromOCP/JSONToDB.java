
package gsi.consumeFromOCP;

import gsi.project.Constantes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Lara Lorna Jiménez Jiménez
 */
public class JSONToDB {
    
    static private boolean exists = false;
    static private int nump = 0;
    static private int numwait = 0;
    static private int numdesc = 0;
    
    public void insertEvent(String send, String url, String entidad){
        
        try{
            
            String newJson = send.replace("\'", "\"");
            
            Class.forName(Constantes.DB_DRIVER);
            Connection conn = DriverManager.getConnection(url, Constantes.DB_USER, Constantes.DB_PASS);
            
            Statement s = conn.createStatement();
   
            String query;
            if(entidad.equals("FireEvent hacia OCP")){
                query = "INSERT INTO FIREEVENT_TO_OCP (fire) VALUES ('" + newJson + "')"; //Para prueba: comprobacion de lo que envio a OCP
            }else if(entidad.equals("FireEvent")){
                query = "INSERT INTO FIREEVENT_FROM_OCP (fireeventstring) VALUES ('" + newJson + "')"; //Para prueba: comprobacion de lo que llega OCP
            }else if(entidad.equals("FirePlan")){
                query = "INSERT INTO json (jsonstring) VALUES ('" + newJson + "')";
            }else{
                query = "";
            }
            
            s.executeUpdate(query);
            
            s.close();
            conn.close();
            
        }catch(ClassNotFoundException ex){
            System.out.println("ClassNotFoundExcepcion: " + ex);
        }catch(SQLException ex){
            System.out.println("SQLExcepcion: " + ex);
        }catch(Exception e){
            System.out.println("Excepcion: " + e);
        }
        
    }
    
    public static void insertFirePlan(String urlDB, String info,String primary, String secondary){
        String query = "";
        boolean almacenado;
        
        try{
            Class.forName(Constantes.DB_DRIVER);
            Connection conn = DriverManager.getConnection(urlDB, Constantes.DB_USER, Constantes.DB_PASS);
            
            Statement s = conn.createStatement();
           
            if(info.equals("ID_TASKS")){               
                query = "INSERT INTO FIREPLAN (ID_TASKS,ID_FIREPLAN) VALUES ("+Integer.parseInt(primary)+","+Integer.parseInt(secondary)+")";           
            }
            else if(info.equals("ID_PERSONS")){                
                query = "INSERT INTO PERSON_FIREPLAN (ID_PERSON) VALUES ("+Integer.parseInt(primary)+")";           
            }
            else if(info.equals("RESOURCES")){               
                query = "INSERT INTO RESOURCES_FIREPLAN (ID_P,ID_T) VALUES ("+Integer.parseInt(primary)+","+Integer.parseInt(secondary)+")";          
            }
            else if(info.equals("NAME")){               
                query = "UPDATE PERSON_FIREPLAN SET NAME='"+primary+"' WHERE ID_PERSON="+Integer.parseInt(secondary)+"";          
            }
            else if(info.equals("PHONE")){                
                query = "INSERT INTO PHONE_FIREPLAN (PHONE,ID_PERSON) VALUES ("+Integer.parseInt(primary)+","+Integer.parseInt(secondary)+")";           
            }
            else if(info.equals("EMAIL")){              
                query = "INSERT INTO EMAIL_FIREPLAN (EMAIL,ID_PERSON) VALUES ('"+primary+"',"+Integer.parseInt(secondary)+")";           
            }
            else if(info.equals("NUMPEOPLE")){              
                JSONToDB.selectFromFirePlan(urlDB, info, secondary);
                System.out.println("Entra en NUMPEOPLE: "+nump+" exists vale: "+exists);
                nump++;
                if(exists){
                    query = "UPDATE TASK_FIREPLAN SET NUMPEOPLE="+Integer.parseInt(primary)+" WHERE ID_TASK="+Integer.parseInt(secondary)+"";
                }else if(!exists){
                    query = "INSERT INTO TASK_FIREPLAN (NUMPEOPLE,ID_TASK) VALUES ("+Integer.parseInt(primary)+","+Integer.parseInt(secondary)+")";
                }
                exists = false;                
            }
            else if(info.equals("WAITINGFORANSWER")){              
                JSONToDB.selectFromFirePlan(urlDB, info, secondary);
                System.out.println("Entra en WAITINGFORANSWER: "+numwait+" exists vale: "+exists);
                numwait++;
                if(exists){
                    //query = "INSERT INTO TASK_FIREPLAN (WAITINGFORANSWER) VALUES ('"+primary+"') WHERE ID_TASK="+Integer.parseInt(secondary)+"";
                    query = "UPDATE TASK_FIREPLAN SET WAITINGFORANSWER='"+primary+"' WHERE ID_TASK="+Integer.parseInt(secondary)+"";
                }else if(!exists){
                    query = "INSERT INTO TASK_FIREPLAN (WAITINGFORANSWER,ID_TASK) VALUES ('"+primary+"',"+Integer.parseInt(secondary)+")";
                }
                exists = false;               
            }
            else if(info.equals("DESCRIPTION")){             
                JSONToDB.selectFromFirePlan(urlDB, info, secondary);
                System.out.println("Entra en DESCRIPTION "+numdesc+" exists vale: "+exists);
                numdesc++;
                if(exists){
                    query = "UPDATE TASK_FIREPLAN SET DESCRIPTION='"+primary+"' WHERE ID_TASK="+Integer.parseInt(secondary)+"";
                }else if(!exists){
                    query = "INSERT INTO TASK_FIREPLAN (DESCRIPTION,ID_TASK) VALUES ('"+primary+"',"+Integer.parseInt(secondary)+")";
                }
                exists = false;             
            }
            else if(info.equals("FIREEVENT")){               
                query = "UPDATE FIREPLAN SET FIREEVENT="+Integer.parseInt(primary)+" WHERE ID_FIREPLAN="+Integer.parseInt(secondary)+"";          
            }
            
            s.executeUpdate(query);
            
            s.close();
            conn.close();
            
            if(info.equals("ID_PERSONS")){
                JSONToDB.insertFirePlan(urlDB,"RESOURCES", primary, secondary);
            }
            
        }catch(ClassNotFoundException ex){
            System.out.println("insertFirePlan ClassNotFoundExcepcion: " + ex);
        }catch(SQLException ex){
            System.out.println("insertFirePlan SQLExcepcion: " + ex);
        }catch(Exception e){
            System.out.println("insertFirePlan Excepcion: " + e);
        }
    }
    
    public static void selectFromFirePlan(String urlDB,String info,String value){
        String query;
        String result = "";
        String columna = "";
        
        try{
            Class.forName(Constantes.DB_DRIVER);
            Connection conn = DriverManager.getConnection(urlDB, Constantes.DB_USER, Constantes.DB_PASS);
            
            Statement s = conn.createStatement();
           
            if(info.equals("NUMPEOPLE") || info.equals("WAITINGFORANSWER") || info.equals("DESCRIPTION")){
                query = "SELECT ID_TASK FROM TASK_FIREPLAN";
                columna = "ID_TASK";        
            }else{
                query = "";
            }
            
            ResultSet rs = s.executeQuery(query);

            while(rs.next()){
                result = Integer.toString(rs.getInt(columna));
                //System.out.println("Query select: "+result);
                if(result.equals(value)){
                    exists = true;
                    break;
                }
            }
            
            conn.close();
            

        }catch(ClassNotFoundException ex){
            System.out.println("selectFromFirePlan ClassNotFoundExcepcion: " + ex);
        }catch(SQLException ex){
            System.out.println("selectFromFirePlan SQLExcepcion: " + ex);
        }catch(Exception e){
            System.out.println("selectFromFirePlan Excepcion: " + e);
        }    
    }   
}
