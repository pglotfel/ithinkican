package ithinkican.driver;

import ithinkican.network.Event;

/**
 * The interface for the CAN driver.  Contains all methods required to successfully handle all ABRS requirements.
 * 
 * @author Paul G.
 *
 */
public interface IDriver {

	//Messages that can be sent
	
	public Event ack();
	public Event getStatus();
	public Event unlock();
	public Event lock();
	public Event enable();
	public Event disable();
	public Event getBikeID();
	public Event initialize();
	public Event reset();
	public Event readyToSend();
}
