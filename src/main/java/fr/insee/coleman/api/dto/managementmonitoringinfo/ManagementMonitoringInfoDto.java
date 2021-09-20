package fr.insee.coleman.api.dto.managementmonitoringinfo;

import fr.insee.coleman.api.domain.TypeManagementMonitoringInfo;

/**
 * ManagementMonitoringInfoDto
 */
public class ManagementMonitoringInfoDto {

    private String idSu;
	private String idContact;
	private String date;
	private TypeManagementMonitoringInfo status;

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

	public TypeManagementMonitoringInfo getStatus() {
		return status;
	}

	public void setStatus(TypeManagementMonitoringInfo status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}