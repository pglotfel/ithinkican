package ithinkican.MCP2515;

import ithinkican.core.IDriver;
import ithinkican.core.SPIChannel;
import ithinkican.core.SPIMode;

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
			driver.write(MCP2515Messages.ack);
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}		
	}

	@Override
	public void getStatus() {
		
		try {
			driver.write(MCP2515Messages.getStatus);
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}		
	}

	@Override
	public void unlock() {
		
		try {
			driver.write(MCP2515Messages.unlock);
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void lock() {
		
		try {
			driver.write(MCP2515Messages.lock);
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}	
	}

	@Override
	public void enable() {
		
		try {
			driver.write(MCP2515Messages.enable);
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void disable() {
		
		try {
			driver.write(MCP2515Messages.disable);
		} catch (IOException e) {
			//TODO: Elevate the exception
			e.printStackTrace();
		}
	}

	@Override
	public void getBikeID() {
		
		try {
			driver.write(MCP2515Messages.getBikeID);
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
}
