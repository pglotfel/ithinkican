package ithinkican.core;

import ithinkican.MCP2515.MCP2515;
import ithinkican.driver.SPI;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;
import ithinkican.statemachine.Auto;
import ithinkican.statemachine.Process;
import ithinkican.statemachine.StateMachine;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
	    final MCP2515 driver = new MCP2515(SPIChannel.CE0, SPIMode.MODE0, 10000000);
	    
	    driver.reset();
	    
	    driver.initialize();
		
//		SpiDevice s = null;
//		
//		try {
//			s = SpiFactory.getInstance(SpiChannel.CS0, 10000000, SpiMode.MODE_0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		SPI driver = new SPI(s);
//		
//		driver.reset();
//		driver.init();
//		driver.clearBuffers();
		
		StateMachine<String> system = new StateMachine<String>();
		
		Process<String, String> ack = new Process<String, String>("ack", str -> {System.out.println("acking!"); 
																				 driver.ack(); 
																				 driver.readyToSend();
																				 return "sleep";});
		
		Auto<String> sleep = new Auto<String>("sleep", n -> {try {
																	Thread.sleep(1000);
															} catch (Exception e) {																						
																	e.printStackTrace();
															} 
															return "ack";});
		
		system.addState(ack)
			.addState(sleep);
		
		system.setInitialState("ack");
		
		while(true) {
			System.out.println("System cycled!");
			system.accept("arg");
		}
	}
}
