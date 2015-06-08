package emery_fertitta.luke.project;

@SuppressWarnings("serial")
public class WrongFloorException extends Exception {
	@Override
	public String getMessage() {
		return "Cannot enter an elevator that is not on the same floor";
	}
}
