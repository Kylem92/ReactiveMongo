package reactiveMongoDb;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.http.HttpEntity;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestHelper {	

	private static final Logger log = LoggerFactory.getLogger(TestHelper.class);

	private TestHelper() {
		// private constructor
	}

	public static String readTestFile(String relativePath) {
		try {
			return FileUtils.readFileToString(new File("src/test/resources/" + relativePath), StandardCharsets.UTF_8).replace("\r\n", "\n");
		} catch (IOException e) {
			Assert.fail("File read error: " +e.getMessage());
			return "";
		}
	}

	public static byte[] readTestFileBinary(String relativePath) {
		try {
			return FileUtils.readFileToByteArray(new File("src/test/resources/" + relativePath));
		} catch (IOException e) {
			Assert.fail("File read error: " +e.getMessage());
			return new byte[0];
		}
	} 

	/**
	 * @return Answer to be used in <code>doAnswer(answer).when(mock).someMethod(matchers);</code><br/>
	 * which simply returns the <b>argumentIdx</b> parameter as a return value of the triggered method.<br/>
	 * 
	 * This is very useful for mocking repositories (i.e. save, update, persist,...)
	 */
	public static <T> Answer<T> getSimplestAnswer(int argumentIdx, Class<T> clazz) {
		return new Answer<T>() {
			@Override
			public T answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgument(argumentIdx);
			}
		};
	}

	/**
	 * @return Generic answer that returns the argumentIdx argument of the method call (index starts at 0, which refers the first argument).<br/>
	 * Additionally it stores the value in captureArgument parameter. 
	 */
	public static <T> Answer<T> getSimpleAnswer(int argumentIdx, AtomicReference<T> captureArgument, Class<T> clazz) {
		return new Answer<T>() {
			@Override
			public T answer(InvocationOnMock invocation) throws Throwable {
				captureArgument.set(invocation.getArgument(argumentIdx));
				return captureArgument.get();
			}
		};
	}

	public static <T, C> Answer<C> getSimpleAnswerDoReturn(int argumentIdx, AtomicReference<T> captureArgument, Class<T> clazz, C response) {
		return new Answer<C>() {
			@Override
			public C answer(InvocationOnMock invocation) throws Throwable {
				captureArgument.set(invocation.getArgument(argumentIdx));
				return response;
			}
		};
	}

	public static <T, C> Answer<C> getSimpleAnswerWithHttpEntityDoReturn(int argumentIdx, AtomicReference<HttpEntity<T>> captureArgument, Class<T> clazz,
			C response) {
		return new Answer<C>() {
			@Override
			public C answer(InvocationOnMock invocation) throws Throwable {
				captureArgument.set(invocation.getArgument(argumentIdx));
				return response;
			}
		};
	}
	
	public static <T, C> Answer<C> getSimpleAnswerWithListDoReturn(int argumentIdx, AtomicReference<List<T>> captureArgument, C response) {
		return new Answer<C>() {
			@Override
			public C answer(InvocationOnMock invocation) throws Throwable {
				captureArgument.set(invocation.getArgument(argumentIdx));
				return response;
			}
		};
	}
	
	public static String objectToJSON(Object obj) throws JsonProcessingException {
		if (obj == null) {
			return "{}";
		}
		ObjectMapper mapper = getObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	public static <T> T objectFromJSON(String json, Class<T> clazz) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		return mapper.readValue(json, clazz);
	}

	private static ObjectMapper getObjectMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

		mapper.setSerializationInclusion(Include.NON_EMPTY);

		final SimpleModule module = new SimpleModule();
		mapper.registerModule(module);
//		mapper.registerModule(new JodaModule());
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());
		mapper.registerModule(new GeoJsonModule());
		return mapper;
	}

	@SuppressWarnings("unchecked")
	public static <T> T objectFromXml(String xml, Class<T> clazz) throws JAXBException {
		final JAXBContext jc = JAXBContext.newInstance(clazz);
		final Unmarshaller u = jc.createUnmarshaller();
		return (T) u.unmarshal(new StringReader(xml));
	}

	public static String objectToXml(Object obj) {
		try {
			JAXBContext jc = JAXBContext.newInstance(obj.getClass());
			final Marshaller u = jc.createMarshaller();
			StringWriter ret = new StringWriter();
			u.marshal(obj, ret);
			return ret.toString();
		} catch (JAXBException e) {
			log.warn("JAXB exception", e);
			return "";
		}
	}
}
