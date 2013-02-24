package org.tymonr.livechat.utils.json;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

/*TODO: 
 * 1. refactor to builder pattern.
 * 2. add array support
 * 3. add nested objects support
 * 4. ... or find a decent lib that already does all of this.
 */
/**
 * Support class for building JSON object containing only key-value pairs
 */
public class JSONBuilder {

	private final List<String> list;

	public JSONBuilder() {
		list = new ArrayList<String>();
	}

	/**
	 * Add key-value pair
	 * 
	 * @param key
	 * @param value
	 */
	public void add(String key, String value) {
		list.add('"' + key + '"' + ':' + '"' + value + '"');
	}

	/**
	 * JSON object containing key-value pairs as a string.
	 */
	@Override
	public String toString() {
		Joiner joiner = Joiner.on(',');
		return '{' + joiner.join(list) + '}';
	}

}
