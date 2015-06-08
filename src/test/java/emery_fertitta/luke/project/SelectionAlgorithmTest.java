package emery_fertitta.luke.project;

import org.junit.Test;

public class SelectionAlgorithmTest {
	// Simulation parameters. See Simulation.runTime() docs for explanations.
	private static final int NUM_FLOORS = 100;
	private static final int NUM_ELEVATORS = 6;
	private static final int NUM_REQUESTS = 100;
	private static final int RUN_TIME = 10000;
	private static final int REQUEST_DELAY = 50;
	private static final int ELEVATOR_DELAY = 50;
	
	@Test
	public void testNaiveSelector() {
		System.out.println("Testing naive selection algorithm...");
		IElevatorSelector selector = new NaiveSelector();
		Simulation.runSim(NUM_FLOORS, NUM_ELEVATORS, NUM_REQUESTS, RUN_TIME,
				REQUEST_DELAY, ELEVATOR_DELAY, selector);
	}
	
	@Test
	public void testRandomSelector() {
		System.out.println("Testing random selection algorithm...");
		IElevatorSelector selector = new RandomSelector();
		Simulation.runSim(NUM_FLOORS, NUM_ELEVATORS, NUM_REQUESTS, RUN_TIME,
				REQUEST_DELAY, ELEVATOR_DELAY, selector);
	}
	
	@Test
	public void testNearestSelector() {
		System.out.println("Testing nearest selection algorithm...");
		IElevatorSelector selector = new NearestSelector();
		Simulation.runSim(NUM_FLOORS, NUM_ELEVATORS, NUM_REQUESTS, RUN_TIME,
				REQUEST_DELAY, ELEVATOR_DELAY, selector);
	}

}
