package com.gtarc.servicedirectory.api.chariot.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.endpoint.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization
 * Provide type information in JSON to deserialize subtype (of Interfaces).
 * 
 * @author dang
 */
public class JacksonPolymorphicDeserializationTest extends Assert {

	// REST 
	private final static String ENDPOINT_ADDRESS = "http://localhost:8082/";
	private final static String WADL_ADDRESS = ENDPOINT_ADDRESS + "?_wadl";
	private static Server server;
	static List<Object> providers = new ArrayList<Object>();
	private static ObjectMapper mapper;

	@BeforeClass
	public static void initialize() throws Exception {
		createObjectMapper();
	}

	private static void createObjectMapper() {
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping(); // defaults for defaults (see below); include as wrapper-array, non-concrete types
		//mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT); // all non-final types
	}

	
	@AfterClass
	public static void destroy() throws Exception {
//		server.stop();
//		server.destroy();
	}

	@Test
	/**
	 * Default typing is configured globally for object mapper. Things to configure are:
	 * - just which types (classes) are affected: ObjectMapper.DefaultTyping.{JAVA_LANG_OBJECT, OBJECT_AND_NON_CONCRETE, NON_CONCRETE_AND_ARRAYS, NON_FINAL}.
	 * - how to include type information: JsonTypeInfo.As.{WRAPPER_OBJECT, PROPERTY, WRAPPER_ARRAY}.
	 * Default setting:  JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
	 */
	public void testDefaultTyping() {
		System.out.println(">>> Testing Jackson default typing <<<");

		Dog dog = new Dog();
		try {
			System.out.println(mapper.writeValueAsString(dog));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		//assertEquals("1234", response);
	}

	@Test
	/**
	 * Things to configure are:
	 * 
	 * Type id: possible choices are CLASS (fully-qualified Java class name), MINIMAL_CLASS (relative Java class name, if base class and sub-class are in same package, leave out package name), NAME (use logical name, separately defined, to determine actual class) and CUSTOM (type id handled using custom resolved)
	 * Inclusion: possible choices are PROPERTY (include as regular property along with member properties), WRAPPER_OBJECT (use additional JSON wrapper object; type id used as field name, actual serializer Object as value), WRAPPER_ARRAY (first element is type id; second element serialized Object)
	 * Property name: for inclusion method of PROPERTY, can use any name; defaults depend on type id.
	 * To plug in custom type id resolver use annotation JsonTypeIdResolver
	 * To plug in custom type resolver use annotation JsonTypeResolver
	 */
	public void testPerClassAnnotation() throws JsonParseException, JsonMappingException, IOException {
		System.out.println(">>> Testing Jackson per class annotation <<<");

		AnnotatedDog anDog = new AnnotatedDog();
		String anDogJson = "";

		System.out.println(mapper.writeValueAsString(anDog));
		anDogJson = mapper.writeValueAsString(anDog);

		AnnotatedDog desDog = mapper.readValue(anDogJson, AnnotatedDog.class);
		//assertEquals(anDog, desDog);
		assertThat(anDog, com.nitorcreations.Matchers.reflectEquals(desDog));
	}

	/**
	 * 1. option:
	 * objectMapper.getSerializationConfig().addMixInAnnotations(Target.class, MixIn.class);
	 * objectMapper.getDeserializationConfig().addMixInAnnotations(Target.class, MixIn.class);
	 * 
	 * 2. option:
	 *  Jackson 1.7 you can do this more conveniently via Module interface for both De/Serialization.
	 * 
	 * Note: new ObjectMapper must be created.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void success_testAnnotatedMixinClass_with_new_mapper() throws JsonParseException, JsonMappingException, IOException {
		System.out.println(">>> Testing Mixin success<<<");

		Dog dog = new Dog();
		String dogJson = "";

		ObjectMapper mapper = new ObjectMapper(); // <-- here it is!!!

		mapper.registerModule(new MyMapperModule());
		//mapper.getSerializationConfig().addMixInAnnotations(Dog.class, DogMixIn.class);
		
		mapper.setVisibility(mapper.getSerializationConfig()
		        .getDefaultVisibilityChecker()
		                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
		                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
		                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
		                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		
		System.out.println(mapper.writeValueAsString(dog));
		dogJson = mapper.writeValueAsString(dog);

		Dog desDog = mapper.readValue(dogJson, Dog.class);
		//assertEquals(anDog, desDog);
		assertThat(dog, com.nitorcreations.Matchers.reflectEquals(desDog));
	}

	@SuppressWarnings("deprecation")
	@Test(expected=RuntimeException.class)
	public void failed_testAnnotatedMixinClass_with_shared_global_mapper() throws JsonParseException, JsonMappingException, IOException {
		Dog dog = new Dog();
		String dogJson = "";

		mapper.registerModule(new MyMapperModule());
		//mapper.getSerializationConfig().addMixInAnnotations(Dog.class, DogMixIn.class);
		
		mapper.setVisibility(mapper.getSerializationConfig()
		        .getDefaultVisibilityChecker()
		                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
		                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
		                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
		                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		
		System.out.println(">>> Testing Mixin failed <<<");
		System.out.println(mapper.writeValueAsString(dog));
		dogJson = mapper.writeValueAsString(dog);

		if (!dogJson.contains("\"barkVol\"")) throw new RuntimeException("Fresh ObjectMapper required!!");
		// https://dzone.com/articles/testing-custom-exceptions
		
		//Dog desDog = mapper.readValue(dogJson, Dog.class);
		//assertThat(dog, com.nitorcreations.Matchers.reflectEquals(desDog));
	}
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
			//return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT, property="@class")
	abstract class DogMixIn {
		@JsonCreator
		DogMixIn(@JsonProperty("barkVol") int barkVolume, @JsonProperty("tall") int height) { }

		  // note: could alternatively annotate fields "w" and "h" as well -- if so, would need to @JsonIgnore getters
		  @JsonProperty("barkVol") abstract int getBarkVolume(); // rename property
		  @JsonProperty("tall") abstract int getHeight(); // rename property
		  @JsonIgnore abstract double getLength(); // we don't need it!
	}

	/* ObjectMapper Module */
	public class MyMapperModule extends SimpleModule
	{
	  @SuppressWarnings("deprecation")
	public MyMapperModule() {
	    super("MixinMapper", new Version(0,0,1,null));
	  }
	  @Override
	  public void setupModule(SetupContext context)
	  {
	    context.setMixInAnnotations(Dog.class, DogMixIn.class);
	    // and so on
	  }
	}
	
	/* DTO */
	public class Zoo {
		public Animal animal;
		public AnnotatedAnimal annotatedAnimal;
	}

	/* Class annotation */ 
	@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT, property="@class")
	static class AnnotatedAnimal { // All animals have names, for our demo purposes... 
		public String name;
		protected AnnotatedAnimal() { }
	}
	
	static class AnnotatedDog extends AnnotatedAnimal {
		public double barkVolume; // in decibels
		public double height; // in cm
		public double length; // in cm

		public AnnotatedDog() { }

		public double getHeight() {
			return this.height;
		}

		public double getLength() {
			return this.length;
		}

		public double getBarkVolume() {
			return this.barkVolume;
		}
	}

	static class AnnotatedCat extends AnnotatedAnimal {
		boolean likesCream;
		public int lives;
		public AnnotatedCat() { }
	}

	/* Intact classes */ 
	static class Animal { // All animals have names, for our demo purposes... 
		public String name;
		protected Animal() { }
	}

	static class Dog extends Animal {
		public double barkVolume; // in decibels
		public double height; // in cm
		public double length; // in cm
		public Dog() { }

		public double getBarkVolume() {
			return barkVolume;
		}
		public void setBarkVolume(double barkVolume) {
			this.barkVolume = barkVolume;
		}
		public double getHeight() {
			return height;
		}
		public void setHeight(double height) {
			this.height = height;
		}
		public double getLength() {
			return length;
		}
		public void setLength(double length) {
			this.length = length;
		}
	}

	static class Cat extends Animal {
		boolean likesCream;
		public int lives;
		public Cat() { }
	}
}
