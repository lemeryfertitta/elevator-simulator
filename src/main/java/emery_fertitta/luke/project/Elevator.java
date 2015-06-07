package emery_fertitta.luke.project;

import java.util.ArrayList;

public class Elevator implements IElevator, Runnable, Cloneable {
	public static final int MIN_FLOOR = 0;
	public enum Direction { UP, DOWN, IDLE };
	
	public Elevator(int numFloors, int startFloor) {
		// Initialize with no destinations.
		destinations = new boolean[numFloors];
		for(int i = 0; i < numFloors; i++){
			destinations[i] = false;
		}
		
		// These values represent no max or min destinations.
		maxDestination = MIN_FLOOR - 1;
		minDestination = numFloors;
		
		direction = Direction.IDLE;
		currentFloor = startFloor;
		
		users = new ArrayList<IElevatorUser>();
	}
	
	public synchronized Direction getDirection(){
		return direction;
	}
	
	@Override
	public synchronized int getCurrentFloor() {
		return currentFloor;
	}
	
	@Override
	public synchronized void requestFloor(int floor) throws InvalidStateException {
		if(users.isEmpty()){
			throw new InvalidStateException();
		}
		addDestination(floor);
	}
	
	/**
	 * <p> Forcefully add a destination to the elevator,
	 * regardless of its current state or direction. </p>
	 * 
	 *  <p> This is to be used by the controller when sending elevators
	 * to floors where elevators have been called. </p>
	 * 
	 * @param floor The floor to be added to the destination list.
	 */
	public synchronized void addDestination(int floor){
		destinations[floor] = true;
		if(floor > maxDestination){
			maxDestination = floor;
		}
		if(floor < minDestination){
			minDestination = floor;
		}
	}

	@Override
	public synchronized boolean isBusy() {
		return !users.isEmpty();
	}


	@Override
	public synchronized Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}
	
	/**
	 * <p> Continuously moves the elevator in the current direction of travel,
	 * or not at all if the elevator is IDLE. </p>
	 * 
	 * <p> The elevator will switch directions if all remaining destinations are 
	 * in the opposite direction. If there are no remaining destinations then 
	 * the elevator will become IDLE. </p>
	 * 
	 * <p> While IDLE, the elevator will continuously poll for unserviced
	 *  destinations. If there are destinations above and below, the closest
	 *  is chosen </p>
	 * 
	 * <p> The floor that the elevator moves to (or remains on) will then be 
	 * removed from the list of requests. </p>
	 */
	@Override
	public void run(){
		while(!Thread.interrupted()){
			// Switch directions if necessary
			if((direction == Direction.UP) && (maxDestination == currentFloor)){
				maxDestination = MIN_FLOOR - 1;
				if(minDestination < currentFloor){
					direction = Direction.DOWN;
				}
				else{
					direction = Direction.IDLE;
				}
			}
			else if((direction == Direction.DOWN) && (minDestination == currentFloor)){
				minDestination = destinations.length;
				if(maxDestination > currentFloor){
					direction = Direction.UP;
				}
				else{
					direction = Direction.IDLE;
				}
			}
			else{
				boolean destinationsBelow = minDestination < currentFloor;
				boolean destinationsAbove = maxDestination > currentFloor;
				if(destinationsBelow && destinationsAbove){
					int downDistance = currentFloor - minDestination;
					int upDistance = maxDestination - currentFloor;
					if(upDistance < downDistance){
						direction = Direction.UP;
					}
					else{
						direction = Direction.DOWN;
					}
				}
				else if(destinationsBelow){
					direction = Direction.DOWN;
				}
				else if(destinationsAbove){
					direction = Direction.UP;
				}
			}

			// Move the elevator
			if(direction == Direction.UP){
				currentFloor++;
			}
			else if(direction == Direction.DOWN){
				currentFloor--;
			}
			destinations[currentFloor] = false;
			notifyUsers();
		}
		
	}
		
	/**
	 * <p> The number of the highest unserviced floor. </p>
	 * 
	 * <p> Set to MIN_FLOOR - 1 if there is no floor to service
	 * above the elevator's current floor. </p>
	 */
	private int maxDestination;
	
	/**
	 * <p> The number of the lowest unserviced floor. </p>
	 * 
	 * <p> Set to the number of floors if there is no floor to service
	 * below the elevator's current floor. </p>
	 */
	private int minDestination;
	private ArrayList<IElevatorUser> users;
	private int currentFloor;
	private boolean[] destinations;
	private Direction direction;
	@Override
	public synchronized void notifyUsers() {
		if(!users.isEmpty()){
			// Create an array so that we can modify the users ArrayList concurrently.
			IElevatorUser[] userArray = users.toArray(new IElevatorUser[users.size()]);
			for(IElevatorUser u : userArray){
				u.update(currentFloor);
			}
		}
	}

	@Override
	public synchronized void enterElevator(IElevatorUser user) {
		users.add(user);
	}

	@Override
	public synchronized void leaveElevator(IElevatorUser user) {
		users.remove(user);
	}

}
