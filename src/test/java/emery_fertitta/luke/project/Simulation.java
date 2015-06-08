package emery_fertitta.luke.project;

import static org.junit.Assert.fail;

import java.util.Random;

public class Simulation {
	/**
	 * Run an elevator simulation.
	 * @param numFloors The number of floors.
	 * @param numElevators The number of elevators to run.
	 * @param numRequests The number of user requests that will be attempted
	 * @param runTime Milliseconds. The amount of time to run the simulation
	 * 		  before stopping.
	 * @param requestDelay Milliseconds. Must be at least 1. 
	 * 	      The max amount of random delay to add between requests
	 * @param elevatorDelay Milliseconds. The amount of time to delay the elevators between switching floors.
	 * @param selector The selection algorithm to use.
	 */
	public static void runSim(int numFloors, int numElevators, int numRequests,
			int runTime, int requestDelay, int elevatorDelay, IElevatorSelector selector){
		IElevatorController controller = new ElevatorController(
				selector, numFloors, numElevators, elevatorDelay);
		RandomUser[] people = new RandomUser[numRequests];

		controller.startElevators();

		// Spawn requests with a random delay between 0 and maxRequestDelay.
		for(int i = 0; i < numRequests; i++){
			people[i] = new RandomUser(numFloors, controller);
			try {
				Thread.sleep(new Random().nextInt(requestDelay));
			} catch (InterruptedException e) {
				fail("Test thread was unexpectedly interrupted.");
			}
		}

		// Wait for the simulation to handle all of the requests.
		try {
			Thread.sleep(runTime);
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
	public static void printProfile(IElevatorController controller, RandomUser[] people){
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
