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

import java.util.Map;
import java.util.Map.Entry;

/**
 * The {@code JSONEncoder} is purely for translating
 * {@link eu.lampl.json4j.json.JSONObject JSONObjects} to strings.
 * 
 * @author Lukas Lampl
 * @since 1.0.0
 */
public abstract class JSONEncoder {
	/**
	 * Translates a {@code JSONObject} map to string representation.
	 * 
	 * @param map	Map of the {@code JSONObject} to translate.
	 * @return A string representation of the {@code JSONObject's} map.
	 */
	public String toString(final Map<String, Object> map) {
		final StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		int counter = 0;
		
		for (final Entry<String, Object> entry : map.entrySet()) {
			final Object value = entry.getValue();
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
	
	/**
	 * Gets the value of an JSON-entry in form of a string.
	 * 
	 * <p>
	 * <ul>
	 * <li>{@code INTEGER} will result in {@code JSON-INTEGER}</li>
	 * <li>{@code DOUBLE} will result in {@code JSON-FLOATINGPOINT}</li>
	 * <li>{@code JSONOBJECT} will result in {@code JSON-OBJECT}</li>
	 * <li>{@code JSONARRAY} will result in {@code JSON-ARRAY}</li>
	 * <li>{@code STRING} will result in {@code "STRING"}</li>
	 * <li>{@code NULL} will result in {@code "null"}</li>
	 * </ul>
	 * 
	 * @param value		The value to translate.
	 * @return The translated value.
	 */
	private String getValueString(final Object value) {
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
	
	/**
	 * Gets the JSON representation of a floating point value.
	 * 
	 * @param value		Floating point to translate.
	 * @return The translated floating point value.
	 */
	private String getFloatingpointValue(final double value) {
		if (Double.isInfinite(value)) {
			return "\"Infinity\"";
		} else if (Double.isNaN(value)) {
			return "\"NaN\"";
		}
		
		return String.valueOf(value);
	}
	
	/**
	 * Translates a single {@link eu.lampl.json4j.json.JSONArray JSONArray}
	 * into a string.
	 * 
	 * @param jsonObjs	The {@code JSONArray} to translate.
	 * @return A string representation of the given {@code JSONArray}.
	 */
	private String handleJSONObjectArray(final JSONArray jsonObjs) {
		final StringBuilder builder = new StringBuilder();
		final StringBuilder content = new StringBuilder();
		builder.append("[\n");
		int counter = 0;
		
		if (jsonObjs.length() > 0) {
			for (final Object obj : jsonObjs.getArray()) {
				if (obj == null) {
					continue;
				}
				
				content.append(getIdentString(getValueString(obj)));
				
				if ((++counter) < jsonObjs.length()) {
					content.append(",\n");
				}
			}
		}
		
		builder.append(getIdentString(content.toString()));
		builder.append("\n]");
		return builder.toString();
	}
	
	/**
	 * Gets an indent line with the given string as the content
	 * after the indentation.
	 * 
	 * @param str	String after the indentation.
	 * @return An indent string.
	 */
	private String getIdentString(final String str) {
		final String[] lines = str.split("\n");
		final StringBuilder builder = new StringBuilder();
		int counter = 0;
		
		for (final String line : lines) {
			builder.append("\t");
			builder.append(line);
			
			if ((++counter) < lines.length) {
				builder.append("\n");
			}
		}
		
		return builder.toString();
	}
}
