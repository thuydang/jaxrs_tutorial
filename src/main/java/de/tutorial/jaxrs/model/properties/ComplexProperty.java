/**
 */
package de.tutorial.jaxrs.model.properties;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Complex Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.tutorial.jaxrs.model.properties.ComplexProperty#getSubProperties <em>Sub Properties</em>}</li>
 * </ul>
 *
 * @see com.gtarc.isco.api.model.properties.PropertiesPackage#getComplexProperty()
 * @model
 * @generated
 */
public interface ComplexProperty extends Property {
	/**
	 * Returns the value of the '<em><b>Sub Properties</b></em>' containment reference list.
	 * The list contents are of type {@link com.gtarc.isco.api.model.properties.Property}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Properties</em>' containment reference list.
	 * @see com.gtarc.isco.api.model.properties.PropertiesPackage#getComplexProperty_SubProperties()
	 * @model containment="true" required="true"
	 * @generated
	 */
	List<Property> getSubProperties();
	void addSubProperty(Property property);

} // ComplexProperty
