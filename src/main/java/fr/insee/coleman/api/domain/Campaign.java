package fr.insee.coleman.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * Campaign
 */
@Entity
@Table (name = "campaign")
@Proxy(lazy = false)
public class Campaign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3105797031529684159L;

	@Id
	@Column (name = "id", updatable = false, nullable = false)	
	private String id;

	@Column	
	private String label;

	@Column
	private Long collectionStartDate;

	@Column
	private Long collectionEndDate;

	public Campaign() {
		super();
	}
	
	public Campaign(Campaign campaign) {
		super();
		this.id = campaign.getId();
		this.label = campaign.getLabel();
		this.collectionStartDate = campaign.getCollectionStartDate();
		this.collectionEndDate = campaign.getCollectionEndDate();
	}

	public Campaign(String id, String label, Long collectionStartDate, Long collectionEndDate) {
		super();
		this.id = id;
		this.label = label;
		this.collectionStartDate = collectionStartDate;
		this.collectionEndDate = collectionEndDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getCollectionStartDate() {
		return collectionStartDate;
	}

	public void setCollectionStartDate(Long collectionStartDate) {
		this.collectionStartDate = collectionStartDate;
	}

	public Long getCollectionEndDate() {
		return collectionEndDate;
	}

	public void setCollectionEndDate(Long collectionEndDate) {
		this.collectionEndDate = collectionEndDate;
	}
}