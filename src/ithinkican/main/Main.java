package ithinkican.main;

import ithinkican.MCP2515.MCP2515;
import ithinkican.driver.NetworkManager;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;
import ithinkican.statemachine.Auto;
import ithinkican.statemachine.Process;
import ithinkican.statemachine.StateMachine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
	
	public static void print(Byte[] b) {
		
		if(b != null) {
			System.out.print("MESSAGE: ");
			for(int i = 0; i < b.length; i++) {
				System.out.print(Integer.toHexString(b[i]) + ", ");
			}
			System.out.println();
		} else {
			System.out.println("NOTHING");
		}
	} 
	
	public static void main(String[] args) throws IOException {
		
		
		//TCP CODE ######################################################## 
		
		try {
			Socket clientSocket = new Socket("localhost", 8081);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Couldn't connect to TCP server...");
			e.printStackTrace();
		}
		  
		  //Portion this out into things when the button is pressed...
		  
		  //outToServer.writeBytes(sentence);
		  
		//#################################################################
		
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
		
		NetworkManager network = new NetworkManager(executor);
		
	    MCP2515 driver = new MCP2515(network, SPIChannel.CE0, SPIMode.MODE0, 10000000);	      
           
	    driver.reset().call();    
	    
	    //Attach interrupts after reset
   	 
	    driver.start();
	    
	    //Initialize controller before starting network
	    
	    driver.initialize().call();	    
	        
	    network.start();    
	    
	    //Attempt to clear out buffers.  Completely arbitrary
	    
        print(network.receive(1000));
        print(network.receive(1000));      
        
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		StateMachine<String> system = new StateMachine<String>();
		
		Process<String, String> ack = new Process<String, String>("ack", str -> {//System.out.println("acking!"); 
																				 //network.submitWrite(driver.ack()); 
																				 return "rts";});
		
		Auto<String> rts = new Auto<String>("rts", n -> {try {
																  Thread.sleep(1000);
															 } catch (Exception e) {																						
																	e.printStackTrace();
															 } 
		                                                     System.out.println("RTS");
		                                                     network.submitWrite(driver.readyToSend());
		                                                     print(network.receive(1000));
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
			system.accept("arg");
		}
	}
}