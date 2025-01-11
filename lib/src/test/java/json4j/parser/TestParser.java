package json4j.parser;

import org.junit.jupiter.api.Test;

import json4j.json.JSONObject;

public class TestParser {
	@Test
	public void testParser001() {
		JSONParser parser = new JSONParser();
		final String str = "{\"MAIN SUBJECT\":[\"Test\"],\"GAZE\":[\"looking at viewer\",\"looking sideways\"],\"LIGHT\":[\"darkness\"],\"FACIAL EXPRESSION\":[\"serious\",\"smiling\",\"laughing\",\"grinning\"]}";
		JSONObject obj = parser.parse(str);
		System.out.println(obj.toString());
	}
}
