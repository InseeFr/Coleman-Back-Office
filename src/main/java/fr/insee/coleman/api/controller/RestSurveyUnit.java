package fr.insee.coleman.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.coleman.api.configuration.JSONCollectionWrapper;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.domain.ResultUpload;
import fr.insee.coleman.api.domain.ExtractionRow;
import fr.insee.coleman.api.domain.SurveyUnit;
import fr.insee.coleman.api.dto.surveyunit.SurveyUnitDto;
import fr.insee.coleman.api.services.CampaignService;
import fr.insee.coleman.api.services.SurveyUnitService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestSurveyUnit {
	static final Logger LOGGER = LoggerFactory.getLogger(RestSurveyUnit.class);

	@Autowired
	CampaignService campaignService;

	@Autowired
	SurveyUnitService surveyUnitService;

	@GetMapping(value = "/campaigns/{idCampaign}/survey-units/{idSu}", produces = "application/json")
	public SurveyUnit getSurveyUnitByIdCampaign(@PathVariable("idSu") String idSu,
			@PathVariable("idCampaign") String idCampaign) throws RessourceNotFoundException {
		LOGGER.info("Request GET for survey-unit n° {}, campaign n° {}", idSu, idCampaign);
		return surveyUnitService.findByIdSurveyUnitAndIdCampaign(idSu, idCampaign);
	}

	@GetMapping(value = "/campaigns/survey-units", produces = "application/json")
	public Page<SurveyUnit> displaySurveyUnitByFilter(@RequestParam String filter1,
			@RequestParam(required = false) String filter2, Pageable pageable) {
		LOGGER.info("Request GET for survey-unit with filter : {}", filter1);
		if (filter2 == null) {
			return surveyUnitService.searchSurveyUnitByIdWithFilterByIdContact(filter1, pageable);
		} else {
			return surveyUnitService.searchSurveyUnitByIdWithFilter(filter1, filter2, pageable);
		}
	}

	@GetMapping(value = "/campaigns/{idCampaign}/survey-units/follow-up", produces = "application/json")
	public JSONCollectionWrapper<ExtractionRow> displaySurveyUnitsToFollowUp(@PathVariable String idCampaign) {
		LOGGER.info("Request GET for su to follow up - campaign {}", idCampaign);
		return new JSONCollectionWrapper<ExtractionRow>(surveyUnitService.getSurveyUnitsToFollowUp(idCampaign));
	}

	@PostMapping(value = "/campaigns/{idCampaign}/survey-units", produces = "application/json")
	public ResponseEntity<ResultUpload> initializeSurveyUnitsViaBatch(
			@Valid @RequestBody List<SurveyUnitDto> surveyUnits, @PathVariable String idCampaign) {
		LOGGER.info("Request bulk POST of survey-units for the batch - campaign {}", idCampaign);
		try {
			ResultUpload result = surveyUnitService.initializeSurveyUnits(idCampaign, surveyUnits);
			return new ResponseEntity<ResultUpload>(result, HttpStatus.OK);
		} catch (RessourceNotFoundException e) {
			LOGGER.error("Error in request: campaign not found {}", idCampaign);
			return new ResponseEntity<ResultUpload>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
