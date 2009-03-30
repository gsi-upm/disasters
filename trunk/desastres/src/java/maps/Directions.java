package maps;

import java.util.Collection;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.util.Logger;

/**
 *
 * @author luis
 */
public class Directions {

    DirectionsBean directions;

    public Directions() {
        directions = new DirectionsBean(" Antonio Machado, Madrid", "Valdezarza, Madrid");
    }

    public void setDirections(String from, String to) {
        directions = new DirectionsBean(from, to);
    }

    public void sendDirections(int AgentID) throws InterruptedException {
        //address = new AddressBean(num + " Antonio Machado, Madrid", "Valdezarza, Madrid");
        //firstly we obtain the Agent current position in the DB
        String lat = DBManager.getField(AgentID, "latitud");
        String lng = DBManager.getField(AgentID, "longitud");
        String lat2;
        String lng2;
        //now we need the id of the Disaster this Agent is assigned to
        String idAssigned = DBManager.getField(AgentID, "idAssigned");
        //case the Agent is not assigned to any event
        if (idAssigned.equals("0")){
            lat2 = lat;
            lng2 = lng;
        } else {
            //we fetch the position of the Disaster
            int disID = new Double(idAssigned).intValue();
            lat2 = DBManager.getField(disID, "latitud");
            lng2 = DBManager.getField(disID, "longitud");
        }
        
        System.out.println("directions to send: "+lat+","+lng+"  "+lat2+","+lng2);
        directions = new DirectionsBean(lat+" , "+lng , lat2+" , "+lng2);
        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        Collection sessions = wctx.getScriptSessionsByPage(currentPage);
        Util utilAll = new Util(sessions);

        System.out.println("Pushing directions: "+directions.getStart());
        utilAll.setValue("start", directions.getStart());
        utilAll.setValue("end", directions.getEnd());
        System.out.println("I am: "+this.toString());
    }
}