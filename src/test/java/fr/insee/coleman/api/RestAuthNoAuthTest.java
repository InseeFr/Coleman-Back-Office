package fr.insee.coleman.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insee.coleman.api.domain.Campaign;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.services.CampaignService;
import io.restassured.RestAssured;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({ "test" })
@ContextConfiguration(initializers = { RestAuthNoAuthTest.Initializer.class })
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"fr.insee.pearljam.application.mode = noauth" })
@TestPropertySource(locations = "classpath:application-test.properties")
public class RestAuthNoAuthTest {

	static final Logger LOGGER = LoggerFactory.getLogger(RestAuthNoAuthTest.class);
	
	@Autowired
	CampaignService campaignService;

	@LocalServerPort
	int port;

	@SuppressWarnings("rawtypes")
	@Container
	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres")
			.withDatabaseName("coleman").withUsername("coleman").withPassword("coleman");

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreSQLContainer.getUsername(),
					"spring.datasource.password=" + postgreSQLContainer.getPassword(), "spring.application.name=api")
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	//////////////////////////API_CAMPAIGNS ///////////////////////
	/**
	 * Test that the GET endpoint "campaigns" return 200
	 * 
	 * @throws Exception
	 * 
	 * @throws JSONException
	 */
	@Test
	@Order(1)
	void testFindCampaign() throws Exception {
		RestAssured.port = port;
		given().when().get("/campaigns").then().statusCode(200).and().assertThat()
				.body("datas[0].id", equalTo("simpsons2021x00"));

	}

	/**
	 * Test that the GET endpoint "campaigns" return 200
	 * 
	 * @throws Exception
	 * 
	 * @throws JSONException
	 */
	@Test
	@Order(2)
	void testFindCampaignById() throws Exception {
		given().when().get("/campaigns/simpsons2021x00").then().statusCode(200).and()
				.assertThat().body("id", equalTo("simpsons2021x00"));

	}

	/**
	 * Test that the Post endpoint "/campaign" returns 200
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Order(3)
	void testPostCampaign() throws InterruptedException, JsonProcessingException, JSONException {
		Campaign campaign = new Campaign();
		campaign.setId("postCampaignTest");
		campaign.setLabel("Test post campaign");
		campaign.setCollectionEndDate(1605909600000l);
		campaign.setCollectionStartDate(1640995199000l);
		given().contentType("application/json")
				.body(new ObjectMapper().writeValueAsString(campaign)).when().post("/campaigns").then().statusCode(200);

		Campaign camp = campaignService.findById(campaign.getId());
		assertNotNull(camp);
		assertEquals(campaign.getLabel(), camp.getLabel());
		assertEquals(campaign.getCollectionEndDate(), camp.getCollectionEndDate());
		assertEquals(campaign.getCollectionStartDate(), camp.getCollectionStartDate());
	}

	/**
	 * Test that the put endpoint "/campaign" returns 200
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Order(4)
	void testPutCampaign() throws InterruptedException, JsonProcessingException, JSONException {
		Campaign campaign = new Campaign();
		campaign.setLabel("Test put campaign");
		campaign.setCollectionEndDate(1605909600000l);
		campaign.setCollectionStartDate(1640995199000l);
		given().contentType("application/json")
				.body(new ObjectMapper().writeValueAsString(campaign)).when().put("/campaigns/postCampaignTest").then()
				.statusCode(200);
		Campaign camp = campaignService.findById("postCampaignTest");
		assertNotNull(camp);
		assertEquals(campaign.getLabel(), camp.getLabel());
		assertEquals(campaign.getCollectionEndDate(), camp.getCollectionEndDate());
		assertEquals(campaign.getCollectionStartDate(), camp.getCollectionStartDate());
	}

	/**
	 * Test that the delete endpoint "/campaign" returns 204
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Order(5)
	void testDeleteCampaign() throws InterruptedException, JsonProcessingException, JSONException {
		given().when().delete("/campaigns/postCampaignTest").then().statusCode(204);
		assertThrows(RessourceNotFoundException.class, () -> {
			campaignService.findById("postCampaignTest");
		});
	}

}
