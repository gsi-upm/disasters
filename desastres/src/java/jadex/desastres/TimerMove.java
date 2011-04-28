package jadex.desastres;

/** 
 * The Timer class allows a graceful exit when an application
 * is stalled due to a networking timeout. Once the timer is
 * set, it must be cleared via the reset() method, or the
 * timeout() method is called.
 * <p>
 * The timeout length is customizable, by changing the 'length'
 * property, or through the constructor. The length represents
 * the length of the timer in milliseconds.
 *
 * @author	David Reilly
 */
public class TimerMove extends Thread
{
	
	/** Rate at which timer is checked */
	protected int m_rate = 100;
	
	/** Length of timeout */
	private int m_length;

	/** Time elapsed */
	private int m_elapsed;

	/**
	  * Creates a timer of a specified length
	  * @param	length	Length of time before timeout occurs
	  */
	public TimerMove ( int length )
	{
		// Assign to member variable
		m_length = length;

		// Set time elapsed
		m_elapsed = 0;
		
		
	}

	
	/** Resets the timer back to zero */
	public synchronized void reset()
	{
		m_elapsed = 0;
	}

	/** Performs timer specific code */
	public void run(int id, Double latitud, Double longitud)
	{
		// Keep looping
		for (;;)
		{
			// Put the timer to sleep
			try
			{ 
				Thread.sleep(m_rate);
			}
			catch (InterruptedException ioe) 
			{
				continue;
			}

			// Use 'synchronized' to prevent conflicts
			synchronized ( this )
			{
				// Increment time remaining
				m_elapsed += m_rate;

				// Check to see if the time has been exceeded
				if (m_elapsed > m_length)
				{
					// Trigger a timeout
					timeout(id, latitud, longitud);
				}
			}

		}
	}

	// Override this to provide custom functionality
	public void timeout(int id, Double latitud, Double longitud)
	{
		
		System.out.println("LLamada a REST modificando latitud y longitud: "+ latitud + " - " +longitud);
		String resultado1 = Connection.connect(Environment.URL+"put/"+id+"/latitud/"+latitud);
		String resultado2 = Connection.connect(Environment.URL+"put/"+id+"/longitud/"+longitud);
		//System.out.println("Resultado de la latitud: "+ resultado1);
		//System.out.println("Resultado de la longitud: "+ resultado2);
		//llama a la aplicacion... le dice el id, latitud y longitud
		try{
		this.sleep(500);
	}
		catch(Exception e){System.out.println ("Error con hebra: "+e);}
	}
	
}