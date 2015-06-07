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
		// Parameters
		int numFloors = 2;
		int numElevators = 1;
		int fromFloor = 0;
		int direction = 1;
		int toFloor = 1;
		IElevatorSelector selector = new NaiveSelector();
		

		IElevatorController controller = new ElevatorController(selector, 
				numFloors, numElevators);
		
		controller.startElevators();
		
		IElevator elevator;
		
		// Try an invalid elevator request.
		try{
			controller.callElevator(fromFloor, -direction);
			fail("Exception was not thrown upon an invalid request");
		}
		catch (InvalidRequestException e){
		}

		// Try a valid elevator request.
		try {
			elevator = controller.callElevator(fromFloor, direction);
		} catch (InvalidRequestException e) {
			fail("An invalid request exception was thrown upon a valid request.");
			return;
		}
		assertEquals(elevator.getCurrentFloor(), fromFloor);

		// Try to make a valid floor request.
		try {
			elevator.requestFloor(toFloor);
		} catch (InvalidStateException e) {
			fail("An invalid state exception was thrown.");
			return;
		}

		// Wait for the elevator to reach its destination.
		// The elevator should be busy while its not at its destination.
		while(elevator.getCurrentFloor() != toFloor){
			// TODO: Uncomment once busy is properly understood.
//			assertEquals(elevator.isBusy(), true);
		}
		assertEquals(elevator.getCurrentFloor(), toFloor);
		controller.stopElevators();
	}
	
	@Test
	public void testManyElevatorsAndFloors() {
		int numFloors = 100;
		int numElevators = 20;
		int fromFloor = 0;
		int direction = 1;
		int toFloor = 1;
		int numRequests = 100;
		IElevatorSelector selector = new RandomSelector();
		
		IElevatorController controller = new ElevatorController(selector, numFloors, numElevators);
		PersonSimulator[] people = new PersonSimulator[numRequests];
		Thread[] peopleThreads = new Thread[numRequests];
		
		controller.startElevators();
		
		for(int i = 0; i < numRequests; i++){
			people[i] = new PersonSimulator(numFloors, controller);
			peopleThreads[i] = new Thread(people[i]);
			peopleThreads[i].start();
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i = 0; i < numRequests; i++){
			System.out.println(people[i].isServiced());
			System.out.println(people[i].getServiceTime());
		}
		
		
		controller.startElevators();
	}
	
}
