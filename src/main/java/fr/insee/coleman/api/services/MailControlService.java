package fr.insee.coleman.api.services;

import java.util.Base64;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class MailControlService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailControlService.class);

	@Value("${fr.insee.coleman.ldap.services.uri.host}")
	public String serviceContactBaseUrl;

	@Value("${fr.insee.coleman.ldap.services.uri.port}")
	public int serviceContactPort;

	@Value("${fr.insee.coleman.ldap.externe.server.pw}")
	public String serviceContactPassword;

	@Value("${fr.insee.coleman.ldap.externe.server.login}")
	public String serviceContactLogin;

	@Value("${fr.insee.coleman.ldap.externe.domaineGestion}")
	public String serviceContactDomain;

	public ResponseEntity<String> getMail(String idContact) {

		String email = "";

		RestTemplate restTemplate = new RestTemplate();

		StringBuilder url = new StringBuilder(serviceContactBaseUrl);
		url.append(":");
		url.append(serviceContactPort); 
		url.append("/annuaire/");
		url.append(serviceContactDomain);
		url.append("/contact/");
		url.append(idContact);
		String plainCreds = serviceContactLogin + ":" + serviceContactPassword;
		String encodedString = Base64.getEncoder().encodeToString(plainCreds.getBytes());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encodedString);

		HttpEntity<String> request = new HttpEntity<>(headers);

		try {
			ResponseEntity<Object> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, Object.class);
			@SuppressWarnings("unchecked")
			LinkedHashMap<String,String> object = (LinkedHashMap<String, String>) response.getBody();
			if(object == null || object.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			if(!HttpStatus.OK.equals(response.getStatusCode())) {
				return new ResponseEntity<>(response.getStatusCode());
			}
			email = object.get("AdresseMessagerie");
			if (email == null) {
				LOGGER.info("Contact does not yet have an email");
				return ResponseEntity.noContent().build();
			}
		} catch (HttpClientErrorException e) {
			LOGGER.error("Error during call of monitoring webservice for contacts");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok().body(email);

	}

}
