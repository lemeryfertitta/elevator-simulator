package emery_fertitta.luke.project;

import java.util.Random;

public class PersonSimulator implements Runnable, IElevatorUser {
	
	private long startTimeNano;
	private double serviceTime;
	private int fromFloor;
	private int toFloor;
	private boolean serviced;
	private IElevatorController controller;
	private IElevator elevator;
	
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
			//TODO: Can occasionally generate a bad request like 0, 0
			elevator = controller.callElevator(fromFloor, toFloor - fromFloor);
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		elevator.enterElevator(this);
		
		try {
			elevator.requestFloor(toFloor);		
		} catch (InvalidStateException e) {
			// TODO: In the gap between enterElevator() and requestFloor(), 
			// the elevator might reach the desired floor
			// and the user may leave the elevator, emptying it.
			// This then causes this exception.
			return;
		}
	}

	@Override
	public void update(int currentFloor) {
		if(currentFloor == toFloor){
			serviced = true;
			serviceTime = (System.nanoTime() - startTimeNano)/1000000000.0;
			elevator.leaveElevator(this);
		}
	}
}
