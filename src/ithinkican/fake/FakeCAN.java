package ithinkican.fake;

import ithinkican.driver.Empty;
import ithinkican.driver.IDriver;

import java.util.concurrent.LinkedBlockingQueue;

public class FakeCAN implements IDriver {
	
	private LinkedBlockingQueue<byte[]> queue;
	
	public FakeCAN() {
		
		queue = new LinkedBlockingQueue<byte[]>();		
	}

	@Override
	public Empty ack() {
		return null;}

	@Override
	public Empty getStatus() {

		byte[] response = {0x01, 0x01, 0x01, 0x01};
		
		try {
			queue.put(response);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Empty unlock() {
		return null;
		}

	@Override
	public Empty lock() {
		return null;}

	@Override
	public Empty enable() {
		return null;}

	@Override
	public Empty disable() {
		return null;}

	@Override
	public Empty getBikeID() {
		
		byte[] response = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
		
		try {
			queue.put(response);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		return null;	
	}
	
	@Override
	public Empty initialize() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Empty reset() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Empty readyToSend() {
		return null;
		// TODO Auto-generated method stub
		
	}
}
