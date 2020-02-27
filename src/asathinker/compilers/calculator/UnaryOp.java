package asathinker.compilers.calculator;

/**
 * 一元运算节点
 * 
 * @author asathinker
 *
 */
public class UnaryOp extends AstNode {
	private Token operator;
	private AstNode operand;

	public UnaryOp(Token operator, AstNode operand) {
		super();
		this.operator = operator;
		this.operand = operand;
	}

	public Token getOperator() {
		return operator;
	}

	public AstNode getOperand() {
		return operand;
	}

	@Override
	public String toString() {
		return new JsonBuilder().append("type", UnaryOp.class.getSimpleName())
				.append("operand", operand.toString())
				.append("operator", operator.getText()).toString();
	}
}
