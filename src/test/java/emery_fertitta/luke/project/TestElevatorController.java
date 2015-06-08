package emery_fertitta.luke.project;

import static org.junit.Assert.*;

import java.util.Random;

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
		int elevatorMoveDelay = 0; // Number of milliseconds to delay an elevator between switching floors.
		IElevatorSelector selector = new NaiveSelector();
		
		IElevatorController controller = new ElevatorController(selector, 
				numFloors, numElevators, elevatorMoveDelay);
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
		
		try {
			elevator.enterElevator(fromFloor, person);
		} catch (WrongFloorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
		try {
			elevator.leaveElevator(toFloor, person);
		} catch (WrongFloorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(elevator.isOccupied(), false);
		controller.stopElevators();
	}
	
	@Test
	public void testManyElevatorsAndFloors() {
		// Parameters
		int numFloors = 100;
		int numElevators = 6;
		int numRequests = 200; // Number of requests to make.
		int waitTime = 10000; // Milliseconds to wait for requests to be fulfilled.
		int maxRequestDelay = 10; // Max number of milliseconds to delay between requests.
		int elevatorMoveDelay = 10; // Number of milliseconds to delay an elevator between switching floors.
		IElevatorSelector selector = new NearestSelector(); // Selection algorithm to test.
		
		IElevatorController controller = new ElevatorController(
				selector, numFloors, numElevators, elevatorMoveDelay);
		PersonSimulator[] people = new PersonSimulator[numRequests];
		Thread[] peopleThreads = new Thread[numRequests];
		
		controller.startElevators();
		
		// Spawn request threads with a random delay between 0 and maxRequestDelay.
		for(int i = 0; i < numRequests; i++){
			people[i] = new PersonSimulator(numFloors, controller);
			peopleThreads[i] = new Thread(people[i]);
			peopleThreads[i].start();
			try {
				Thread.sleep(new Random().nextInt(maxRequestDelay));
			} catch (InterruptedException e) {
				fail("Test thread was unexpectedly interrupted.");
			}
		}
		
		// Wait for the simulation to handle all of the requests.
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			fail("Test thread was unexpectedly interrupted.");
		}
		
		controller.stopElevators();
		printProfile(controller, people);
}
	
	public void printProfile(IElevatorController controller, PersonSimulator[] people){
		int elevatorMoves = controller.getTotalElevatorMoves();
		double serviceTimeMax = 0;
		double serviceTimeTotal = 0;
		int numServiced = 0;
		for(PersonSimulator p : people){
			if(p.isServiced()){
				numServiced++;
				double serviceTime = p.getServiceTime();
				serviceTimeTotal += serviceTime;
				if(serviceTime > serviceTimeMax){
					serviceTimeMax = serviceTime;
				}
			}
		}
		double avgServiceTime = serviceTimeTotal/(people.length);
		System.out.println("----- ELEVATOR PROFILE -----");
		System.out.println("Number of elevator moves: " + Integer.toString(elevatorMoves));
		System.out.println("Percent of requests serviced: " + 
				Double.toString(100.0*((double)numServiced/people.length)) + "%");
		System.out.println("Maximum service time (excluding unserviced): " + Double.toString(serviceTimeMax));
		System.out.println("Average service time (excluding unserviced): " + Double.toString(avgServiceTime));
		System.out.println("----- ---------------- -----");
	}
	
}
