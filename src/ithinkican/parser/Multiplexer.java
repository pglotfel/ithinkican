package ithinkican.parser;

import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * <P> Handles multiplexing an incoming message queue into various queues or handling the events.  That is, 
 * this class allows you to either buffer a message for a later time or handle it immediately.
 * 
 * @author Paul G.
 *
 */
public class Multiplexer {

	//This is a class to handle predicate/handler pairs.
	
	private class Pair {
		
		public Predicate<Byte[]> predicate; 
		public Handler handler;
		
		private Pair(Predicate<Byte[]> predicate, Handler handler) {
			this.predicate = predicate;
			this.handler = handler;
		}
	}
 
	private LinkedBlockingQueue<Byte[]> input;
	private ScheduledExecutorService executor;
	private Future<?> future; 
	
	private final Runnable task = new Runnable() {
		@Override
		public void run() {
			while(true) {
				try {
					Byte[] bytes = input.take();
					for(Pair p : handlers) {		
						if(p.predicate.test(bytes)) {				
							p.handler.handle(bytes);
							break;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	};
	
	private final ArrayList<Pair> handlers = new ArrayList<Pair>();
	
	/**
	 * 
	 * @param executor The executor on which the task is executed.  Consumes an entire thread!
	 * @param input The input feeding into the multiplexer
	 */
	public Multiplexer(ScheduledExecutorService executor, LinkedBlockingQueue<Byte[]> input) {

		this.executor = executor; 
		this.input = input;	
	}
	
	/**
	 * Starts the multiplexer (i.e., schedules the task on the executor).
	 */
	public void start() {
		future = executor.schedule(task, 0, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Stops the multiplexer (i.e., cancels the running task).
	 */
	public void stop() {
		future.cancel(true);
	}	
	
	/**
	 * Adds a handler onto the multiplexer.  The handler is typically a stream or a callback.
	 * 
	 * @param predicate Determines where each message goes.  If a predicate returns true, the given handler is executed.
	 * Only one handler is ever executed for each message.
	 * 
	 * @param handler Handles the given message if the predicate for that message returns true.
	 */
	public void addHandler(Predicate<Byte[]> predicate, Handler handler) {
		handlers.add(new Pair(predicate, handler));
	}
}
