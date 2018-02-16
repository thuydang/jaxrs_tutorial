package de.tutorial.jaxrs.model.runtimeenvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModelProperty;

/**
 * TODO: replace this with device from CHARIOT model from ontology model api.
 * 
 * @author dang
 *
 */
@XmlRootElement(name = "RuntimeEnvironment")
public class RuntimeEnvironment {
	private UUID id;
	private String platform;
	private int reStatus;
	private List<Device> managedDevices = new ArrayList<Device>();

	@XmlElement(name = "id")
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@XmlElement(name = "platform")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@XmlElement(name = "reStatus")
	@ApiModelProperty(value = "RE Status", allowableValues = "1-registered,2-active,3-closed")
	public int getReStatus() {
		return reStatus;
	}

	public void setReStatus(int reStatus) {
		this.reStatus = reStatus;
	}


	@XmlElement(name = "managedDevices")
	public List<Device> getManagedDevices() {
		return managedDevices;
	}

	public void registerDevice(Device device) {
		if (this.managedDevices == null) {
			this.managedDevices = new ArrayList<Device>();
		} else {
			this.managedDevices.add(device);
		}
	}


}
