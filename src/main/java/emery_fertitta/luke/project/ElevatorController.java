package emery_fertitta.luke.project;

public class ElevatorController implements IElevatorController {


	public ElevatorController(IElevatorSelector selector, int numFloors,
			int numElevators){
		this.minFloor = Elevator.MIN_FLOOR;
		this.maxFloor = Elevator.MIN_FLOOR + numFloors;
		this.selector = selector;
		this.elevators = new Elevator[numElevators];
		this.elevatorThreads = new Thread[numElevators];
		for(int i = 0; i < numElevators; i++){
			elevators[i] = new Elevator(numFloors, minFloor);
			elevatorThreads[i] = new Thread(elevators[i]);
		}
	}

	@Override
	public IElevator callElevator(int fromFloor, int direction)
			throws InvalidRequestException {

		// Verify that fromFloor is a valid floor
		if((fromFloor < minFloor) || (fromFloor > maxFloor)){
			throw new InvalidRequestException();
		}

		// Verify that the desired direction is valid
		if(((fromFloor == minFloor) && (direction <= 0)) || 
				((fromFloor == maxFloor) && (direction > 0))){
			throw new InvalidRequestException();
		}

		int selectedIndex =  selector.selectElevator(cloneElevators());
		elevators[selectedIndex].addDestination(fromFloor);
		return elevators[selectedIndex];
	}

	
	private Elevator[] cloneElevators(){
		Elevator[] elevatorsClone = new Elevator[elevators.length];
		for(int i = 0; i < elevators.length; i++){
			try {
				elevatorsClone[i] = (Elevator) elevators[i].clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return elevatorsClone;
	}


	@Override
	public void startElevators() {
		for(Thread t : elevatorThreads){
			t.start();
		}
	}

	@Override
	public void stopElevators() {
		for(Thread t : elevatorThreads){
			t.interrupt();
		}
	}

	public int getTotalElevatorMoves(){
		int total = 0;
		for(Elevator e : elevators){
			total += e.getMovesMade();
		}
		return total;
	}
	
	private int minFloor;
	private int maxFloor;
	private IElevatorSelector selector;
	private Elevator[] elevators;
	private Thread[] elevatorThreads;
}
