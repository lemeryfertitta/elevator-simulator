package emery_fertitta.luke.project;


public interface IElevator {
    /**
     * <p> Requests the Elevator to move to a certain floor. 
     * This method imitates pressing a button inside the
     * elevator. Therefore, it should not move the Elevator immediately
     * but just register the request. </p>
     * 
     * <p> This method throws an InvalidStateException if the Elevator is NOT occupied.
     * Use enterElevator() before calling floor requests. </p>
     */
    public void requestFloor(int floor) throws InvalidStateException;
    
    /**
     * @return false if empty, true otherwise.
     */
    public boolean isOccupied();
    
    /**
     * @return the floor where the Elevator is now.
     */
    public int getCurrentFloor();
    
    /**
     * Inform the users of the elevator of some state change.
     */
    public void notifyUsers();
    
    /**
     * Add the user to the list of users occupying the elevator.
     * @param user A person who called the elevator.
     */
    public void enterElevator(IElevatorUser user);
    
    /**
     * Remove the user from the list of users occupying the elevator.
     * @param user A person inside the elevator.
     */
    public void leaveElevator(IElevatorUser user);
    
    /**
     * For profiling.
     * @return The number of floor switches made by the elevator.
     */
    public int getMovesMade();
    
}
