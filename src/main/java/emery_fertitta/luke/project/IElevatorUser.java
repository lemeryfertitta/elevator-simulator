package emery_fertitta.luke.project;

public interface IElevatorUser {
	/**
	 * Inform the user of the state of the elevator they are occupying.
	 * @param currentFloor The floor that the elevator is on.
	 */
	public void update(int currentFloor);
}
