package fr.insee.coleman.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.coleman.api.configuration.JSONCollectionWrapper;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;
import fr.insee.coleman.api.domain.ResultUpload;
import fr.insee.coleman.api.domain.Upload;
import fr.insee.coleman.api.dto.upload.UploadDto;
import fr.insee.coleman.api.services.UploadService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestUpload {
	static final Logger LOGGER = LoggerFactory.getLogger(RestUpload.class);

	@Autowired
	UploadService uploadService;

	@DeleteMapping(value = "/uploads/{id}")
	public ResponseEntity<Upload> deleteOneUpload(@PathVariable Long id) {
		LOGGER.info("Request DELETE for upload n° {}", id);
		if (uploadService.deleteUpload(id)) {
			return new ResponseEntity<Upload>(HttpStatus.NO_CONTENT);
		}
		else {
			LOGGER.error("Erro : upload not found");
			return new ResponseEntity<Upload>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/campaigns/{idCampaign}/uploads", produces = "application/json")
	public JSONCollectionWrapper<Upload> displayAllUploads(@PathVariable String idCampaign) {
		LOGGER.info("Request GET for uploads");
		return new JSONCollectionWrapper<Upload>(uploadService.findAllByIdCampaign(idCampaign));
	}

	@PostMapping(value = "/campaigns/{idCampaign}/uploads", produces = "application/json")
	public ResultUpload addManagementMonitoringInfoViaUpload(@PathVariable String idCampaign,
			@RequestBody UploadDto request) throws RessourceNotValidatedException {
		LOGGER.info("Request POST to add an upload");
		ResultUpload retourInfo = uploadService.save(idCampaign, request);
		return retourInfo;
	}

	@PostMapping(value = "/campaigns/{idCampaign}/uploads/validation", produces = "application/json")
	public boolean checkManagementMonitoringInfo(@PathVariable String idCampaign, @RequestBody UploadDto request) {
		boolean booleen = uploadService.checkUploadIsValid(idCampaign, request);
		return booleen;
	}
}
