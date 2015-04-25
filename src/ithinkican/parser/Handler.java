package ithinkican.parser;


/**
 * 
 * <P> An FI that handles incoming network messages.  Intended for use with the MessageParser class.
 * 
 * @author Paul G.
 */
@FunctionalInterface
public interface Handler {
	/**
	 * <P>  Handle the incoming message.
	 * 
	 * @param bytes A Byte[] that contains a network message (not parsed at all).
	 */
	public void handle(Byte[] bytes);
}
