
package gsi.consumeFromOCP;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;


/**
 *
 * @author lara
 */
public class JSONparser {
    
    static int num = 0;
    static int id_task = -1;
    static int id_lastTask = -1;
    static int id_person = -1;
    static int id_lastPerson = -1;
    static int id_fireplan = -1;
    static int id_fireEvent = -1;
    static boolean bool = false;

    public static void produce(String content, String urlDB){
        
        JSONObject json = (JSONObject) JSONSerializer.toJSON( content );     
        Set set=json.keySet();
        Iterator it=set.iterator();
        while(it.hasNext()){
            String key=(String)it.next();
            System.out.println("Key produce: "+key);
            
            id_fireplan = Integer.parseInt(key.substring(key.indexOf("-")+1, key.length()));
            System.out.println("ID: "+Integer.toString(id_fireplan));
            parse(key,json.get(key).toString(),urlDB);
        }
        
    }
    
   public static void parse (String object,String content, String urlDB){
        
       try{
            JSONObject json = (JSONObject) JSONSerializer.toJSON(content);     
            Set set=json.keySet();
            Iterator it=set.iterator();
            while(it.hasNext()){
                num++;
                String key=(String)it.next();
                System.out.println("Key parse "+Integer.toString(num) +": "+key);
                if (json.get(key) instanceof JSONObject){
                   String aux=json.get(key).toString();
                   String obj=aux.substring(aux.indexOf("\"")+1,aux.indexOf(":")-1);
                   String cont=aux.substring(aux.indexOf(":")+1,aux.length()-1);
                   parse(obj,cont, urlDB);
                }
                else if (json.get(key) instanceof JSONArray){
                    //System.out.println("Entra aqui "+Integer.toString(num));
                    JSONArray jsonMainArr = (JSONArray) json.get(key);
                    System.out.println("Tamaño del array: "+Integer.toString(jsonMainArr.size()));
                    for (int i = 0; i < jsonMainArr.size(); i++) {
                        Object child = jsonMainArr.get(i);
                        if (child instanceof JSONObject){
                            String aux=child.toString();
                            if(num==3){System.out.println("Aux: "+aux);}
                            System.out.println("Child de "+num+": "+aux);
                            String obj=aux.substring(aux.indexOf("\"")+1,aux.indexOf(":")-1);
                            String cont=aux.substring(aux.indexOf(":")+1,aux.length()-1);
                            String type = obj.substring(obj.indexOf("\"")+1,obj.indexOf("-"));
                            System.out.println("Type: "+type);
                            if(type.equals("Task")){
                                id_task = Integer.parseInt(obj.substring(obj.indexOf("-")+1,obj.length()));
                            }else if(type.equals("Person")){
                                id_person = Integer.parseInt(obj.substring(obj.indexOf("-")+1,obj.length()));
                            }
                            //Sustituir ID_TASKS y ID_PERSONS por type??
                            if((id_lastTask == -1)||(id_task != id_lastTask)){ //No ha habido un task antes o ha cambiado
                                id_lastTask = id_task;  //guardo el último task para futura comprobacion
                                JSONToDB.insertFirePlan(urlDB, "ID_TASKS", Integer.toString(id_task), Integer.toString(id_fireplan));
                            }
                            
                            if((id_person != -1) && (id_lastPerson != id_person)){
                                id_lastPerson = id_person;
                                JSONToDB.insertFirePlan(urlDB, "ID_PERSONS", Integer.toString(id_person), Integer.toString(id_task));
                            }
                            
                            System.out.println("ID_TASK: "+ Integer.toString(id_task));
                            System.out.println("ID_PERSON: "+ Integer.toString(id_person));
                            parse(obj,cont,urlDB);
                        }
                        else if (child instanceof Integer){
                            //System.out.println("Integer dentro");
                        }
                        else if (child instanceof Boolean){
                            //System.out.println("Boolean dentro");
                        }
                        else if (child instanceof String){
                            System.out.println("String dentro");
                            if(child.toString().startsWith("Task-")){ //Tasks sin contenido
                                id_task = Integer.parseInt(child.toString().substring(child.toString().indexOf("-")+1,child.toString().length()));
                                //System.out.println("ID_TASK: "+Integer.toString(id_task));
                            }
                            else if(child.toString().startsWith("phone-")){
                                String phone = child.toString().substring(child.toString().indexOf("-")+1,child.toString().length());
                                //System.out.println("Phone: "+phone);
                            }
                        }
                        else {
                            System.out.println("Otro dentro");
                        }
                    }
                }
                else if (json.get(key) instanceof Integer){
                    System.out.println("Integer fuera");
                    int numPeople = (Integer)json.get(key);
                    if(key.equals("numPeople")){
                       System.out.println("Numpeople: "+json.getString(key).toString()+ " task: "+Integer.toString(id_task));
                       JSONToDB.insertFirePlan(urlDB, "NUMPEOPLE", json.getString(key).toString(), Integer.toString(id_task));
                    }
                }
                else if (json.get(key) instanceof Boolean){
                    System.out.println("Boolean fuera");
                    if(key.equals("waitingForAnAnswer")){
                        System.out.println("waitingForAnAnswer: "+json.getString(key).toString()+ " task: "+Integer.toString(id_task));
                        JSONToDB.insertFirePlan(urlDB, "WAITINGFORANSWER", json.getString(key).toString(), Integer.toString(id_task));
                    }
                }
                else if (json.get(key) instanceof String){
                    System.out.println("String fuera");
                     
                    if(key.equals("fireEvent")){
                        String s = json.get(key).toString();
                        id_fireEvent = Integer.parseInt(s.substring(s.indexOf("-")+1));
                        System.out.println("fireEventy: "+Integer.toString(id_fireEvent)); 
                        JSONToDB.insertFirePlan(urlDB,"FIREEVENT",Integer.toString(id_fireEvent), Integer.toString(id_fireplan));
                    }
                    else if(key.equals("name")){  //Necesariamente sera unico por persona. Siempre se dara aqui.
                        String name = json.get(key).toString();
                        System.out.println("Name siempre unico: "+name+ " Person: "+Integer.toString(id_person));
                        JSONToDB.insertFirePlan(urlDB, "NAME", name, Integer.toString(id_person));
                    }
                    else if(key.equals("phone")){ //Si telefono unico para persona. Si varios phones arriba. 
                        String phone = json.get(key).toString();
                        if(phone.length()>8){ //Verificamos que es un teléfono: mas de 8 digitos
                            System.out.println("phone unico: "+json.get(key).toString()+ " Person: "+Integer.toString(id_person));
                        }
                        JSONToDB.insertFirePlan(urlDB, "PHONE", phone, Integer.toString(id_person));
                    }
                    else if(key.equals("email")){
                        String email = json.get(key).toString();
                        if(email.contains("@")){ //Verificamos que es un email
                            //System.out.println("Email unico: "+json.get(key).toString()+ " Person: "+Integer.toString(id_person));
                        }
                        JSONToDB.insertFirePlan(urlDB, "EMAIL", email, Integer.toString(id_person));
                    }
                    else if(key.equals("description")){
                        String description = json.get(key).toString();
                        //System.out.println("Descripcion: "+json.get(key).toString()+ " Task: "+Integer.toString(id_task));
                        JSONToDB.insertFirePlan(urlDB, "DESCRIPTION", description, Integer.toString(id_task));
                    }
                }
                else {
                    //System.out.println("Otro fuera");
                }
            }
        }
        catch(Exception e){
            //e.printStackTrace();
            System.err.println("Error: "+e);
        }
    }
    
}
