package emery_fertitta.luke.project;


public interface IElevator {
    /**
     * Requests the Elevator to move to a certain floor. 
     * This method imitates pressing a button inside the
     * elevator. Therefore, it should not move the Elevator immediately
     * but just register the request.
     * 
     * @param floor Desired destination floor.
     * @throws InvalidStateException if the elevator is NOT occupied. Use enterElevator() before
     * making a request
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
     * Adds a user to the list of users waiting to be picked up by the elevator.
     * @param user
     */
    public void addRequester(IElevatorUser user);
    
    /**
     * Add the user to the list of users occupying the elevator, and removes them
     * from the list of users requesting the elevator.
     * @param user A person who called the elevator.
     */
    public void enterElevator(int fromFloor, IElevatorUser user) throws WrongFloorException;
    
    /**
     * Remove the user from the list of users occupying the elevator.
     * @param user A person inside the elevator.
     */
    public void leaveElevator(int toFloor, IElevatorUser user) throws WrongFloorException;
    
    /**
     * For profiling.
     * @return The number of floor switches made by the elevator.
     */
    public int getMovesMade();
    
}
