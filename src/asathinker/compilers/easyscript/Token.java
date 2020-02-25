package asathinker.compilers.easyscript;

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
		TOKEN_TYPE_TABLE.put("=", Type.Assignment);
		TOKEN_TYPE_TABLE.put("+", Type.Plus);
		TOKEN_TYPE_TABLE.put("-", Type.Minus);
		TOKEN_TYPE_TABLE.put("*", Type.Star);
		TOKEN_TYPE_TABLE.put("/", Type.Slash);
		TOKEN_TYPE_TABLE.put(">", Type.Gt);
		TOKEN_TYPE_TABLE.put("<", Type.Lt);
		TOKEN_TYPE_TABLE.put(">=", Type.Ge);
		TOKEN_TYPE_TABLE.put("<=", Type.Le);
		TOKEN_TYPE_TABLE.put("==", Type.Eq);
		TOKEN_TYPE_TABLE.put("!=", Type.Neq);
		TOKEN_TYPE_TABLE.put("(", Type.LeftParen);
		TOKEN_TYPE_TABLE.put(")", Type.RightParen);
		TOKEN_TYPE_TABLE.put("{", Type.LeftBrace);
		TOKEN_TYPE_TABLE.put("}", Type.RightBrace);
		TOKEN_TYPE_TABLE.put(",", Type.Comma);
		TOKEN_TYPE_TABLE.put(";", Type.Semicolon);
		TOKEN_TYPE_TABLE.put("if", Type.If);
		TOKEN_TYPE_TABLE.put("else", Type.Else);
		TOKEN_TYPE_TABLE.put("while", Type.While);
		TOKEN_TYPE_TABLE.put("return", Type.Return);
		TOKEN_TYPE_TABLE.put("break", Type.Break);
		TOKEN_TYPE_TABLE.put("function", Type.Function);
		TOKEN_TYPE_TABLE.put("void", Type.Void);
		TOKEN_TYPE_TABLE.put("number", Type.Number);
		TOKEN_TYPE_TABLE.put("bool", Type.Bool);
		TOKEN_TYPE_TABLE.put("false", Type.False);
		TOKEN_TYPE_TABLE.put("true", Type.True);
		// 支持中文编程
		TOKEN_TYPE_TABLE.put("赋值为", Type.Assignment);
		TOKEN_TYPE_TABLE.put("加", Type.Plus);
		TOKEN_TYPE_TABLE.put("减", Type.Minus);
		TOKEN_TYPE_TABLE.put("乘", Type.Star);
		TOKEN_TYPE_TABLE.put("除", Type.Slash);
		TOKEN_TYPE_TABLE.put("大于", Type.Gt);
		TOKEN_TYPE_TABLE.put("小于", Type.Lt);
		TOKEN_TYPE_TABLE.put("大于等于", Type.Ge);
		TOKEN_TYPE_TABLE.put("大于等于", Type.Le);
		TOKEN_TYPE_TABLE.put("等于", Type.Eq);
		TOKEN_TYPE_TABLE.put("不等于", Type.Neq);
		TOKEN_TYPE_TABLE.put("（", Type.LeftParen);
		TOKEN_TYPE_TABLE.put("）", Type.RightParen);
		TOKEN_TYPE_TABLE.put("开始", Type.LeftBrace);
		TOKEN_TYPE_TABLE.put("结束", Type.RightBrace);
		TOKEN_TYPE_TABLE.put("，", Type.Comma);
		TOKEN_TYPE_TABLE.put("。", Type.Semicolon);
		TOKEN_TYPE_TABLE.put("如果", Type.If);
		TOKEN_TYPE_TABLE.put("那么", Type.Else);
		TOKEN_TYPE_TABLE.put("当", Type.While);
		TOKEN_TYPE_TABLE.put("返回", Type.Return);
		TOKEN_TYPE_TABLE.put("间断", Type.Break);
		TOKEN_TYPE_TABLE.put("函数", Type.Function);
		TOKEN_TYPE_TABLE.put("空", Type.Void);
		TOKEN_TYPE_TABLE.put("数值", Type.Number);
		TOKEN_TYPE_TABLE.put("布尔", Type.Bool);
		TOKEN_TYPE_TABLE.put("假", Type.False);
		TOKEN_TYPE_TABLE.put("真", Type.True);
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
		NumberLiteral("NumberLiteral"), Plus("Plus"), Minus("Minus"), Star("Star"),
		Slash("Slash"), LeftParen("LeftParen"), RightParen("RightParen"),
		LeftBrace("LeftBrace"), RightBrace("RightBrace"), Comma("Comma"),
		Semicolon("Semicolon"), Assignment("Assignment"), Bool("Bool"),
		True("True"), False("False"), Gt("Gt"), Lt("Lt"), Ge("Ge"), Le("Le"),
		Eq("Eq"), Neq("Neq"),
		// 标识符
		Identifier("Identifier"),
		// 关键字
		If("If"), Else("Else"), Return("Return"), Break("Break"),
		Function("Function"), While("While"), Number("Number"), Void("Void");

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
		 * 获取当前token所在的起始列
		 * 
		 * @return 起始列
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
