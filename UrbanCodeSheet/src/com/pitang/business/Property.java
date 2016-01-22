package com.pitang.business;

public class Property {

	private MetaProperty metaProperty;

	private String value;
	
	private Property() {
		super();
	}
	
	public Property(final MetaProperty metaProperty, final String value) {
		this();
		this.metaProperty = metaProperty;
		this.value = value;
	}

	public MetaProperty getMetaProperty() {
		return this.metaProperty;
	}

	public void setMetaProperty(final MetaProperty metaProperty) {
		this.metaProperty = metaProperty;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		return ((this.metaProperty == null) ? 0 : this.metaProperty.hashCode()) + ((this.value == null) ? 0 : this.value.hashCode());
	}
	
	@Override
	public boolean equals(final Object obj) {
		boolean result = false;
		
		if((obj != null) && ((this == obj) || (((this.getClass() == obj.getClass()) && this.metaProperty.equals(((Property) obj).metaProperty)) && (this.value != null) && this.value.equals(((Property) obj).value)))) {
			result = true;
		}
		
		return result;
	}

}
