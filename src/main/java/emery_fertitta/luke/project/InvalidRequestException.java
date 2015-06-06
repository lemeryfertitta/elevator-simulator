package emery_fertitta.luke.project;

@SuppressWarnings("serial")
public class InvalidRequestException extends Exception {
	@Override
	public String getMessage() {
		return "The elevator request is ill-formed. "
				+ "Either fromFloor < minimum floor, "
				+ "or > maximum floor, or direction is invalid";
	}
}
