package ithinkican.MCP2515;

import ithinkican.driver.Empty;
import ithinkican.driver.IDriver;
import ithinkican.driver.NetworkManager;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

public class MCP2515 implements IDriver {
	
	private SpiDevice driver;
	
    private final GpioController gpio = GpioFactory.getInstance();

    // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
    private final GpioPinDigitalInput messageInterrupt = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29);   
    
    private NetworkManager manager;
	
	public MCP2515(NetworkManager manager, SPIChannel channel, SPIMode mode, int speed) throws IOException {
		
		SpiChannel c = null;
		SpiMode m = null;
		
		this.manager = manager;
		
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
                	manager.submitRead(read());
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
		
		//Set up threading 
	}
	
	public Supplier<Byte[]> read() {
		
		Supplier<Byte[]> supplier = new Supplier<Byte[]>() {

			@Override
			public Byte[] get() {
				return new Byte[1];
			}			
		};
		
		return supplier;
	}
	
	@Override
	public Empty ack() {
		
		return new Empty() {

			@Override
			public void call() {
				
				try {
					for(int i = 0; i < MCP2515Messages.ack.length; i++) {
						driver.write(MCP2515Messages.ack[i]);
					}
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}				
			}		
		};	
	}

	@Override
	public Empty getStatus() {
		
		return new Empty() {
			
			@Override
			public void call() {
				try {
					for(int i = 0; i < MCP2515Messages.getStatus.length; i++) {
						driver.write(MCP2515Messages.getStatus[i]);
					}
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}			
			}	
		};
	}

	@Override
	public Empty unlock() {
		
		return new Empty() {

			@Override
			public void call() {
				
				try {
					for(int i = 0; i < MCP2515Messages.unlock.length; i++) {
						driver.write(MCP2515Messages.unlock[i]);
					}
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}
				
			}
			
		};
	}

	@Override
	public Empty lock() {
		
		return new Empty() {

			@Override
			public void call() {
				
				try {
					for(int i = 0; i < MCP2515Messages.lock.length; i++) {
						driver.write(MCP2515Messages.lock[i]);
					}
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}				
			}
		};	
	}

	@Override
	public Empty enable() {
		
		return new Empty() {
			
			

			@Override
			public void call() {
				
				try {
					for(int i = 0; i < MCP2515Messages.enable.length; i++) {
						driver.write(MCP2515Messages.enable[i]);
					}
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}
				
			}
			
		};
	}

	@Override
	public Empty disable() {
		
		return new Empty() {

			@Override
			public void call() {
				
				try {
					for(int i = 0; i < MCP2515Messages.disable.length; i++) {
						driver.write(MCP2515Messages.disable[i]);
					}
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}
				
			}
		};

	}

	@Override
	public Empty getBikeID() {
		
		return new Empty() {

			@Override
			public void call() {
				
				try {
					for(int i = 0; i < MCP2515Messages.getBikeID.length; i++) {
						driver.write(MCP2515Messages.getBikeID[i]);
					}
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}
				
			}
			
		};

	}

	@Override
	public Empty initialize() {
		
		return new Empty() {

			@Override
			public void call() {
				
				try {
					for(int i = 0; i < MCP2515Messages.initialize.length; i++) {
						driver.write(MCP2515Messages.initialize[i]);
					}
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}
				
			}
			
		};

	}

	@Override
	public Empty reset() {
		
		return new Empty() {

			@Override
			public void call() {
				
				try {
					driver.write(MCP2515Messages.reset);
				} catch (IOException e) {
					//TODO: Elevate the exception
					e.printStackTrace();
				}
				
			}
			
		};

	}

	@Override
	public Empty readyToSend() {
		
		return new Empty() {

			@Override
			public void call() {
				try {
					for(int i = 0; i < MCP2515Messages.readyToSend.length; i++) {
						driver.write(MCP2515Messages.readyToSend[i]);
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
				
			}		
		};
	}
	
	//NOT CURRENTly IN AN INTERFACE
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
