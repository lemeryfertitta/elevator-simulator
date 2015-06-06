package emery_fertitta.luke.project;

public interface IElevatorController{
    /**
     * This is a blocking call that returns an Elevator instance.
     * 
     * @param fromFloor Floor number where the call is made from.
     * @param direction If > 0, going up; if <= 0 going down.
     * 
     * @return an instance of an Elevator
     * @throws InvalidRequestException when fromFloor < minimum floor, or > maximum floor, or direction is invalid.
     */
    public IElevator callElevator(int fromFloor, int direction) throws InvalidRequestException;
}
