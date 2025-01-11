package json4j.json;

import java.util.LinkedHashMap;
import java.util.Map;

public class JSONObject extends JSONEncoder {
	private Map<String, Object> map = new LinkedHashMap<String, Object>();
	
	@Override
	public String toString() {
		return super.toString(this.map);
	}
	
	public void setAttribute(String key, Object value) {
		this.map.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return this.map.get(key);
	}
	
	public Map<String, Object> getMap() {
		return this.map;
	}
}
