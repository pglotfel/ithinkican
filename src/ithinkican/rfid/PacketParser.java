package ithinkican.rfid;

import java.nio.ByteBuffer;

public class PacketParser {
	
	private byte header = 0x02;
	private byte end = 0x03;
	private int length = 16;
	private State state = State.EMPTY;
	private ByteBuffer buffer = ByteBuffer.allocate(20);
	
	public PacketParser() {
		
	}
	
	public State parse(Byte b) {
		
		switch(state) {
		
		case EMPTY: 
			if(b == header) {
				buffer.put(b);
				state = State.PARTIAL;
			} else {
				//State stays the same.  Toss the byte!
			}
			break;	
			
			
		case PARTIAL:
			
			if(buffer.position() < length - 1) {
				//Haven't received the whole message yet.  Don't chane the state from PARTIAL
				buffer.put(b);
			} else {
				//Buffer's length is 16 or greater... 	
				
				buffer.put(b);
				
				if(verifyMessage(buffer.array())) {
					state = State.COMPLETE;
				} else {
					flush();
					state = State.EMPTY;
				}
			}
			break;
			
		default:
			flush();
			state = State.EMPTY;
			break;
		}

		return state;
	}
	
	public void flush() {

		buffer.clear();
		state = State.EMPTY;
	}
	
	public byte[] getMessage() {
		
		byte[] message = buffer.array().clone();
		flush();
		
		return message;
	}
	
	public boolean verifyMessage(byte[] b) {
		
		return(b[0] == header && b[15] == end && checksum(b));
	}
	
	public boolean checksum(byte[] b) {
		
		//Make this work in the future...
		
		int cs = b[1]; 
		
		for(int i = 2; i <= 5; i++) {
			cs ^= b[i];
		}
		
		return (true);
	}
}
