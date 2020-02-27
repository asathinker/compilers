package asathinker.compilers.calculator;

import java.util.Arrays;
import java.util.List;

/**
 * 四则运算语法分析
 * 
 * @author asathinker
 *
 */
public class CalculatorParser implements Parser {

	@Override
	public AstNode parse(Lexer lexer) throws CalculatorException {
		return expr(lexer);
	}

	/**
	 * 规则如下： expr = add
	 * 
	 * @param lexer
	 * @return
	 * @throws CalculatorException
	 */
	private AstNode expr(Lexer lexer) throws CalculatorException {
		return add(lexer);
	}

	/**
	 * 规则如下： add = mul (Plus mul)* | mul (Minus mul)*
	 * 
	 * @param lexer
	 * @return
	 * @throws CalculatorException
	 */
	private AstNode add(Lexer lexer) throws CalculatorException {
		return binaryOp(lexer, Arrays.asList(Token.Type.Plus, Token.Type.Minus),
				(Lexer l) -> {
					return mul(l);
				});
	}

	/**
	 * 规则如下： mul = primary (Star primary)* | primary (Slash primary)*
	 * 
	 * @param lexer
	 * @return
	 * @throws CalculatorException
	 */
	private AstNode mul(Lexer lexer) throws CalculatorException {
		return binaryOp(lexer, Arrays.asList(Token.Type.Star, Token.Type.Slash),
				(Lexer l) -> {
					return primary(l);
				});
	}

	private static interface BuildOperand {
		/**
		 * 获取下一个操作数
		 * 
		 * @param lexer
		 * @return
		 * @throws CalculatorException
		 */
		public AstNode apply(Lexer lexer) throws CalculatorException;
	}

	/**
	 * 二元运算符，左结合
	 * 
	 * 规则如下： binaryOp = left (operator right)*
	 * 
	 * @param lexer
	 * @param acceptTypes
	 * @param buildOperand
	 * @return AstNode
	 * @throws CalculatorException
	 */
	private AstNode binaryOp(Lexer lexer, List<Token.Type> acceptTypes,
			BuildOperand buildOperand) throws CalculatorException {
		// 获取左操作数
		AstNode left = buildOperand.apply(lexer);
		if (left != null) {
			while (true) {
				// 获取当前位置，用于回溯
				Token.Position pos = lexer.getPosition();
				// 获取操作符
				Token token = lexer.getNext();
				// 判断当前标识符是否是可接受的类型
				if (token == null || !acceptTypes.contains(token.getType())) {
					// 回溯，进行下一阶段匹配
					lexer.setPosition(pos);
					break;
				}
				// 获取右操作数
				AstNode right = buildOperand.apply(lexer);
				// 当右操作数不存在时，当前二元运算匹配失败
				if (right == null) {
					throw new CalculatorException(null, lexer.getPosition(), "缺少右操作数");
				}
				// 构建二元运算
				left = new BinaryOp(token, left, right);
			}
		}
		return left;
	}

	/**
	 * 规则如下： primary = NumberLiteral | LeftParen add RightParen | (Plus | Minus) primary
	 * 
	 * @param lexer
	 * @return
	 * @throws CalculatorException
	 */
	private AstNode primary(Lexer lexer) throws CalculatorException {
		// 获取下一个token
		Token token = lexer.getNext();
		if (token != null) {
			if (token.getType() == Token.Type.NumberLiteral) {
				// 匹配数值常量
				return new Constant(token);
			} else if (token.getType() == Token.Type.LeftParen) {
				// 匹配括号表达式
				AstNode addNode = add(lexer);
				if (addNode != null) {
					token = lexer.getNext();
					// 只有匹配了右括号才是匹配成功
					if (token != null && token.getType() == Token.Type.RightParen) {
						return addNode;
					} else {
						// 右括号匹配失败
						throw new CalculatorException(token.getText(), lexer.getPosition(),
								"缺少右括号");
					}
				} else {
					// 括号中的表达式匹配失败
					throw new CalculatorException(token.getText(), lexer.getPosition(),
							"括号中缺少表达式");
				}
			} else if (token.getType() == Token.Type.Plus
					|| token.getType() == Token.Type.Minus) {
				// 匹配符号运算
				AstNode operand = primary(lexer);
				if (operand != null) {
					// 构建一元运算
					return new UnaryOp(token, operand);
				} else {
					// 当操作数不存在时，当前一元运算匹配失败
					throw new CalculatorException(token.getText(), lexer.getPosition(),
							"缺少操作数");
				}
			} else {
				// 当前token不能构成合法结构
				throw new CalculatorException(token.getText(), lexer.getPosition(),
						"无法构成合法的语句");
			}
		}
		return null;
	}
}
