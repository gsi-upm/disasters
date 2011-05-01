package gsi.simulator.rest;

import java.io.IOException;
import java.net.MalformedURLException;

import gsi.disasters.SizeType;
import gsi.disasters.DensityType;
import jadex.desastres.Environment;

/**
 * This class provides an interface for using the REST API.
 * @author al.lopezf
 */
public class EventsManagement {

    private static final String URL_BASE = "http://localhost:8080/disasters/rest/";
    private static final String POST = "post/";
    private static final String PUT = "put/";
    private static final String DELETE = "delete/";
    private static final String LIST = "events/";

     /**
     * Method used to remove the blanks in a String (useful for URLs)
     * @param String with the original text
     * @return the String without blanks
     */
    public static String removeBlanks(String string){
        String newString = "";
        String newChar;
            for (int i=0;i<string.length();i++){
                newChar = string.substring(i,i+1);

                if(newChar.equals(" ")){newString=newString.concat("+");}
                else{newString=newString.concat(newChar);}

            }

        return newString;
    }


    /*
     * List all the events in the app
     * @return a String with all the events, in a JSON notation.
     */
    public static String listAllEvents() {
        WebFile web;
        String response = "error";
        try {
            web = new WebFile(URL_BASE + LIST);
            response = (String) web.getContent();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response.trim();
    }


    /*
     * Inserts a fire into the database.
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param name the name of the fire (i.e. "Big fire in /street")
     * @param size (it can be huge, big, medium or small)
     * @param traffic (it can be low , medium or high)
     *
     * @return the unique ID of the fire. This is important if you want to modify
     * or erase this fire later.
     */
    public static int insertFire(double lat, double lon, String name, SizeType size,
            DensityType traffic) {
        WebFile web;
        int response = -1;
        String content = "error";
        String URL = "";
        try {
            URL += URL_BASE + POST + "type=fire&latitud=" + lat + "&longitud=" + lon;
            if (name != null) {
                URL += "&name=" + name;
            }

            if (size != null) {
                if (size.equals(SizeType.SMALL)) {
                    URL += "&size=small";
                }
                if (size.equals(SizeType.MEDIUM)) {
                    URL += "&size=medium";
                }
                if (size.equals(SizeType.BIG)) {
                    URL += "&size=big";
                }
                if (size.equals(SizeType.HUGE)) {
                    URL += "&size=huge";
                }
            }
            if (traffic != null) {
                if (traffic.equals(DensityType.LOW)) {
                    URL += "&traffic=low";
                }
                if (traffic.equals(DensityType.MEDIUM)) {
                    URL += "&traffic=medium";
                }
                if (traffic.equals(DensityType.HIGH)) {
                    URL += "&traffic=high";
                }
            }
            URL = removeBlanks(URL);
            web = new WebFile(URL);
            System.out.println(URL);
            content = (String) web.getContent();
            System.out.println(content.trim());
            content = content.substring(content.indexOf("\"id\":") + 5, content.indexOf("}"));
            response = Integer.parseInt(content);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    /**
     *
     * @param type It can be police, firemen, ambulance, nurse if it is referring
     * to a resource, or slight, serious, dead or trapped, if referring to a victim.
     * @param name Name of the resource or victim
     * @param quantity Number of resources or victims to insert
     * @param lat latitude
     * @param lon longitude
     * @param idAssigned id of the disaster which this resource or victim is assigned
     *  If it is -1 , the resource or victim is nos associated to any disaster.
     *
     * @return the id of the resource or victim. This is important if you want to modify
     * or erase it later.
     */
    public static int insertResourcesOrVictims(String type, String name,
            int quantity, double lat, double lon, int idAssigned) {
        WebFile web;
        int response = -1;
        String content = "error";
        String URL = "";
        try {
            URL += URL_BASE + POST + "&type=" + type + "&quantity=" + quantity + "&latitud=" + lat +
                    "&longitud=" + lon;
            if (name != null) {
                URL += "&name=" + name;
            }

            if (idAssigned != -1) {
                URL += "&idAssigned=" + idAssigned;
            }

            URL = removeBlanks(URL);
            web = new WebFile(URL);
            content = (String) web.getContent();
            System.out.println(content.trim());
            content = content.substring(content.indexOf("\"id\":") + 5, content.indexOf("}"));
            response = Integer.parseInt(content);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * Modifies a parameter in the database
     * @param id identifier of the marker
     * @param parameter parameter that must be changed
     * @param value value of the parameter
     */
    public static void modify(int id, String parameter, String value) {
        WebFile web;
        String content = "error";
        String URL = "";
        try {
            URL += URL_BASE + PUT + id + "/" + parameter + "/" + value;
            URL = removeBlanks(URL);
            web = new WebFile(URL);
            content = (String) web.getContent();
            System.out.println(content.trim());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes a disaster, resource or victim
     * @param id The id of the disaster, resource or victim to delete. This id
     * is the one given when you created it (the id in the database).
     */
    public static void delete(int id) {
        WebFile web;
        String content = "";
        String URL = "";
        try {
            URL += URL_BASE + DELETE + "id/" + id;

            URL = removeBlanks(URL);
            web = new WebFile(URL);
            content = (String) web.getContent();
            System.out.println(content.trim());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Tests


    public static void main(String[] args) {

    try {
    System.out.println(listAllEvents());
    //Draw a fire and put it out
    int id = insertFire(40, -4.2, "Info", "huge", null);
    // System.out.println(id);
    insertResourcesOrVictims("serious","nombre",3,40,-4.1,id);
    Thread.sleep(5000); //Waits 5 seconds
    System.out.println("Wake up...");
    delete(id); //End
    } catch (Exception e) {
    e.printStackTrace();
    }
    }  */
}
