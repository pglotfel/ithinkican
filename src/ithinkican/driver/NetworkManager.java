package ithinkican.driver;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class NetworkManager implements IConducer<Empty, Byte[]> {
	
	private LinkedBlockingQueue<Byte[]> data; //Houses data from the network
	
	private LinkedBlockingQueue<Empty> tasks; //Houses tasks that are to be deployed onto the network.  These can be reads or writes, because they must mutually exclusively access the network.
	
	private ExecutorService executor;
	
	private final Callable<Void> batch = new Callable<Void>() {

		@Override
		public Void call() throws Exception {
			
			while(true) {			
				tasks.take().call();
				System.out.println("Executing command...");
			}
		}		
	};
	
	private Future<Void> future;
	
	public NetworkManager(ExecutorService executor) throws IOException {
		
		data = new LinkedBlockingQueue<Byte[]>();
		tasks = new LinkedBlockingQueue<Empty>();
		this.executor = executor;
	}
	
	public void start() {
		future = executor.submit(batch);
	}
	
	public void shutdown() {
		future.cancel(true);
	}

	@Override
	public int getQueueSize() {
		
		return tasks.size();
	}

	@Override
	public boolean submitWrite(Empty e) {
		return tasks.add(e);
	}
	

	@Override
	public boolean submitWrite(Supplier<Byte[]> supplier, CompletableFuture<Byte[]> future) {
		
		Empty e = new Empty() {

			@Override
			public void call() {
				
				future.complete(supplier.get());
			}			
		};
		
		return submitWrite(e);
	}
	
	@Override
	public boolean submitRead(Supplier<Byte[]> supplier) {
		
		Empty e = new Empty() {

			@Override
			public void call() {
				data.add(supplier.get());
			}	
		};
		
		return submitWrite(e);	
	}


	@Override
	public Byte[] receive() {
		
		Byte[] ret = null;
		
		try {
			ret = data.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	@Override
	public Byte[] recieve(int timeout) {
		
		Byte[] ret = null;
		
		try {
			ret = data.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
