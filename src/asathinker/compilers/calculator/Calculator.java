package asathinker.compilers.calculator;

public class Calculator {
	public double evaluate(AstNode ast) {
		return 0;
	}

	public static void main(String[] args) {
		String code = "-3.14 + 3.14 * 3.14 + (3.14 + 3.14) / 3.14 - 3.14";
		CalculatorLexer lexer = new CalculatorLexer(code);
		try {
			for (Token token = lexer.getNext(); token != null; token = lexer
					.getNext()) {
				System.out.println(String.format("%-15s %s",
						token.getType().getName(), token.getText()));
			}
		} catch (CalculatorException exception) {
			System.err.println("字符:" + exception.getToken() + " 位置:"
					+ exception.getPosition().getIndex() + " 错误原因:"
					+ exception.getMessage());
		}
	}
}
