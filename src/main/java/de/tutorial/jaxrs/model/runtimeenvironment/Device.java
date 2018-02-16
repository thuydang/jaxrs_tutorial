package de.tutorial.jaxrs.model.runtimeenvironment;

import java.net.URI;
import java.util.List;

import de.tutorial.jaxrs.model.properties.Property;

public interface Device {

	/**
	 * IoLite Meta
	 * @return
	 */
	String getName();
	void setName(String name);
	/**
	 * IoLite Meta
	 * @return
	 */
	URI getNamespaceUri();
	void setNamespaceUri(URI namespace);

	/**
	 * IoLite Meta
	 * @return
	 */
	URI getIdentifier();
	void setIdentifier(URI uri);

	/**
	 * IoLite Meta
	 * @return
	 */
	String getFriendlyName();
	void setFriendlyName(String friendlyName);

	/**
	 * IoLite Meta
	 * @return
	 */
	String getVendor();
	void setVendor(String vendor);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link com.gtarc.isco.api.model.properties.Property}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see com.gtarc.isco.api.model.domainmodel.DomainmodelPackage#getDevice_Properties()
	 * @model containment="true"
	 * @generated
	 */
	List<Property> getMandatoryProperties();
	void addMandatoryProperties(Property property);

	List<Property> getOptionalProperties();
	void addOptionalProperties(Property property);
}
