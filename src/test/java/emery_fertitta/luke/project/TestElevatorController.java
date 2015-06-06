package emery_fertitta.luke.project;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestElevatorController {
	/**
	 * Number of milliseconds to wait on a given action before timing out.
	 */
	private static final long TIMEOUT = 10000;
	
	/**
	 * Most basic test: one elevator, two floors, and a single request.
	 */
	@Test(timeout=TIMEOUT)
	public void testBasicElevatorUsage() {
		int numFloors = 2;
		int numElevators = 1;
		int minFloor = 0;
		int fromFloor = 0;
		int direction = 1;
		int toFloor = 1;

		IElevatorSelector selector = new NaiveSelector();
		IElevatorController controller = new ElevatorController(
				selector, numFloors, numElevators, minFloor);
		IElevator elevator;
		
		// Try to make an invalid elevator request.
		try{
			controller.callElevator(fromFloor, -direction);
			fail("Exception was not thrown upon an invalid request");
		}
		catch (InvalidRequestException e){
		}

		// Try to make a valid elevator request.
		try {
			elevator = controller.callElevator(fromFloor, direction);
		} catch (InvalidRequestException e) {
			fail("An invalid request exception was thrown upon a valid request.");
			return;
		}
		assertEquals(elevator.currentFloor(), fromFloor);

		// Try to make a valid floor request.
		try {
			elevator.requestFloor(toFloor);
		} catch (InvalidStateException e) {
			fail("An invalid state exception was thrown.");
			return;
		}

		// Wait for the elevator to reach its destination.
		// The elevator should be busy while its not at its destination.
		while(elevator.currentFloor() != toFloor){
			assertEquals(elevator.isBusy(), true);
		}
		assertEquals(elevator.currentFloor(), toFloor);
	}
}
