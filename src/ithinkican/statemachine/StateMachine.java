package ithinkican.statemachine;

import java.util.HashMap;

public class StateMachine {
	
	private HashMap<String, State<?>> states;
	private State<?> currentState;
	
	public StateMachine() {
		this.states = new HashMap<String, State<?>>();
		currentState = null;
	}
	
	public void addState(State<?> state) {
		states.put(state.getIdentifier(), state);
	}
	
	public void setInitialState(State<?> state) {
		currentState = state;
	}
	
	public void inject(Object o) {
		currentState = states.get(currentState.advance(o));
	}
}
