package asathinker.compilers.calculator;

public interface Parser {
	/**
	 * 根据语法规则转换成抽象语法树
	 * 
	 * @param lexer
	 * @return 抽象语法树
	 */
	public AstNode parse(Lexer lexer) throws CalculatorException;
}
