package fr.insee.coleman.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.coleman.api.services.MailControlService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class RestContact {

	private static final Logger LOGGER = LogManager.getLogger(RestContact.class);

	@Autowired
	MailControlService mailControlService;

	@ApiOperation(value = "Get mail of contact")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The mail has been found for this contact"),
			@ApiResponse(code = 500, message = "An error has occured.") })
	@GetMapping("/contact/{idContact}/mail")
	public ResponseEntity<String> mailConsultation(@PathVariable String idContact) {
		LOGGER.info("GET mail for contact with id" + idContact);
		return mailControlService.getMail(idContact);

	}
}
