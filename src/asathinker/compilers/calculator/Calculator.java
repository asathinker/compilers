package asathinker.compilers.calculator;

/**
 * 四则运算计算器
 * 
 * @author asathinker
 *
 */
public class Calculator {
	/**
	 * 递归求解四则运算语法树
	 * 
	 * @param ast
	 * @return 四则运算结果
	 */
	public static double evaluate(AstNode ast) {
		return 0;
	}

	public static void main(String[] args) {
		String code = "-3.14 + 3.14 * 3.14 + (3.14 + 3.14) / 3.14 - 3.14";
		try {
			// 词法分析
			CalculatorLexer lexer = new CalculatorLexer(code);
			System.out.println("词法标记：");
			for (Token token = lexer.getNext(); token != null; token = lexer
					.getNext()) {
				System.out.println(String.format("%-15s %s", token.getType().getName(),
						token.getText()));
			}
			// 生成语法树
			CalculatorParser parser = new CalculatorParser();
			AstNode ast = parser.parse(new CalculatorLexer(code));
			System.out.println("语法树：");
			System.out.println(ast);
			// 计算表达式结果
			System.out.println("运算结果：");
			System.out.println(Calculator.evaluate(ast));
		} catch (CalculatorException exception) {
			System.err.println("字符:" + exception.getToken() + " 位置:"
					+ exception.getPosition().getIndex() + " 错误原因:"
					+ exception.getMessage());
		}
	}
}
