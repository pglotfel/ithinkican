package ithinkican.statemachine;

import java.util.function.Function;

public abstract class State<R> implements Function<Object, R> {
	
	private R identifier;
	
	public State(R identifier) {
		this.identifier = identifier; 
	}
	
	public R getIdentifier() {
		return identifier;
	}
}
