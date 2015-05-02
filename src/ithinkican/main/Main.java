package ithinkican.main;

import ithinkican.MCP2515.MCP2515;
import ithinkican.driver.SPIChannel;
import ithinkican.driver.SPIMode;
import ithinkican.network.NetworkManager;
import ithinkican.parser.Multiplexer;
import ithinkican.parser.Stream;
import ithinkican.rfid.Reader;
import ithinkican.statemachine.Auto;
import ithinkican.statemachine.ConcurrentStateMachine;
import ithinkican.statemachine.Process;
import ithinkican.util.Component;
import ithinkican.util.SystemGraph;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main implements Component {
	
	private final static SystemGraph application = new SystemGraph();
	private final static ScheduledExecutorService executor = Executors.newScheduledThreadPool(6);
	private final static ConcurrentStateMachine<String> sm = new ConcurrentStateMachine<String>(executor);
	private final static Reader rfid = new Reader("/dev/ttyUSB0");
	
	private final static NetworkManager network = new NetworkManager(executor, 400);
	
	private final static Stream status = new Stream();
	private final static Multiplexer mux = new Multiplexer(executor, network.getData());
	
	private static MCP2515 driver;
	
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	
	private Future<?> future;
	
	private static void task() {
		network.submitWrite(driver.readyToSend());
	}
	
	public Main() throws IOException {
		clientSocket = new Socket("localhost", 8081);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		driver = new MCP2515(network, SPIChannel.CE0, SPIMode.MODE0, 10000000);	
	}
	
	@Override 
	public void start() {
		future = executor.scheduleAtFixedRate(Main::task, 0, 1500, TimeUnit.MILLISECONDS);
	}
	
	@Override 
	public void stop() {
		future.cancel(true);
	}
	
	
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
	
	public static String init(String input) {
		
		System.out.println("init!");
		
		String ret = "init";
		
		switch(input) {
		case "rfid": 
			ret = "get-status";
			break;
		default: 
			ret = "init";
		}
		
		return ret;
	}
	
	public static String clear(Void n) {
		try {
			outToServer.writeBytes("default;");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "init";
	}
	
	public static String getStatus(Void n) {
		
		System.out.println("getting status...");
		try {
			outToServer.writeBytes("loading;");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		network.submitWrite(driver.getStatus());
		
        Byte[] b = status.receive(5000);
       
        print(b);
             
	    if(b != null) {
	        if(b[1] != 0) {
	    	    return "unlock";
	        } else {
	        	return "clear";
	        }      
	    } else {		
		   return "clear";
	    }
	}
	
	public static String unlock(Object n) {
		
		System.out.println("unlocking!");
		
		try {
			outToServer.writeBytes("bike0;");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		network.submitWrite(driver.unlock());
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
     	 
		return "init";
	}
	
	public static void main(String[] args) throws IOException {
		
		
		//TODO: Put TCP code in other file.  Put everything into a system graph to start!
		
		//TCP CODE ######################################################## 
		
		Main abrs = new Main();
		
//		try {
//			Socket clientSocket = new Socket("localhost", 8081);
//			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//		} catch (IOException e) {
//			System.err.println("Couldn't connect to TCP server...");
//		}
		
		//Status messages
		mux.addHandler(bs -> (bs[0] == 0x11), status);
		
		//Just handle returns immediately
		mux.addHandler(bs -> (bs[0] == 0x08), bs -> System.out.println("Got a return!"));
		      			        
	    driver.reset().call();    
	    
		application.addVertex("RFID", rfid, "State Machine");
		application.addVertex("State Machine", sm, "abrs");
		application.addVertex("CAN driver", driver, "network manager");
		application.addVertex("network manager", network, "mux");
		application.addVertex("mux", mux, "abrs");	
		application.addVertex("abrs", abrs);
		
		application.start();
	    
	    driver.initialize().call();	    
		  
		  //Portion this out into things when the button is pressed...
		  
		  //outToServer.writeBytes(sentence);
		  
		//#################################################################
   
	    
	    //Attempt to clear out buffers? 
        
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Process<String, String> init = new Process<String, String>("init", Main::init);
		Auto<String> getStatus = new Auto<String>("get-status", Main::getStatus);
		Auto<String> unlock = new Auto<String>("unlock", Main::unlock);
		Auto<String> clear = new Auto<String>("clear", Main::clear);
		
		sm.addState(init)
			.addState(getStatus)
			.addState(unlock)
			.addState(clear);
		
		sm.setInitialState("init");
		
		rfid.addEvent(b -> {System.out.println("Got message!"); sm.accept("rfid");});
		
		//TODO: Ewwwww.  Only for a test!
		
	}
}
