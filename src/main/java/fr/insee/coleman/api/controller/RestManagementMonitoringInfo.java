package fr.insee.coleman.api.controller;

import java.util.List;

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
import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.domain.ResultUpload;
import fr.insee.coleman.api.dto.managementmonitoringinfo.ManagementMonitoringInfoDto;
import fr.insee.coleman.api.exception.DuplicateResourceException;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;
import fr.insee.coleman.api.services.CampaignService;
import fr.insee.coleman.api.services.ManagementMonitoringInfoService;
import fr.insee.coleman.api.services.SurveyUnitService;
import fr.insee.coleman.api.services.UploadService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestManagementMonitoringInfo {
	static final Logger LOGGER = LoggerFactory.getLogger(RestManagementMonitoringInfo.class);

	@Autowired
	ManagementMonitoringInfoService managementMonitoringInfoService;
	@Autowired
	UploadService uploadService;
	@Autowired
	SurveyUnitService surveyUnitService;
	@Autowired
	CampaignService campaignService;

	@GetMapping(value = "/management-monitoring-info/{id}", produces = "application/json")
	public ManagementMonitoringInfo displayManagementMonitoringInfo(@PathVariable long id) {
		return managementMonitoringInfoService.getMonitoringInfoById(id);
	}

	@PostMapping(value = "campaigns/{idCampaign}/management-monitoring-info", produces = "application/json")
	public ResponseEntity<ManagementMonitoringInfo> addManagementMonitoringInfoInfosViaBatch(@PathVariable String idCampaign,
			@RequestBody ManagementMonitoringInfoDto request) {
		LOGGER.info("Request POST for batch - campaign {}", idCampaign);
		try {
			ManagementMonitoringInfo mm = managementMonitoringInfoService.saveBatch(idCampaign, request);
			return new ResponseEntity<>(mm, HttpStatus.CREATED);
		} catch (RessourceNotFoundException e) {
			LOGGER.error("Error in demand");
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (DuplicateResourceException e) {
			LOGGER.error("Error: Status already exist");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (RessourceNotValidatedException e) {
			LOGGER.error("Error: Status not authorize");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "campaigns/{idCampaign}/management-monitoring-infos", produces = "application/json")
	public ResponseEntity<ResultUpload> addManagementMonitoringInfoViaBatch(@PathVariable String idCampaign,
			@RequestBody List<ManagementMonitoringInfoDto> request) {
		LOGGER.info("Request POST with mass data for batch - campaign {}", idCampaign);

		try {
			campaignService.findById(idCampaign);
			ResultUpload result = managementMonitoringInfoService.saveManagementMonitoringInfoInMassProcessingBatch(idCampaign, request);
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (RessourceNotFoundException e) {
			// Campaign does not exist
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/campaigns/{idCampaign}/survey-units/{idSu}/management-monitoring-infos", produces = "application/json")
	public JSONCollectionWrapper<ManagementMonitoringInfo> displayManagementMonitoringInfoOfOneSurveyUnit(@PathVariable String idSu,
			@PathVariable String idCampaign) {
		LOGGER.info("Request GET to display the folowwing informations for the survey-unit n° {}, campaign n° {}", idSu, idCampaign);

		return new JSONCollectionWrapper<>(
				managementMonitoringInfoService.findBySurveyUnit(surveyUnitService.findByIdSurveyUnitAndIdCampaign(idSu, idCampaign)));
	}

	// Check MMI that can be deleted
	@DeleteMapping(value = "/management-monitoring-infos/{id}")
	public ResponseEntity<ManagementMonitoringInfo> deleteAManagementMonitoringInfo(@PathVariable long id) {
		LOGGER.info("Request DELETE to delete info n° {}", id);
		ManagementMonitoringInfo mm = managementMonitoringInfoService.getMonitoringInfoById(id);
		if (mm == null) {
			LOGGER.error("Erreur: info suivi non trouvée");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		managementMonitoringInfoService.delete(mm);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
