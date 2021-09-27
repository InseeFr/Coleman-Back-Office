package fr.insee.coleman.api.controller;

import java.util.List;

import fr.insee.coleman.api.domain.RedirectUnit;
import fr.insee.coleman.api.services.SurveyUnitService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.coleman.api.configuration.JSONCollectionWrapper;
import fr.insee.coleman.api.domain.Campaign;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;
import fr.insee.coleman.api.services.CampaignService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestCampaign {

	static final Logger LOGGER = LoggerFactory.getLogger(RestCampaign.class);

	@Autowired
	CampaignService campaignService;

	@Autowired
	SurveyUnitService surveyUnitService;

	/* GET /campaigns : To retrieve the list of ongoing campaigns */
	@GetMapping(value = "/campaigns", produces = "application/json")
	public JSONCollectionWrapper<Campaign> displayCampaignInProgress() {
		LOGGER.info("Request GET campaigns");
		return new JSONCollectionWrapper<Campaign>(campaignService.getCampaigns());
	}

	/*
	 * POST /campaigns : creates a campaign with the given idCampaign. Checks that the 
	 * the idCampaign does not already exist
	 */

	@PostMapping(value = "/campaigns", produces = "application/json")
	public void addCampaigns(@RequestBody Campaign newCampaign) throws RessourceNotFoundException {
		LOGGER.info("Request POST to add a campaign");
		campaignService.save(newCampaign);
	}

	@PutMapping(value = "/campaigns/{idCampaign}", produces = "application/json")
	public void updateCampaign(@PathVariable String idCampaign, @RequestBody Campaign newCampaign)
			throws RessourceNotFoundException {
		LOGGER.info("Request PUT to add a campaign");
		campaignService.updateCampaign(idCampaign, newCampaign);
	}

	@GetMapping(value = "/campaigns/{idCampaign}", produces = "application/json")
	public Campaign displayACampaign(@PathVariable String idCampaign) throws RessourceNotFoundException {
		LOGGER.info("Request GET with campaign id : {}", idCampaign);
		return campaignService.findById(idCampaign);
	}
	
	@GetMapping(value = "/campaigns/contact/{idec}", produces = "application/json")
	public List<Campaign> getCampaignsContactsAssociatedByIdec(@PathVariable String idec) throws RessourceNotFoundException {
		LOGGER.info("Request GET with idec : {}", idec);
		return campaignService.findContactByIdec(idec);
	}

	@GetMapping(value = "/campaigns/redirect-unit/contact/{idec}", produces = "application/json")
	public RedirectUnit getOpenCampaignsContactsAssociatedByIdec(@PathVariable String idec) throws RessourceNotFoundException {
		RedirectUnit ru =new RedirectUnit();
		LOGGER.info("Request GET with idec : {}", idec);
		ru.setOpenedCampaignsIds(campaignService.findOpenedCampaignsByIdec(idec));
		ru.setIdContact(idec);
		ru.setIdUe(surveyUnitService.findByIdContact(idec).getIdSu());
		return ru;
	}

	/*
	 * DELETE /campaigns : Deletes the campaign with the given idCampaign. Checks if
	 * campaign exists : if not, throws NotFoundException Checks if campaign is finished
	 * : if not, throws RessourceNonValideException
	 */

	@DeleteMapping(value = "/campaigns/{idCampaign}", produces = "application/json")
	public ResponseEntity<Campaign> deleteACampaign(@PathVariable String idCampaign) {
		LOGGER.info("Request DELETE with idCampaign : " + idCampaign);
		try {
			campaignService.deleteById(idCampaign);
			return new ResponseEntity<Campaign>(HttpStatus.NO_CONTENT);
		} catch (RessourceNotFoundException e) {
			LOGGER.warn(e.toString());
			return new ResponseEntity<Campaign>(HttpStatus.NOT_FOUND);

		} catch (RessourceNotValidatedException e) {
			LOGGER.warn(e.toString());
			return new ResponseEntity<Campaign>(HttpStatus.NOT_FOUND);
		}
	}

}
