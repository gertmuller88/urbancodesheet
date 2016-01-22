package com.pitang.business;

import java.util.LinkedHashMap;
import java.util.Map;

public class Component {
	
	private String name;
	
	private String application;
	
	private String acronym;
	
	private ComponentTemplate template;
	
	private ResourcePool pool;
	
	private Map<String, Environment> environments;
	
	private Map<String, Property> properties;
	
	private Component() {
		super();
		this.environments = new LinkedHashMap<>();
		this.properties = new LinkedHashMap<>();
	}
	
	public Component(final String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getApplication() {
		return this.application;
	}

	public void setApplication(final String application) {
		this.application = application;
	}

	public String getAcronym() {
		return this.acronym;
	}

	public void setAcronym(final String acronym) {
		this.acronym = acronym;
	}

	public ComponentTemplate getTemplate() {
		return this.template;
	}

	public void setTemplate(final ComponentTemplate template) {
		this.template = template;
	}

	public ResourcePool getPool() {
		return this.pool;
	}

	public void setPool(final ResourcePool pool) {
		this.pool = pool;
	}

	public Map<String, Environment> getEnvironments() {
		return this.environments;
	}

	public void setEnvironments(final Map<String, Environment> environments) {
		this.environments = environments;
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

		if((obj != null) && ((this == obj) || ((this.getClass() == obj.getClass()) && (this.name != null) && this.name.equals(((Component) obj).name)))) {
			result = true;
		}

		return result;
	}

}
