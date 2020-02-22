package asathinker.compilers.calculator;

import asathinker.compilers.calculator.Token.Position;

public class CalculatorException extends Exception {

	private static final long serialVersionUID = 1732652851033313018L;

	private String token;

	private Position position;

	private String message;

	public CalculatorException(String token, Position position, String message) {
		super();
		this.token = token;
		this.position = position;
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getToken() {
		return token;
	}

	public Position getPosition() {
		return position;
	}

	public String getMessage() {
		return message;
	}
}
