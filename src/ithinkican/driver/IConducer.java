package ithinkican.driver;

public interface IConducer<R, T> {
	
	public boolean accept(R input);
	public T receive();
	
	public int getQueueSize();
}
