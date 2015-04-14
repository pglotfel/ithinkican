package ithinkican.statemachine;

import java.util.function.Function;

public class State<R, T> {
	
	private R identifier;
	private Function<T, String> func;
	
	public State(R identifier, Function<T, String> func) {
		this.identifier = identifier;
		this.func = func;
	}

	public R getIdentifier() {
		return identifier;
	}
	
	public String advance(Object value) {
		return func.apply((T) value);
	}
}
