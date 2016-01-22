package com.pitang.util.rest;

public enum Module {
	agentCLI("agentCLI"), agentPool("agentPool"), application("application"), applicationProcess("applicationProcess"), component("component"), componentTemplate("componentTemplate"), environment("environment"), group("group"), process("process"), resource("resource"), resourceTemplate("resourceTemplate"), team("team"), teamsecurity("teamsecurity"), user("user"), version("version");

	private final String value;

	private Module(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
