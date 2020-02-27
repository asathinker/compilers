package asathinker.compilers.calculator;

import java.util.HashMap;
import java.util.Map;

public class JsonBuilder {

	private Map<String, Object> keyValues = new HashMap<String, Object>();

	public JsonBuilder append(String key, Object value) {
		if (value != null) {
			keyValues.put(key, value);
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		boolean isFirstEntry = true;
		for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
			if (!isFirstEntry) {
				buffer.append(",");
			}
			isFirstEntry = false;
			buffer.append("\"").append(entry.getKey()).append("\"");
			buffer.append(":");
			String valueString = entry.getValue().toString();
			// 数组或者对象
			if (valueString.startsWith("{") || valueString.startsWith("[")) {
				buffer.append(entry.getValue().toString());
			} else {
				buffer.append("\"").append(entry.getValue().toString()).append("\"");
			}
		}
		buffer.append("}");
		return buffer.toString();
	}
}
