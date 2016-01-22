package com.pitang.util;

import java.util.HashMap;
import java.util.Map;

public class Session {

	private static Session instance = null;

	private final Map<String, Object> attributes;

	private Session() {
		this.attributes = new HashMap<>();
	}

	public static Session getInstance() {
		if(Session.instance == null) {
			Session.instance = new Session();
		}

		return Session.instance;
	}

	public void setAttribute(final String name, final Object value) {
		this.attributes.put(name, value);
	}

	public Object getAttribute(final String name) {
		return this.attributes.get(name);
	}

}
