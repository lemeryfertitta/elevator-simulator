package emery_fertitta.luke.project;

import java.util.Random;

/**
 * Select an elevator at random.
 */
public class RandomSelector implements IElevatorSelector {
	
	@Override
	public int selectElevator(int fromFloor, int direction, ElevatorState[] elevators) {
		return rand.nextInt(elevators.length);
	}
	
	private Random rand = new Random();

}
