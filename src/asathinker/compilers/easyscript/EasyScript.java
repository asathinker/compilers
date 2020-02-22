package asathinker.compilers.easyscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EasyScript {
	public double evaluate(AstNode ast) {
		return 0;
	}

	public static void main(String[] args) {
		String code = EasyScript.readFileContent(
				EasyScript.class.getResource("script.es").getPath().toString());
		EasyScriptLexer lexer = new EasyScriptLexer(code);
		try {
			for (Token token = lexer.getNext(); token != null; token = lexer
					.getNext()) {
				System.out.println(String.format("%-15s %s", token.getType().getName(),
						token.getText()));
			}
		} catch (EasyScriptException exception) {
			System.err.println("字符:" + exception.getToken() + " 位置:"
					+ exception.getPosition().getIndex() + " 错误原因:"
					+ exception.getMessage());
		}
	}

	static String readFileContent(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			while ((tempStr = reader.readLine()) != null) {
				sbf.append(tempStr).append("\n");
			}
			reader.close();
			return sbf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return sbf.toString();
	}
}
