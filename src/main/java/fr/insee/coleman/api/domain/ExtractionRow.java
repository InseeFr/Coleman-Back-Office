package fr.insee.coleman.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtractionRow {

	private String status;
	private Long dateInfo;
	private String idSu;
	private String idContact;
	private String lastname;
	private String firstname;
	private String address;
	private int batchNumber;
	private int pnd;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getDateInfo() {
		return dateInfo;
	}

	public void setDateInfo(Long dateInfo) {
		this.dateInfo = dateInfo;
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

	public int getPnd() {
		return pnd;
	}

	public void setPnd(int pnd) {
		this.pnd = pnd;
	}

}
