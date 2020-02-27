package asathinker.compilers.calculator;

/**
 * 常量节点
 * 
 * @author asathinker
 *
 */
public class Constant extends AstNode {
	private Token value;

	public Token getValue() {
		return value;
	}

	public Constant(Token value) {
		super();
		this.value = value;
	}

	@Override
	public String toString() {
		return new JsonBuilder().append("type", Constant.class.getSimpleName())
				.append("value", value.getText()).toString();
	}
}
