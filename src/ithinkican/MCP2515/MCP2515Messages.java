package ithinkican.MCP2515;

/**
 * Contains all CAN controller messages required for ABRS operation.
 * 
 * @author Paul G.
 *
 */
public class MCP2515Messages {
	
	public static final byte[] reset = {
		(byte) 0xC0
	};
	
	public static final byte[][] initialize = {
		{0x05, 0x60, 0x60, (byte) 0xFF},
		{0x02, 0x0F, 0x00}, 
		{0x05, 0x2B, 0x03, (byte) 0xFF},
		{0x02, 0x0C, 0x0F}
	};
	
	public static final byte[][] ack = {
		
		{0x02, 0x31, 0x11}, 
		{0x02, 0x32, 0x00}, 
		{0x02, 0x35, 0x04},
		{0x02, 0x36, 0x03}, 
		{0x02, 0x37, 0x03},
		{0x02, 0x38, 0x03},
		{0x02, 0x39, 0x03},
		{(byte) 0x81}
		};

	public static final byte[][] lock = {
		{0x02, 0x31, 0x11}, 
		{0x02, 0x32, 0x00}, 
		{0x02, 0x35, 0x04},
		{0x02, 0x36, 0x09}, 
		{0x02, 0x37, 0x09},
		{0x02, 0x38, 0x09},
		{0x02, 0x39, 0x09},
		{(byte) 0x81}		
		};
	
	public static final byte[][] unlock = {
		{0x02, 0x31, 0x11}, 
		{0x02, 0x32, 0x00}, 
		{0x02, 0x35, 0x04},
		{0x02, 0x36, 0x16}, 
		{0x02, 0x37, 0x16},
		{0x02, 0x38, 0x16},
		{0x02, 0x39, 0x16},
		{(byte) 0x81}
	};
	
	public static final byte[][] enable = {
		{0x02, 0x31, 0x11}, 
		{0x02, 0x32, 0x00}, 
		{0x02, 0x35, 0x04},
		{0x02, 0x36, 0x0A}, 
		{0x02, 0x37, 0x0A},
		{0x02, 0x38, 0x0A},
		{0x02, 0x39, 0x0A},
		{(byte) 0x81}
	};
	
	public static final byte[][] disable = {
		{0x02, 0x31, 0x11}, 
		{0x02, 0x32, 0x00}, 
		{0x02, 0x35, 0x04},
		{0x02, 0x36, 0x17}, 
		{0x02, 0x37, 0x17},
		{0x02, 0x38, 0x17},
		{0x02, 0x39, 0x17},
		{(byte) 0x81}
	};
	
	public static final byte[][] getStatus = {
		{0x02, 0x31, 0x11}, 
		{0x02, 0x32, 0x00}, 
		{0x02, 0x35, 0x04},
		{0x02, 0x36, 0x01}, 
		{0x02, 0x37, 0x01},
		{0x02, 0x38, 0x01},
		{0x02, 0x39, 0x01},
		{(byte) 0x81}	
	};
	
	public static final byte[][] getBikeID = {
		{0x02, 0x31, 0x11}, 
		{0x02, 0x32, 0x00}, 
		{0x02, 0x35, 0x04},
		{0x02, 0x36, 0x08}, 
		{0x02, 0x37, 0x08},
		{0x02, 0x38, 0x08},
		{0x02, 0x39, 0x08},
		{(byte) 0x81}
	};
	
	public static final byte[][] readyToSend = {
		{0x02, 0x31, 0x11}, 
		{0x02, 0x32, 0x00}, 
		{0x02, 0x35, 0x04},
		{0x02, 0x36, 0x04}, 
		{0x02, 0x37, 0x04},
		{0x02, 0x38, 0x04},
		{0x02, 0x39, 0x04},
		{(byte) 0x81}
	};
}
