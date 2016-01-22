package com.pitang.util.rest;

public enum Operation {
	create("create"), info("info"), tag("tag"), teams("teams"), addComponentToApp("addComponentToApp"), addBaseResource("addBaseResource"), componentsInApplication("componentsInApplication"), createEnvironment("createEnvironment"), propValue("propValue");
	
	private final String value;

	private Operation(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
