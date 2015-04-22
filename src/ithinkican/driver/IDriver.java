package ithinkican.driver;

public interface IDriver {

	//Messages that can be sent
	
	public Empty ack();
	public Empty getStatus();
	public Empty unlock();
	public Empty lock();
	public Empty enable();
	public Empty disable();
	public Empty getBikeID();
	public Empty initialize();
	public Empty reset();
	public Empty readyToSend();
}
