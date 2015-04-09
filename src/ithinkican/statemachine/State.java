package ithinkican.statemachine;

public class State<T> {
	
	private String identifier;
	private Callable<String, T> action;
	
	public State(String identifier, Callable<String, T> action) {
		this.identifier = identifier;
		this.action = action;	
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String advance(Object o) {
		return action.call((T) o);
	}
}
