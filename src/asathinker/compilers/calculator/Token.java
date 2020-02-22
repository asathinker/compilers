package asathinker.compilers.calculator;

import java.util.HashMap;
import java.util.Map;

/**
 * 词法分析的标识
 * 
 * @author asathinker
 *
 */
public class Token {
	private String text;

	private Position position;

	private Type type;

	public static final Map<String, Type> TOKEN_TYPE_TABLE;

	static {
		TOKEN_TYPE_TABLE = new HashMap<String, Type>();
		TOKEN_TYPE_TABLE.put("+", Type.Plus);
		TOKEN_TYPE_TABLE.put("-", Type.Minus);
		TOKEN_TYPE_TABLE.put("*", Type.Star);
		TOKEN_TYPE_TABLE.put("/", Type.Slash);
		TOKEN_TYPE_TABLE.put("(", Type.LeftParen);
		TOKEN_TYPE_TABLE.put(")", Type.RightParen);
	}

	public Token(String text, Position position, Type type) {
		super();
		this.text = text;
		this.position = position;
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public Position getPosition() {
		return position;
	}

	public Type getType() {
		return type;
	}

	public static enum Type {
		// 数值
		NumberLiteral("NumberLiteral"),
		// +
		Plus("Plus"),
		// -
		Minus("Minus"),
		// *
		Star("Star"),
		// /
		Slash("Slash"),
		// (
		LeftParen("LeftParen"),
		// )
		RightParen("RightParen");

		private String name;

		private Type(String name) {
			this.name = name;
		}

		/**
		 * 获取类型的名称
		 * 
		 * @return 类型的名称
		 */
		public String getName() {
			return name;
		}
	}

	public static class Position {
		private int index = 0;
		private int row = 0;
		private int col = 0;

		/**
		 * 拷贝position对象
		 * 
		 * @return position 副本
		 */
		public Position copy() {
			Position copyObject = new Position();
			copyObject.index = index;
			copyObject.row = row;
			copyObject.col = col;
			return copyObject;
		}

		/**
		 * 获取当前token所在的文本的起始下标
		 * 
		 * @return 文本下标
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * 获取当前token所在的行
		 * 
		 * @return 行
		 */
		public int getRow() {
			return row;
		}

		/**
		 * 获取当前token所在的终止列
		 * 
		 * @return 终止列
		 */
		public int getCol() {
			return col;
		}

		/**
		 * 下一列
		 */
		public void nextIndex() {
			col++;
			index++;
		}

		/**
		 * 下一行
		 */
		public void newLine() {
			row++;
			col = 0;
			index++;
		}
	}
}
