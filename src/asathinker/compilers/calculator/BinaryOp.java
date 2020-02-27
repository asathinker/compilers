package asathinker.compilers.calculator;

/**
 * 二元运算节点
 * 
 * @author asathinker
 *
 */
public class BinaryOp extends AstNode {
	private Token operator;
	private AstNode left;
	private AstNode right;

	public BinaryOp(Token operator, AstNode left, AstNode right) {
		super();
		this.operator = operator;
		this.left = left;
		this.right = right;
	}

	public Token getOperator() {
		return operator;
	}

	public AstNode getLeft() {
		return left;
	}

	public AstNode getRight() {
		return right;
	}

	@Override
	public String toString() {
		return new JsonBuilder().append("type", BinaryOp.class.getSimpleName())
				.append("left", left.toString()).append("right", right.toString())
				.append("operator", operator.getText()).toString();
	}
}
