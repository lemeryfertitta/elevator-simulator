package emery_fertitta.luke.project;

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
