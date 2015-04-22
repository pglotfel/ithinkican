package ithinkican.driver;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface IConducer<R, T> {
	
	public boolean submit(R input);
	public boolean submit(Supplier<T> supplier, CompletableFuture<T> future);
	public T receive();
	
	public int getQueueSize();
}
