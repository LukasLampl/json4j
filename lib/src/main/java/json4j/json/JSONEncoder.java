package json4j.json;

import java.util.Map;
import java.util.Map.Entry;

public abstract class JSONEncoder {
	public String toString(Map<String, Object> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		int counter = 0;
		
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			builder.append("\t\"");
			builder.append(entry.getKey().toString());
			builder.append("\" : ");
			builder.append(getValueString(value));
			
			if (value instanceof JSONObject
				|| value instanceof JSONArray) {
				if ((++counter) < map.size()) {
					builder.append(",\n");
				}
				continue;
			}
			
			if ((++counter) < map.size()) {
				builder.append(",\n");
			}
		}
		
		builder.append("\n}");
		return builder.toString();
	}
	
	private String getValueString(Object value) {
		final String valueStr;
		
		if (value == null) {
			valueStr = "null";
		} else if (value instanceof Integer) {
			valueStr = String.valueOf(Integer.parseInt(value.toString()));
		} else if (value instanceof Double) {
			valueStr = getFloatingpointValue(((Double)value));
		} else if (value instanceof JSONObject) {
			valueStr = getIdentString(((JSONObject)value).toString());
		} else if (value instanceof JSONArray) {
			valueStr = handleJSONObjectArray((JSONArray)value);
		} else {
			valueStr = "\"" + value.toString() + "\"";
		}
		
		return valueStr;
	}
	
	private String getFloatingpointValue(double value) {
		if (Double.isInfinite(value)) {
			return "\"Infinity\"";
		} else if (Double.isNaN(value)) {
			return "\"NaN\"";
		}
		
		return String.valueOf(value);
	}
	
	private String handleJSONObjectArray(JSONArray jsonObjs) {
		StringBuilder builder = new StringBuilder();
		StringBuilder content = new StringBuilder();
		builder.append("[\n");
		int counter = 0;
		
		for (Object obj : jsonObjs.getArray()) {
			if (obj == null) {
				continue;
			}
			
			content.append(getIdentString(getValueString(obj)));
			
			if ((++counter) < jsonObjs.length()) {
				content.append(",\n");
			}
		}
		
		builder.append(getIdentString(content.toString()));
		builder.append("\n]");
		return builder.toString();
	}
	
	private String getIdentString(String str) {
		String[] lines = str.split("\n");
		StringBuilder builder = new StringBuilder();
		int counter = 0;
		
		for (String line : lines) {
			builder.append("\t");
			builder.append(line);
			
			if ((++counter) < lines.length) {
				builder.append("\n");
			}
		}
		
		return builder.toString();
	}
}
