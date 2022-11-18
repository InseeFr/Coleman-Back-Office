package fr.insee.coleman.api.services;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.HttpClients;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import fr.insee.archi.spoc.content.MessageTemplate;
import fr.insee.archi.spoc.content.NameValuePairType;
import fr.insee.archi.spoc.content.Recipient;
import fr.insee.archi.spoc.content.SendRequest;
import fr.insee.archi.spoc.content.SendRequest.Recipients;
import fr.insee.coleman.api.dto.mail.FreeFollowUpMailDto;
import fr.insee.coleman.api.utils.ResponseFromExternalService;

@Service
public class SendMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);

    @Value("${fr.insee.coleman.spoc.healthcheck}")
    private String spocHeathcheck;

    @Value("${fr.insee.coleman.spoc.url}")
    private String spocUrl;

    @Value("${fr.insee.coleman.spoc.login}")
    private String spocLogin;

    @Value("${fr.insee.coleman.spoc.pw}")
    private String spocPw;

    private static final String REGEX_MAIL = "^([a-zA-Z0-9_]+([\\.-]{1}[a-zA-Z0-9_]+)*@[a-zA-Z0-9]+([\\.-]{1}[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,})?$";

    public ResponseFromExternalService sendMail(FreeFollowUpMailDto mailFollowUp) {

        ResponseFromExternalService responseExt = new ResponseFromExternalService();

        //Verification de la validité des adresses mails
        if ( !isEmailValid(mailFollowUp.getEmail()) || !isEmailValid(mailFollowUp.getData().getMailBoiteRetour())) {
            responseExt.setStatus(HttpStatus.BAD_REQUEST);
            responseExt.setMessage("Mail invalide");
            LOGGER.warn("Mail invalide");
            return responseExt;
        }

        //Remplissage des paramètres du mail
        SendRequest sendRequest = null;
        try {
            sendRequest = getSendRequestMail(mailFollowUp);
        }
        catch (IOException e) {
            responseExt.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
            responseExt.setMessage("Modèle de mail non trouvé");
            LOGGER.warn("Modèle de mail non trouvé");
            return responseExt;
        }

        //Vérification du service d'envoi de mail
        if ( !testHealthCheckSpoc())
        {
            responseExt.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
            responseExt.setMessage("Le service d'envoi de mail ne répond pas.");
            LOGGER.warn("Le service d'envoi de mail ne répond pas");
            return responseExt;
        }

        //envoi du mail
        try {
            LOGGER.info("Envoi du mail...");
            String response = spocInstance().request().post(Entity.entity(sendRequest, MediaType.APPLICATION_XML), String.class);
            LOGGER.info("Reponse du WS d'envoi de mail : " + response);
            responseExt.setStatus(HttpStatus.OK);
            String msg = "Le mail a bien été envoyé à l'adresse : " + sendRequest.getRecipients().getRecipient().get(0).getAddress() + ".";
            responseExt.setMessage(msg);
            LOGGER.info(msg);
        }
        catch (Exception e) {
            responseExt.setStatus(HttpStatus.BAD_REQUEST);
            responseExt.setMessage("Une erreur inconnue est survenue.");
            LOGGER.error("Une erreur inconnue est survenue.");
        }

        return responseExt;

    }

    public SendRequest getSendRequestMail(FreeFollowUpMailDto mailFollowUp) throws IOException {
        LOGGER.info("Création du message...");

        MessageTemplate messageTemplate = new MessageTemplate();

        File file = new File("src/main/resources/mailTemplates/Mail_relancelibreWeb.html");
        messageTemplate.setContent(FileUtils.readFileToString(file, "UTF-8"));
        messageTemplate.setSender(mailFollowUp.getData().getMailBoiteRetour());
        messageTemplate.setSubject(mailFollowUp.getData().getMailObjet());

        Recipient destinataire = createRecipientMail(mailFollowUp);
        Recipients destinataires = new Recipients();
        destinataires.getRecipient().add(destinataire);

        // préparation de la requête à envoyer
        SendRequest sendRequest = new SendRequest();
        sendRequest.setMessageTemplate(messageTemplate);
        sendRequest.setRecipients(destinataires);
        LOGGER.info("Message créé.");

        return sendRequest;
    }

    private Recipient createRecipientMail(FreeFollowUpMailDto mailFollowUp) {
        // création du destinataire
        Recipient destinataire = new Recipient();
        destinataire.setAddress(mailFollowUp.getEmail());

        NameValuePairType property = new NameValuePairType();
        property.setName("Ue_CalcIdentifiant");
        property.setValue(mailFollowUp.getData().getUeCalcIdentifiant());
        destinataire.getProperties().add(property);

        NameValuePairType property2 = new NameValuePairType();
        property2.setName("Enq_ThemeMieuxConnaitreMail");
        property2.setValue(mailFollowUp.getData().getEnquThemeMieuxConnaitreMail());
        destinataire.getProperties().add(property2);

        NameValuePairType property3 = new NameValuePairType();
        property3.setName("Enq_ServiceCollecteurSignataireFonction");
        property3.setValue(mailFollowUp.getData().getEnqServiceCollecteurSignataireFonction());
        destinataire.getProperties().add(property3);

        NameValuePairType property4 = new NameValuePairType();
        property4.setName("Enq_ServiceCollecteurSignataireNom");
        property4.setValue(mailFollowUp.getData().getEnqServiceCollecteurSignataireNom());
        destinataire.getProperties().add(property4);

        NameValuePairType property5 = new NameValuePairType();
        property5.setName("Enq_RespTraitement");
        property5.setValue(mailFollowUp.getData().getEnqRespTraitement());
        destinataire.getProperties().add(property5);

        NameValuePairType property6 = new NameValuePairType();
        property6.setName("Enq_RespOperationnel");
        property6.setValue(mailFollowUp.getData().getEnqRespOperationnel());
        destinataire.getProperties().add(property6);

        NameValuePairType property7 = new NameValuePairType();
        property7.setName("Enq_MailRespOperationnel");
        property7.setValue(mailFollowUp.getData().getEnqMailRespOperationnel());
        destinataire.getProperties().add(property7);

        NameValuePairType property8 = new NameValuePairType();
        property8.setName("Enq_UrlEnquete");
        property8.setValue(mailFollowUp.getData().getEnqUrlEnquete());
        destinataire.getProperties().add(property8);

        NameValuePairType property9 = new NameValuePairType();
        property9.setName("Mail_Objet");
        property9.setValue(mailFollowUp.getData().getMailObjet());
        destinataire.getProperties().add(property9);

        NameValuePairType property10 = new NameValuePairType();
        property10.setName("Mail_BoiteRetour");
        property10.setValue(mailFollowUp.getData().getMailBoiteRetour());
        destinataire.getProperties().add(property10);

        NameValuePairType property11 = new NameValuePairType();
        property11.setName("Enq_LogoPrestataire");
        property11.setValue(mailFollowUp.getData().getEnqLogoPrestataire());
        destinataire.getProperties().add(property11);

        NameValuePairType property12 = new NameValuePairType();
        property12.setName("Enq_Prestataire");
        property12.setValue(mailFollowUp.getData().getEnqPrestataire());
        destinataire.getProperties().add(property12);

        NameValuePairType property13 = new NameValuePairType();
        property13.setName("Enq_RelanceLibreMailParagraphe1");
        property13.setValue(mailFollowUp.getData().getEnqRelanceLibreMailParagrapheUn());
        destinataire.getProperties().add(property13);

        NameValuePairType property14 = new NameValuePairType();
        property14.setName("Enq_RelanceLibreMailParagraphe2");
        property14.setValue(mailFollowUp.getData().getEnqRelanceLibreMailParagrapheDeux());
        destinataire.getProperties().add(property14);

        NameValuePairType property15 = new NameValuePairType();
        property15.setName("Enq_RelanceLibreMailParagraphe3");
        property15.setValue(mailFollowUp.getData().getEnqRelanceLibreMailParagrapheTrois());
        destinataire.getProperties().add(property15);

        NameValuePairType property16 = new NameValuePairType();
        property16.setName("Enq_RelanceLibreMailParagraphe4");
        property16.setValue(mailFollowUp.getData().getEnqRelanceLibreMailParagrapheQuatre());
        destinataire.getProperties().add(property16);

        NameValuePairType property17 = new NameValuePairType();
        property17.setName("Enq_ComplementConnexion");
        property17.setValue(mailFollowUp.getData().getEnqComplementConnexion());
        destinataire.getProperties().add(property17);
        return destinataire;
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile(REGEX_MAIL).matcher(email).matches();
    }

    /**
     * Appel au HealthCheck de Spoc
     * @return true or false
     */
    private boolean testHealthCheckSpoc() {
        URI uri = setUri(spocHeathcheck);
        boolean response;
        ResponseEntity<String> coucou;

        try {
            LOGGER.info("Checking SPOC...");
            coucou = restTemplate().getForEntity(uri, String.class);
            response = coucou.getStatusCode().equals(HttpStatus.OK);
        }
        catch (RestClientException e) {
            response = false;
            LOGGER.info("Service SPOC : ERROR");
            LOGGER.error(e.getMessage());
        }
        if (response) {
            LOGGER.info("Service SPOC : OK");
        }
        return response;
    }

    private URI setUri(String uriString) {
        URI uri = null;
        try {
            uri = new URI(uriString);
        }
        catch (URISyntaxException e) {
            LOGGER.error("Uri {} invalide ", uriString);
        }
        return uri;
    }

    private RestTemplate restTemplate() {
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        return new RestTemplate(requestFactory);
    }

    private WebTarget spocInstance() {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 15000); // 15 sec tiemout
        client.property(ClientProperties.READ_TIMEOUT, 15000); // 15 sec tiemout

        /* creation d'un client authentifié pour SPOC */
        HttpAuthenticationFeature authentificationFeature = HttpAuthenticationFeature.basic(spocLogin, spocPw);
        WebTarget webTarget = client.target(spocUrl).register(authentificationFeature).register(MultiPartFeature.class);

        return webTarget;

    }

}