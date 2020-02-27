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
		// 常量节点
		if (ast instanceof Constant) {
			// 常量运算，字符串转数
			return Double.valueOf(((Constant) ast).getValue().getText());
		} else if (ast instanceof BinaryOp) {
			// 二元运算节点
			BinaryOp binaryOp = (BinaryOp) ast;
			// 左操作数
			AstNode left = binaryOp.getLeft();
			// 右操作数
			AstNode right = binaryOp.getRight();
			if (binaryOp.getOperator().getType() == Token.Type.Plus) {
				// 加法
				return evaluate(left) + evaluate(right);
			} else if (binaryOp.getOperator().getType() == Token.Type.Minus) {
				// 减法
				return evaluate(left) - evaluate(right);
			} else if (binaryOp.getOperator().getType() == Token.Type.Star) {
				// 乘法
				return evaluate(left) * evaluate(right);
			} else {
				// 除法
				return evaluate(left) / evaluate(right);
			}
		} else {
			// 一元运算节点
			UnaryOp unaryOp = (UnaryOp) ast;
			// 操作数
			AstNode operand = unaryOp.getOperand();
			if (unaryOp.getOperator().getType() == Token.Type.Minus) {
				// 取负运算
				return -evaluate(operand);
			}
			return evaluate(operand);
		}
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
