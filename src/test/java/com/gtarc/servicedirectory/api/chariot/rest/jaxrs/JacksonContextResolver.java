package com.gtarc.servicedirectory.api.chariot.rest.jaxrs;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gtarc.servicedirectory.api.chariot.rest.jackson.DeviceMapperModule;

	/* ContextResolver for custome objectmapper */
@Provider
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

    final ObjectMapper mapper;

    public JacksonContextResolver() {
        /*
         * Register JodaModule to handle Joda DateTime Objects.
         * https://github.com/FasterXML/jackson-datatype-jsr310
         */
    	super();
        mapper = createDefaultMapper();
    }


    private ObjectMapper createDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // config
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.enableDefaultTyping(); // defaults for defaults (see below); include as wrapper-array, non-concrete types
		//mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types

		// modules
		//mapper.registerModule(new JSR310Module());
        mapper.registerModule(new DeviceMapperModule());

		return mapper;
	}

	//@Override
    public ObjectMapper getContext(Class<?> arg0) {
    	System.out.println(">>>>>> check that!");
        return mapper;
    }
}