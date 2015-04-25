package ithinkican.rfid;

@FunctionalInterface
public interface Event {
	public void call(byte[] b);
}
