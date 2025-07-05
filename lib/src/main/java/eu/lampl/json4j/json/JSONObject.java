/////////////////////////////////////////////////////////////
///////////////////////    LICENSE    ///////////////////////
/////////////////////////////////////////////////////////////
/*
The json4j library for basic conversion of Objects to JSON and back.

Copyright (C) 2025  Lukas Nian En Lampl

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package eu.lampl.json4j.json;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A representative for all {@code JSONObjects}. The class contains basic functionalities
 * for creating and translating Java based JSONObjects to string representation.
 * 
 * @author Lukas Lampl
 * @since 1.0.0
 */
public class JSONObject extends JSONEncoder {
	/**
	 * Holds all key-value-pairs of the JSONObject.
	 */
	private Map<String, Object> map = new LinkedHashMap<String, Object>();
	
	@Override
	public String toString() {
		return super.toString(this.map);
	}
	
	/**
	 * Sets an attribute to the {@code JSONObject}.
	 * 
	 * <p>
	 * If a key-value-pair already exists, it'll be overwritten.
	 * 
	 * @param key		Key to the attribute.
	 * @param value		Value of the attribute.
	 */
	public void setAttribute(final String key, final Object value) {
		this.map.put(key, value);
	}
	
	/**
	 * Gets the mapped attribute value of the given key.
	 * 
	 * <p>
	 * If there's no key-value-pair the functions returns {@code null}.
	 * 
	 * @param key	Key to the attribute.
	 * @return The mapped value to the key.
	 */
	public Object getAttribute(final String key) {
		return this.map.get(key);
	}
	
	/**
	 * Gets the raw map of the {@code JSONObject} where all key-value-pairs
	 * are stored.
	 * 
	 * @return The raw map of the {@code JSONObject}.
	 */
	public Map<String, Object> getMap() {
		return this.map;
	}
}
