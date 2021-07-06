package fr.insee.coleman.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import fr.insee.coleman.api.domain.Campaign;
import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.domain.SurveyUnit;
import fr.insee.coleman.api.domain.TypeManagementMonitoringInfo;
import fr.insee.coleman.api.domain.Upload;
import fr.insee.coleman.api.dto.managementmonitoringinfo.ManagementMonitoringInfoDto;
import fr.insee.coleman.api.dto.surveyunit.SurveyUnitDto;
import fr.insee.coleman.api.dto.upload.UploadDto;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.services.CampaignService;
import fr.insee.coleman.api.services.ManagementMonitoringInfoService;
import fr.insee.coleman.api.services.OrderService;
import fr.insee.coleman.api.services.SurveyUnitService;
import fr.insee.coleman.api.services.UploadService;
import fr.insee.ctsc.ldap.xml.beans.Contact;
import io.restassured.RestAssured;
import io.restassured.response.Response;


@ExtendWith(SpringExtension.class)
@ActiveProfiles({ "test" })
@ContextConfiguration(initializers = { RestAuthKeycloakTest.Initializer.class })
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties= {"fr.insee.pearljam.application.mode = keycloak"})
@TestPropertySource(locations="classpath:application-test.properties")
public class RestAuthKeycloakTest {
	
	static final Logger LOGGER = LoggerFactory.getLogger(RestAuthKeycloakTest.class);
	
	@Autowired
	ManagementMonitoringInfoService managementMonitoringInfoService;
	@Autowired
	SurveyUnitService surveyUnitService;
	@Autowired
	CampaignService campaignService;
	@Autowired
	OrderService orderService;
	@Autowired
	DataLoader dataLoader;
	@Autowired
	UploadService uploadService;
	@Autowired
	Environment environment;
	
	@LocalServerPort
	int port;
	
	@Container
	public static KeycloakContainer keycloak = new KeycloakContainer().withRealmImportFile("realm.json");
	
	public static final String CLIENT_SECRET = "8951f422-44dd-45b4-a6ac-dde6748075d7";
	public static final String CLIENT = "client-web";
	
	public static ClientAndServer clientAndServer;
	public static MockServerClient mockServerClient;

	@SuppressWarnings("rawtypes")
	@Container
	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres")
			.withDatabaseName("coleman").withUsername("coleman").withPassword("coleman");

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
					.of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
							"spring.datasource.username=" + postgreSQLContainer.getUsername(),
							"spring.datasource.password=" + postgreSQLContainer.getPassword(),
							"spring.application.name=api")
							.applyTo(configurableApplicationContext.getEnvironment());
		}
	}
	
	//////////////////////////API_CAMPAIGNS ///////////////////////
	/**
	* Test that the GET endpoint "campaigns" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(1)
	void testFindCampaign() throws Exception {
		RestAssured.port = port;
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given().auth().oauth2(accessToken).when().get("/campaigns")
		.then().statusCode(200)
		.and().assertThat().body("datas[0].id", equalTo("simpsons2021x00"));
	
	}
	
	/**
	* Test that the GET endpoint "campaigns" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(2)
	void testFindCampaignById() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given().auth().oauth2(accessToken).when().get("/campaigns/simpsons2021x00")
		.then().statusCode(200)
		.and().assertThat().body("id", equalTo("simpsons2021x00"));
	
	}
	
	/**
	 * Test that the Post endpoint
	 * "/campaign" returns 200
	 * @throws InterruptedException
	 */
	@Test
	@Order(3)
	void testPostCampaign() throws InterruptedException, JsonProcessingException, JSONException {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		Campaign campaign = new Campaign();
		campaign.setId("postCampaignTest");
		campaign.setLabel("Test post campaign");
		campaign.setCollectionEndDate(1605909600000l);		
		campaign.setCollectionStartDate(1640995199000l);		
		given()
			.auth().oauth2(accessToken)
		 	.contentType("application/json")
			.body(new ObjectMapper().writeValueAsString(campaign))
			.when()
			.post("/campaigns")
			.then()
			.statusCode(200);
		
		Campaign camp = campaignService.findById(campaign.getId());
		assertNotNull(camp);
		assertEquals(campaign.getLabel(), camp.getLabel());
		assertEquals(campaign.getCollectionEndDate(), camp.getCollectionEndDate());
		assertEquals(campaign.getCollectionStartDate(), camp.getCollectionStartDate());
	}
	
	/**
	 * Test that the put endpoint
	 * "/campaign" returns 200
	 * @throws InterruptedException
	 */
	@Test
	@Order(4)
	void testPutCampaign() throws InterruptedException, JsonProcessingException, JSONException {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		Campaign campaign = new Campaign();
		campaign.setLabel("Test put campaign");
		campaign.setCollectionEndDate(1605909600000l);		
		campaign.setCollectionStartDate(1640995199000l);		
		given()
			.auth().oauth2(accessToken)
		 	.contentType("application/json")
			.body(new ObjectMapper().writeValueAsString(campaign))
			.when()
			.put("/campaigns/postCampaignTest")
			.then()
			.statusCode(200);
		Campaign camp = campaignService.findById("postCampaignTest");
		assertNotNull(camp);
		assertEquals(campaign.getLabel(), camp.getLabel());
		assertEquals(campaign.getCollectionEndDate(), camp.getCollectionEndDate());
		assertEquals(campaign.getCollectionStartDate(), camp.getCollectionStartDate());
	}
	
	/**
	 * Test that the delete endpoint
	 * "/campaign" returns 204
	 * @throws InterruptedException
	 */
	@Test
	@Order(5)
	void testDeleteCampaign() throws InterruptedException, JsonProcessingException, JSONException {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given()
			.auth().oauth2(accessToken)
			.when()
			.delete("/campaigns/postCampaignTest")
			.then()
			.statusCode(204);
		assertThrows(RessourceNotFoundException.class, () -> {campaignService.findById("postCampaignTest");});
	}
	
	/**
	* Test that the GET endpoint "campaigns/contact/{idec}" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(6)
	void testFindCampaignByIdContact() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given().auth().oauth2(accessToken).when().get("/campaigns/contact/A4F2MCB")
		.then().statusCode(200)
		.and().assertThat().body("id", hasItem("simpsons2021x00"));
	
	}
	
	//////////////////////////API_MONITORING///////////////////////
	
	/**
	* Test that the GET endpoint "/campaigns/{idCampaign}/progress" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(7)
	void testGetDataForProgress() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given().auth().oauth2(accessToken).when().get("/campaigns/simpsons2021x00/monitoring/progress")
		.then().statusCode(200)
		.and().assertThat().body("datas[0].nbSu", equalTo(1))
		.and().assertThat().body("datas[0].batchNumber", equalTo(1))
		.and().assertThat().body("datas[0].nbIntReceived", equalTo(0))
		.and().assertThat().body("datas[0].nbPapReceived", equalTo(0))
		.and().assertThat().body("datas[0].nbPND", equalTo(0))
		.and().assertThat().body("datas[0].nbHC", equalTo(0))
		.and().assertThat().body("datas[0].nbRefusal", equalTo(0))
		.and().assertThat().body("datas[0].nbOtherWastes", equalTo(0))
		.and().assertThat().body("datas[0].nbIntPart", equalTo(0));
	
	}
	
	/**
	* Test that the GET endpoint "/campaigns/{idCampaign}/extraction" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(8)
	void testGetDataForExtraction() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given().auth().oauth2(accessToken).when().get("/campaigns/simpsons2021x00/extraction")
		.then().statusCode(200)
		.and().assertThat().body("datas[0].status", equalTo("INITLA"))
		.and().assertThat().body("datas[0].dateInfo", equalTo(1605477600000L))
		.and().assertThat().body("datas[0].idSu", equalTo("9300036162000Z"))
		.and().assertThat().body("datas[0].idContact", equalTo("A4F2MCB"))
		.and().assertThat().body("datas[0].lastname", equalTo("DUPONT"))
		.and().assertThat().body("datas[0].firstname", equalTo("CHLOE"))
		.and().assertThat().body("datas[0].address", equalTo("ST CAPRAIS - 03190"))
		.and().assertThat().body("datas[0].batchNumber", equalTo(3))
		.and().assertThat().body("datas[0].pnd", equalTo(0));
	
	}
	
	/**
	* Test that the GET endpoint "/management-monitoring-info/{id}" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(9)
	void testGetDataToFollowUp() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given().auth().oauth2(accessToken).when().get("/campaigns/simpsons2021x00/monitoring/follow-up")
		.then().statusCode(200)
		.and().assertThat().body("datas[0].nb", equalTo(1))
		.and().assertThat().body("datas[0].freq", equalTo(0))
		.and().assertThat().body("datas[0].batchNum", equalTo(1));
	
	}
	
	//////////////////////////API_MONITORING_INFO///////////////////////
	
	/**
	* Test that the GET endpoint "/management-monitoring-info/{idSu}" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(10)
	void testGetManagementMonitoringInfoById() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given().auth().oauth2(accessToken).when().get("/management-monitoring-info/2")
		.then().statusCode(200)
		.and().assertThat().body("idManagementMonitoringInfo", equalTo(2))
		.and().assertThat().body("surveyUnit.idContact", equalTo("V4LJ4BT"))
		.and().assertThat().body("surveyUnit.lastname", equalTo("BOULANGER"))
		.and().assertThat().body("surveyUnit.address", equalTo("ARTAIX - 71110"))
		.and().assertThat().body("surveyUnit.batchNumber", equalTo(1))
		.and().assertThat().body("surveyUnit.campaign.id", equalTo("simpsons2021x00"))
		.and().assertThat().body("surveyUnit.campaign.label", equalTo("Survey on the Simpsons tv show 2021"))
		.and().assertThat().body("surveyUnit.campaign.collectionStartDate", equalTo(1605909600000L))
		.and().assertThat().body("surveyUnit.campaign.collectionEndDate", equalTo(1640995199000L))
		.and().assertThat().body("surveyUnit.firstName", equalTo("JACQUES"))
		.and().assertThat().body("surveyUnit.idSu", equalTo("7400000751000Z"))
		.and().assertThat().body("status", equalTo("INITLA"))
		.and().assertThat().body("dateInfo", equalTo(1605477600000L))
		.and().assertThat().body("upload", equalTo(null));
	
	}
	
	/**
	* Test that the GET endpoint "/campaigns/{idCampaign}/survey-units/{idSu}/management-monitoring-infos" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(11)
	void testGetManagementMonitoringInfoByIdSuAndIdCampaign() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given().auth().oauth2(accessToken).when().get("/campaigns/simpsons2021x00/survey-units/9300036162000Z/management-monitoring-infos")
		.then().statusCode(200)
		.and().assertThat().body("datas[0].idManagementMonitoringInfo", equalTo(1))
		.and().assertThat().body("datas[0].surveyUnit.idContact", equalTo("A4F2MCB"))
		.and().assertThat().body("datas[0].surveyUnit.lastname", equalTo("DUPONT"))
		.and().assertThat().body("datas[0].surveyUnit.address", equalTo("ST CAPRAIS - 03190"))
		.and().assertThat().body("datas[0].surveyUnit.batchNumber", equalTo(3))
		.and().assertThat().body("datas[0].surveyUnit.campaign.id", equalTo("simpsons2021x00"))
		.and().assertThat().body("datas[0].surveyUnit.campaign.label", equalTo("Survey on the Simpsons tv show 2021"))
		.and().assertThat().body("datas[0].surveyUnit.campaign.collectionStartDate", equalTo(1605909600000L))
		.and().assertThat().body("datas[0].surveyUnit.campaign.collectionEndDate", equalTo(1640995199000L))
		.and().assertThat().body("datas[0].surveyUnit.firstName", equalTo("CHLOE"))
		.and().assertThat().body("datas[0].surveyUnit.idSu", equalTo("9300036162000Z"))
		.and().assertThat().body("datas[0].status", equalTo("INITLA"))
		.and().assertThat().body("datas[0].dateInfo", equalTo(1605477600000L))
		.and().assertThat().body("datas[0].upload", equalTo(null));
	
	}
	
	/**
	* Test that the POST endpoint "/campaigns/{idCampaign}/management-monitoring-info" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(12)
	void testPostManagementMonitoringInfoByIdCampaign() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		ManagementMonitoringInfoDto managementMonitoringInfoDto = new ManagementMonitoringInfoDto();
		managementMonitoringInfoDto.setIdSu("1100001761000Z");
		managementMonitoringInfoDto.setIdContact("A4F2MCB");
		managementMonitoringInfoDto.setDate("23/06/2021");
		managementMonitoringInfoDto.setStatus(TypeManagementMonitoringInfo.FOLLOWUP);
		given()
			.auth().oauth2(accessToken)
		 	.contentType("application/json")
			.body(new ObjectMapper().writeValueAsString(managementMonitoringInfoDto))
			.when()
			.post("campaigns/simpsons2021x00/management-monitoring-info")
			.then()
			.statusCode(201);
		Collection<ManagementMonitoringInfo> managementMonitoringCollection = managementMonitoringInfoService.findBySurveyUnit(surveyUnitService.findByIdSurveyUnitAndIdCampaign("1100001761000Z", "simpsons2021x00"));
		assertNotNull(managementMonitoringCollection);
	}
	

	/**
	* Test that the POST endpoint "/campaigns/{idCampaign}/management-monitoring-infos" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(13)
	void testPostManagementMonitoringInfosByIdCampaign() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		ManagementMonitoringInfoDto managementMonitoringInfoDto1 = new ManagementMonitoringInfoDto();
		managementMonitoringInfoDto1.setIdSu("1100001761000Z");
		managementMonitoringInfoDto1.setIdContact("A4F2MCB");
		managementMonitoringInfoDto1.setDate("23/06/2021");
		managementMonitoringInfoDto1.setStatus(TypeManagementMonitoringInfo.REFUSAL);
		ManagementMonitoringInfoDto managementMonitoringInfoDto2 = new ManagementMonitoringInfoDto();
		managementMonitoringInfoDto2.setIdSu("1100001761000Z");
		managementMonitoringInfoDto2.setIdContact("A4F2MCB");
		managementMonitoringInfoDto2.setDate("23/06/2021");
		managementMonitoringInfoDto2.setStatus(TypeManagementMonitoringInfo.REFUSAL);
		given()
			.auth().oauth2(accessToken)
		 	.contentType("application/json")
			.body(new ObjectMapper().writeValueAsString(List.of(managementMonitoringInfoDto1, managementMonitoringInfoDto2)))
			.when()
			.post("campaigns/simpsons2021x00/management-monitoring-infos")
			.then()
			.statusCode(202);
		Collection<ManagementMonitoringInfo> managementMonitoringCollection = managementMonitoringInfoService.findBySurveyUnit(surveyUnitService.findByIdSurveyUnitAndIdCampaign("1100001761000Z", "simpsons2021x00"));
		assertNotNull(managementMonitoringCollection);
	}
	
	/**
	* Test that the DELETE endpoint "/management-monitoring-infos/{id}" return 200
	 * @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(14)
	void testDeleteManagementMonitoringInfosById() throws Exception {
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		given()
			.auth().oauth2(accessToken)
			.when()
			.delete("/management-monitoring-infos/5")
			.then()
			.statusCode(204);
		assertTrue(managementMonitoringInfoService.findBySurveyUnit(
				surveyUnitService.findByIdSurveyUnitAndIdCampaign("1100001761000Z", "simpsons2021x00")).isEmpty());
	}
	
	/**
	* Test that the GET endpoint "/campaigns/survey-units" returns 200 with filter by contactId
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(15)
	void testFindSuOfCampaignWithFilterByContact() throws Exception {
		RestAssured.port = port;
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");

		Response resp = given().auth().oauth2(accessToken).when().get("/campaigns/survey-units?filter1=a4&page=0&size=5&sort=idSu,DESC");
	    resp.then().statusCode(200);
	    
	    assertEquals(Integer.valueOf(6), resp.path("totalElements"));
	    assertEquals("A4F2MCB", resp.path("content[0].idContact"));
	    assertEquals("DUPONT", resp.path("content[0].lastname"));
	    assertEquals("ST CAPRAIS - 03190", resp.path("content[0].address"));
	    assertEquals(Integer.valueOf(3), resp.path("content[0].batchNumber"));
	    assertEquals("simpsons2021x00", resp.path("content[0].campaign.id"));
	    assertEquals("CHLOE", resp.path("content[0].firstName"));
	    assertEquals("9300036162000Z", resp.path("content[0].idSu"));
	}
	
	/**
	* Test that the GET endpoint "/campaigns/survey-units" returns 200 with filter by contactId
	* and idSu
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(16)
	void testFindSuOfCampaignWithFilterByContactAndIdSu() throws Exception {
		RestAssured.port = port;
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");

		Response resp = given().auth().oauth2(accessToken).when().get("/campaigns/survey-units?filter1=a4&filter2=530&page=0&size=5&sort=idSu,DESC");
	    resp.then().statusCode(200);
	    
	    assertEquals(Integer.valueOf(6), resp.path("totalElements"));
	    assertEquals("AKTLA44", resp.path("content[0].idContact"));
	    assertEquals("FABRES", resp.path("content[0].lastname"));
	    assertEquals("ST PHAL - 10130", resp.path("content[0].address"));
	    assertEquals(Integer.valueOf(2), resp.path("content[0].batchNumber"));
	    assertEquals("simpsons2021x00", resp.path("content[0].campaign.id"));
	    assertEquals("THIERRY", resp.path("content[0].firstName"));
	    assertEquals("2100200361000Z", resp.path("content[0].idSu"));
	}
	
	/**
	* Test that the GET endpoint "/campaigns/{id}/survey-units/follow-up" returns 200
	* and idSu
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(17)
	void testGetSuToFollowUp() throws Exception {
		RestAssured.port = port;
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");

		Response resp = given().auth().oauth2(accessToken).when().get("/campaigns/simpsons2021x00/survey-units/follow-up");
	    resp.then().statusCode(200)
	    .assertThat().body("datas.size()", is(5));
	    
	    
	    assertEquals("4200004052000Z", resp.path("datas[2].idSu"));
	    assertEquals(Integer.valueOf(2), resp.path("datas[2].batchNumber"));
	    assertEquals(Integer.valueOf(0), resp.path("datas[2].pnd"));

	}
	
	/**
	* Test that the GET endpoint "/campaigns/{id}/survey-units/{idSu}" returns 200
	* and idSu
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(18)
	void testGetSu() throws Exception {
		RestAssured.port = port;
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");

		Response resp = given().auth().oauth2(accessToken).when().get("/campaigns/simpsons2021x00/survey-units/1100002861000Z");
	    resp.then().statusCode(200);   
	    
	    assertEquals("Q2PVW6B", resp.path("idContact"));
	    assertEquals("RENARD", resp.path("lastname"));
	    assertEquals("TREON - 28500", resp.path("address"));
	    assertEquals(Integer.valueOf(3), resp.path("batchNumber"));
	    assertEquals("simpsons2021x00", resp.path("campaign.id"));
	    assertEquals("BERTRAND", resp.path("firstName"));
	    assertEquals("1100002861000Z", resp.path("idSu"));

	}
	
	/**
	* Test that the POST endpoint "/campaigns/{id}/survey-units" returns 200
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(19)
	void testPostSurveyUnits() throws Exception {
		RestAssured.port = port;
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		List<SurveyUnitDto> list = new ArrayList<>();
		SurveyUnitDto su1 = new SurveyUnitDto("AAidSuTest", "idContactTest", "lastnameTest", "firstNameTest",
				"addressTest", 2, "simpsons2021x00");
		SurveyUnitDto su2 = new SurveyUnitDto("AAidSuTest2", "idContactTest2", "lastnameTest2", "firstNameTest2",
				"addressTest2", 1, "simpsons2021x00");
		list.add(su1);
		list.add(su2);
		
		Response resp = given().auth().oauth2(accessToken)
				.contentType("application/json")
				.body(new ObjectMapper().writeValueAsString(list))
				.when().post("/campaigns/simpsons2021x00/survey-units");
	    resp.then().statusCode(200);   
	    
	    assertEquals("AAidSuTest", resp.path("OK[0].id"));
	    assertEquals("AAidSuTest2", resp.path("OK[1].id"));
	    
	    SurveyUnit surveyUnit1 = surveyUnitService.findByIdSu(su1.getIdSu());
	    SurveyUnit surveyUnit2 = surveyUnitService.findByIdSu(su2.getIdSu());
	    
	    assertNotNull(surveyUnit1);
	    assertNotNull(surveyUnit2);
	    
	    assertEquals("AAidSuTest", surveyUnit1.getIdSu());
	    assertEquals("idContactTest", surveyUnit1.getIdContact());
	    assertEquals("lastnameTest", surveyUnit1.getLastname());
	    assertEquals("firstNameTest", surveyUnit1.getFirstName());
	    assertEquals("addressTest", surveyUnit1.getAddress());
	    assertEquals(Integer.valueOf(2), surveyUnit1.getBatchNumber());
	    assertEquals("simpsons2021x00", surveyUnit1.getCampaign().getId());
	    
	    assertEquals("AAidSuTest2", surveyUnit2.getIdSu());
	    assertEquals("idContactTest2", surveyUnit2.getIdContact());
	    assertEquals("lastnameTest2", surveyUnit2.getLastname());
	    assertEquals("firstNameTest2", surveyUnit2.getFirstName());
	    assertEquals("addressTest2", surveyUnit2.getAddress());
	    assertEquals(Integer.valueOf(1), surveyUnit2.getBatchNumber());
	    assertEquals("simpsons2021x00", surveyUnit2.getCampaign().getId());
	}
	

	/**
	* Test that the GET endpoint "/campaigns/{id}/uploads" returns 200
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(20)
	void testGetUpload() throws Exception {
		RestAssured.port = port;
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		Upload upload = new Upload(1L, 1111111L, new ArrayList<ManagementMonitoringInfo>());
		uploadService.saveAndFlush(upload);
		SurveyUnit su = surveyUnitService.findByIdSurveyUnitAndIdCampaign("9300036162000Z", "simpsons2021x00");
		ManagementMonitoringInfo mmi = new ManagementMonitoringInfo(
				null, su, TypeManagementMonitoringInfo.INITLA, 11111L,
				upload);
		managementMonitoringInfoService.saveAndFlush(mmi);

		Response resp = given().auth().oauth2(accessToken).when().get("/campaigns/simpsons2021x00/uploads");
	    resp.then().statusCode(200)
	    .assertThat().body("datas.size()", is(1));  
	    
	    assertEquals(Integer.valueOf(1), resp.path("datas[0].id"));
	    assertEquals(Integer.valueOf(1111111), resp.path("datas[0].date"));

	}
	
	/**
	* Test that the POST endpoint "/campaigns/{id}/uploads" returns 200
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(21)
		void testPostUpload() throws Exception {
			RestAssured.port = port;
			String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
			UploadDto dto = new UploadDto();
			List<ManagementMonitoringInfoDto> list = new ArrayList<>();
			ManagementMonitoringInfoDto mmiDto1 = new ManagementMonitoringInfoDto();
			mmiDto1.setDate("01/01/2021");
			mmiDto1.setIdContact("AKTLA44");
			mmiDto1.setIdSu("2100200361000Z");
			mmiDto1.setStatus(TypeManagementMonitoringInfo.INITLA);
			list.add(mmiDto1);
			
			ManagementMonitoringInfoDto mmiDto2 = new ManagementMonitoringInfoDto();
			mmiDto2.setDate("01/01/2021");
			mmiDto2.setIdContact("APL5VW3");
			mmiDto2.setIdSu("4200004052000Z");
			mmiDto2.setStatus(TypeManagementMonitoringInfo.INITLA);
			list.add(mmiDto2);
			
			dto.setData(list);
			
			SurveyUnit su = surveyUnitService.findByIdSurveyUnitAndIdCampaign("2100200361000Z", "simpsons2021x00");
			SurveyUnit su2 = surveyUnitService.findByIdSurveyUnitAndIdCampaign("4200004052000Z", "simpsons2021x00");
			

			Response resp = given().auth().oauth2(accessToken)
					.contentType("application/json")
					.body(new ObjectMapper().writeValueAsString(dto))
					.when().post("/campaigns/simpsons2021x00/uploads");
		    resp.then().statusCode(200);    
		    
		    List<ManagementMonitoringInfo> mmiList1 = (List<ManagementMonitoringInfo>) managementMonitoringInfoService.findBySurveyUnit(su);
		    List<ManagementMonitoringInfo> mmiList2 = (List<ManagementMonitoringInfo>) managementMonitoringInfoService.findBySurveyUnit(su2);

		    assertEquals(TypeManagementMonitoringInfo.INITLA, mmiList1.get(1).getStatus());
		    assertEquals(su, mmiList1.get(1).getSurveyUnit());
		    assertEquals(TypeManagementMonitoringInfo.INITLA, mmiList2.get(1).getStatus());
		    assertEquals(su2, mmiList2.get(1).getSurveyUnit());



		}
	
	/**
	* Test that the POST endpoint "/campaigns/{id}/uploads/validation" returns 200
	* and true when correct format
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(22)
		void testPostUploadValidationTrue() throws Exception {
			RestAssured.port = port;
			String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
			UploadDto dto = new UploadDto();
			List<ManagementMonitoringInfoDto> list = new ArrayList<>();
			ManagementMonitoringInfoDto mmiDto1 = new ManagementMonitoringInfoDto();
			mmiDto1.setDate("01/01/2021");
			mmiDto1.setIdContact("AKTLA44");
			mmiDto1.setIdSu("2100200361000Z");
			mmiDto1.setStatus(TypeManagementMonitoringInfo.INITLA);
			list.add(mmiDto1);
			
			ManagementMonitoringInfoDto mmiDto2 = new ManagementMonitoringInfoDto();
			mmiDto2.setDate("01/01/2021");
			mmiDto2.setIdContact("APL5VW3");
			mmiDto2.setIdSu("4200004052000Z");
			mmiDto2.setStatus(TypeManagementMonitoringInfo.INITLA);
			list.add(mmiDto2);
			
			dto.setData(list);
			


			Response resp = given().auth().oauth2(accessToken)
					.contentType("application/json")
					.body(new ObjectMapper().writeValueAsString(dto))
					.when().post("/campaigns/simpsons2021x00/uploads/validation");
		    resp.then().statusCode(200);    

		    assertEquals("true", resp.as(String.class));

		}
	
	/**
	* Test that the POST endpoint "/campaigns/{id}/uploads/validation" returns 200
	* and false when one of the mmi is empty
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(23)
	void testPostUploadValidationFalse() throws Exception {
		RestAssured.port = port;
		String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
		UploadDto dto = new UploadDto();
		List<ManagementMonitoringInfoDto> list = new ArrayList<>();
		ManagementMonitoringInfoDto mmiDto1 = new ManagementMonitoringInfoDto();
		mmiDto1.setDate("01/01/2021");
		mmiDto1.setIdContact("AKTLA44");
		mmiDto1.setIdSu("2100200361000Z");
		mmiDto1.setStatus(TypeManagementMonitoringInfo.INITLA);
		list.add(mmiDto1);
		
		ManagementMonitoringInfoDto mmiDto2 = new ManagementMonitoringInfoDto();
		list.add(mmiDto2);
		dto.setData(list);

		Response resp = given().auth().oauth2(accessToken)
				.contentType("application/json")
				.body(new ObjectMapper().writeValueAsString(dto))
				.when().post("/campaigns/simpsons2021x00/uploads/validation");
	    resp.then().statusCode(200);    

	    assertEquals("false", resp.as(String.class));

	}
	
	/**
	* Test that the DELETE endpoint "/campaigns/{id}/uploads/validation" returns 204
	* deletes the upload and its child MMIs
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(24)
		void testDeleteUpload() throws Exception {
			RestAssured.port = port;
			String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
			
			SurveyUnit su = surveyUnitService.findByIdSurveyUnitAndIdCampaign("9300036162000Z", "simpsons2021x00");
			

			Response resp = given().auth().oauth2(accessToken)
					.contentType("application/json")
					.when().delete("/uploads/1");
		    resp.then().statusCode(204);    
		    
		    List<ManagementMonitoringInfo> mmiList = (List<ManagementMonitoringInfo>) managementMonitoringInfoService.findBySurveyUnit(su);

		    assertTrue(uploadService.findById(1L).isEmpty());
		    assertEquals(2, mmiList.size());


		}
	
	/**
	* Test that the GET endpoint "/contact/{idContact}/mail" returns 200
	* @throws Exception 
	* 
	* @throws JSONException
	*/
	@Test
	@Order(25)
		void testContactMail() throws Exception {
			String url = "/annuaire/coleman/contact/A4F2MCB";
			//http://localhost/annuaire/coleman/contact/A4F2MCB
			Contact contact = new Contact();
			contact.setPrenom("Test");
			contact.setNom("Nomtest");
			contact.setIdentifiant("A4F2MCB");
			contact.setCivilite("MR");
			contact.setAdresseMessagerie("test@test.com");
			contact.setHasPassword(false);
			clientAndServer = ClientAndServer.startClientAndServer(8081);
			mockServerClient = new MockServerClient("127.0.0.1", 8081);
			String expectedBody = new ObjectMapper().writeValueAsString(contact);
			mockServerClient.when(request().withPath(url))
					.respond(response().withStatusCode(200)
							.withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
							.withBody(expectedBody));
			RestAssured.port = port;
			String accessToken = resourceOwnerLogin(CLIENT, CLIENT_SECRET, "abc", "a");
			Response resp = given().auth().oauth2(accessToken).when().get("/contact/A4F2MCB/mail");
		    resp.then().statusCode(200);
		    assertEquals(resp.getBody().asString(), contact.getAdresseMessagerie());
		}
	
	/***
	 * This method retreive the access token of the keycloak client
	 * @param clientId
	 * @param clientSecret
	 * @param username
	 * @param password
	 * @return
	 * @throws JSONException
	 */
	public String resourceOwnerLogin(String clientId, String clientSecret, String username, String password) throws JSONException {
	      Response response =
	              given().auth().preemptive().basic(clientId, clientSecret)   
	                      .formParam("grant_type", "password")
	                      .formParam("username", username)
	                      .formParam("password", password)
	                      .when()
	                      .post( keycloak.getAuthServerUrl() + "/realms/insee-realm/protocol/openid-connect/token");
	      JSONObject jsonObject = new JSONObject(response.getBody().asString());
	      String accessToken = jsonObject.get("access_token").toString();
	      return accessToken;
	   }
	
	@AfterAll
	public static void  cleanUp() {
		if(postgreSQLContainer!=null) {
			postgreSQLContainer.close();
		}
	}
}
