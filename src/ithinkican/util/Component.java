package ithinkican.util;

/**
 * Forces classes to provide setup and shutdown methods a la the Clojure "Component" library.
 * 
 * @author Paul G.
 *
 */
public interface Component {
	public void start();
	public void stop();
}
