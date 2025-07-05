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

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code JSONArray} is a representative container class for
 * all JSON array structures.
 * 
 * It mimics the structure by using an internal list that is expanded
 * or shrunk based on the operation.
 * 
 * @author Lukas Lampl
 * @since 1.0.0
 */
public class JSONArray extends JSONEncoder {
	/**
	 * Holds the raw objects of the {@code JSONArray}.
	 */
	private List<Object> array = new ArrayList<Object>();
	
	/**
	 * Appends a single object to the end of the current
	 * {@code JSONArray}.
	 * 
	 * @param obj	Object to append.
	 */
	public void add(final Object obj) {
		this.array.add(obj);
	}
	
	/**
	 * Appends a list of objects as raw JSON data to the array.
	 * 
	 * @param objs	Objects to append at the end of the {@code JSONArray}
	 */
	public void addAll(final List<Object> objs) {
		this.array.addAll(objs);
	}
	
	/**
	 * Gets the object at the given index.
	 * 
	 * @param index		Index of the object to get.
	 * @return The object at the given index.
	 */
	public Object get(final int index) {
		return this.array.get(index);
	}
	
	/**
	 * Gets the underlying array that holds all the JSON data.
	 * 
	 * @return The underlying array with the raw JSON data.
	 */
	public List<Object> getArray() {
		return this.array;
	}
	
	/**
	 * Gets the length of the {@code JSONArray}.
	 * 
	 * <p>
	 * The length is determined by how many elements are in the array.
	 * 
	 * @return The length of the {@code JSONArray}.
	 */
	public int length() {
		return this.array.size();
	}
	
	@Override
	public String toString() {
		return super.JSONObjectArrayToString(this);
	}
}
