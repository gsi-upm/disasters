package disasters;

import disasters.caronte.Entorno;
import disasters.desastres.Environment;

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
 * @author David Reilly
 */
public class TimerJSON extends Thread{

	/** Rate at which timer is checked. */
	protected int m_rate = 100;
	/** Length of timeout. */
	private int m_length;
	/** Time elapsed. */
	private int m_elapsed;
	
	/** Entorno de caronte. */
	private Entorno ent;
	/** Entorno de desastres. */
	private Environment env;

	/**
	 * Creates a timer of a specified length.
	 * 
	 * @param length length of time before timeout occurs
	 * @param ent entorno de caronte
	 */
	public TimerJSON(int length, Entorno ent){
		// Assign to member variable
		m_length = length;

		// Set time elapsed
		m_elapsed = 0;

		this.ent = ent;
	}
	
	/**
	 * Creates a timer of a specified length.
	 * 
	 * @param length length of time before timeout occurs
	 * @param env entorno de desastres
	 */
	public TimerJSON(int length, Environment env){
		// Assign to member variable
		m_length = length;

		// Set time elapsed
		m_elapsed = 0;

		this.env = env;
	}

	/**
	 * Resets the timer back to zero.
	 */
	public synchronized void reset(){
		m_elapsed = 0;
	}

	/**
	 * Performs timer specific code.
	 */
	public void run(){
		// Keep looping
		for(;;){
			// Put the timer to sleep
			try{
				Thread.sleep(m_rate);
			}catch(InterruptedException ioe){
				continue;
			}

			// Use 'synchronized' to prevent conflicts
			synchronized(this){
				// Increment time remaining
				m_elapsed += m_rate;

				// Check to see if the time has been exceeded
				if(m_elapsed > m_length){
					// Trigger a timeout
					timeout();
				}
			}
		}
	}

	// Override this to provide custom functionality
	/**
	 * Timeout.
	 */
	public void timeout(){
		System.out.println("## ENV: Actualizando el JSON...");
		if(ent != null){
			//ent.actualiza();
		}else if(env != null){
			env.actualiza();
		}
	}
}