package json4j.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import json4j.json.JSONObject;

public class JSONGenerator {
	public JSONGenerator() {
		super();
	}
	
	public void write(JSONObject obj, File file) {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("File can't be a directory.");
		}
		
		try {
			file.createNewFile();
			
			Files.write(Path.of(file.getAbsolutePath()),
					obj.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
