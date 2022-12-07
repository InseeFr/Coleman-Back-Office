package fr.insee.coleman.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.coleman.api.dto.mail.FreeFollowUpMailDto;
import fr.insee.coleman.api.services.SendMailService;
import fr.insee.coleman.api.utils.ResponseFromExternalService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class MailController {

    private static final Logger LOGGER = LogManager.getLogger(MailController.class);

    @Autowired
    private SendMailService mailService;

    @PostMapping(value = "/contact/send-mail", produces = "application/json")
    public ResponseEntity<?> sendMailDiversToContact(@RequestBody FreeFollowUpMailDto followUpMailDto) {
        LOGGER.info("Request send mail to {}", followUpMailDto.getEmail());
        ResponseFromExternalService responseMail = mailService.sendMail(followUpMailDto);
        return ResponseEntity.status(responseMail.getStatus()).body(responseMail.getMessage());
    }

}
