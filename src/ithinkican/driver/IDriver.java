package ithinkican.driver;

import ithinkican.network.NetworkFunction;

/**
 * The interface for the CAN driver.  Contains all methods required to successfully handle all ABRS requirements.
 * 
 * @author Paul G.
 *
 */
public interface IDriver {

	//Messages that can be sent
	
	public NetworkFunction ack();
	public NetworkFunction getStatus();
	public NetworkFunction unlock();
	public NetworkFunction lock();
	public NetworkFunction enable();
	public NetworkFunction disable();
	public NetworkFunction getBikeID();
	public NetworkFunction initialize();
	public NetworkFunction reset();
	public NetworkFunction readyToSend();
}
