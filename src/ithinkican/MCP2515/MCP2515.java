package ithinkican.MCP2515;

import ithinkican.driver.IConducer;
import ithinkican.driver.IDriver;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;

import java.awt.Event;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

public class MCP2515 implements IDriver, IConducer<Byte[], Byte[]> {
	
	private SpiDevice driver;
	private LinkedBlockingQueue<Byte[]> in;
	private LinkedBlockingQueue<Byte[]> out;
	
    private final GpioController gpio = GpioFactory.getInstance();

    // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
    private final GpioPinDigitalInput messageInterrupt = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29);   
	
	public MCP2515(SPIChannel channel, SPIMode mode, int speed) throws IOException {
		
		SpiChannel c = null;
		SpiMode m = null;
		
		in = new LinkedBlockingQueue<Byte[]>();
		out = new LinkedBlockingQueue<Byte[]>();
		
		messageInterrupt.addListener(new GpioPinListenerDigital() {	    	 
	    	  
			@Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
                System.out.println(" --> MESSAGE RECEIVED: " + event.getPin() + " = " + event.getState());
    			//TODO: Add message to queue...
                
                if(event.getState() == PinState.LOW) {
                	//Message is in RX0
                	System.out.println("Clearing buffer zero...");                	
                	clearBufferOne();     
                	System.out.println(Integer.toHexString(readByte(0x2C)[2]));
                }
            }
        });
        		
		switch(channel) {
			case CE0:
				c = SpiChannel.CS0;
				break;
			case CE1:
				c = SpiChannel.CS1;
				break;	
		}
		
		switch(mode) {
			case MODE0:
				m = SpiMode.MODE_0;
				break;
			case MODE1:
				m = SpiMode.MODE_1;
				break;
			case MODE2:
				m = SpiMode.MODE_2;
				break;
			case MODE3:
				m = SpiMode.MODE_3;
				break;
		}
				
		driver = SpiFactory.getInstance(c, speed, m);
	}

	@Override
	public void ack() {
		
		try {
			for(int i = 0; i < MCP2515Messages.ack.length; i++) {
				driver.write(MCP2515Messages.ack[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}		
	}

	@Override
	public void getStatus() {
		
		try {
			for(int i = 0; i < MCP2515Messages.getStatus.length; i++) {
				driver.write(MCP2515Messages.getStatus[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}		
	}

	@Override
	public void unlock() {
		
		try {
			for(int i = 0; i < MCP2515Messages.unlock.length; i++) {
				driver.write(MCP2515Messages.unlock[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void lock() {
		
		try {
			for(int i = 0; i < MCP2515Messages.lock.length; i++) {
				driver.write(MCP2515Messages.lock[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}	
	}

	@Override
	public void enable() {
		
		try {
			for(int i = 0; i < MCP2515Messages.enable.length; i++) {
				driver.write(MCP2515Messages.enable[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void disable() {
		
		try {
			for(int i = 0; i < MCP2515Messages.disable.length; i++) {
				driver.write(MCP2515Messages.disable[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void getBikeID() {
		
		try {
			for(int i = 0; i < MCP2515Messages.getBikeID.length; i++) {
				driver.write(MCP2515Messages.getBikeID[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void initialize() {
		
		try {
			for(int i = 0; i < MCP2515Messages.initialize.length; i++) {
				driver.write(MCP2515Messages.initialize[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void reset() {
		
		try {
			driver.write(MCP2515Messages.reset);
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void readyToSend() {
		try {
			for(int i = 0; i < MCP2515Messages.readyToSend.length; i++) {
				driver.write(MCP2515Messages.readyToSend[i]);
			}
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}		
	}

	@Override
	public int getQueueSize() {
		
		return in.size();
	}

	@Override
	public boolean accept(Byte[] b) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Byte[] receive() {
		
		Byte[] ret = null;
		
		try {
			ret = in.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	//NOT CURRENT IN AN INTERFACE
	public byte[] readByte(int address) {
		
		byte[] msg = {(byte) 0x03, (byte) address, (byte) 0xDF};
		byte [] bs = null;
		
		try {
			bs = driver.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bs;
	}
	
	public void clearBufferOne() {
		
		byte [] msg = {0x05, 0x2C, (byte) 0xFF, 0x00};
		
		try {
			driver.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clearBufferTwo() {
		
		byte [] msg = {(byte) 0x05, (byte) 0x2C, (byte) 0x02, (byte) 0x00};
		
		try {
			driver.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getBufferOneData() {
		
		Byte[] b = new Byte[4];
		
		b[0] = readByte(0x66)[2];
		b[1] = readByte(0x67)[2];
		b[2] = readByte(0x68)[2];
		b[3] = readByte(0x69)[2];	
	}
}
