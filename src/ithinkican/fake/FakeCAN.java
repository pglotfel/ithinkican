package ithinkican.fake;

import ithinkican.driver.IDriver;

import java.util.concurrent.LinkedBlockingQueue;

public class FakeCAN implements IDriver {
	
	private LinkedBlockingQueue<byte[]> queue;
	
	public FakeCAN() {
		
		queue = new LinkedBlockingQueue<byte[]>();		
	}

	@Override
	public void ack() {}

	@Override
	public void getStatus() {

		byte[] response = {0x01, 0x01, 0x01, 0x01};
		
		try {
			queue.put(response);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unlock() {}

	@Override
	public void lock() {}

	@Override
	public void enable() {}

	@Override
	public void disable() {}

	@Override
	public void getBikeID() {
		
		byte[] response = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
		
		try {
			queue.put(response);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}	
	}

	@Override
	public byte[] getMessage() {
		byte[] ret = null;
		
		try {
			ret = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	@Override
	public int getQueueSize() {
		return queue.size();
	}
}
