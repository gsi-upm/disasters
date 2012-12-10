package disasters.otros;

import jadex.bdi.runtime.*;
import jadex.bridge.fipa.SFipa;
//import roads.*;

/**
 * Moving plan.
 */
public class MovingPlan extends Plan{
    //Plan attributes.
    //DirectionsBean dirn;

	/**
	 * Constructor.
	 */
	public MovingPlan(){
        // Initialization code.
        System.out.println("Created: " + this);
    }

	/**
	 * Cuerpo del plan.
	 */
    public void body(){
        //Plan code.
        while(true){
            try{
                System.out.println("waiting for a message");
                IMessageEvent me = waitForMessageEvent("getDirections");
                System.out.println("Received an order");
                String st = (String) me.getParameter(SFipa.CONTENT).getValue();
                String from = st.substring(st.indexOf("from:") + 6, st.indexOf("to:"));
                String to = st.substring(st.indexOf("to:") + 4);
                System.out.println("from: " + from);
                System.out.println("to: " + to);
                //dirn = new DirectionsBean();
                //dirn.setDirections(from, to);
            }catch(Exception ioe){
                System.out.println("IOException: " + ioe);
            }
        }
    }

    /*public void body() {
		//Plan code.
		try{
			IMessageEvent me = (IMessageEvent) getInitialEvent();
			System.out.println("Received an order");
			String st = (String) me.getContent();
			String from = st.substring(st.indexOf("from:") + 6, st.indexOf("to:"));
			String to = st.substring(st.indexOf("to:") + 4);
			System.out.println("from: " + from);
			System.out.println("to: " + to);
			addr = new AddressBean(from, to);
		}catch(Exception ioe){
			System.out.println("IOException: " + ioe);
		}
	}
    public void sendDirections() throws InterruptedException{
        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        Collection sessions = wctx.getScriptSessionsByPage(currentPage);
        Util utilAll = new Util(sessions);

        utilAll.setValue("start", addr.getStart());
        utilAll.setValue("end", addr.getEnd());
    }*/
}