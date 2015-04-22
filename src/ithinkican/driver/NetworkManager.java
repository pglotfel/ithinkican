package ithinkican.driver;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public class NetworkManager implements IConducer<Empty, Byte[]> {
	
	private LinkedBlockingQueue<Byte[]> in; //Coming in from the network
	
	private LinkedBlockingQueue<Empty> out; //Going out onto the network
	
	private final Callable<Void> task = new Callable<Void>() {

		@Override
		public Void call() throws Exception {
			
			while(true) {			
				out.take().call();
				System.out.println("Executing command...");
			}
		}		
	};
	
	private Future<Void> future;
	
	public NetworkManager(ExecutorService executor) throws IOException {
		
		in = new LinkedBlockingQueue<Byte[]>();
		out = new LinkedBlockingQueue<Empty>();
		future = executor.submit(task);
		
	}
	
	public void shutdown() {
		future.cancel(true);
	}

	@Override
	public int getQueueSize() {
		
		return in.size();
	}

	@Override
	public boolean submit(Empty e) {
		return out.add(e);
	}
	

	@Override
	public boolean submit(Supplier<Byte[]> supplier, CompletableFuture<Byte[]> future) {
		
		Empty e = new Empty() {

			@Override
			public void call() {
				
				future.complete(supplier.get());
			}			
		};
		
		return submit(e);
	}


	@Override
	public Byte[] receive() {
		
		Byte[] ret = null;
		
		try {
			ret = in.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
