package ithinkican.parser;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <P>  A stream of messages.  This class buffers incoming messages so that they may be handled at a later time.
 * @author Paul G.
 */
public class Stream implements Handler {
	
	private final LinkedBlockingQueue<Byte[]> queue = new LinkedBlockingQueue<Byte[]>();

	/**
	 * Generating a new stream requires no arguments
	 */
	public Stream() {
		
	}

	@Override
	public void handle(Byte[] bytes) {
		
		try {
			queue.put(bytes);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * 
	 * @return The queue backing the stream.
	 */
	public LinkedBlockingQueue<Byte[]> getQueue() {
		return queue;
	}
	
	/**
	 * Takes a message from the internal queue.  Blocking!
	 * 
	 * 
	 * @return a Byte[] representing a message.
	 */
	public Byte[] receive() {
		
		Byte[] ret = null;
		
		try {
			ret = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Get a message within a certain number of ms.  Otherwise, return null!
	 * 
	 * @param timeout The timeout (in ms).
	 * @return a Byte[] representing a message (or null).
	 */
	public Byte[] receive(int timeout) {
		Byte[] ret = null;
		
		try {
			ret = queue.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
