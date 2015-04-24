package ithinkican.driver;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class NetworkManager implements IConducer<Empty, Byte[]> {
	
	private LinkedBlockingQueue<Byte[]> data; //Houses data from the network
	
	private LinkedBlockingQueue<Empty> writeTasks; 
	private LinkedBlockingQueue<Empty> tasks; //Houses tasks that are to be deployed onto the network.  These can be reads or writes, because they must mutually exclusively access the network.
	
	private ScheduledExecutorService executor;
	
	private final Runnable addWrites = new Runnable() {

		@Override
		public void run() {
			
			System.out.println("Adding write command...");
			
			Empty e = writeTasks.poll();
			
			if(e != null) {
				tasks.add(e);
			}	
		}		
	};
	
	private final Callable<Void> executeTasks = new Callable<Void>() {

		@Override
		public Void call() throws Exception {
			
			while(true) {						
				System.out.println("Executing command...");
				tasks.take().call();
			}
		}		
	};
	
	private Vector<Future<?>> futures;
	
	public NetworkManager(ScheduledExecutorService executor) throws IOException {
		
		data = new LinkedBlockingQueue<Byte[]>();
		writeTasks = new LinkedBlockingQueue<Empty>();
		tasks = new LinkedBlockingQueue<Empty>();
		futures = new Vector<Future<?>>();
		this.executor = executor;
	}
	
	public void start() {
		
		futures.add(executor.schedule(executeTasks, 0, TimeUnit.MILLISECONDS));
		futures.add(executor.scheduleAtFixedRate(addWrites, 0, 100, TimeUnit.MILLISECONDS));
	}
	
	public void shutdown() {
		
		for(Future<?> f : futures) {
			f.cancel(true);
		}
	}

	@Override
	public int getQueueSize() {
		
		return tasks.size();
	}

	@Override
	public boolean submitWrite(Empty e) {
		return writeTasks.add(e);
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
		
		return tasks.add(e);	
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
	public Byte[] receive(int timeout) {
		
		Byte[] ret = null;
		
		try {
			ret = data.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
