package fr.insee.coleman.api.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataFreeFollowUpMailDto {

    @JsonProperty("Ue_CalcIdentifiant")
    private String ueCalcIdentifiant;

    @JsonProperty("Enq_ThemeMieuxConnaitreMail")
    private String enquThemeMieuxConnaitreMail;

    @JsonProperty("Enq_ServiceCollecteurSignataireFonction")
    private String enqServiceCollecteurSignataireFonction;

    @JsonProperty("Enq_ServiceCollecteurSignataireNom")
    private String enqServiceCollecteurSignataireNom;

    @JsonProperty("Enq_RespTraitement")
    private String enqRespTraitement;

    @JsonProperty("Enq_RespOperationnel")
    private String enqRespOperationnel;

    @JsonProperty("Enq_MailRespOperationnel")
    private String enqMailRespOperationnel;

    @JsonProperty("Enq_UrlEnquete")
    private String enqUrlEnquete;

    @JsonProperty("Mail_Objet")
    private String mailObjet;

    @JsonProperty("Mail_BoiteRetour")
    private String mailBoiteRetour;

    @JsonProperty("Enq_LogoPrestataire")
    private String enqLogoPrestataire;

    @JsonProperty("Enq_Prestataire")
    private String enqPrestataire;

    @JsonProperty("Enq_RelanceLibreMailParagraphe1")
    private String enqRelanceLibreMailParagrapheUn;

    @JsonProperty("Enq_RelanceLibreMailParagraphe2")
    private String enqRelanceLibreMailParagrapheDeux;

    @JsonProperty("Enq_RelanceLibreMailParagraphe3")
    private String enqRelanceLibreMailParagrapheTrois;

    @JsonProperty("Enq_RelanceLibreMailParagraphe4")
    private String enqRelanceLibreMailParagrapheQuatre;

    @JsonProperty("Enq_ComplementConnexion")
    private String enqComplementConnexion;
   
    @JsonProperty("Ue_CalcIdentifiant")
    public String getUeCalcIdentifiant() {
        return ueCalcIdentifiant;
    }

    public void setUeCalcIdentifiant(String ueCalcIdentifiant) {
        this.ueCalcIdentifiant = ueCalcIdentifiant;
    }

    public String getEnquThemeMieuxConnaitreMail() {
        return enquThemeMieuxConnaitreMail;
    }

    public void setEnquThemeMieuxConnaitreMail(String enquThemeMieuxConnaitreMail) {
        this.enquThemeMieuxConnaitreMail = enquThemeMieuxConnaitreMail;
    }

    public String getEnqServiceCollecteurSignataireFonction() {
        return enqServiceCollecteurSignataireFonction;
    }

    public void setEnqServiceCollecteurSignataireFonction(String enqServiceCollecteurSignataireFonctionl) {
        this.enqServiceCollecteurSignataireFonction = enqServiceCollecteurSignataireFonctionl;
    }

    public String getEnqServiceCollecteurSignataireNom() {
        return enqServiceCollecteurSignataireNom;
    }

    public void setEnqServiceCollecteurSignataireNom(String enqServiceCollecteurSignataireNom) {
        this.enqServiceCollecteurSignataireNom = enqServiceCollecteurSignataireNom;
    }

    public String getEnqRespTraitement() {
        return enqRespTraitement;
    }

    public void setEnqRespTraitement(String enqRespTraitement) {
        this.enqRespTraitement = enqRespTraitement;
    }

    public String getEnqRespOperationnel() {
        return enqRespOperationnel;
    }

    public void setEnqRespOperationnel(String enqRespOperationnel) {
        this.enqRespOperationnel = enqRespOperationnel;
    }

    public String getEnqMailRespOperationnel() {
        return enqMailRespOperationnel;
    }

    public void setEnqMailRespOperationnel(String enqMailRespOperationnel) {
        this.enqMailRespOperationnel = enqMailRespOperationnel;
    }

    public String getEnqUrlEnquete() {
        return enqUrlEnquete;
    }

    public void setEnqUrlEnquete(String enqUrlEnquete) {
        this.enqUrlEnquete = enqUrlEnquete;
    }

    public String getMailObjet() {
        return mailObjet;
    }

    public void setMailObjet(String mailObjet) {
        this.mailObjet = mailObjet;
    }

    public String getMailBoiteRetour() {
        return mailBoiteRetour;
    }

    public void setMailBoiteRetour(String mailBoiteRetour) {
        this.mailBoiteRetour = mailBoiteRetour;
    }

    public String getEnqLogoPrestataire() {
        return enqLogoPrestataire;
    }

    public void setEnqLogoPrestataire(String enqLogoPrestataire) {
        this.enqLogoPrestataire = enqLogoPrestataire;
    }

    public String getEnqPrestataire() {
        return enqPrestataire;
    }

    public void setEnqPrestataire(String enqPrestataire) {
        this.enqPrestataire = enqPrestataire;
    }

    public String getEnqRelanceLibreMailParagrapheUn() {
        return enqRelanceLibreMailParagrapheUn;
    }

    public void setEnqRelanceLibreMailParagrapheUn(String enqRelanceLibreMailParagrapheUn) {
        this.enqRelanceLibreMailParagrapheUn = enqRelanceLibreMailParagrapheUn;
    }

    public String getEnqRelanceLibreMailParagrapheDeux() {
        return enqRelanceLibreMailParagrapheDeux;
    }

    public void setEnqRelanceLibreMailParagrapheDeux(String enqRelanceLibreMailParagrapheDeux) {
        this.enqRelanceLibreMailParagrapheDeux = enqRelanceLibreMailParagrapheDeux;
    }

    public String getEnqRelanceLibreMailParagrapheTrois() {
        return enqRelanceLibreMailParagrapheTrois;
    }

    public void setEnqRelanceLibreMailParagrapheTrois(String enqRelanceLibreMailParagrapheTrois) {
        this.enqRelanceLibreMailParagrapheTrois = enqRelanceLibreMailParagrapheTrois;
    }

    public String getEnqRelanceLibreMailParagrapheQuatre() {
        return enqRelanceLibreMailParagrapheQuatre;
    }

    public void setEnqRelanceLibreMailParagrapheQuatre(String enqRelanceLibreMailParagrapheQuatre) {
        this.enqRelanceLibreMailParagrapheQuatre = enqRelanceLibreMailParagrapheQuatre;
    }

    public String getEnqComplementConnexion() {
        return enqComplementConnexion;
    }

    public void setEnqComplementConnexion(String enqComplementConnexion) {
        this.enqComplementConnexion = enqComplementConnexion;
    }
    
    
}
