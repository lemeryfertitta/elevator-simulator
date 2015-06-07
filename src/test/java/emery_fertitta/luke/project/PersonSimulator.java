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
	
	public double getServiceTime(){
		return serviceTime;
	}

	@Override
	public void run() {
		startTimeNano = System.nanoTime();
		try {
			elevator = controller.callElevator(fromFloor, toFloor - fromFloor);
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			elevator.enterElevator(this);
			elevator.requestFloor(toFloor);		
		} catch (InvalidStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
