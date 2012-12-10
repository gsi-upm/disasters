package gsi;

import com.meterware.httpunit.*;
import com.meterware.servletunit.*;
import junit.framework.*;
import junit.textui.*;

/**
 * 
 * 
 * @author Juan Luis Molina Nogales
 */
public class PruebasCaronte extends TestCase{

	public PruebasCaronte(String s){
		super(s);
	}

	public static TestSuite suite(){
		return new TestSuite(PruebasCaronte.class);
	}

	public void testGetForm() throws Exception{
		ServletRunner sr = new ServletRunner("web.xml");       // (1) use the web.xml file to define mappings
		ServletUnitClient client = sr.newClient();             // (2) create a client to invoke the application

		try{
			client.getResponse("http://localhost/PoolEditor"); // (3) invoke the servlet w/o authorization
			fail("PoolEditor is not protected");
		}catch(AuthorizationRequiredException e){}             // (4) verify that access is denied
		
		client.setAuthorization("aUser", "pool-admin");        // (5) specify authorization and
		client.getResponse("http://localhost/PoolEditor");     //     invoke the servlet again
	}

	public static void main(String args[]){
		TestRunner.run(suite());
	}
}