package fr.insee.coleman.api.domain;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

public class Contact {
	  
	  private String identifiant;
	  private String nomCommun;
	  private String nom;
	  private String prenom;
	  private String domaineDeGestion;
	  private String adresseMessagerie;
	  private String numeroTelephone;
	  private String description;
	  private X509Certificate certificate;
	  private Object adresse;
	  private Object organisationDeRattachement;
	  private String civilite;
	  private String identifiantMetier;
	  private String repertoireDeDistribution;
	  private String telephonePortable;
	  private String facSimile;
	  private Boolean hasPassword;
	  private Set<String> propriete;
	  private Set<String> inseeRoleApplicatif;
	  private byte[] codePin;
	  private Date dateCreation;
	  private String organisation;
	  private String inseeTimbre;
	  private String inseeOrganisme;
	  private String inseeAdresseCorrespondantLigne1;
	  private String inseeAdresseCorrespondantLigne2;
	  private String inseeAdresseCorrespondantLigne3;
	  private String postalCode;
	  private String inseeNomCorrespondant;
	  private String inseeMailCorrespondant;
	  private String inseeTelephoneNumberCorrespondant;
	/**
	 * @return the identifiant
	 */
	public String getIdentifiant() {
		return identifiant;
	}
	/**
	 * @param identifiant the identifiant to set
	 */
	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}
	/**
	 * @return the nomCommun
	 */
	public String getNomCommun() {
		return nomCommun;
	}
	/**
	 * @param nomCommun the nomCommun to set
	 */
	public void setNomCommun(String nomCommun) {
		this.nomCommun = nomCommun;
	}
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}
	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	/**
	 * @return the domaineDeGestion
	 */
	public String getDomaineDeGestion() {
		return domaineDeGestion;
	}
	/**
	 * @param domaineDeGestion the domaineDeGestion to set
	 */
	public void setDomaineDeGestion(String domaineDeGestion) {
		this.domaineDeGestion = domaineDeGestion;
	}
	/**
	 * @return the adresseMessagerie
	 */
	public String getAdresseMessagerie() {
		return adresseMessagerie;
	}
	/**
	 * @param adresseMessagerie the adresseMessagerie to set
	 */
	public void setAdresseMessagerie(String adresseMessagerie) {
		this.adresseMessagerie = adresseMessagerie;
	}
	/**
	 * @return the numeroTelephone
	 */
	public String getNumeroTelephone() {
		return numeroTelephone;
	}
	/**
	 * @param numeroTelephone the numeroTelephone to set
	 */
	public void setNumeroTelephone(String numeroTelephone) {
		this.numeroTelephone = numeroTelephone;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the certificate
	 */
	public X509Certificate getCertificate() {
		return certificate;
	}
	/**
	 * @param certificate the certificate to set
	 */
	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}
	/**
	 * @return the adresse
	 */
	public Object getAdresse() {
		return adresse;
	}
	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(Object adresse) {
		this.adresse = adresse;
	}
	/**
	 * @return the organisationDeRattachement
	 */
	public Object getOrganisationDeRattachement() {
		return organisationDeRattachement;
	}
	/**
	 * @param organisationDeRattachement the organisationDeRattachement to set
	 */
	public void setOrganisationDeRattachement(Object organisationDeRattachement) {
		this.organisationDeRattachement = organisationDeRattachement;
	}
	/**
	 * @return the civilite
	 */
	public String getCivilite() {
		return civilite;
	}
	/**
	 * @param civilite the civilite to set
	 */
	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}
	/**
	 * @return the identifiantMetier
	 */
	public String getIdentifiantMetier() {
		return identifiantMetier;
	}
	/**
	 * @param identifiantMetier the identifiantMetier to set
	 */
	public void setIdentifiantMetier(String identifiantMetier) {
		this.identifiantMetier = identifiantMetier;
	}
	/**
	 * @return the repertoireDeDistribution
	 */
	public String getRepertoireDeDistribution() {
		return repertoireDeDistribution;
	}
	/**
	 * @param repertoireDeDistribution the repertoireDeDistribution to set
	 */
	public void setRepertoireDeDistribution(String repertoireDeDistribution) {
		this.repertoireDeDistribution = repertoireDeDistribution;
	}
	/**
	 * @return the telephonePortable
	 */
	public String getTelephonePortable() {
		return telephonePortable;
	}
	/**
	 * @param telephonePortable the telephonePortable to set
	 */
	public void setTelephonePortable(String telephonePortable) {
		this.telephonePortable = telephonePortable;
	}
	/**
	 * @return the facSimile
	 */
	public String getFacSimile() {
		return facSimile;
	}
	/**
	 * @param facSimile the facSimile to set
	 */
	public void setFacSimile(String facSimile) {
		this.facSimile = facSimile;
	}
	/**
	 * @return the hasPassword
	 */
	public Boolean getHasPassword() {
		return hasPassword;
	}
	/**
	 * @param hasPassword the hasPassword to set
	 */
	public void setHasPassword(Boolean hasPassword) {
		this.hasPassword = hasPassword;
	}
	/**
	 * @return the propriete
	 */
	public Set<String> getPropriete() {
		return propriete;
	}
	/**
	 * @param propriete the propriete to set
	 */
	public void setPropriete(Set<String> propriete) {
		this.propriete = propriete;
	}
	/**
	 * @return the inseeRoleApplicatif
	 */
	public Set<String> getInseeRoleApplicatif() {
		return inseeRoleApplicatif;
	}
	/**
	 * @param inseeRoleApplicatif the inseeRoleApplicatif to set
	 */
	public void setInseeRoleApplicatif(Set<String> inseeRoleApplicatif) {
		this.inseeRoleApplicatif = inseeRoleApplicatif;
	}
	/**
	 * @return the codePin
	 */
	public byte[] getCodePin() {
		return codePin;
	}
	/**
	 * @param codePin the codePin to set
	 */
	public void setCodePin(byte[] codePin) {
		this.codePin = codePin;
	}
	/**
	 * @return the dateCreation
	 */
	public Date getDateCreation() {
		return dateCreation;
	}
	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	/**
	 * @return the organisation
	 */
	public String getOrganisation() {
		return organisation;
	}
	/**
	 * @param organisation the organisation to set
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	/**
	 * @return the inseeTimbre
	 */
	public String getInseeTimbre() {
		return inseeTimbre;
	}
	/**
	 * @param inseeTimbre the inseeTimbre to set
	 */
	public void setInseeTimbre(String inseeTimbre) {
		this.inseeTimbre = inseeTimbre;
	}
	/**
	 * @return the inseeOrganisme
	 */
	public String getInseeOrganisme() {
		return inseeOrganisme;
	}
	/**
	 * @param inseeOrganisme the inseeOrganisme to set
	 */
	public void setInseeOrganisme(String inseeOrganisme) {
		this.inseeOrganisme = inseeOrganisme;
	}
	/**
	 * @return the inseeAdresseCorrespondantLigne1
	 */
	public String getInseeAdresseCorrespondantLigne1() {
		return inseeAdresseCorrespondantLigne1;
	}
	/**
	 * @param inseeAdresseCorrespondantLigne1 the inseeAdresseCorrespondantLigne1 to set
	 */
	public void setInseeAdresseCorrespondantLigne1(String inseeAdresseCorrespondantLigne1) {
		this.inseeAdresseCorrespondantLigne1 = inseeAdresseCorrespondantLigne1;
	}
	/**
	 * @return the inseeAdresseCorrespondantLigne2
	 */
	public String getInseeAdresseCorrespondantLigne2() {
		return inseeAdresseCorrespondantLigne2;
	}
	/**
	 * @param inseeAdresseCorrespondantLigne2 the inseeAdresseCorrespondantLigne2 to set
	 */
	public void setInseeAdresseCorrespondantLigne2(String inseeAdresseCorrespondantLigne2) {
		this.inseeAdresseCorrespondantLigne2 = inseeAdresseCorrespondantLigne2;
	}
	/**
	 * @return the inseeAdresseCorrespondantLigne3
	 */
	public String getInseeAdresseCorrespondantLigne3() {
		return inseeAdresseCorrespondantLigne3;
	}
	/**
	 * @param inseeAdresseCorrespondantLigne3 the inseeAdresseCorrespondantLigne3 to set
	 */
	public void setInseeAdresseCorrespondantLigne3(String inseeAdresseCorrespondantLigne3) {
		this.inseeAdresseCorrespondantLigne3 = inseeAdresseCorrespondantLigne3;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	/**
	 * @return the inseeNomCorrespondant
	 */
	public String getInseeNomCorrespondant() {
		return inseeNomCorrespondant;
	}
	/**
	 * @param inseeNomCorrespondant the inseeNomCorrespondant to set
	 */
	public void setInseeNomCorrespondant(String inseeNomCorrespondant) {
		this.inseeNomCorrespondant = inseeNomCorrespondant;
	}
	/**
	 * @return the inseeMailCorrespondant
	 */
	public String getInseeMailCorrespondant() {
		return inseeMailCorrespondant;
	}
	/**
	 * @param inseeMailCorrespondant the inseeMailCorrespondant to set
	 */
	public void setInseeMailCorrespondant(String inseeMailCorrespondant) {
		this.inseeMailCorrespondant = inseeMailCorrespondant;
	}
	/**
	 * @return the inseeTelephoneNumberCorrespondant
	 */
	public String getInseeTelephoneNumberCorrespondant() {
		return inseeTelephoneNumberCorrespondant;
	}
	/**
	 * @param inseeTelephoneNumberCorrespondant the inseeTelephoneNumberCorrespondant to set
	 */
	public void setInseeTelephoneNumberCorrespondant(String inseeTelephoneNumberCorrespondant) {
		this.inseeTelephoneNumberCorrespondant = inseeTelephoneNumberCorrespondant;
	}
	  
	  
}
