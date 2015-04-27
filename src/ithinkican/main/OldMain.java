package ithinkican.main;

import ithinkican.MCP2515.MCP2515;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;
import ithinkican.network.NetworkManager;
import ithinkican.parser.Multiplexer;
import ithinkican.parser.Stream;
import ithinkican.rfid.Reader;
import ithinkican.statemachine.Auto;
import ithinkican.statemachine.Process;
import ithinkican.statemachine.StateMachine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class OldMain {
	
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
		
		
		//TODO: Put TCP code in other file.  Put everything into a system graph to start!
		
		//TCP CODE ######################################################## 
		
		try {
			Socket clientSocket = new Socket("localhost", 8081);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Couldn't connect to TCP server...");
		}
		
		Reader rfid = new Reader("/dev/ttyUSB0");
		rfid.addEvent(b -> {System.out.println("Got message!");});
		rfid.start();
		  
		  //Portion this out into things when the button is pressed...
		  
		  //outToServer.writeBytes(sentence);
		  
		//#################################################################
		
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
		
		NetworkManager network = new NetworkManager(executor, 100);
		
		Multiplexer mux = new Multiplexer(executor, network.getData());
		
		Stream status = new Stream();
		
		//Status messages
		mux.addHandler(bs -> (bs[0] == 0x11), status);
		
		//Just handle returns immediately
		mux.addHandler(bs -> (bs[0] == 0x08), bs -> System.out.println("Got a return!"));
		
	    MCP2515 driver = new MCP2515(network, SPIChannel.CE0, SPIMode.MODE0, 10000000);	      
           
	    driver.reset().call();    
	    
	    //Attach interrupts after reset
   	 
	    driver.start();
	    
	    //Initialize controller before starting network
	    
	    driver.initialize().call();	    
	        
	    network.start();    
	    
	    mux.start();	    
	    
	    //Attempt to clear out buffers.  Completely arbitrary
	    
	    //TODO: Can get the network queue then try to grab messages?
//        print(network.receive(1000));
//        print(network.receive(1000));      
        
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		StateMachine<String> system = new StateMachine<String>();
		
		Process<String, String> ack = new Process<String, String>("ack", str -> {//System.out.println("acking!"); 
																				 network.submitWrite(driver.getStatus()); 
																				 return "rts";});
		
		Auto<String> rts = new Auto<String>("rts", n -> {try {
																  Thread.sleep(1000);
															 } catch (Exception e) {																						
																	e.printStackTrace();
															 } 
		                                                     System.out.println("RTS");
		                                                     network.submitWrite(driver.readyToSend());
		                                                     
		                                                     Byte[] b = status.receive(1000);
		                                                     print(b);
		                                                     System.out.println(b[1]);
		                                                     if(b[1] != 0) {
		                                                    	 network.submitWrite(driver.unlock());
		                                                     } else {
		                                                    	 System.out.println("No bike!");
		                                                     }
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
		
		//TODO: Ewwwww.  Only for a test!
		
		Scanner s = new Scanner(System.in);
		
		while(true) {
			s.next();
			system.accept("arg");
		}
	}
}
