package emery_fertitta.luke.project;

public class ElevatorController implements IElevatorController {
	private int numFloors;
	private int numElevators;
	
	public ElevatorController(int numFloors, int numElevators){
		this.numFloors = numFloors;
		this.numElevators = numElevators;
	}
	
	@Override
	public IElevator callElevator(int fromFloor, int direction)
			throws InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

}
