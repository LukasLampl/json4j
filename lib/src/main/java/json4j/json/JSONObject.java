package json4j.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JSONObject {
	private Map<String, Object> map = new LinkedHashMap<String, Object>();
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		int counter = 0;
		
		for (Entry<String, Object> entry : this.map.entrySet()) {
			Object value = entry.getValue();
			builder.append("\t\"");
			builder.append(entry.getKey().toString());
			builder.append("\" : ");
			
			if (value instanceof JSONObject) {
				builder.append(getIdentString(((JSONObject)value).toString()));
				
				if ((++counter) < this.map.size()) {
					builder.append(",\n");
				}
				continue;
			} else if (value instanceof JSONObject[]) {
				builder.append(handleJSONObjectArray((JSONObject[])value));
				
				if ((++counter) < this.map.size()) {
					builder.append(",\n");
				}
				continue;
			}
			
			final String valueStr;
			
			if (value == null) {
				valueStr = "null";
			} else if (value instanceof Integer) {
				valueStr = String.valueOf(Integer.parseInt(entry.getValue().toString()));
			} else if (value instanceof Double) {
				valueStr = getFloatingpointValue(((Double)value));
			} else {
				valueStr = "\"" + value.toString() + "\"";
			}
			
			builder.append(valueStr);
			
			if ((++counter) < this.map.size()) {
				builder.append(",\n");
			}
		}
		
		builder.append("\n}");
		return builder.toString();
	}
	
	private String getFloatingpointValue(double value) {
		if (Double.isInfinite(value)) {
			return "\"Infinity\"";
		} else if (Double.isNaN(value)) {
			return "\"NaN\"";
		}
		
		return String.valueOf(value);
	}
	
	private String handleJSONObjectArray(JSONObject[] jsonObjs) {
		StringBuilder builder = new StringBuilder();
		builder.append("[\n");
		int counter = 0;
		
		for (JSONObject obj : jsonObjs) {
			if (obj == null) {
				continue;
			}
			
			builder.append(getIdentString(obj.toString()));
			
			if ((++counter) < jsonObjs.length) {
				builder.append(",\n");
			}
		}
		
		builder.append("]");
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
	
	public void setAttribute(String key, Object value) {
		this.map.put(key, value);
	}
}
