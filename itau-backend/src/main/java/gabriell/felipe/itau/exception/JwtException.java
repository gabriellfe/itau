package gabriell.felipe.itau.exception;

public class JwtException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String error;
	
	public String getError() {
		return error;
	}

	public JwtException(String message, String error) {
		super(message);
		this.error = error;
	}
}
