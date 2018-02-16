package de.tutorial.jaxrs.model.runtimeenvironment;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModelProperty;
import de.tutorial.jaxrs.model.properties.Property;
import de.tutorial.jaxrs.model.runtimeenvironment.Device;;

@XmlRootElement(name = "SensingDevice")
public class SensingDevice implements Device {

	private List<Property> mandatoryProperties = new ArrayList<Property>();
	private List<Property> optionalProperties = new ArrayList<Property>();

	private String platform;
	private int status;


	private String name;
	private URI namespaceUri;
	private URI identifier;
	private String friendlyName;
	private String vendor;

	@XmlElement(name = "platform")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@XmlElement(name = "reStatus")
	@ApiModelProperty(value = "Device Status", allowableValues = "1-registered,2-active,3-closed")
	public int getStatus() {
		return status;
	}

	public void setStatus(int reStatus) {
		this.status = reStatus;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the namespaceUri
	 */
	public URI getNamespaceUri() {
		return namespaceUri;
	}

	/**
	 * @param namespaceUri the namespaceUri to set
	 */
	public void setNamespaceUri(URI namespaceUri) {
		this.namespaceUri = namespaceUri;
	}

	/**
	 * @return the identifier
	 */
	public URI getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the friendlyName
	 */
	public String getFriendlyName() {
		return friendlyName;
	}

	/**
	 * @param friendlyName the friendlyName to set
	 */
	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public void addMandatoryProperties(Property property) {
		mandatoryProperties.add(property);
	}

	public void addOptionalProperties(Property property) {
		optionalProperties.add(property);

	}

	public List<Property> getMandatoryProperties() {
		return mandatoryProperties;
	}

	public List<Property> getOptionalProperties() {
		return optionalProperties;
	}
}
