package fr.insee.coleman.api.controller;

import fr.insee.coleman.api.configuration.JSONCollectionWrapper;
import fr.insee.coleman.api.domain.*;
import fr.insee.coleman.api.dto.managementmonitoringinfo.ManagementMonitoringInfoDto;
import fr.insee.coleman.api.exception.DuplicateResourceException;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;
import fr.insee.coleman.api.services.CampaignService;
import fr.insee.coleman.api.services.ManagementMonitoringInfoService;
import fr.insee.coleman.api.services.SurveyUnitService;
import fr.insee.coleman.api.services.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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


	@GetMapping(value = "/campaigns/{idCampaign}/survey-units/{idSu}/state", produces = "application/json")
	public ResponseEntity<String> getStateByIdCampaignBySurveyUnit(@PathVariable("idSu") String idSu,
													   @PathVariable("idCampaign") String idCampaign) throws RessourceNotFoundException {
		LOGGER.info("Request State for survey-unit n° {}, campaign n° {}", idSu, idCampaign);

		SurveyUnit su = surveyUnitService.findByIdSurveyUnitAndIdCampaign(idSu,idCampaign);

		if (su == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("survey unit not found for this campaign or campaign not found");
		}

		Optional<TypeManagementMonitoringInfo> state = managementMonitoringInfoService.getState(su);
		if(!state.isPresent()){
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no state found in database");
		}
		return ResponseEntity.status(HttpStatus.OK).body((state.get()).name());

	}


	@GetMapping(value = "/campaigns/{idCampaign}/survey-units/{idSu}/isToFollowUp", produces = "application/json")
	public ResponseEntity<?> getIsToFollowUpByIdCampaignBySurveyUnit(@PathVariable("idSu") String idSu,
															@PathVariable("idCampaign") String idCampaign) throws RessourceNotFoundException {
		LOGGER.info("Check if survey-unit n° {}, campaign n° {} is eligible to followUp", idSu, idCampaign);

		SurveyUnit su = surveyUnitService.findByIdSurveyUnitAndIdCampaign(idSu,idCampaign);

		if (su == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("survey unit not found for this campaign or campaign not found");
		}

		Optional<TypeManagementMonitoringInfo> state = managementMonitoringInfoService.getState(su);
		if(!state.isPresent()){
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no state found in database");
		}

		return ResponseEntity.status(HttpStatus.OK).body(Arrays.stream(NoFollowUpManagementMonitoringInfos.values()).collect(Collectors.toList()).contains(state) ? false : true );
	}


	@GetMapping(value = "/campaigns/{idCampaign}/survey-units/{idSu}/isToExtract", produces = "application/json")
	public ResponseEntity<?> getIsToExtractByIdCampaignBySurveyUnit(@PathVariable("idSu") String idSu,
															@PathVariable("idCampaign") String idCampaign) throws RessourceNotFoundException {
		LOGGER.info("Check if survey-unit n° {}, campaign n° {} is eligible for extraction", idSu, idCampaign);

		SurveyUnit su = surveyUnitService.findByIdSurveyUnitAndIdCampaign(idSu,idCampaign);

		if (su == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("survey unit not found for this campaign or campaign not found");
		}


		Optional<TypeManagementMonitoringInfo> state = managementMonitoringInfoService.getState(su);
		if(!state.isPresent()){
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no state found in database");
		}
		return ResponseEntity.status(HttpStatus.OK).body(Arrays.stream(ExtractManagementMonitoringInfos.values()).collect(Collectors.toList()).contains(state) ? true : false );

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
