package emery_fertitta.luke.project;

@SuppressWarnings("serial")
public class InvalidStateException extends Exception {
	@Override
	public String getMessage() {
		return "Request made an non-busy elevator";
	}
}
