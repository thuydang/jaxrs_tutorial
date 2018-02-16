package com.gtarc.servicedirectory.api.chariot.rest.jackson;
/* ObjectMapper Module */

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.tutorial.jaxrs.model.properties.Property;
import de.tutorial.jaxrs.model.runtimeenvironment.Device;

public class DeviceMapperModule extends SimpleModule
{
	@SuppressWarnings("deprecation")
	public DeviceMapperModule() {
		super("DeviceMapperModule", new Version(0,0,1,null));
	}
	@Override
	public void setupModule(SetupContext context)
	{
		context.setMixInAnnotations(Device.class, DeviceMixIn.class);
		// and so on
	}

	@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT, property="@class")
	abstract class DeviceMixIn {
//		@JsonCreator
//		DeviceMixIn(@JsonProperty("barkVol") int barkVolume, @JsonProperty("tall") int height) { }

		  // note: could alternatively annotate fields "w" and "h" as well -- if so, would need to @JsonIgnore getters
		@JsonProperty("nameMixin") abstract String getName();
		@JsonProperty("namespaceMixin") abstract URI getNamespaceUri();
		@JsonProperty("idMixin") abstract URI getIdentifier();
		@JsonProperty("vendorMixin") abstract String getVendor();
		@JsonProperty("friendlyNameMixin") abstract String getFriendlyName();
		@JsonProperty("mandatoryPropMixin") abstract List<Property> getMandatoryProperties();
		@JsonProperty("optionalPropMixin") abstract List<Property> getOptionalProperties();
		//@JsonIgnore abstract double getLength(); // we don't need it!
	}
}