package ithinkican.rfid;

@FunctionalInterface
public interface ReaderEvent {
	public void call(byte[] b);
}
