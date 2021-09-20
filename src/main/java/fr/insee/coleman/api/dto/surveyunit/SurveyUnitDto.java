package fr.insee.coleman.api.dto.surveyunit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * SurveyUnitDto
 */
public class SurveyUnitDto {
    @NotBlank
	private String idSu;
	@NotBlank
	private String idContact;
	@NotBlank
	private String lastname;
	@NotBlank
	private String firstname;
	@NotBlank
	private String address;
	@NotNull
	private int batchNumber;

	private String idCampaign;

    public SurveyUnitDto() {
		super();
	}

	public SurveyUnitDto(@NotNull String idSu, @NotNull String idContact, @NotNull String lastname, @NotNull String firstname,
			@NotNull String address, @NotNull int batchNumber, String idCampaign) {
		super();
		this.idSu = idSu;
		this.idContact = idContact;
		this.lastname = lastname;
		this.firstname = firstname;
		this.address = address;
		this.batchNumber = batchNumber;
		this.idCampaign = idCampaign;
	}

	public String getIdCampaign() {
		return idCampaign;
	}

	public void setIdCampaign(String idCampaign) {
		this.idCampaign = idCampaign;
	}

	public String getIdSu() {
		return idSu;
	}

	public void setIdSu(String idSu) {
		this.idSu = idSu;
	}

	public String getIdContact() {
		return idContact;
	}

	public void setIdContact(String idContact) {
		this.idContact = idContact;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}
    
}