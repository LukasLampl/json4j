package json4j.json;

import java.util.ArrayList;
import java.util.List;

public class JSONArray {
	private List<Object> array = new ArrayList<Object>();
	
	public void add(Object obj) {
		this.array.add(obj);
	}
	
	public void addAll(List<Object> objs) {
		this.array.addAll(objs);
	}
	
	public Object get(int index) {
		return this.array.get(index);
	}
	
	public List<Object> getArray() {
		return this.array;
	}
	
	public int length() {
		return this.array.size();
	}
}
