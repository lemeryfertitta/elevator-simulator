package emery_fertitta.luke.project;

import java.util.Random;

public class RandomSelector implements IElevatorSelector {
	
	@Override
	public int selectElevator(Elevator[] elevators) {
		return rand.nextInt(elevators.length);
	}
	
	private Random rand = new Random();

}
