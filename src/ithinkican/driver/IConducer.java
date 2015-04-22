package ithinkican.driver;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface IConducer<R, T> {
	
	public boolean submitWrite(R input);
	public boolean submitWrite(Supplier<T> supplier, CompletableFuture<T> future);
	public boolean submitRead(Supplier<T> supplier);
	public T receive();
	
	public int getQueueSize();
}
