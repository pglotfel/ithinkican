package ithinkican.statemachine;

import java.util.HashMap;

public class StateMachine<T> {
	
	private HashMap<T, State<T, ?>> states;
	private State<T, ?> currentState;
	
	public StateMachine() {
		this.states = new HashMap<T, State<T, ?>>();
		currentState = null;
	}
	
	public void addState(State<T, ?> state) {
		states.put(state.getIdentifier(), state);
	}
	
	public void setInitialState(String state) {
		currentState = states.get(state);
	}
	
	public void inject(Object o) {
		currentState = states.get(currentState.advance(o));
	}
	
	public State<T, ?> getCurrentState() {
		return currentState;
	}
	
	public static void main(String[] args) {
		StateMachine<String> sm = new StateMachine<String>();
		sm.addState(new State<String, String>("Hello", str -> {System.out.println(str); return "Hello";}));
		sm.setInitialState("Hello");
		sm.inject("hi");
		sm.inject("yo");
		System.out.println(sm.getCurrentState().getIdentifier());
	}
}
