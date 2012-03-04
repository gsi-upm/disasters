package roads;

import java.util.*;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

/**
 *
 * @author luis
 */
public class DirectionsBean {

	private int resourcesList[];
	private String start = "";
	private String end = "";

	public DirectionsBean() {
		resourcesList = DBManager.getResources();
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int[] getResourcesList() {
		return resourcesList;
	}

	public void setResourcesList(int resourcesList[]) {
		this.resourcesList = resourcesList;
	}

	public void sendDirections() throws InterruptedException {
		try {
			int ids[] = getResourcesList();
			HashMap<String, String> hm = new HashMap<String, String>();
			for (int i = 0; i < ids.length; i++) {
				int AgentID = new Integer(ids[i]).intValue();
				//firstly we obtain the Agent current position in the DB
				String lat = DBManager.getField(AgentID, "latitud");
				String lng = DBManager.getField(AgentID, "longitud");
				String lat2;
				String lng2;
				//now we need the id of the Disaster this Agent is assigned to
				String idAssigned = DBManager.getField(AgentID, "idAssigned");
				//case the Agent is not assigned to any event
				if (idAssigned.equals("0.0")) {
					lat2 = lat;
					lng2 = lng;
				} else {
					//we fetch the position of the Disaster
					int disID = new Double(idAssigned).intValue();
					lat2 = DBManager.getField(disID, "latitud");
					lng2 = DBManager.getField(disID, "longitud");
				}
				String stID = new Integer(AgentID).toString();
				hm.put(stID, stID);
				hm.put("start" + stID, lat + "," + lng);
				hm.put("end" + stID, lat2 + "," + lng2);
			}

			WebContext wctx = WebContextFactory.get();
			String currentPage = wctx.getCurrentPage();

			Collection sessions = wctx.getScriptSessionsByPage(currentPage);
			Util utilAll = new Util(sessions);
			utilAll.setValues(hm, true);
		} catch (Exception e) {
			System.out.println("Excepcion en DirectionsBean.sendDirections(): " + e);
		}
	}
}