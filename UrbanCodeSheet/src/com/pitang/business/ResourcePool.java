package com.pitang.business;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResourcePool {

	private String name;

	private Map<String, Environment> environments;
	
	private ResourcePool() {
		super();
		this.environments = new LinkedHashMap<>();
	}
	
	public ResourcePool(final String name) {
		this();
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public Map<String, Environment> getEnvironments() {
		return this.environments;
	}
	
	public void setEnvironments(final Map<String, Environment> environments) {
		this.environments = environments;
	}

	@Override
	public int hashCode() {
		return (this.name == null) ? 0 : this.name.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		boolean result = false;

		if((obj != null) && ((this == obj) || ((this.getClass() == obj.getClass()) && (this.name != null) && this.name.equals(((ResourcePool) obj).name)))) {
			result = true;
		}

		return result;
	}
	
}
