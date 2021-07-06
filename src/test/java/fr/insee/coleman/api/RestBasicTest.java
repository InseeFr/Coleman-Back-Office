package fr.insee.coleman.api;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import fr.insee.coleman.api.controller.RestBasic;

@SpringBootTest
public class RestBasicTest {

	@Test
	public void testHelloWorld() {
		RestBasic api = new RestBasic();

		// GIVEN
		assertEquals("Hello World", api.helloWorld());
		// WHEN

		// THEN
	}

}
