package fr.insee.coleman.api.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "survey_unit")
@IdClass(SurveyUnitId.class)
@JsonRootName(value = "surveyUnit")
public class SurveyUnit {

	@Id
	@Column
	@JsonProperty("idSu")
	private String idSu;

	@JsonBackReference
	@OneToMany(mappedBy = "surveyUnit")
	private List<ManagementMonitoringInfo> managementMonitoringInfos;

	@Id
	@Column
	private String idContact;

	@Column
	private String lastname;

	@Column
	private String firstname;

	@Column
	private String address;

	@Column(name = "batchNum")
	private int batchNumber;

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "idCampaign", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonProperty(access = Access.READ_ONLY)
	private Campaign campaign;

	public SurveyUnit() {
		super();
	}

	public SurveyUnit(String idSu, List<ManagementMonitoringInfo> managementMonitoringInfos, String idContact,
			String lastname, String firstname, String address, int batchNumber, Campaign campaign) {
		super();
		this.idSu = idSu;
		this.managementMonitoringInfos = managementMonitoringInfos;
		this.idContact = idContact;
		this.lastname = lastname;
		this.firstname = firstname;
		this.address = address;
		this.batchNumber = batchNumber;
		this.campaign = campaign;
	}

	public boolean equals(Object objetctToCompare) {
		// verifying type and nullity
		if (objetctToCompare == null || objetctToCompare.getClass() != SurveyUnit.class) {
			return false;
		}
		SurveyUnit suToCompare = (SurveyUnit) objetctToCompare;

		return (this.idSu.equals(suToCompare.getIdSu()));
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + ((idSu != null) ? idSu.hashCode() : (int) Math.random());
		hash = 31 * hash + idContact.hashCode();
		return hash;

	}

	public String toString() {
		return "idSu: " + idSu + ", idContact: " + idContact + ", lastname: " + lastname + ", firstname: " + firstname + ", address: "
				+ address + ", batch number: " + batchNumber;
	}

	public String getIdSu() {
		return idSu;
	}

	public void setIdSu(String idSu) {
		this.idSu = idSu;
	}

	public List<ManagementMonitoringInfo> getManagementMonitoringInfos() {
		return managementMonitoringInfos;
	}

	public void setManagementMonitoringInfos(List<ManagementMonitoringInfo> managementMonitoringInfos) {
		this.managementMonitoringInfos = managementMonitoringInfos;
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

	public String getFirstName() {
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

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
}
