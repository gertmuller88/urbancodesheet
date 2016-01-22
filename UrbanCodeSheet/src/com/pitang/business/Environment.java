package com.pitang.business;

import java.util.LinkedHashMap;
import java.util.Map;

public class Environment {

	private String name;
	
	private ResourcePool pool;

	private Map<String, Property> properties;
	
	private Environment() {
		super();
		this.properties = new LinkedHashMap<>();
	}
	
	public Environment(final String name, final ResourcePool pool) {
		this();
		this.name = name;
		this.pool = pool;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public ResourcePool getPool() {
		return this.pool;
	}
	
	public void setPool(final ResourcePool pool) {
		this.pool = pool;
	}
	
	public Map<String, Property> getProperties() {
		return this.properties;
	}
	
	public void setProperties(final Map<String, Property> properties) {
		this.properties = properties;
	}

	@Override
	public int hashCode() {
		return (this.name == null) ? 0 : this.name.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		boolean result = false;

		if((obj != null) && ((this == obj) || ((this.getClass() == obj.getClass()) && (this.name != null) && this.name.equals(((Environment) obj).name)))) {
			result = true;
		}

		return result;
	}
	
}
