package ithinkican.statemachine;

import java.util.HashMap;
import java.util.function.Consumer;

public class StateMachine<T> implements Consumer<T> {
	
	private HashMap<T, State<T>> states;
	private State<T> currentState;
	
	public StateMachine() {
		this.states = new HashMap<T, State<T>>();
		currentState = null;
	}
	
	public StateMachine<T> addState(State<T> state) {
		states.put(state.getIdentifier(), state);
		return this;
	}
	
	public void setInitialState(T state) {
		currentState = states.get(state);
	}
	
	public State<T> getCurrentState() {
		return currentState;
	}
	
	@Override
	public void accept(Object arg) {	
		
		State<T> result = states.get(currentState.apply(arg));
		
		while(result instanceof Auto) {
			result = states.get(result.apply(null));
		}
		
		currentState = result;
	}
}
