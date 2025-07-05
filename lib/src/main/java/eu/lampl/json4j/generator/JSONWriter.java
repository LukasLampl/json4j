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

package eu.lampl.json4j.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import eu.lampl.json4j.json.JSONObject;

/**
 * The {@code JSONWriter} is responsible for writing {@link eu.lampl.json4j.json.JSONObject JSONObjects}
 * to the given file.
 * 
 * @author Lukas Lampl
 * @since 1.0.0
 */
public class JSONWriter {
	/**
	 * Writes the given {@link eu.lampl.json4j.json.JSONObject JSONObject} to the given
	 * file.
	 * 
	 * @param obj		The {@code JSONObject} to write into the given file.
	 * @param file		File in which to write the {@code JSONObject}.
	 * @param append	Whether the given {@code JSONObject} should be appended to the end of the file or replace the content.
	 * 
	 * @throws IOException When an I/O exception occurred while writing the {@code JSONObject}.
	 */
	public void write(final JSONObject obj, final File file, final boolean append) throws IOException {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("File can't be a directory.");
		}

		file.createNewFile();
			
		Files.write(Path.of(file.getAbsolutePath()),
				obj.toString().getBytes(), append ?
				StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
	}
}
