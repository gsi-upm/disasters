package gsi.sendToOCP;

import gsi.project.Constantes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Lara Lorna Jimenez Jimenez
 * 
 */
public class IDFireQuery {
    
    public static int getID_DB(String url, String[] param){
        
        int id = 1; //valor por defecto 
        
        //String tipo = param[1]; No me sirve para identificar el evento
        String event;
        if(param[2].equals("fire")){
            event = Integer.toString(1); //1 en DB equivale al evento fire
        }else{
            event = Integer.toString(0); //No está asociado a ningun evento, asi la 
                                         //seleccion no nos sacara ningun ID, ya q 
                                         //no nos interesa
        }
        String size = param[3];
        // String descripcion = param[4]; union de los campos descripcion e info, no lo usamos para la comprobación
        String nombre = param[5];
        String longitud = param[6];
        String planta = param[7];
        String latitud = param[8];
        String fecha = param[9];
        
        try{
            /* TRAZAS
            System.out.println("Event entrante:"+event); 
            System.out.println("Size entrante:"+size);
            System.out.println("Descripcion entrante:"+param[4]);
            System.out.println("Nombre entrante:"+nombre);
            System.out.println("Longitud entrante:"+longitud);
            System.out.println("Planta entrante:"+planta);
            System.out.println("Latitud entrante:"+latitud);
            System.out.println("Fecha entrante:"+fecha);
            */
            
            Class.forName(Constantes.DB_DRIVER);
            Connection conexion = DriverManager.getConnection(url,
                            Constantes.DB_USER, Constantes.DB_PASS);
            Statement s = conexion.createStatement();
   
            String query;
            
            if(size.equals("-1")){ 
                //Busco los unicos que no se pueden cambiar(a parte de los 
                //anteriores que vienen de actuar() en mapa_caronte2.js) al 
                //hacer eliminar, para asegurarme.
                query = "SELECT ID FROM CATASTROFES WHERE"
                                         + " LONGITUD="+longitud
                                         + " AND LATITUD="+latitud
                                        // + " AND FECHA='"+fecha+"'" --> eliminar: desde metodo actuar() en 
                                                                          //mapa_caronte.js no envia fecha
                                         + " AND TIPO="+event;
            }else{
            
                //Duermo la hebra 100ms pq si no la info que estoy intentando comparar, todavía no estará en DB.
                //Si proviene de "eliminar", no hay que esperar a que la info se meta en DB, deberia estar ya.
                Thread.sleep(100);
                
                query = "SELECT ID FROM CATASTROFES WHERE"
                                             + " SIZE='"+size+"'"
                                             + " AND NOMBRE='"+nombre+"'"
                                             + " AND LONGITUD="+longitud
                                             + " AND PLANTA="+planta
                                             + " AND LATITUD="+latitud
                                             + " AND FECHA='"+fecha+"'"
                                             + " AND TIPO="+event;
            }
            
            ResultSet rs = s.executeQuery(query);

            while(rs.next()){
                id=rs.getInt("ID");
            }

            conexion.close();
            
        }catch(ClassNotFoundException ex){
            System.out.println("ClassNotFoundExcepcion: " + ex);
        }catch(SQLException ex){
            System.out.println("SQLExcepcion: " + ex);
        }catch(Exception e){
            System.out.println("Excepcion: " + e);
        }
        
        return id;
        
    }
    
}
