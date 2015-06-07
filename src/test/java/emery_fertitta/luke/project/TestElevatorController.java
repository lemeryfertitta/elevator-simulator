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
		SimpleUser person = new SimpleUser(toFloor);
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
		assertEquals(fromFloor, elevator.getCurrentFloor());
		
		// Try to make an invalid floor request.
		try {
			elevator.requestFloor(toFloor);
			fail("An invalid state exception wasn't thrown.");
		} catch (InvalidStateException e) {
		}
		
		elevator.enterElevator(person);
		
		// Try to make a valid floor request.
		try {
			elevator.requestFloor(toFloor);
		} catch (InvalidStateException e) {
			fail("An invalid state exception was thrown.");
		}

		while(!person.isServiced()){
			if(!elevator.isOccupied()){
				fail("Elevator in use should still be occupied");
			}
		}
		elevator.leaveElevator(person);
		assertEquals(elevator.isOccupied(), false);
		controller.stopElevators();
	}
	
	@Test
	public void testManyElevatorsAndFloors() {
		int numFloors = 100;
		int numElevators = 2;
		int fromFloor = 0;
		int direction = 1;
		int toFloor = 1;
		int numRequests = 100;
		int waitTimeMilli = 5000;
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
			Thread.sleep(waitTimeMilli);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(PersonSimulator p : people){
			if(!p.isServiced()){
				fail("An elevator usage was not completed after " + 
						Double.toString(waitTimeMilli/1000.0) + " seconds.");
			}
		}
		
		controller.stopElevators();
		printProfile(controller, people);
}
	
	public void printProfile(IElevatorController controller, PersonSimulator[] people){
		int elevatorMoves = controller.getTotalElevatorMoves();
		double serviceTimeMax = 0;
		double serviceTimeTotal = 0;
		for(PersonSimulator p : people){
			double serviceTime = p.getServiceTime();
			serviceTimeTotal += serviceTime;
			if(serviceTime > serviceTimeMax){
				serviceTimeMax = serviceTime;
			}
		}
		double avgServiceTime = serviceTimeTotal/(people.length);
		System.out.println("----- ELEVATOR PROFILE -----");
		System.out.println("Number of elevator moves: " + Integer.toString(elevatorMoves));
		System.out.println("Maximum service time: " + Double.toString(serviceTimeMax));
		System.out.println("Average service time: " + Double.toString(avgServiceTime));
		System.out.println("----- ---------------- -----");
	}
	
}
