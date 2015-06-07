package emery_fertitta.luke.project;

import java.util.ArrayList;
import java.util.Arrays;

public class Elevator implements IElevator, Runnable {
	/**
	 * Lowest possible floor.
	 */
	public static final int MIN_FLOOR = 0;
	
	/**
	 * Delay time in milliseconds to simulate time for an elevator to move.
	 */
	public static final int MOVE_TIME = 10;
	
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
		
		movesMade = 0;
	}
	
	public int getMovesMade(){
		return movesMade;
	}
	
	public synchronized Direction getDirection(){
		return direction;
	}
	
	@Override
	public synchronized int getCurrentFloor() {
		return currentFloor;
	}
	
	@Override
	public synchronized boolean isOccupied() {
		return !users.isEmpty();
	}
	
	@Override
	public synchronized void requestFloor(int floor) throws InvalidStateException {
		if(users.isEmpty()){
			throw new InvalidStateException();
		}
		addDestination(floor);
	}
	
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
				movesMade++;
			}
			else if(direction == Direction.DOWN){
				currentFloor--;
				movesMade++;
			}
			destinations[currentFloor] = false;
			notifyUsers();
			
			try {
				Thread.sleep(MOVE_TIME);
			} catch (InterruptedException e) {
				return;
			}
		}
		
	}

	public ElevatorState getState(){
		return new ElevatorState(currentFloor, users.size(),
				direction, Arrays.copyOf(destinations, destinations.length));
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
	private int currentFloor;
	private ArrayList<IElevatorUser> users;
	private boolean[] destinations;
	private Direction direction;
	
	/**
	 * Number of times the elevator has switched floors. For profiling.
	 */
	private int movesMade;
	


}
