package ithinkican.network;

/**
 * An event.  Essentially an empty functionl.  Intended to handle blocks of SPI driver operations.
 * 
 * @author Paul G.
 *
 */
@FunctionalInterface
public interface NetworkFunction {
	public void call();
}
