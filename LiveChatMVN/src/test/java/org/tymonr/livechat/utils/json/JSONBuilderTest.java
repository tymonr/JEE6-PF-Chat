package org.tymonr.livechat.utils.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JSONBuilderTest {

	@Test
	public void testGivesEmptyJSONObject_whenNothingAdded() {
		JSONBuilder builder = new JSONBuilder();

		assertEquals("{}", builder.toString());
	}

	@Test
	public void testSinglePari() {
		JSONBuilder builder = new JSONBuilder();
		builder.add("key1", "value1");

		assertEquals("{\"key1\":\"value1\"}", builder.toString());
	}

	@Test
	public void testTwoPairs() {
		JSONBuilder builder = new JSONBuilder();
		builder.add("key1", "value1");
		builder.add("key2", "value2");

		String expected = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
		assertEquals(expected, builder.toString());
	}
}
