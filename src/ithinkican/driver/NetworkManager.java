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

public class NetworkManager {
	
	private LinkedBlockingQueue<Byte[]> data; //Houses data from the network	
	private LinkedBlockingQueue<Event> writeTasks; 
	private LinkedBlockingQueue<Event> tasks; //Houses tasks that are to be deployed onto the network.  These can be reads or writes, because they must mutually exclusively access the network.
	
	private ScheduledExecutorService executor;
	
	private final Runnable addWrites = new Runnable() {

		@Override
		public void run() {
			
			Event e = writeTasks.poll();
			
			if(e != null) {
				//System.out.println("Adding write command...");
				tasks.add(e);
			}	
		}		
	};
	
	private final Callable<Void> executeTasks = new Callable<Void>() {

		@Override
		public Void call() throws Exception {
			
			while(true) {						
				//System.out.println("Executing command...");
				tasks.take().call();
			}
		}		
	};
	
	private Vector<Future<?>> futures;
	
	public NetworkManager(ScheduledExecutorService executor) throws IOException {
		
		data = new LinkedBlockingQueue<Byte[]>();
		writeTasks = new LinkedBlockingQueue<Event>();
		tasks = new LinkedBlockingQueue<Event>();
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

	public int getQueueSize() {	
		return tasks.size();
	}

	public boolean submitWrite(Event e) {
		return writeTasks.add(e);
	}
	
	public boolean submitWrite(Supplier<Byte[]> supplier, CompletableFuture<Byte[]> future) {
		
		Event e = new Event() {

			@Override
			public void call() {
				
				future.complete(supplier.get());
			}			
		};
		
		return submitWrite(e);
	}
	
	public boolean submitRead(Supplier<Byte[]> supplier) {
		
		Event e = new Event() {

			@Override
			public void call() {
				data.add(supplier.get());
			}	
		};
		
		return tasks.add(e);	
	}


	public LinkedBlockingQueue<Byte[]> getData() {
		return data;
	}
}
