package fr.insee.coleman.api.services;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import fr.insee.coleman.api.domain.Contact;

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
			ResponseEntity<Contact> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, Contact.class);

			switch (response.getStatusCodeValue()) {
			case 200:
				Contact contact = response.getBody();
				if (contact != null) {
					email = contact.getAdresseMessagerie();

					if (email != null) {
						LOGGER.info("Contact already has an email");

					} else {
						LOGGER.info("Contact does not yet have an email");
					}
				}
				break;

			default:
				email = "";
			}
		} catch (HttpClientErrorException e) {
			LOGGER.error("Error during call of monitoring webservice for contacts");
			email = "";
		}
		return ResponseEntity.ok().body(email);

	}

}
