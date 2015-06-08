package emery_fertitta.luke.project;

/**
 * The minimal information about the state of an elevator
 * for making the decision of which elevator to send to an elevator call.
 */
public class ElevatorState {
	private int currentFloor;
	private int numUsers;
	private Elevator.Direction direction;
	private boolean[] destinations;
	
	public ElevatorState(int currentFloor, int numUsers, Elevator.Direction direction, boolean[] destinations){
		this.currentFloor = currentFloor;
		this.numUsers = numUsers;
		this.direction = direction;
		this.destinations = destinations;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}

	public int getNumUsers() {
		return numUsers;
	}
	
	public Elevator.Direction getDirection() {
		return direction;
	}
	
	public boolean[] getDestinations() {
		return destinations;
	}

}
