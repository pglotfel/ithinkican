package ithinkican.rfid;


import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Reader {
	
	private SerialPort port;
	private final LinkedBlockingQueue<Event> events = new LinkedBlockingQueue<Event>();
	private final PacketParser parser = new PacketParser();
	private int inputSize;
	
	public Reader(String path) {
		port = new SerialPort(path);
	}
	
	public void start() throws SerialPortException {
		port.openPort();
		port.setParams(9600, 8, 1, 0);
		port.setEventsMask(SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR);
		
		port.addEventListener(new SerialPortEventListener() {

			@Override
			public void serialEvent(SerialPortEvent e) {
				
				if(e.isRXCHAR()) {
					inputSize = e.getEventValue();
					ByteBuffer b = ByteBuffer.allocate(inputSize);
					try {
						b.put(port.readBytes(inputSize));
						for(int i = 0; i < inputSize; i++) {
							if(parser.parse(b.get(i)) == State.COMPLETE) {
								byte[] msg = parser.getMessage();
								handle(msg);							
							}					
						}
					} catch (SerialPortException e1) {
						e1.printStackTrace();
					}
				}
				
			};
		
		});
	}
	
	public void stop() throws SerialPortException {
		port.closePort();
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public void handle(byte[] b) {			
		for(Event e : events) {
			e.call(b);
		}	
	}
}
