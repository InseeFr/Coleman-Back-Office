package fr.insee.coleman.api.controller;

import fr.insee.coleman.api.domain.Campaign;
import fr.insee.coleman.api.domain.RedirectUnit;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.services.CampaignService;
import fr.insee.coleman.api.services.SurveyUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestRedirectUnit {

    static final Logger LOGGER = LoggerFactory.getLogger(RestRedirectUnit.class);

    @Autowired
    CampaignService campaignService;

    @Autowired
    SurveyUnitService surveyUnitService;

    @GetMapping(value = "/campaigns/redirect-unit/contact", produces = "application/json")
    public RedirectUnit getOpenCampaignsContactsAssociatedByIdec(HttpServletRequest httpRequest) throws RessourceNotFoundException {


        String idec = httpRequest.getRemoteUser().toUpperCase();
        RedirectUnit ru = new RedirectUnit();
        LOGGER.info("Request redirect-unit with idec : {}", idec);
        ru.setOpenedCampaignsIds(campaignService.findOpenedCampaignsByIdec(idec));
        ru.setIdContact(idec);
        if (ru.getOpenedCampaignsIds().size() > 0) {
            ru.setIdUe(surveyUnitService.findByIdContactAndIdCampaign(idec, ru.getOpenedCampaignsIds().get(0)).getIdSu());
        }
        return ru;
    }
}
