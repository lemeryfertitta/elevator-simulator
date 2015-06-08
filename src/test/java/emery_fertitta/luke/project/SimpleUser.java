package emery_fertitta.luke.project;

/**
 * A basic elevator user implementation to run the simplest of tests.
 * This user only checks to see if it reaches its desired floor,
 * but will not call the elevator on its own.
 */
public class SimpleUser implements IElevatorUser {
	
	public SimpleUser(int toFloor) {
		serviced = false;
		this.toFloor = toFloor;
	}
	
	public boolean isServiced(){
		return serviced;
	}
	
	@Override
	public void update(int currentFloor) {
		if(currentFloor == toFloor){
			serviced = true;
		}
	}
	
	private boolean serviced;
	private int toFloor;

}
