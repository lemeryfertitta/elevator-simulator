package emery_fertitta.luke.project;

//TODO: Allow for floor < 0 (need to add parameter and modify move method)
public class Elevator implements IElevator, Cloneable {
	
	public enum Direction { UP, DOWN, IDLE };
	
	public Elevator(int numFloors, int startFloor) {
		
		destinations = new boolean[numFloors];
		for(int i = 0; i < numFloors; i++){
			destinations[i] = false;
		}
		direction = Direction.IDLE;
		currentFloor = startFloor;
	}
	
	@Override
	public void requestFloor(int floor) throws InvalidStateException {
		// TODO: Determine what the purpose the InvalidStateException would be.
//		if(!isBusy()){
//			throw new InvalidStateException();
//		}
		destinations[floor] = true;
	}

	@Override
	public boolean isBusy() {
		return direction != Direction.IDLE;
	}

	@Override
	public int currentFloor() {
		return currentFloor;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}
	
	public void removeDestination(int floor){
		destinations[floor] = false;
	}
	
	public void addDestination(int floor){
		destinations[floor] = true;
	}
	
	/**
	 * <p> Moves the elevator one floor in the current direction of travel,
	 * or not at all if the elevator is IDLE. </p>
	 * 
	 * <p> The elevator will switch directions if it has serviced all requests in 
	 * the direction that it is currently traveling in. </p>
	 * 
	 * <p> The floor that the elevator moves to (or remains on) will then be 
	 * removed from the list of requests. </p>
	 */
	public void move(){
		if(direction == Direction.UP){
			if(isRequestInDirection(Direction.UP)){
				currentFloor++;
			}
			else{
				if(isRequestInDirection(Direction.DOWN)){
					direction = Direction.DOWN;
					currentFloor--;
				}
				else{
					direction = Direction.IDLE;
				}
			}
			
		}
		else if(direction == Direction.DOWN){
			if(isRequestInDirection(Direction.DOWN)){
				currentFloor--;
			}
			else{
				if(isRequestInDirection(Direction.UP)){
					direction = Direction.UP;
					currentFloor++;
				}
				else{
					direction = Direction.IDLE;
				}
			}
		}
		else{
			// Find the nearest unserviced request, if any,
			// and head in that direction.
			int upDistance = 0;
			for(int i = (currentFloor + 1); i < destinations.length; i++){
				if(destinations[i]){
					upDistance = i - currentFloor;
					break;
				}
			}
			int downDistance = 0;
			for(int i = (currentFloor - 1); i >= 0; i--){
				if(destinations[i]){
					downDistance = currentFloor - i;
					break;
				}
			}
			if((downDistance != 0) || (upDistance != 0)){
				if((downDistance != 0) && (downDistance < upDistance)){
					direction = Direction.DOWN;
				}
				else{
					direction = Direction.UP;
				}
			}
		}
		destinations[currentFloor] = false;
	}

	/**
	 * Verifies whether there still exists more unserviced requests in the
	 * current direction of travel.
	 */
	private boolean isRequestInDirection(Direction direction){
		boolean existsRequest = false;
		if(direction == Direction.UP){
			for(int i = (currentFloor + 1); i < destinations.length; i++){
				if(destinations[i]){
					existsRequest = true;
					break;
				}
			}
		}
		else if(direction == Direction.DOWN){
			for(int i = (currentFloor - 1); i >= 0; i--){
				if(destinations[i]){
					existsRequest = true;
					break;
				}
			}
		}
		return existsRequest;
		
	}
	
	private int currentFloor;
	private boolean[] destinations;
	private Direction direction;

}
