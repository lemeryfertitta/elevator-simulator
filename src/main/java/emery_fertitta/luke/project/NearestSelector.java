package emery_fertitta.luke.project;

/**
 * This elevator selection algorithm chooses the elevator which 
 * will require the least moves from its current state to service the request.
 * Note that this algorithm uses an overestimate of the distance by assuming 
 * that the elevator will always go to the top or bottom floor before switching 
 * directions.
 */
public class NearestSelector implements IElevatorSelector {

	@Override
	public int selectElevator(int fromFloor, ElevatorState[] elevators) {
		int minDistance = Integer.MAX_VALUE;
		int minIndex = 0;
		for(int i = 0; i < elevators.length; i++){
			int distance = 0;
			int elevatorFloor = elevators[i].getCurrentFloor();
			if(elevatorFloor == fromFloor){
				// This elevator is already at the desired floor
				return i;
			}
			Elevator.Direction direction = elevators[i].getDirection();
			if(direction == Elevator.Direction.UP){
				distance = fromFloor - elevatorFloor;
				if(distance < 0){
					// Floor is below and elevator is going up
					distance = -distance + 2*(elevators[i].getDestinations().length - elevatorFloor);
				}
			}
			else if(direction == Elevator.Direction.DOWN){
				distance = elevatorFloor - fromFloor;
				if(distance < 0){
					// Floor is above and going down.
					distance = -distance + 2*(elevatorFloor);
				}
			}
			else{
				// The elevator is idle, so the shortest path will be taken.
				distance = Math.abs(elevatorFloor - fromFloor);
			}
			
			// Set new candidate if necessary.
			if(distance < minDistance){
				minDistance = distance;
				minIndex = i;
			}
		}
		return minIndex;
	}

}
