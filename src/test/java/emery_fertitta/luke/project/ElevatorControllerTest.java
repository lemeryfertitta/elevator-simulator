package emery_fertitta.luke.project;

import static org.junit.Assert.*;

import org.junit.Test;

public class ElevatorControllerTest {

	/**
	 * Test the functionality of calling an elevator.
	 */
	@Test
	public void testElevatorCall() {
		int numFloors = 2;
		int numElevators = 1;
		int moveDelay = 1;
		int threadWait = 1000; // Milliseconds to wait for elevator to service user
		IElevatorSelector selector = new NaiveSelector();
		IElevatorController controller = new ElevatorController(
				selector, numFloors, numElevators, moveDelay);
		controller.startElevators();
		
		RandomUser user = new RandomUser(numFloors, controller);
		
		// Wait for elevator to service our user.
		try {
			Thread.sleep(threadWait);
		} catch (InterruptedException e) {
			fail("Test thread was unexpectedly interrupted.");
		}
		
		assertEquals(true, user.isServiced());
		
		controller.stopElevators();

	}

}
