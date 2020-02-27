package asathinker.compilers.calculator;

import asathinker.compilers.calculator.Token.Position;

/**
 * 四则运算词法分析
 * 
 * @author asathinker
 *
 */
public class CalculatorLexer implements Lexer {

	private static enum State {
		Init, Plus, Minus, Star, Slash, LeftParen, RightParen, Number, Number_Dot,
		Float_Number
	}

	private String code;

	private Position position = new Position();

	public CalculatorLexer(String code) {
		/**
		 * 状态机解析完最后一个字符后就会跳出，但此时的token可能不为空，所以我们 需要在跳出状态机以后再次检查token到底是属于哪种Token类型，
		 * 这就会增加编码的复杂度，所以为了保证所有token都完全在状态机里面解析， 我们只要在表达式的最后面加上一个空白字符就好了。
		 */
		this.code = code + " ";
	}

	@Override
	public Token getNext() throws CalculatorException {
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
				} else if (isLeftParen(ch)) { // 判断是否为(
					state = State.LeftParen; // 状态迁移到LeftParen
				} else if (isRightParen(ch)) { // 判断是否为)
					state = State.RightParen; // 状态迁移到RightParen
				} else {
					// 当前的字符在Init状态下不是一个合法的字符
					throw new CalculatorException("" + ch, position.copy(), "不合法的字符");
				}
				// 把当前字符放入到token中
				token.append(ch);
				// 转换到下一个字符
				position.nextIndex();
				break;
			case Plus:
			case Minus:
			case Star:
			case Slash:
			case LeftParen:
			case RightParen:
				// 当前的token已经解析完成了，可以直接返回
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
					state = State.Float_Number; // 状态迁移到Number_Dot，
					// 这是的token类似于314.6
				} else {
					// 当前的字符在Number_Dot状态下不是一个合法的字符
					throw new CalculatorException("" + ch, position.copy(), "不合法的字符");
				}
				break;
			case Float_Number:
				if (isDigit(ch)) { // 如果是数值
					token.append(ch); // 把字符放入token
					position.nextIndex(); // 移动到下一个字符，尽可能多的匹配
				} else {
					// 如果是其他字符，说明我们当前的token已经解析完成了，可以直接返回
					return new Token(token.toString(), position.copy(),
							Token.Type.NumberLiteral);
				}
				break;
			}
		}
		return null;
	}

	@Override
	public Position getPosition() {
		/**
		 * 返回一份拷贝
		 */
		return position.copy();
	}

	@Override
	public void setPosition(Token.Position position) {
		/**
		 * 设置当前位置
		 */
		this.position = position;
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
		return ch == '(';
	}

	/**
	 * 判断是否为')'
	 * 
	 * @param ch
	 * @return 如果ch是')'返回 true，否则返回 false
	 */
	private boolean isRightParen(char ch) {
		return ch == ')';
	}
}
