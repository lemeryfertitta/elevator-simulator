package emery_fertitta.luke.project;

public interface IElevatorController{
    /**
     * Calls an elevator and adds the user to the list of people waiting at the floor.
     * The user will be notified when an elevator has been made available at the floor.
     * 
     * @param fromFloor Floor number where the call is made from.
     * @param direction If > 0, going up; if <= 0 going down.
     * 
     * @throws InvalidRequestException when fromFloor < minimum floor, or > maximum floor, or direction is invalid.
     */
    public void callElevator(IElevatorUser user, int fromFloor, int direction) throws InvalidRequestException;
    
    /**
     * Allow the elevators to begin moving.
     */
    public void startElevators();
    
    /**
     * Force the elevators to stop moving.
     */
    public void stopElevators();
    
    /**
	 * For profiling.
	 * @return The count of floor switches across all elevators.
	 */
	public int getTotalElevatorMoves();
}
