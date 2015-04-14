package ithinkican.statemachine;

import java.util.HashMap;
import java.util.function.Consumer;

public class StateMachine<T> implements Consumer<T> {
	
	private HashMap<T, State<T, ?>> states;
	private State<T, ?> currentState;
	
	public StateMachine() {
		this.states = new HashMap<T, State<T, ?>>();
		currentState = null;
	}
	
	public StateMachine<T> addState(State<T, ?> state) {
		states.put(state.getIdentifier(), state);
		return this;
	}
	
	public void setInitialState(String state) {
		currentState = states.get(state);
	}
	
	public State<T, ?> getCurrentState() {
		return currentState;
	}
	
	@Override
	public void accept(Object arg) {
		currentState = states.get(currentState.advance(arg));		
	}
	
	public static void main (String [] args) {
		//Make states
		State<String, String> hi = new State<String, String>("hi", str -> {System.out.println("hi"); return "bye";});
		State<String,String> bye = new State<String, String>("bye", str -> {System.out.println("bye"); return "hi";});
		
		//Add states
		
		StateMachine<String> sm = new StateMachine<String>();
		sm.addState(hi)
		  .addState(bye);
		sm.setInitialState("hi");
		
		//Run state machine
		
		String s = "yo";
		
		sm.accept(s);
		sm.accept(s);
		sm.accept(s);
	}
}
