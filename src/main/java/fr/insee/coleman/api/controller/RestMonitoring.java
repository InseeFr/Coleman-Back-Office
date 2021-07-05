package fr.insee.coleman.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.coleman.api.configuration.JSONCollectionWrapper;
import fr.insee.coleman.api.domain.BatchProgress;
import fr.insee.coleman.api.domain.ExtractionRow;
import fr.insee.coleman.api.domain.UpRow;
import fr.insee.coleman.api.services.SuiviService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestMonitoring {
	static final Logger LOGGER = LoggerFactory.getLogger(RestMonitoring.class);

	@Autowired
	SuiviService suivi;

	@GetMapping(value = "/campaigns/{idCampaign}/monitoring/progress", produces = "application/json")
	public JSONCollectionWrapper<BatchProgress> getDataForProgress(@PathVariable String idCampaign) {
		LOGGER.info("Request GET for monitoring table for campaign : {}", idCampaign);
		return suivi.getProgress(idCampaign);
	}

	@GetMapping(value = "campaigns/{idCampaign}/monitoring/follow-up", produces = "application/json")
	public JSONCollectionWrapper<UpRow> getDataToFollowUp(@PathVariable String idCampaign) {
		LOGGER.info("Request GET for following table for campaign : {}", idCampaign);
		return suivi.getFollowUp(idCampaign);
	}

	@GetMapping(value = "campaigns/{idCampaign}/extraction", produces = "application/json")
	public JSONCollectionWrapper<ExtractionRow> provideDataForExtraction(@PathVariable String idCampaign) {
		LOGGER.info("Request GET for extraction of campaign : {}", idCampaign);
		return suivi.getExtraction(idCampaign);
	}
}
