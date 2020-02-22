package asathinker.compilers.calculator;

/**
 * 抽象语法树节点的基础类，其他所有节点都继承自该类
 * 
 * @author asathinker
 *
 */
public abstract class AstNode {
	private static long UniqueIdCounter = 0;

	private String uniqueId;

	public AstNode() {
		uniqueId = "ast_node_" + (UniqueIdCounter++);
	}

	/**
	 * 获取抽象语法树节点的唯一标识
	 * 
	 * @return unqiueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}
}
