package json4j.parser;

import java.util.ArrayList;
import java.util.List;

import json4j.json.JSONObject;

public class JSONParser {
	class Report {
		private int stopIndex;
		private Object obj;

		public Report(int stopIndex, Object obj) {
			this.stopIndex = stopIndex;
			this.obj = obj;
		}

		public int getLastStop() {
			return this.stopIndex;
		}

		public Object getObject() {
			return this.obj;
		}
	}

	public JSONObject parse(String str) {
		if (str == null || str.isEmpty()) {
			throw new IllegalArgumentException("Input string cannot be null or empty");
		}

		int index = skipWhitespace(str, 0);
		if (str.charAt(index) != '{' || str.charAt(str.length() - 1) != '}') {
			throw new IllegalArgumentException("Invalid JSON object format");
		}

		return (JSONObject) parseObject(str, index).getObject();
	}

	private int skipWhitespace(String str, int index) {
		while (index < str.length() && Character.isWhitespace(str.charAt(index))) {
			index++;
		}
		return index;
	}

	private Report parseObject(String str, int index) {
		JSONObject obj = new JSONObject();
		int len = str.length();

		index = skipWhitespace(str, index + 1);

		while (index < len) {
			if (str.charAt(index) == '}') {
				return new Report(index + 1, obj);
			}

			if (str.charAt(index) != '"') {
				throw new IllegalArgumentException("Expected string key at index " + index);
			}

			Report keyReport = parseString(str, index);
			String key = (String) keyReport.getObject();
			index = skipWhitespace(str, keyReport.getLastStop());

			if (str.charAt(index++) != ':') {
				throw new IllegalArgumentException("Expected ':' after key at index " + index);
			}

			index = skipWhitespace(str, index);
			Report valueReport = getValue(str, index);
			index = skipWhitespace(str, valueReport.getLastStop());
			obj.setAttribute(key, valueReport.getObject());

			if (str.charAt(index) == '}') {
				return new Report(index + 1, obj);
			} else if (str.charAt(index) != ',') {
				throw new IllegalArgumentException("Expected ',' or '}' at index " + index);
			}
			index = skipWhitespace(str, index + 1);
		}

		throw new IllegalArgumentException("Unterminated JSON object");
	}

	private Report getValue(String str, int index) {
		index = skipWhitespace(str, index);

		if (str.charAt(index) == '{') {
			return parseObject(str, index);
		} else if (str.charAt(index) == '[') {
			return parseArray(str, index);
		} else if (str.charAt(index) == '"') {
			return parseString(str, index);
		} else if (str.startsWith("true", index)) {
			return new Report(index + 4, Boolean.TRUE);
		} else if (str.startsWith("false", index)) {
			return new Report(index + 5, Boolean.FALSE);
		} else if (str.startsWith("null", index)) {
			return new Report(index + 4, null);
		} else if (Character.isDigit(str.charAt(index)) || str.charAt(index) == '-') {
			return parseNumber(str, index);
		}

		throw new IllegalArgumentException("Unexpected character '" + str.charAt(index) + "' at index " + index);
	}

	private Report parseArray(String str, int index) {
		if (str.charAt(index) != '[') {
			throw new IllegalStateException("Expected '[' but got '" + str.charAt(index) + "' at index " + index);
		}

		List<Object> values = new ArrayList<>();
		index = skipWhitespace(str, index + 1);

		while (index < str.length()) {
			if (str.charAt(index) == ']') {
				return new Report(index + 1, convertToAccordingArray(values));
			}

			Report valueReport = getValue(str, index);
			values.add(valueReport.getObject());
			index = skipWhitespace(str, valueReport.getLastStop());

			if (str.charAt(index) == ',') {
				index = skipWhitespace(str, index + 1);
			} else if (str.charAt(index) != ']') {
				throw new IllegalArgumentException(
						"Expected ',' or ']' but got '" + str.charAt(index) + "' at index " + index);
			}
		}

		throw new IllegalArgumentException("Unterminated array starting at index " + index);
	}

	private Report parseNumber(String str, int index) {
		int start = index;
		boolean isDouble = false;

		while (index < str.length()
				&& (Character.isDigit(str.charAt(index)) || "-+.eE".indexOf(str.charAt(index)) != -1)) {
			if (str.charAt(index) == '.' || str.charAt(index) == 'e' || str.charAt(index) == 'E') {
				isDouble = true;
			}
			index++;
		}

		String numberStr = str.substring(start, index);
		Object number = isDouble ? Double.parseDouble(numberStr) : Integer.parseInt(numberStr);

		return new Report(index, number);
	}

	private Report parseString(String str, int index) {
		if (str.charAt(index) != '"') {
			throw new IllegalStateException("Expected '\"' but got '" + str.charAt(index) + "' at index " + index);
		}

		StringBuilder value = new StringBuilder();
		index++;

		while (index < str.length()) {
			char current = str.charAt(index);
			if (current == '"' && (index == 0 || str.charAt(index - 1) != '\\')) {
				break;
			}
			if (current == '\\' && index + 1 < str.length()) {
				char next = str.charAt(index + 1);
				switch (next) {
				case 'n':
					value.append('\n');
					break;
				case 't':
					value.append('\t');
					break;
				case 'r':
					value.append('\r');
					break;
				case '\\':
					value.append('\\');
					break;
				case '"':
					value.append('"');
					break;
				default:
					value.append(next);
				}
				index++; // Skip the escaped character
			} else {
				value.append(current);
			}
			index++;
		}

		if (index >= str.length() || str.charAt(index) != '"') {
			throw new IllegalArgumentException("Unterminated string starting at index " + index);
		}

		return new Report(index + 1, value.toString());
	}
	
	private Object convertToAccordingArray(List<Object> objs) {
		Object[] array;
		
		if (objs.getFirst() instanceof JSONObject) {
			array = new JSONObject[objs.size()];
		} else {
			array = new Object[objs.size()];
		}
		
		int index = 0;

		for (Object obj : objs) {
			array[index++] = obj;
		}
		
		return array;
	}
}
