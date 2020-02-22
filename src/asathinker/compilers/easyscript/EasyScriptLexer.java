package asathinker.compilers.easyscript;

import asathinker.compilers.easyscript.Token.Position;

public class EasyScriptLexer implements Lexer {

	private static enum State {
		Init, Plus, Minus, Star, Slash, LeftParen, RightParen, Number, Number_Dot,
		Number_Float, Comma, LeftBrace, RightBrace, Semicolon, Identifier,
		Assignment, Gt, Lt, Ge, Le, Eq, Neq, Not, Ge_Le_Eq_Neq;
	}

	private String code;

	private Position position = new Position();

	public EasyScriptLexer(String code) {
		/**
		 * 状态机解析完最后一个字符后就会跳出，但此时的token可能不为空，所以我们 需要在跳出状态机以后再次检查token到底是属于哪种Token类型，
		 * 这就会增加编码的复杂度，所以为了保证所有token都完全在状态机里面解析， 我们只要在表达式的最后面加上一个空白字符就好了。
		 */
		this.code = code + " ";
	}

	@Override
	public Token getNext() throws EasyScriptException {
		// 过滤所有前导空白字符和换行符
		while (true) {
			// 判断是否已经解析完
			if (position.getIndex() >= code.length()) {
				break;
			} else if (isBlank(code.charAt(position.getIndex()))) {
				// 过滤空白字符
				position.nextIndex();
			} else if (isNewLine(code.charAt(position.getIndex()))) {
				// 过滤换行符
				position.newLine();
			} else {
				break;
			}
		}
		// 初始化状态设置为Init
		State state = State.Init;
		// 初始化当前token
		StringBuffer token = new StringBuffer();
		// 判断是否已经解析完
		while (position.getIndex() < code.length()) {
			// 获取当前index所在的字符
			char ch = code.charAt(position.getIndex());
			switch (state) {
			case Init:
				if (isPlus(ch)) { // 判断是否为+
					state = State.Plus; // 状态迁移到Plus
				} else if (isMinus(ch)) { // 判断是否为-
					state = State.Minus; // 状态迁移到Minus
				} else if (isStar(ch)) { // 判断是否为*
					state = State.Star; // 状态迁移到Star
				} else if (isSlash(ch)) { // 判断是否为/
					state = State.Slash; // 状态迁移到Slash
				} else if (isDigit(ch)) { // 判断是为数值
					state = State.Number; // 状态迁移到Number
				} else if (isLeftParen(ch)) { // 判断是否为(,（
					state = State.LeftParen; // 状态迁移到LeftParen
				} else if (isRightParen(ch)) { // 判断是否为),）
					state = State.RightParen; // 状态迁移到RightParen
				} else if (isComma(ch)) {// 判断是否为,，
					state = State.Comma; // 状态迁移到Comma
				} else if (isLeftBrace(ch)) {// 判断是否为{
					state = State.LeftBrace;// 状态迁移到LeftBrace
				} else if (isRightBrace(ch)) {// 判断是否为}
					state = State.RightBrace;// 状态迁移到RightBrace
				} else if (isSemicolon(ch)) {// 判断是否为; 。
					state = State.Semicolon;// 状态迁移到Semicolon
				} else if (isAlpha(ch)) {// 判断是否为合法字符
					state = State.Identifier;// 状态迁移到Identifier
				} else if (isAssignment(ch)) {// 判断是否为=
					state = State.Assignment;// 状态迁移到Assignment
				} else if (isGt(ch)) {// 判断是否为>
					state = State.Gt;// 状态迁移到Gt
				} else if (isLt(ch)) {// 判断是否为<
					state = State.Lt;// 状态迁移到Lt
				} else if (isNot(ch)) {// 判断是否为!
					state = State.Not;// 状态迁移到Not
				} else {
					// 当前的字符在Init状态下不是一个合法的字符
					throw new EasyScriptException("" + ch, position.copy(), "不合法的字符");
				}
				// 把当前字符放入到token中
				token.append(ch);
				// 转换到下一个字符
				position.nextIndex();
				break;
			case Identifier:
				// 尽可能多的匹配字符，下划线，数值
				if (isAlpha(ch) || isDigit(ch)) {
					token.append(ch);
					position.nextIndex();
				} else {
					// 如果当前标识符是关键字
					if (Token.TOKEN_TYPE_TABLE.containsKey(token.toString())) {
						return new Token(token.toString(), position.copy(),
								Token.TOKEN_TYPE_TABLE.get(token.toString()));
					} else {
						// 返回标识符
						return new Token(token.toString(), position.copy(),
								Token.Type.Identifier);
					}
				}
				break;
			case Gt:
			case Lt:
			case Not:
			case Assignment:
				if (isAssignment(ch)) {
					token.append(ch);
					position.nextIndex();
					state = State.Ge_Le_Eq_Neq; // 如果当前字符是=号，状态迁移到Ge_Le_Eq_Neq
				} else {
					// 如果为其他字符，直接返回当前token
					return new Token(token.toString(), position.copy(),
							Token.TOKEN_TYPE_TABLE.get(token.toString()));
				}
				break;
			case Ge_Le_Eq_Neq:
			case Ge:
			case Le:
			case Eq:
			case Neq:
			case Plus:
			case Minus:
			case Star:
			case Slash:
			case LeftParen:
			case RightParen:
			case Comma:
			case LeftBrace:
			case RightBrace:
			case Semicolon:
				// 返回当前token
				return new Token(token.toString(), position.copy(),
						Token.TOKEN_TYPE_TABLE.get(token.toString()));
			case Number:
				if (isDigit(ch)) { // 如果是数值
					token.append(ch); // 把字符放入token
					position.nextIndex(); // 移动到下一个字符，尽可能多的匹配
				} else if (isDot(ch)) { // 如果是.
					token.append(ch); // 把字符放入token
					position.nextIndex(); // 移动到下一个字符
					state = State.Number_Dot; // 状态迁移到Number_Dot，
					// 这时的token类似于314.
				} else {
					// 如果是其他字符，说明我们当前的token已经解析完成了，可以直接返回
					return new Token(token.toString(), position.copy(),
							Token.Type.NumberLiteral);
				}
				break;
			case Number_Dot:
				if (isDigit(ch)) {
					token.append(ch); // 把字符放入token
					position.nextIndex(); // 移动到下一个字符
					state = State.Number_Float; // 状态迁移到Number_Dot，
					// 这是的token类似于314.6
				} else {
					// 当前的字符在Number_Dot状态下不是一个合法的字符
					throw new EasyScriptException("" + ch, position.copy(), "不合法的字符");
				}
				break;
			case Number_Float:
				if (isDigit(ch)) { // 如果是数值
					token.append(ch); // 把字符放入token
					position.nextIndex(); // 移动到下一个字符，尽可能多的匹配
				} else {
					// 如果是其他字符，说明我们当前的token已经解析完成了，可以直接返回
					return new Token(token.toString(), position.copy(),
							Token.Type.NumberLiteral);
				}
				break;
			default:
				break;
			}
		}
		return null;
	}

	@Override
	public Position getPosition() {
		return position.copy();
	}

	@Override
	public void setPosition(Token.Position position) {
		this.position = position;
	}

	/**
	 * 判断是否为合法字符
	 * 
	 * @param ch
	 * @return 如果是合法字符返回true，否则返回false
	 */
	private boolean isAlpha(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_')
				|| (ch >= '\u4E00' && ch <= '\u9FA5');
	}

	/**
	 * 判断是否为空白字符
	 * 
	 * @param ch
	 * @return 如果ch是空白字符返回 true，否则返回 false
	 */
	private boolean isBlank(char ch) {
		return ch == ' ' || ch == '\t';
	}

	/**
	 * 判断是否为换行符
	 * 
	 * @param ch
	 * @return 如果ch是换行符返回 true，否则返回 false
	 */
	private boolean isNewLine(char ch) {
		return ch == '\n';
	}

	/**
	 * 判断是否为数值字符
	 * 
	 * @param ch
	 * @return 如果ch是数值字符返回 true，否则返回 false
	 */
	private boolean isDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * 判断是否为'.'
	 * 
	 * @param ch
	 * @return 如果ch是'.'返回 true，否则返回 false
	 */
	private boolean isDot(char ch) {
		return ch == '.';
	}

	/**
	 * 判断是否为'-'
	 * 
	 * @param ch
	 * @return 如果ch是'-'返回 true，否则返回 false
	 */
	private boolean isMinus(char ch) {
		return ch == '-';
	}

	/**
	 * 判断是否为'+'
	 * 
	 * @param ch
	 * @return 如果ch是'+'返回 true，否则返回 false
	 */
	private boolean isPlus(char ch) {
		return ch == '+';
	}

	/**
	 * 判断是否为'*'
	 * 
	 * @param ch
	 * @return 如果ch是'*'返回 true，否则返回 false
	 */
	private boolean isStar(char ch) {
		return ch == '*';
	}

	/**
	 * 判断是否为'/'
	 * 
	 * @param ch
	 * @return 如果ch是'/'返回 true，否则返回 false
	 */
	private boolean isSlash(char ch) {
		return ch == '/';
	}

	/**
	 * 判断是否为'('
	 * 
	 * @param ch
	 * @return 如果ch是'('返回 true，否则返回 false
	 */
	private boolean isLeftParen(char ch) {
		return ch == '(' || ch == '（';
	}

	/**
	 * 判断是否为')'
	 * 
	 * @param ch
	 * @return 如果ch是')'返回 true，否则返回 false
	 */
	private boolean isRightParen(char ch) {
		return ch == ')' || ch == '）';
	}

	/**
	 * 判断是否为','
	 * 
	 * @param ch
	 * @return 如果ch是','返回 true，否则返回 false
	 */
	private boolean isComma(char ch) {
		return ch == ',' || ch == '，';
	}

	/**
	 * 判断是否为';'
	 * 
	 * @param ch
	 * @return 如果ch是';'返回 true，否则返回 false
	 */
	private boolean isSemicolon(char ch) {
		return ch == ';' || ch == '。';
	}

	/**
	 * 判断是否为'{'
	 * 
	 * @param ch
	 * @return 如果ch是'{'返回 true，否则返回 false
	 */
	private boolean isLeftBrace(char ch) {
		return ch == '{';
	}

	/**
	 * 判断是否为'}'
	 * 
	 * @param ch
	 * @return 如果ch是'{'返回 true，否则返回 false
	 */
	private boolean isRightBrace(char ch) {
		return ch == '}';
	}

	/**
	 * 判断是否为'='
	 * 
	 * @param ch
	 * @return 如果ch是'='返回 true，否则返回 false
	 */
	private boolean isAssignment(char ch) {
		return ch == '=';
	}

	/**
	 * 判断是否为'>'
	 * 
	 * @param ch
	 * @return 如果ch是'>'返回 true，否则返回 false
	 */
	private boolean isGt(char ch) {
		return ch == '>';
	}

	/**
	 * 判断是否为'<'
	 * 
	 * @param ch
	 * @return 如果ch是'<'返回 true，否则返回 false
	 */
	private boolean isLt(char ch) {
		return ch == '<';
	}

	/**
	 * 判断是否为'!'
	 * 
	 * @param ch
	 * @return 如果ch是'!'返回 true，否则返回 false
	 */
	private boolean isNot(char ch) {
		return ch == '!';
	}
}
