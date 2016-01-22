package com.pitang.business;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ComponentTemplate {

	private String name;

	private Map<String, MetaProperty> properties;

	private Map<String, ResourcePool> pools;
	
	private ComponentTemplate() {
		super();
		this.properties = new LinkedHashMap<>();
		this.pools = new LinkedHashMap<>();
	}
	
	public ComponentTemplate(final String name) {
		this();
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}

	public Map<String, MetaProperty> getProperties() {
		return this.properties;
	}

	public Map<String, MetaProperty> getPropertiesByScope(final PropertyScope scope) {
		final Map<String, MetaProperty> result = new LinkedHashMap<>();

		for(final Entry<String, MetaProperty> property : this.properties.entrySet()) {
			if(property.getValue().getScope().equals(scope)) {
				result.put(property.getKey(), property.getValue());
			}
		}

		return result;
	}

	public void setProperties(final Map<String, MetaProperty> properties) {
		this.properties = properties;
	}
	
	public Map<String, ResourcePool> getPools() {
		return this.pools;
	}
	
	public void setPools(final Map<String, ResourcePool> pools) {
		this.pools = pools;
	}
	
	@Override
	public int hashCode() {
		return (this.name == null) ? 0 : this.name.hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		boolean result = false;
		
		if((obj != null) && ((this == obj) || ((this.getClass() == obj.getClass()) && (this.name != null) && this.name.equals(((ComponentTemplate) obj).name)))) {
			result = true;
		}
		
		return result;
	}

}
