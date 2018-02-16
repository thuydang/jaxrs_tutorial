package com.gtarc.servicedirectory.api.chariot.rest.jackson;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.eclipse.emf.common.util.EList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.gtarc.isco.api.model.domainmodel.Device;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gtarc.isco.api.model.domainmodel.Device;
import com.gtarc.isco.api.model.domainmodel.Domain;
import com.gtarc.isco.api.model.domainmodel.Service;
import com.gtarc.isco.api.model.domainmodel.impl.ServiceImpl;

public class ChariotObjectMapper extends ObjectMapper{

	private static final long serialVersionUID = 5672946888729305914L;

	public ChariotObjectMapper() {
		super();
		super.setSerializationInclusion(Include.NON_EMPTY);
        super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		super.enableDefaultTyping(); // defaults for defaults (see below); include as wrapper-array, non-concrete types
		//mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
		super.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types

		// modules
		//mapper.registerModule(new JSR310Module());
        super.registerModule(new DeviceMapperModule());
	}
}