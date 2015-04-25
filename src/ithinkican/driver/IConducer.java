package ithinkican.driver;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

//I should probably just delete this...

public interface IConducer<R, T> {
	
	public boolean submitWrite(R input);
	public boolean submitWrite(Supplier<T> supplier, CompletableFuture<T> future);
	public boolean submitRead(Supplier<T> supplier);
	public T receive();
	public T receive(int timeout);
	
	public int getQueueSize();
}
