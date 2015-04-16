package ithinkican.core;

import ithinkican.MCP2515.MCP2515;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;
import ithinkican.statemachine.Process;
import ithinkican.statemachine.StateMachine;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
	    final MCP2515 driver = new MCP2515(SPIChannel.CE0, SPIMode.MODE0, 10000000);
		
		StateMachine<String> system = new StateMachine<String>();
		
		Process<String, String> ack = new Process<String, String>("ack", str -> {System.out.println("acking!"); driver.ack(); return "sleep";});
		
		Process<String, String> sleep = new Process<String, String>("sleep", str -> {try {
																						Thread.sleep(1000);
																					} catch (Exception e) {
																						// TODO Auto-generated catch block
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
