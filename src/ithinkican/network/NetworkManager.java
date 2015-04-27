package ithinkican.network;

import ithinkican.util.Component;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <P> A network manager that handles incoming and outgoing network transmissions.  In particular, it makes sure that writes only occur so often and that 
 * reads/writes are mutually exclusive.  Also, that writes do not overlap.  Most (tm) methods are thread safe.
 * 
 * @author Paul G.
 *
 */
public class NetworkManager implements Component{
	
	private LinkedBlockingQueue<Byte[]> data; //Houses data from the network	
	private LinkedBlockingQueue<NetworkFunction> writeTasks; 
	private LinkedBlockingQueue<NetworkFunction> tasks; //Houses tasks that are to be deployed onto the network.  These can be reads or writes, because they must mutually exclusively access the network.
	
	private ScheduledExecutorService executor;
	private int period = 100;
	
	private final Runnable addWrites = new Runnable() {

		@Override
		public void run() {
			
			NetworkFunction e = writeTasks.poll();
			
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
	
	/**
	 * 
	 * @param executor The executor on which the reads/writes are executed.  Will consume one thread.
	 * @param period The period of execution for the write tasks.
	 * @throws IOException
	 */
	public NetworkManager(ScheduledExecutorService executor, int period) {
		
		data = new LinkedBlockingQueue<Byte[]>();
		writeTasks = new LinkedBlockingQueue<NetworkFunction>();
		tasks = new LinkedBlockingQueue<NetworkFunction>();
		futures = new Vector<Future<?>>();
		this.executor = executor;
		this.period = period;
	}
	
	@Override
	public void start() {
		
		futures.add(executor.schedule(executeTasks, 0, TimeUnit.MILLISECONDS));
		futures.add(executor.scheduleAtFixedRate(addWrites, 0, period, TimeUnit.MILLISECONDS));
	}
	
	@Override
	public void stop() {
		
		for(Future<?> f : futures) {
			f.cancel(true);
		}
	}
	
	/**
	 * Submits a write event to be placed onto the network.
	 * 
	 * @param event The event which will be submitted as a write event.  Write events are executed periodically
	 * @return
	 */
	public boolean submitWrite(NetworkFunction event) {
		return writeTasks.add(event);
	}
	
	/**
	 * Submits a write event with a CompletableFuture (i.e., a promise).  The future will be completed with the result of calling the supplier function.
	 * 
	 * @param supplier
	 * @param future
	 * @return A boolean representing a successful/unsuccessful 'put' into the task queue.
	 */
	public boolean submitWrite(Supplier<Byte[]> supplier, CompletableFuture<Byte[]> future) {
		
		NetworkFunction e = new NetworkFunction() {

			@Override
			public void call() {
				
				future.complete(supplier.get());
			}			
		};
		
		return submitWrite(e);
	}
	
	/**
	 * Submits a read event to take place.  Adds the result of calling the supplier function to the data queue.
	 * 
	 * @param supplier The supplying function to be called.
	 * @return The successful/unsuccessful result of adding the task to the task queue.
	 */
	public boolean submitRead(Supplier<Byte[]> supplier) {
		
		NetworkFunction e = new NetworkFunction() {

			@Override
			public void call() {
				data.add(supplier.get());
			}	
		};
		
		return tasks.add(e);	
	}
	
	/**
	 * 
	 * @return The size of the task queue
	 */
	public int getQueueSize() {	
		return tasks.size();
	}

	/**
	 * The incoming data queue.
	 * 
	 * @return The incoming data queue.
	 */
	public LinkedBlockingQueue<Byte[]> getData() {
		return data;
	}
}
