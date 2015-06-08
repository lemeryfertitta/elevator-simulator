package emery_fertitta.luke.project;

import java.util.Random;

/**
 * A random request generator that can be run concurrently to avoid
 * blocking while waiting for an elevator to be called.
 */
public class PersonSimulator implements Runnable, IElevatorUser {
	
	public PersonSimulator(int numFloors, IElevatorController controller){
		Random r = new Random();
		fromFloor = r.nextInt(numFloors);
		toFloor = r.nextInt(numFloors);
		this.controller = controller;
		serviced = false;
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
	
	/**
	 * Call an elevator, enter it, and request a floor.
	 */
	@Override
	public void run() {
		startTimeNano = System.nanoTime();
		try {
			int direction = toFloor - fromFloor;
			if((direction == 0) && (fromFloor == 0)){
				// To prevent a down request on the bottom floor.
				direction = 1;
			}
			elevator = controller.callElevator(fromFloor, toFloor - fromFloor);
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			return;
		}
		try {
			elevator.enterElevator(fromFloor, this);
		} catch (WrongFloorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			elevator.requestFloor(toFloor);		
		} catch (InvalidStateException e) {
			// TODO: Handle more appropriately.
			/* In the gap between enterElevator() and requestFloor(), 
			 * the elevator might reach the desired floor
			 * and the user may leave the elevator, emptying it.
			 * This then causes this exception.
			 * The exception is currently ignored because the person has beens serviced.
			 */
			return;
		}
	}

	@Override
	public void update(int currentFloor) {
		if(currentFloor == toFloor){
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
	
	private long startTimeNano;
	private double serviceTime;
	private int fromFloor;
	private int toFloor;
	private boolean serviced;
	private IElevatorController controller;
	private IElevator elevator;
}
