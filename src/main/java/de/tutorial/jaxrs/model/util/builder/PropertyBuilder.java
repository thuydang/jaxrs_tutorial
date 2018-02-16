package de.tutorial.jaxrs.model.util.builder;

import java.util.ArrayList;
import java.util.List;

import de.tutorial.jaxrs.model.properties.Property;
import de.tutorial.jaxrs.model.properties.impl.ComplexPropertyImpl;
import de.tutorial.jaxrs.model.properties.impl.SimplePropertyImpl;

public class PropertyBuilder {

	protected String label;

	/* Simple Property Only */
	protected Object value;
	protected String unit;
	
	/* Complex Property */
	List<Property> subProperties = new ArrayList<Property>();

	public String getLabel() {
		return label;
	}

	public PropertyBuilder setLabel(String label) {
		this.label = label;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public PropertyBuilder setValue(Object value) {
		this.value = value;
		return this;
	}

	public String getUnit() {
		return unit;
	}

	public PropertyBuilder setUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public List<Property> getSubProperties() {
		return subProperties;
	}

	public PropertyBuilder addSubProperty(Property subProperty) {
		this.subProperties.add(subProperty);
		return this;
	}
	
	public Property build() {
		Property property;

		if (subProperties.isEmpty()) {
			property = new SimplePropertyImpl();
			((SimplePropertyImpl) property).setUnit(this.unit);
			((SimplePropertyImpl) property).setValue(this.value);
		} else {
			property = new ComplexPropertyImpl();
			this.subProperties.forEach(p -> ((ComplexPropertyImpl) property).addSubProperty(p));
		}

		property.setLabel(this.label);

		return property;
	}
}
