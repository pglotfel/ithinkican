package ithinkican.fake;

import ithinkican.driver.IDriver;
import ithinkican.network.Event;

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
	public Event ack() {
		return null;}

	@Override
	public Event getStatus() {

		byte[] response = {0x01, 0x01, 0x01, 0x01};
		
		try {
			queue.put(response);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Event unlock() {
		return null;
		}

	@Override
	public Event lock() {
		return null;}

	@Override
	public Event enable() {
		return null;}

	@Override
	public Event disable() {
		return null;}

	@Override
	public Event getBikeID() {
		
		byte[] response = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
		
		try {
			queue.put(response);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		return null;	
	}

	@Override
	public Event reset() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Event readyToSend() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Event initialize() {
		// TODO Auto-generated method stub
		return null;
	}
}
