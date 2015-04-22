package ithinkican.core;

import ithinkican.MCP2515.MCP2515;
import ithinkican.driver.NetworkManager;
import ithinkican.driver.SPI;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;
import ithinkican.statemachine.Auto;
import ithinkican.statemachine.Process;
import ithinkican.statemachine.StateMachine;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		final ExecutorService executor = Executors.newCachedThreadPool();
		
		final NetworkManager network = new NetworkManager(executor);
		
	    final MCP2515 driver = new MCP2515(network, SPIChannel.CE0, SPIMode.MODE0, 10000000);
	    
	    driver.reset();
	    
	    driver.initialize();
		
		StateMachine<String> system = new StateMachine<String>();
		
		Process<String, String> ack = new Process<String, String>("ack", str -> {System.out.println("acking!"); 
																				 network.submit(driver.ack()); 
																				 return "rts";});
		
		Auto<String> rts = new Auto<String>("rts", n -> {try {
																  Thread.sleep(1000);
															 } catch (Exception e) {																						
																	e.printStackTrace();
															 } 
		                                                     System.out.println("RTS");
		                                                     network.submit(driver.readyToSend());
															 return "sleep";});
		
		Auto<String> sleep = new Auto<String>("sleep", n -> {try {
																	Thread.sleep(1000);
															} catch (Exception e) {																						
																	e.printStackTrace();
															} 
															return "ack";});
		
		system.addState(ack)
			.addState(sleep)
			.addState(rts);
		
		system.setInitialState("ack");
		
		while(true) {
			System.out.println("System cycled!");
			system.accept("arg");
		}
	}
}
