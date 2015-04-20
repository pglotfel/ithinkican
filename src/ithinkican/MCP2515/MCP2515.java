package ithinkican.MCP2515;

import ithinkican.driver.IDriver;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

public class MCP2515 implements IDriver {
	
	private SpiDevice driver;
	private LinkedBlockingQueue<byte[]> queue;
	
    private final GpioController gpio = GpioFactory.getInstance();

    // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
    private final GpioPinDigitalInput messageInterrupt = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_DOWN);   
	
	public MCP2515(SPIChannel channel, SPIMode mode, int speed) throws IOException {
		
		SpiChannel c = null;
		SpiMode m = null;
		
		queue = new LinkedBlockingQueue<byte[]>();
		
		messageInterrupt.addListener(new GpioPinListenerDigital() {	    	 
	    	  
			@Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
                System.out.println(" --> MESSAGE RECEIVED: " + event.getPin() + " = " + event.getState());
    			//TODO: Add message to queue...
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
	public byte[] getMessage() {
		
		byte[] ret = null;
		
		try {
			ret = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public int getQueueSize() {
		
		return queue.size();
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
}
