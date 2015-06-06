package emery_fertitta.luke.project;


public interface IElevator {
    /**
     * Requests the Elevator to move to a certain floor. This method imitates pressing a button inside the
     * elevator. Therefore, it should not move the Elevator immediately but just register the request.
     * 
     * Bonus: this method should throw InvalidStateException if the Elevator is NOT busy.
     */
    void requestFloor(int floor) throws InvalidStateException;
    
    /**
     * Returns the internal state of the Elevator.
     */
    boolean isBusy();
    
    /**
     * Returns the floor where the Elevator is now.
     */
    int currentFloor();
    
}
