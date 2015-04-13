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
	
	public static void main (String [] args) {
		
		/*
		 *Example showing use of functions!  Awesome! 
		 * Also, put this in a test!
		 */
		
		State<String, String> s = new State<String, String>("Test state", (String str) -> {System.out.println(str); return str;});
		
		s.advance("hello");
		
	}
}
