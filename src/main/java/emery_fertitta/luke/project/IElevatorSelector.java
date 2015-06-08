package emery_fertitta.luke.project;

public interface IElevatorSelector {
	/**
	 * This algorithm decides which elevator should be tasked with 
	 * responding to a call.
	 * 
	 * @param fromFloor The floor from which a call was made.
	 * @param elevators The array of elevators states to select from.
	 * @return The index of the selected elevator. Must be in range [0, elevators.length)
	 */
	public int selectElevator(int fromFloor, int direction, ElevatorState[] elevators);
}
