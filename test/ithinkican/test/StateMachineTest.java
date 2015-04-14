package ithinkican.test;

import ithinkican.statemachine.State;
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
		State<String, String> hi = new State<String, String>("hi", str -> {return "bye";});
		State<String,String> bye = new State<String, String>("bye", str -> {return "hi";});
		
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
}
