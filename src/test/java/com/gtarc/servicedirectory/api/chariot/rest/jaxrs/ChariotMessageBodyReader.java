package com.gtarc.servicedirectory.api.chariot.rest.jaxrs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtarc.servicedirectory.api.chariot.rest.jackson.ChariotObjectMapper;

import de.tutorial.jaxrs.model.runtimeenvironment.ActuatingDevice;
import de.tutorial.jaxrs.model.runtimeenvironment.Device;
import de.tutorial.jaxrs.model.runtimeenvironment.SensingDevice;

//@Consumes({ "application/json", "application/xml"})
@Consumes("application/json")
@Provider
public class ChariotMessageBodyReader implements MessageBodyReader<Object> {
	static Logger logger = Logger.getLogger(ChariotMessageBodyReader.class);

	private final ObjectMapper mapper;

	public ChariotMessageBodyReader() {
        mapper = new ChariotObjectMapper();
	}

//	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		logger.info(">>> Try reading... " + type);

        if ( type.equals(Device.class) || type.equals(SensingDevice.class) || type.equals(ActuatingDevice.class) ){
        	return true;
        } else {
        	System.out.println(this.getClass() + " Can not read: " + type);
        }
//        return InputStream.class.isAssignableFrom(type);
        return false;
    }

// 	@Override
	public Device readFrom(Class<Object> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
 		
 		final String jsonData = getStringFromInputStream(entityStream);
        
        if (type.equals( type.equals(Device.class))){
        	Device retDevice = mapper.readValue(stringToStream(jsonData), Device.class);
        	return retDevice;
        }
        
        /*
        if(type.equals(SmartCityObject.class)){
        	Service service = mapper.readValue(stringToStream(jsonData), ServiceImpl.class);
        	return service;
        }*/
        //entityStream.reset();
 		//return mapper.readValue(entityStream, Device.class);
 		
        
	
        return null;//commented for testing
 	   /*
       return new FilterInputStream(is) {
             @Override
             public int read(byte[] b) throws IOException {
                 // filter out some bytes
             }             
        }    
        */
    }

 	/**
     * Taken from <a href=
     * "http://www.mkyong.com/java/how-to-convert-inputstream-to-string-in-java/"
     * >www.mkyong.com</a>
     * 
     * @param is
     *            {@link InputStream}
     * @return Stream content as String
     */
    private String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    private InputStream stringToStream(final String str) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(str.getBytes("UTF-8"));
    }
}
