package com.gtarc.servicedirectory.api.chariot.rest;

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
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gtarc.chariot.model.runtimeenvironment.Device;
import com.gtarc.chariot.model.runtimeenvironment.SensingDevice;
import com.gtarc.chariot.model.util.builder.DeviceBuilder;
import com.gtarc.chariot.model.util.builder.PropertyBuilder;
import com.gtarc.servicedirectory.api.chariot.rest.jackson.DeviceMapperModule;
import com.gtarc.servicedirectory.api.chariot.rest.jaxrs.ChariotMessageBodyReader;
import com.gtarc.servicedirectory.api.chariot.rest.jaxrs.ChariotMessageBodyWriter;
import com.gtarc.servicedirectory.api.chariot.rest.jaxrs.JacksonContextResolver;
import com.gtarc.servicedirectory.api.chariot.rest.resource.DeviceResource;
import com.gtarc.servicedirectory.api.chariot.rest.resource.RuntimeEnvironmentResource;

/**
 * Test CXF
 * https://cwiki.apache.org/confluence/display/CXF20DOC/JAXRS+Testing
 * http://cxf.apache.org/docs/jax-rs-client-api.html#JAX-RSClientAPI-CXFWebClientAPI
 * @author dang
 *
 */
public class ChariotServiceDirectoryApiTest extends Assert {
	static Logger logger = Logger.getLogger(ChariotServiceDirectoryApiTest.class);

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
		mapper.registerModule(new DeviceMapperModule());


		// Using ObjectMapperResolver / Provider
		JacksonJsonProvider jacksonJsonProvider = //new JacksonJsonProvider();
		new JacksonJsonProvider(mapper);
		//jacksonJsonProvider.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		providers.add(jacksonJsonProvider);
		
		//jackson jaxb
		JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
		jacksonJaxbJsonProvider.setMapper(mapper);
		providers.add(jacksonJaxbJsonProvider);

		// context resolver
		providers.add(new JacksonContextResolver());
		// message body
		providers.add(new ChariotMessageBodyWriter());
		providers.add(new ChariotMessageBodyReader());
		sf.setProvider(providers);



		sf.setResourceClasses(DeviceResource.class);
		sf.setResourceProvider(DeviceResource.class,
				new SingletonResourceProvider(new RuntimeEnvironmentResource(), true));
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

	//@Test
	public void testEchoWithWebClient() {
		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		//client.accept("text/xml");
		client.path("/echo/1234");
		String response = client.get(String.class);
		//Book book = client.get(Book.class);
		assertEquals("1234", response);
	}

	@Test
	public void testPostInterfaceWithWebClient() {
		System.out.println(">>> Begin Test <<<");

		WebClient client = WebClient.create(ENDPOINT_ADDRESS, providers);
		client.type(MediaType.APPLICATION_JSON);
		client.accept(MediaType.APPLICATION_JSON);
		//client.accept(MediaType.APPLICATION_XML);
		client.path("/device/echoRegister/");

		ClientConfiguration config = WebClient.getConfig(client);
		config.getInInterceptors().add(new LoggingInInterceptor());
		config.getOutInterceptors().add(new LoggingOutInterceptor());

		// Create Device
		Device device = new DeviceBuilder()
				.setName("Camera")
				.setNamespaceUri(URI.create("http://iolite.de"))
				.setIdentifier(URI.create("http://iolite.de#" + "Camera1"))
				.addMandatoryProperties(new PropertyBuilder()
						.setLabel("liveVideoUri")
						.setUnit("URI")
						.setValue("camera1.livestream.com")
						.build())
				.addOptionalProperties(new PropertyBuilder()
						.setLabel("batteryLevel")
						.setUnit("%")
						.setValue(Integer.parseInt("80"))
						.build())
				.buildSensingDevice();

		// Jackson mapper
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new DeviceMapperModule());
		
		// Create request
		Response response = null;

		try {
			System.out.println(">>> WebClient post: " + mapper.writeValueAsString(device));
			//response = client.post(mapper.writeValueAsString(device));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response = client.post(device);
		//response = client.post(Entity.entity(device, "application/json; charset=UTF-8"));
		// Response Error 415: caused by no message body reader.
		assertEquals(200, response.getStatus());

		// Read object
		System.out.println("WebClient Response: " + response.readEntity(String.class));
		Device getDevice = response.readEntity(SensingDevice.class);

		assertNotNull(getDevice);
		//assertEquals("1234", response);
		//assertEquals(device.getAccessibility(), Accessibility.OPEN);
	}

	/**
	 * POST
	 * @param obj
	 * @return
	 */
	//@Test
	public void testPOSTWithHttpClient() {
		System.out.println(">>> Begin Test <<<");

		String jsonStr = "{'com.gtarc.isco.api.model.domainmodel.impl.DeviceImpl':{"
				+ "'domains':{'org.eclipse.emf.ecore.util.EDataTypeUniqueEList':['LIVING']},"
				+ "'id':'d-995fb057-7a91-4ff3-bf8a-fec0a0266e6e',"
				+ "'accessibility':'OPEN',"
				+ "'location':{'com.gtarc.isco.api.model.spatialdata.impl.LocationImpl':{"
				+ "'latitude':2.0,'longitude':1.0,'altitude':0.0}},'operationalArea':{"
				+ "'com.gtarc.isco.api.model.spatialdata.impl.CircleImpl':{'radius':5}},"
				+ "'activationStatus':'OFF','owner':'MUNICIPALITY',"
				+ "'properties':{'org.eclipse.emf.ecore.util.EObjectResolvingEList':[]}}}";

		//jsonStr ="{\"this\":\"is it\"}";

		//HttpPost post = new HttpPost("http://localhost:8081/" + "plannerservice/request/");
		//String path = "/plannerservice/"+"request/";
		String path = "/isco/postecho/";
		HttpPost post = new HttpPost(ENDPOINT_ADDRESS + path);
		// this works
		//HttpPost post = new HttpPost("https://jsonplaceholder.typicode.com" + "/posts/");


		//post.addHeader("content-type", "application/json;charset=UTF-8");
		post.addHeader("Accept","application/json");
		post.addHeader("Content-Type", "application/json");
		//post.setEntity(new FileEntity(input, ContentType.TEXT_XML));

		StringEntity requestEntity = null;
		try {
			requestEntity = new StringEntity(jsonStr);
			requestEntity.setChunked(true);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//requestEntity.setContentType("application/json");
		//ContentType.APPLICATION_JSON
		post.setEntity(requestEntity);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			CloseableHttpResponse response = httpClient.execute(post);
			System.out.println("Response status code: " + response.getStatusLine().getStatusCode());
			System.out.println("Response status : " + response.toString());
			System.out.println("Response body: ");
			assertEquals(200, response.getStatusLine().getStatusCode());
			System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Release current connection to the connection pool once you are
			// done
			post.releaseConnection();
		}
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
