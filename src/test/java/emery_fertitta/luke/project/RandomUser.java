package emery_fertitta.luke.project;

import java.util.Random;

/**
 * A random request generator.
 */
public class RandomUser implements IElevatorUser {
	
	/**
	 * Initialize a request and call an elevator.
	 * @param numFloors
	 * @param controller
	 */
	public RandomUser(int numFloors, IElevatorController controller){
		Random r = new Random();
		fromFloor = r.nextInt(numFloors);
		toFloor = r.nextInt(numFloors);
		serviced = false;
		waiting = true;
		int direction = toFloor - fromFloor;
		if((direction == 0) && (fromFloor == 0)){
			// To prevent a down request on the bottom floor.
			direction = 1;
		}
		startTimeNano = System.nanoTime();
		try{
			controller.callElevator(this, fromFloor, direction);
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isServiced(){
		return serviced;
	}
	
	/**
	 * @return The amount of time since the elevator was initially called.
	 */
	public double getServiceTime(){
		return serviceTime;
	}

	@Override
	public void update(IElevator elevator){
		int currentFloor = elevator.getCurrentFloor();
		if((waiting) && (currentFloor == fromFloor)){
			// Requested elevator has reached the floor we are waiting on.
			waiting = false;
			try {
				elevator.enterElevator(currentFloor, this);
				
			} catch (WrongFloorException e) {
				e.printStackTrace();
			}
			try {
				elevator.requestFloor(toFloor);
			} catch (InvalidStateException e) {
				e.printStackTrace();
			}
		}
		else if((!waiting) && (currentFloor == toFloor)){
			// Occupied elevator has reach the floor we are going to.
			serviced = true;
			serviceTime = (System.nanoTime() - startTimeNano)/1000000000.0;
			try {
				elevator.leaveElevator(toFloor, this);
			} catch (WrongFloorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private boolean waiting;
	private long startTimeNano;
	private double serviceTime;
	private int fromFloor;
	private int toFloor;
	private boolean serviced;
}
