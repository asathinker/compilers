package asathinker.compilers.easyscript;

public interface Lexer {
	/**
	 * 获取下一个token
	 * 
	 * @return token
	 */
	public Token getNext() throws EasyScriptException;

	/**
	 * 获取当前所在的位置
	 * 
	 * @return 位置
	 */
	public Token.Position getPosition();

	/**
	 * 设置位置
	 */
	public void setPosition(Token.Position position);
}
