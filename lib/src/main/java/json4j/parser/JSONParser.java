package json4j.parser;

import json4j.json.JSONArray;
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

		return (JSONObject)parseObject(str, index).getObject();
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

			Report keyReport = parseString(str, index);//, true);
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
			return parseString(str, index);//, false);
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

		JSONArray array = new JSONArray();
		index = skipWhitespace(str, index + 1);

		while (index < str.length()) {
			if (str.charAt(index) == ']') {
				return new Report(index + 1, array);
			}

			Report valueReport = getValue(str, index);
			array.add(valueReport.getObject());
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
			char current = str.charAt(index++);
			if (current == '\\') {
				if (index >= str.length()) {
					throw new IllegalArgumentException("Unterminated escape sequence");
				}
				char escaped = str.charAt(index++);
				switch (escaped) {
				case '"':
					value.append('"');
					break;
				case '\\':
					value.append('\\');
					break;
				case '/':
					value.append('/');
					break;
				case 'b':
					value.append('\b');
					break;
				case 'f':
					value.append('\f');
					break;
				case 'n':
					value.append('\n');
					break;
				case 'r':
					value.append('\r');
					break;
				case 't':
					value.append('\t');
					break;
				case 'u':
					if (index + 4 > str.length()) {
						throw new IllegalArgumentException("Invalid Unicode escape sequence");
					}
					String hex = str.substring(index, index + 4);
					value.append((char) Integer.parseInt(hex, 16));
					index += 4;
					break;
				default:
					throw new IllegalArgumentException("Invalid escape character: " + escaped);
				}
			} else if (current == '"') {
				break;
			} else {
				value.append(current);
			}
		}

		return new Report(index, (String)value.toString());
	}

}
