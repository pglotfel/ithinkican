package ithinkican.test;

import ithinkican.driver.IDriver;
import ithinkican.fake.FakeCAN;

import org.junit.Before;
import org.junit.Test;

public class CANTest {

	private IDriver driver;
	
	@Before
	public void setUp() throws Exception {
		driver = new FakeCAN();
	}

	@Test
	public void test() {
		
		driver.getStatus();
		
		//TODO:
//		assert(driver.getQueueSize() == 1);
//		
//		assert(driver.getMessage() != null);
//		
//		driver.getBikeID();
//		
//		assert(driver.getQueueSize() == 2);
//		
//		assert(driver.getMessage() != null);
//		assert(driver.getMessage() != null);
	}
}
