package ithinkican.statemachine;

public interface Callable<T,V> {
	public T call(V input);
}
