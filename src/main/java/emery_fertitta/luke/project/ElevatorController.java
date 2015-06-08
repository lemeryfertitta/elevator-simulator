package emery_fertitta.luke.project;

import java.util.ArrayList;

public class ElevatorController implements IElevatorController {


	@SuppressWarnings("unchecked")
	public ElevatorController(IElevatorSelector selector, int numFloors,
			int numElevators, int moveDelay){
		this.minFloor = Elevator.MIN_FLOOR;
		this.maxFloor = Elevator.MIN_FLOOR + numFloors;
		this.selector = selector;
		this.waitingUsers = (ArrayList<IElevatorUser>[]) new ArrayList[numFloors];
		for(int i = 0; i < numFloors; i++){
			waitingUsers[i] = new ArrayList<IElevatorUser>();
		}
		this.elevators = new Elevator[numElevators];
		this.elevatorThreads = new Thread[numElevators];
		for(int i = 0; i < numElevators; i++){
			elevators[i] = new Elevator(numFloors, minFloor, moveDelay);
			elevatorThreads[i] = new Thread(elevators[i]);
		}
	}

	@Override
	public void callElevator(IElevatorUser user, int fromFloor, int direction)
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
		
		// Get the elevator states and pass to the selection algorithm.
		ElevatorState[] states = new ElevatorState[elevators.length];
		for(int i = 0; i < elevators.length; i++){
			states[i] = elevators[i].getState();
		}
		int selectedIndex =  selector.selectElevator(fromFloor, direction, states);
		
		// Send the elevator to that floor.
		elevators[selectedIndex].addDestination(fromFloor);
		
		// Add the user to the elevator so they can be notified.
		elevators[selectedIndex].addRequester(user);
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
	private ArrayList<IElevatorUser>[] waitingUsers;
	private Elevator[] elevators;
	private Thread[] elevatorThreads;
}
