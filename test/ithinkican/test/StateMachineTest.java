package ithinkican.test;

import ithinkican.statemachine.Auto;
import ithinkican.statemachine.Process;
import ithinkican.statemachine.StateMachine;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class StateMachineTest {
	
	private StateMachine<String> sm;

	@Before
	public void setUp() throws Exception {
		
		sm = new StateMachine<String>();
		
		//Make states
		Process<String, String> hi = new Process<String, String>("hi", str -> {return "bye";});
		Process<String,String> bye = new Process<String, String>("bye", str -> {return "hi";});
		
		//Add states
		
		sm.addState(hi)
		  .addState(bye);
		sm.setInitialState("hi");
	}

	@Test
	public void test() {
		
		Random gen = new Random();
		
		int runTime = gen.nextInt(100)+50;
		int counter = runTime;
		
		String s = "input";

		while(counter > 0) {
			sm.accept(s);
			counter--;
		}
		
		if(runTime % 2 == 0) {
			assert(sm.getCurrentState().getIdentifier() == "hi");
		} else {
			assert(sm.getCurrentState().getIdentifier() == "bye");
		}		
	}
	
	@Test 
	public void autoTest() {
		
		StateMachine<String> sm = new StateMachine<String>();
		
		Process<String, String> p = new Process<String,String>("init", str -> {System.out.println("init"); return "auto";});
		
		Auto<String> a = new Auto<String>("auto", (Void v) -> {System.out.println("auto"); return "init";});
		
		sm.addState(p)
		.addState(a);
		
		sm.setInitialState("init");
		
		sm.accept("hello");
		
		assert(sm.getCurrentState().getIdentifier() == "init");
	}
}
