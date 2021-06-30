package fr.insee.coleman.api.controller;

import java.util.List;

//TODO Check if needed
//import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.coleman.api.configuration.JSONCollectionWrapper;
import fr.insee.coleman.api.exception.DuplicateResourceException;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;
import fr.insee.coleman.api.domain.ResultUpload;
import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.dto.managementmonitoringinfo.ManagementMonitoringInfoDto;
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

	@RequestMapping(value = "/management-monitoring-info/{id}", method = RequestMethod.GET, produces = "application/json")
	public ManagementMonitoringInfo displayManagementMonitoringInfo(@PathVariable long id) {
		return managementMonitoringInfoService.getMonitoringInfoById(id);
	}

	@RequestMapping(value = "campaigns/{idCampaign}/management-monitoring-info", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ManagementMonitoringInfo> addManagementMonitoringInfoInfosViaBatch(@PathVariable String idCampaign,
			@RequestBody ManagementMonitoringInfoDto request) {
		LOGGER.info("Request POST for batch - campaign {}", idCampaign);
		try {
			ManagementMonitoringInfo mm = managementMonitoringInfoService.saveBatch(idCampaign, request);
			return new ResponseEntity<ManagementMonitoringInfo>(mm, HttpStatus.CREATED);
		} catch (RessourceNotFoundException e) {
			LOGGER.error("Error in demand");
			return new ResponseEntity<ManagementMonitoringInfo>(HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (DuplicateResourceException e) {
			LOGGER.error("Error: Status already exist");
			return new ResponseEntity<ManagementMonitoringInfo>(HttpStatus.CONFLICT);
		} catch (RessourceNotValidatedException e) {
			LOGGER.error("Error: Status not authorize");
			return new ResponseEntity<ManagementMonitoringInfo>(HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "campaigns/{idCampaign}/management-monitoring-infos", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ResultUpload> addManagementMonitoringInfoViaBatch(@PathVariable String idCampaign,
			@RequestBody List<ManagementMonitoringInfoDto> request) {
		LOGGER.info("Request POST with mass data for batch - campaign {}", idCampaign);

		try {
			campaignService.findById(idCampaign);
			ResultUpload result = managementMonitoringInfoService.saveManagementMonitoringInfoInMassProcessingBatch(idCampaign, request);
			return new ResponseEntity<ResultUpload>(result, HttpStatus.ACCEPTED);
		} catch (RessourceNotFoundException e) {
			// Campaign does not exist
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/campaigns/{idCampaign}/survey-units/{idSu}/management-monitoring-infos", produces = "application/json")
	public JSONCollectionWrapper<ManagementMonitoringInfo> displayManagementMonitoringInfoOfOneSurveyUnit(@PathVariable String idSu,
			@PathVariable String idCampaign) {
		LOGGER.info("Request GET to display the folowwing informations for the survey-unit n° {}, campaign n° {}", idSu, idCampaign);

		return new JSONCollectionWrapper<ManagementMonitoringInfo>(
				managementMonitoringInfoService.findBySurveyUnit(surveyUnitService.findByIdSurveyUnitAndIdCampaign(idSu, idCampaign)));
	}

	// Vérifier les isg que l'on peut delete ou non
	@RequestMapping(value = "/management-monitoring-infos/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ManagementMonitoringInfo> deleteAManagementMonitoringInfo(@PathVariable long id) {
		LOGGER.info("Request DELETE to delete info n° " + id);
		ManagementMonitoringInfo mm = managementMonitoringInfoService.getMonitoringInfoById(id);
		if (mm == null) {
			LOGGER.error("Erreur: info suivi non trouvée");
			return new ResponseEntity<ManagementMonitoringInfo>(HttpStatus.NOT_FOUND);
		}
		managementMonitoringInfoService.delete(mm);
		return new ResponseEntity<ManagementMonitoringInfo>(HttpStatus.NO_CONTENT);
	}

}
