package ithinkican.statemachine;

import ithinkican.util.Component;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ConcurrentStateMachine<T> implements Consumer<T>, Component {
	
	private HashMap<T, State<T>> states;
	private State<T> currentState;
	private ScheduledExecutorService executor;
	private LinkedBlockingQueue<Object> queue;
	private Future<?> future;
	
	private final Runnable executeTasks = new Runnable() {

		@Override
		public void run() {
			
			while(true) {
				try {
					advance(queue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	};
	
	public ConcurrentStateMachine(ScheduledExecutorService executor) {
		
		this.states = new HashMap<T, State<T>>();
		this.executor = executor;
		queue = new LinkedBlockingQueue<Object>();
		currentState = null;
	}
	
	@Override
	public void start() {
		
		future = executor.schedule(executeTasks, 0, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void stop() {
		
		future.cancel(true);
	}
	
	public ConcurrentStateMachine<T> addState(State<T> state) {
		
		states.put(state.getIdentifier(), state);
		return this;
	}
	
	public void setInitialState(T state) {
		currentState = states.get(state);
	}
	
	public State<T> getCurrentState() {
		return currentState;
	}
	
	private void advance(Object arg) {
		
		State<T> result = states.get(currentState.apply(arg));
		
		while(result instanceof Auto) {
			result = states.get(result.apply(null));
		}
		
		//Clear any input (i.e., prevent button mashing)
		queue.clear();
		
		currentState = result;
	}
	
	@Override
	public void accept(Object arg) {	
		
		queue.add(arg);
	}
}
