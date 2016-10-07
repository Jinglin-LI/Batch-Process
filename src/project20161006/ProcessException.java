package project20161006;


public class ProcessException extends Exception {
	
	/**
	 * Exception used to signal problems when executing batch files
	 */
	private static final long serialVersionUID = 1L;


	public ProcessException(String message) {
		super(message);
	}

	public ProcessException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
