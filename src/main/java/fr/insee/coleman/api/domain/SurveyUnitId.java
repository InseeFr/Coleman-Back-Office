package fr.insee.coleman.api.domain;

import java.io.Serializable;


public class SurveyUnitId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2522745228031000697L;
	
	private String idSu;
	private String campaign;
	private String idContact;
	
	public SurveyUnitId() {
	}

	public SurveyUnitId(String idSu, String campaign, String idContact) {
		super();
		this.idSu = idSu;
		this.campaign = campaign;
		this.idContact = idContact;
	}

	public String getIdSu() {
		return idSu;
	}

	public void setIdSu(String idSu) {
		this.idSu = idSu;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getIdContact() {
		return idContact;
	}

	public void setIdContact(String idContact) {
		this.idContact = idContact;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campaign == null) ? 0 : campaign.hashCode());
		result = prime * result + ((idContact == null) ? 0 : idContact.hashCode());
		result = prime * result + ((idSu == null) ? 0 : idSu.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SurveyUnitId other = (SurveyUnitId) obj;
		if (campaign == null) {
			if (other.campaign != null)
				return false;
		} else if (!campaign.equals(other.campaign))
			return false;
		if (idContact == null) {
			if (other.idContact != null)
				return false;
		} else if (!idContact.equals(other.idContact))
			return false;
		if (idSu == null) {
			if (other.idSu != null)
				return false;
		} else if (!idSu.equals(other.idSu))
			return false;
		return true;
	}
}
