package fr.insee.coleman.api.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.coleman.api.domain.Campaign;
import fr.insee.coleman.api.domain.SurveyUnit;
import fr.insee.coleman.api.exception.DuplicateResourceException;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;
import fr.insee.coleman.api.repository.CampaignRepository;

@Service
public class CampaignService {

	static final Logger LOGGER = LoggerFactory.getLogger(CampaignService.class);

	@Autowired
	SurveyUnitService surveyUnitService;

	@Autowired
	CampaignRepository campaignRepository;

	@Autowired
	UploadService uploadService;

	public Collection<Campaign> getCampaigns() {
		return campaignRepository.findAll();
	}

	public Campaign findById(String idCampaign) throws RessourceNotFoundException {
		return campaignRepository.findById(idCampaign)
				.orElseThrow(() -> new RessourceNotFoundException("Campaign ", idCampaign));
	}
	
	public List<Campaign> findContactByIdec(String idec) throws RessourceNotFoundException {
		return surveyUnitService.findMultipleByIdContact(idec).stream().map(SurveyUnit::getCampaign).collect(Collectors.toList());
	}

	public List<Campaign> findOpenedCampaignsByIdec(String idec) throws RessourceNotFoundException {
		List<Campaign> campaignsFound = surveyUnitService.findMultipleByIdContact(idec).stream().map(SurveyUnit::getCampaign).collect(Collectors.toList());
		List<Campaign> openCampaignsFound =new ArrayList<>();
		Long dateToday = new Date().getTime();
		campaignsFound.stream().forEach(camp -> {
			if ((camp.getCollectionEndDate()>dateToday) && (camp.getCollectionStartDate()<dateToday)){
				openCampaignsFound.add(camp);
			}
		});
		return openCampaignsFound;
	}

	// Creating and saving the campaign to get the Id
	public Campaign save(Campaign newCampaign) {
		String idCampaign = newCampaign.getId();
		if (!campaignRepository.existsById(idCampaign)) {
			return saveAndFlush(newCampaign);
		} else {
			throw new DuplicateResourceException("The campaign ", idCampaign);
		}
	}

	public Campaign saveAndFlush(Campaign campaign) {
		return campaignRepository.saveAndFlush(campaign);
	}

	public Campaign updateCampaign(String idCampaign, Campaign campaign) throws RessourceNotFoundException {
		return campaignRepository.findById(idCampaign).map(c -> {
			LOGGER.info("Updating the campaign with id " + idCampaign);
			c.setLabel(campaign.getLabel());
			c.setCollectionStartDate(campaign.getCollectionStartDate());
			c.setCollectionEndDate(campaign.getCollectionEndDate());
			return campaignRepository.save(c);
		}).orElseThrow(() -> new RessourceNotFoundException("Campaign", idCampaign));
	}

	private boolean campaignIsDeletable(Campaign campaign) {
		long current = new Date().getTime();
		long campaignEnd;
		try {
			campaignEnd = campaign.getCollectionEndDate();
		} catch (Exception e) {
			return false;
		}
		return current > campaignEnd;
	}

	public void deleteById(String idCampaign) throws RessourceNotFoundException, RessourceNotValidatedException {
		Campaign campaign = campaignRepository.findById(idCampaign)
				.orElseThrow(() -> new RessourceNotFoundException("Campaign", idCampaign));
		if (!campaignIsDeletable(campaign))
			throw new RessourceNotValidatedException("Campaign", idCampaign);
		surveyUnitService.deleteByCampaignId(idCampaign);
		campaignRepository.deleteById(idCampaign);
		uploadService.removeEmptyUploads();

	}

}
