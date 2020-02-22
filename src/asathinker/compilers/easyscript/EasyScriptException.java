package asathinker.compilers.easyscript;

import asathinker.compilers.easyscript.Token.Position;

public class EasyScriptException extends Exception {

	private static final long serialVersionUID = 1732652851033313018L;

	private String token;

	private Position position;

	private String message;

	public EasyScriptException(String token, Position position, String message) {
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
