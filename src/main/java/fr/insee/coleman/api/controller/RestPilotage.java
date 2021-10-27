package fr.insee.coleman.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.coleman.api.configuration.ApplicationProperties;
import fr.insee.coleman.api.configuration.ApplicationProperties.Mode;
import fr.insee.coleman.api.constants.Constants;
import fr.insee.coleman.api.dto.surveyunit.HabilitationDto;
import fr.insee.coleman.api.services.CampaignService;
import fr.insee.coleman.api.services.SurveyUnitService;
import fr.insee.coleman.api.services.UserService;

import java.util.Locale;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class RestPilotage {

    static final Logger LOGGER = LoggerFactory.getLogger(RestPilotage.class);

    @Autowired
    CampaignService campaignService;

    @Autowired
    UserService userService;

    @Autowired
    SurveyUnitService surveyUnitService;

    @Autowired
    ApplicationProperties applicationProperties;

    @Value("${fr.insee.coleman.manager.role:#{null}}")
    private String managerRole;


    @GetMapping(value = "/api/check-habilitation", produces = "application/json")
    public ResponseEntity<HabilitationDto> checkHabilitation(HttpServletRequest request,
                                                             @RequestParam(value = "id", required = true) String id,
                                                             @RequestParam(value = "role", required = false) String role,
                                                             @RequestParam(value = "campaign", required = true) String campaign) {
        HabilitationDto resp = new HabilitationDto();

        if (role != null && !role.isBlank()) {
            if (role.equals(Constants.REVIEWER) && request.isUserInRole(managerRole)) {
                LOGGER.info("Check habilitation of reviewer {} for accessing survey-unit {} of campaign {} resulted in true",request.getRemoteUser(),id,campaign);
                resp.setHabilitated(true);
            } else {
                resp.setHabilitated(false);
                LOGGER.warn("Only '{}' is accepted as a role in query argument", Constants.REVIEWER);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
        } else {
            if (applicationProperties.getMode().equals(Mode.noauth)) {
                resp.setHabilitated(true);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
            String idec = request.getRemoteUser().toUpperCase();

            boolean habilitated = surveyUnitService.checkContact(id, idec,campaign);
            LOGGER.info("Check habilitation of interviewer {} for accessing survey-unit {} of campaign {} resulted in {}",idec,id,campaign,habilitated);
            resp.setHabilitated(habilitated);
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


}
