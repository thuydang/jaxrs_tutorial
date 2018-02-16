package com.gtarc.servicedirectory.api.chariot.rest.jaxrs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static java.nio.charset.StandardCharsets.UTF_8;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtarc.chariot.model.runtimeenvironment.ActuatingDevice;
import com.gtarc.chariot.model.runtimeenvironment.Device;
import com.gtarc.chariot.model.runtimeenvironment.SensingDevice;
import com.gtarc.servicedirectory.api.chariot.rest.jackson.ChariotObjectMapper;

/**
 * A MessageBodyWriter implementation that uses Jackson ObjectMapper to serialize objects to JSON.
 */
@Produces("application/json")
@Provider
public class ChariotMessageBodyWriter implements MessageBodyWriter<Object> {
	static Logger logger = Logger.getLogger(ChariotMessageBodyWriter.class);

    private final ObjectMapper mapper;

    public ChariotMessageBodyWriter() {
        mapper = new ChariotObjectMapper();
        //mapper.getSerializationConfig().configure(
        //        JsonSerialize.Inclusion.NON_NULL);
    }

//    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        if (type.equals(Device.class) || type.equals(SensingDevice.class) || type.equals(ActuatingDevice.class)) {
        	logger.info("Try writing..." + type);
        	return true;
        }
        else
        	logger.info(" Can not write: " + type.getClass());
        //if (type.isAnnotationPresent((Class<? extends Annotation>) DeviceImpl.class))
        //    return mapper.canSerialize(type);
        return false;
    }

	//@Override
    public long getSize(Object data, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

	//@Override
    public void writeTo(Object data, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> headers, OutputStream out)
            throws IOException, WebApplicationException {
		System.out.println(">>> MessageBodyWriter - MediaType: " + mediaType.toString());
		System.out.println(">>> MessageBodyWriter - ObjectMapper: " + mapper.writeValueAsString(data));

        Writer writer = new OutputStreamWriter(out, UTF_8);
        mapper.writeValue(writer, data);
        //writer.flush();
        //out.flush();
		//out.write(mapper.writeValueAsBytes(data));
		
		//DataOutputStream dos = new DataOutputStream(out);
		//dos.writeUTF(mapper.writeValueAsString(data));
		//out.flush();

		//String jsonStr = mapper.writeValueAsString(data);
		//out.write(jsonStr.getBytes());
        /*
        Writer writer = new OutputStreamWriter(entityStream, UTF_8);
        JsonMetadata.toJson(metadata, writer);
        writer.flush();
        entityStream.flush();
        */

    }

}
