package emery_fertitta.luke.project;

/**
 * This elevator selection algorithm chooses the elevator which 
 * will require the least moves from its current state to service the request.
 * Note that this algorithm uses an overestimate of the distance by assuming 
 * that the elevator will always go to the top or bottom floor before switching 
 * directions. Additionally, the elevator incurs a penalty to its distance if the request
 * is in the opposite direction of the elevator, by again overestimating the distance.
 */
public class NearestSelector implements IElevatorSelector {

	@Override
	public int selectElevator(int fromFloor, int direction, ElevatorState[] elevators) {
		int minDistance = Integer.MAX_VALUE;
		int minIndex = 0;
		for(int i = 0; i < elevators.length; i++){
			int distance = 0;
			int elevatorFloor = elevators[i].getCurrentFloor();
			if(elevatorFloor == fromFloor){
				// This elevator is already at the desired floor
				return i;
			}
			Elevator.Direction elevatorDirection = elevators[i].getDirection();
			if(elevatorDirection == Elevator.Direction.UP){
				distance = fromFloor - elevatorFloor;
				if(distance < 0){
					// Floor is below and elevator is going up
					distance = -distance + 2*(elevators[i].getDestinations().length - elevatorFloor);
					// Elevator is not going in the desired direction
					if(direction > 0){
						distance += 2*fromFloor;
					}
				}
				else if(direction <= 0){
					// Elevator is not going in the desired direction
					distance += 2*(elevators[i].getDestinations().length - fromFloor);
				}
			}
			else if(elevatorDirection == Elevator.Direction.DOWN){
				distance = elevatorFloor - fromFloor;
				if(distance < 0){
					// Floor is above and going down.
					distance = -distance + 2*(elevatorFloor);
					// Elevator is not going in the desired direction
					if(direction <= 0){
						distance += 2*(elevators[i].getDestinations().length - fromFloor);;
					}
				}
				else if(direction > 0){
					// Elevator is not going in the desired direction
					distance += 2*fromFloor;
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
