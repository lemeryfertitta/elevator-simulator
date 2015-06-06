package emery_fertitta.luke.project;

public interface IElevatorSelector {
	/**
	 * This algorithm decides which elevator should be tasked with 
	 * responding to a call.
	 * 
	 * @param elevators The array of elevators to select from.
	 * @return The index of the selected elevator.
	 */
	public int selectElevator(Elevator[] elevators);
}