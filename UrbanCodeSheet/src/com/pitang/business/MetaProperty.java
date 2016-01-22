package com.pitang.business;

public class MetaProperty {

	private String name;

	private PropertyScope scope;

	private MetaProperty() {
		super();
	}

	public MetaProperty(final String name) {
		this();
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public PropertyScope getScope() {
		return this.scope;
	}
	
	public void setScope(final PropertyScope scope) {
		this.scope = scope;
	}
	
	@Override
	public int hashCode() {
		return (this.name == null) ? 0 : this.name.hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		boolean result = false;
		
		if((obj != null) && ((this == obj) || ((this.getClass() == obj.getClass()) && (this.name != null) && this.name.equals(((MetaProperty) obj).name)))) {
			result = true;
		}
		
		return result;
	}
	
}
