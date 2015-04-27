package ithinkican.fake;

import ithinkican.driver.IDriver;
import ithinkican.network.NetworkFunction;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Fake CAN driver for testing.  Not currently fully implemented!
 * 
 * @author Paul G.
 *
 */
public class FakeCAN implements IDriver {
	
	private LinkedBlockingQueue<byte[]> queue;
	
	public FakeCAN() {
		
		queue = new LinkedBlockingQueue<byte[]>();		
	}

	@Override
	public NetworkFunction ack() {
		return null;}

	@Override
	public NetworkFunction getStatus() {

		byte[] response = {0x01, 0x01, 0x01, 0x01};
		
		try {
			queue.put(response);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public NetworkFunction unlock() {
		return null;
		}

	@Override
	public NetworkFunction lock() {
		return null;}

	@Override
	public NetworkFunction enable() {
		return null;}

	@Override
	public NetworkFunction disable() {
		return null;}

	@Override
	public NetworkFunction getBikeID() {
		
		byte[] response = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
		
		try {
			queue.put(response);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		return null;	
	}

	@Override
	public NetworkFunction reset() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public NetworkFunction readyToSend() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public NetworkFunction initialize() {
		// TODO Auto-generated method stub
		return null;
	}
}
