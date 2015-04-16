package ithinkican.statemachine;

import java.util.function.Function;

public class Auto<R> extends State<R> {
	
	private Function<Void, R> func;

	public Auto(R identifier, Function<Void, R> func) {
		super(identifier);	
		this.func = func;
	}

	@Override
	public R apply(Object t) {
		return func.apply(null);
	}
}
