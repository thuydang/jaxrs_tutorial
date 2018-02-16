/**
 */
package de.tutorial.jaxrs.model.properties.impl;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;

import de.tutorial.jaxrs.model.properties.ComplexProperty;
import de.tutorial.jaxrs.model.properties.Property;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Complex Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.gtarc.isco.api.model.properties.impl.ComplexPropertyImpl#getSubProperties <em>Sub Properties</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComplexPropertyImpl extends PropertyImpl implements ComplexProperty {
	/**
	 * The cached value of the '{@link #getSubProperties() <em>Sub Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubProperties()
	 * @generated
	 * @ordered
	 */
	protected List<Property> subProperties;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexPropertyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Property> getSubProperties() {
		if (subProperties == null) {
			subProperties = new ArrayList<Property>();
		}
		return subProperties;
	}

	public void addSubProperty(Property property) {
		if (subProperties == null) {
			subProperties = new ArrayList<Property>();
		}
		subProperties.add(property);
	}

} //ComplexPropertyImpl
