package ithinkican.util;

/**
 * Forces classes to provide setup and shutdown methods a la the Clojure "Component" library.
 * 
 * @author Paul G.
 *
 */
public interface Component {
	/**
	 * Initialize method
	 */
	public void start();
	/**
	 * Shutdown method
	 */
	public void stop();
}
