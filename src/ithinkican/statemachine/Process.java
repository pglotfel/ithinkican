package ithinkican.statemachine;

import java.util.function.Function;

public class Process<R, T> extends State<R>{
	
	private Function<T, R> func;
	
	public Process(R identifier, Function<T, R> func) {
		super(identifier);
		this.func = func;
	}

	@Override
	public R apply(Object value) {
		return func.apply((T) value);
	}
}
