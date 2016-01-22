package com.pitang.business;

public enum PropertyScope {
	
	COMPONENT("Componente"), ENVIRONMENT("Ambiente");

	private String name;

	private PropertyScope(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static PropertyScope fromName(final String name) {
		PropertyScope result = null;

		for(final PropertyScope value : PropertyScope.values()) {
			if(value.getName().equals(name)) {
				result = value;
			}
		}

		return result;
	}

}
