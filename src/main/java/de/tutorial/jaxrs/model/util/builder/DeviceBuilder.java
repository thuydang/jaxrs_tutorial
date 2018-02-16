package de.tutorial.jaxrs.model.util.builder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import de.tutorial.jaxrs.model.properties.Property;
import de.tutorial.jaxrs.model.runtimeenvironment.ActuatingDevice;
import de.tutorial.jaxrs.model.runtimeenvironment.Device;
import de.tutorial.jaxrs.model.runtimeenvironment.SensingDevice;

public class DeviceBuilder {

	private List<Property> mandatoryProperties = new ArrayList<Property>();
	private List<Property> optionalProperties = new ArrayList<Property>();
	private String platform;
	private int status;

	private String name;
	private URI namespaceUri;
	private URI identifier;
	private String friendlyName;
	private String vendor;
	
	
	public String getPlatform() {
		return platform;
	}

	public DeviceBuilder setPlatform(String platform) {
		this.platform = platform;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public DeviceBuilder setStatus(int status) {
		this.status = status;
		return this;
	}

	public String getName() {
		return name;
	}

	public DeviceBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public URI getNamespaceUri() {
		return namespaceUri;
	}

	public DeviceBuilder setNamespaceUri(URI namespaceUri) {
		this.namespaceUri = namespaceUri;
		return this;
	}

	public URI getIdentifier() {
		return identifier;
	}

	public DeviceBuilder setIdentifier(URI identifier) {
		this.identifier = identifier;
		return this;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public DeviceBuilder setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
		return this;
	}

	public String getVendor() {
		return vendor;
	}

	public DeviceBuilder setVendor(String vendor) {
		this.vendor = vendor;
		return this;
	}

	public DeviceBuilder addMandatoryProperties(Property property) {
		mandatoryProperties.add(property);
		return this;
	}
	
	public DeviceBuilder addOptionalProperties(Property property) {
		optionalProperties.add(property);
		return this;
	}
	
	public SensingDevice buildSensingDevice() {
		SensingDevice device = new SensingDevice();
		device.setName(name);
		device.setNamespaceUri(namespaceUri);
		device.setIdentifier(identifier);
		device.setFriendlyName(friendlyName);
		device.setVendor(vendor);

		device.setPlatform(platform);
		device.setStatus(status);
		
		mandatoryProperties.forEach(p -> device.addMandatoryProperties(p));
		optionalProperties.forEach(p -> device.addOptionalProperties(p));

		return device;
	}

	public ActuatingDevice buildActuating() {
		ActuatingDevice device = new ActuatingDevice();
		device.setName(name);
		device.setNamespaceUri(namespaceUri);
		device.setIdentifier(identifier);
		device.setFriendlyName(friendlyName);
		device.setVendor(vendor);

		device.setPlatform(platform);
		device.setStatus(status);
		
		mandatoryProperties.forEach(p -> device.addMandatoryProperties(p));
		optionalProperties.forEach(p -> device.addOptionalProperties(p));

		return device;
	}
}
