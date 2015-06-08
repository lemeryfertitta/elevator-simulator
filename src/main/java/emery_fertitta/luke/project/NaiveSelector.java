package emery_fertitta.luke.project;

/**
 * Only use the first elevator.
 */
public class NaiveSelector implements IElevatorSelector {

	@Override
	public int selectElevator(int fromFloor, int direction, ElevatorState[] elevators) {
		return 0;
	}

}
