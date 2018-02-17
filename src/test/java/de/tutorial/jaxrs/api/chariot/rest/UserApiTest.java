package de.tutorial.jaxrs.api.chariot.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jaxrs.Jaxrs2TypesModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.tutorial.jaxrs.api.chariot.rest.jackson.DeviceMapperModule;
import de.tutorial.jaxrs.api.chariot.rest.jaxrs.ChariotMessageBodyReader;
import de.tutorial.jaxrs.api.chariot.rest.jaxrs.ChariotMessageBodyWriter;
import de.tutorial.jaxrs.api.chariot.rest.jaxrs.JacksonContextResolver;
import de.tutorial.jaxrs.api.chariot.rest.resource.DeviceResource;
import de.tutorial.jaxrs.api.chariot.rest.resource.UserResource;
import de.tutorial.jaxrs.model.runtimeenvironment.Device;
import de.tutorial.jaxrs.model.runtimeenvironment.SensingDevice;
import de.tutorial.jaxrs.model.runtimeenvironment.User;
import de.tutorial.jaxrs.model.util.builder.DeviceBuilder;
import de.tutorial.jaxrs.model.util.builder.PropertyBuilder;

/**
 * Test CXF
 * https://cwiki.apache.org/confluence/display/CXF20DOC/JAXRS+Testing
 * http://cxf.apache.org/docs/jax-rs-client-api.html#JAX-RSClientAPI-CXFWebClientAPI
 * @author dang
 *
 */
public class UserApiTest extends Assert {
	static Logger logger = Logger.getLogger(UserApiTest.class);

	// REST 
	private final static String ENDPOINT_ADDRESS = "http://localhost:8082/";
	private final static String WADL_ADDRESS = ENDPOINT_ADDRESS + "?_wadl";
	private static Server server;
	static List<Object> providers = new ArrayList<Object>();

	@BeforeClass
	public static void initialize() throws Exception {
		// <-- Log to console in DEBUG Mode. More http://logging.apache.org/log4j/1.2/manual.html 
		BasicConfigurator.configure(); 
		startServer();
	}

	//	private static ObjectMapper createObjectMapper() {
	//		ObjectMapper mapper = new ObjectMapper();
	//
	//		mapper.enableDefaultTyping(); // defaults for defaults (see below); include as wrapper-array, non-concrete types
	//		//mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
	//		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
	//		mapper.registerModule(new DeviceMapperModule());
	//
	//		return mapper;
	//	}

	private static void startServer() throws Exception {
		/**
		 * 1. provide mapper for Device Interface.
		 * 2. register the mapper with json provider.
		 * 3. register the provider with cxf.
		 * test: can handle plain java object w/o annotation!
		 * 4. handling Interface requires body reader/writer??? Try it.
		 */
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();

		// Add custome object mapper
		//providers.add(new JacksonJsonProvider(new CustomObjectMapper()));

		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(); // defaults for defaults (see below); include as wrapper-array, non-concrete types
		//mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
		mapper.registerModule(new Jaxrs2TypesModule());


		// Using ObjectMapperResolver / Provider
		/* class JacksonJsonProvider implementing javax.ws.rs.ext.MessageBodyReader and javax.ws.rs.ext.MessageBodyWriter that JAX-RS defines for pluggable support for data formats. JacksonJsonProvider (and JacksonJaxbJsonProvider) can then be registered with JAX-RS container to make Jackson the standard JSON reader/writer provider.*/
		JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
//				new JacksonJsonProvider(mapper);
		//jacksonJsonProvider.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		providers.add(jacksonJsonProvider);

		//jackson jaxb
		//		JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
		//		jacksonJaxbJsonProvider.setMapper(mapper);
		//		providers.add(jacksonJaxbJsonProvider);

		// context resolver
//		providers.add(new JacksonContextResolver());
//		// message body
//		providers.add(new ChariotMessageBodyWriter());
//		providers.add(new ChariotMessageBodyReader());

		sf.setProvider(providers);

		sf.setResourceClasses(UserResource.class);
		sf.setResourceProvider(UserResource.class,
				new SingletonResourceProvider(new UserResource(), true));
		sf.setAddress(ENDPOINT_ADDRESS);

		sf.getInInterceptors().add(new LoggingInInterceptor());
		sf.getOutInterceptors().add(new LoggingOutInterceptor());

		server = sf.create();
	}

	@AfterClass
	public static void destroy() throws Exception {
		server.stop();
		server.destroy();
	}

	@Test
	public void testPostInterfaceWithWebClient() {
		System.out.println(">>> Begin Test <<<");

		WebClient client = WebClient.create(ENDPOINT_ADDRESS, providers);
		client.type(MediaType.APPLICATION_JSON);
		client.accept(MediaType.APPLICATION_JSON);
		//client.accept(MediaType.APPLICATION_XML);
		client.path("/user");

		ClientConfiguration config = WebClient.getConfig(client);
		config.getInInterceptors().add(new LoggingInInterceptor());
		config.getOutInterceptors().add(new LoggingOutInterceptor());

		// Create User

		// Create request
		Response response = null;
		User user = new User();
		user.setFirstName("Thuy");
		user.setLastName("Dang");

//		Jackson mapper
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
		// NOTE: generate different json then the internal mapper!!

		String json = "";

		try { 
			json = mapper.writeValueAsString(user);
			System.out.println(">>> WebClient post: " + json);
			//response = client.post(json);

			//response = client.post(mapper.writeValueAsString(user));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response = client.post(user); // using internal mapper
		//response = client.post(Entity.entity(device, "application/json; charset=UTF-8"));
		// Response Error 415: caused by no message body reader.
		assertEquals(200, response.getStatus());

		// Read object
		//System.out.println("WebClient Response: " + response.readEntity(String.class));
		User getUser = response.readEntity(User.class);

		assertNotNull(getUser);
		//assertEquals(device.getAccessibility(), Accessibility.OPEN);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
			//return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}






}
