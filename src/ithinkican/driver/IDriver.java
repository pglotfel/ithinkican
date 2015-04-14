package ithinkican.driver;

public interface IDriver {

	//Messages that can be sent
	
	public void ack();
	public void getStatus();
	public void unlock();
	public void lock();
	public void enable();
	public void disable();
	public void getBikeID();
	
	//Message actions
	public byte[] getMessage();
	public int getQueueSize();
}
