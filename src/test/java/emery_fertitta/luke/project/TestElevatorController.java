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
		int elevatorMoveDelay = 0; // Number of milliseconds to delay an elevator between switching floors.
		IElevatorSelector selector = new NaiveSelector();
		
		IElevatorController controller = new ElevatorController(selector, 
				numFloors, numElevators, elevatorMoveDelay);
		
		controller.startElevators();
		RandomUser person = new RandomUser(numFloors, controller);
		
		while(!person.isServiced()){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
		IElevatorSelector selector = new NaiveSelector(); // Selection algorithm to test.
		
		IElevatorController controller = new ElevatorController(
				selector, numFloors, numElevators, elevatorMoveDelay);
		RandomUser[] people = new RandomUser[numRequests];
		
		controller.startElevators();
		
		// Spawn requests with a random delay between 0 and maxRequestDelay.
		for(int i = 0; i < numRequests; i++){
			people[i] = new RandomUser(numFloors, controller);
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
	
	/**
	 * Provide some basic information for a test that has been run.
	 * @param controller The controller used for the test run.
	 * @param people The array of people used for the test run.
	 */
	public void printProfile(IElevatorController controller, RandomUser[] people){
		int elevatorMoves = controller.getTotalElevatorMoves();
		double serviceTimeMax = 0;
		double serviceTimeTotal = 0;
		int numServiced = 0;
		for(RandomUser p : people){
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
