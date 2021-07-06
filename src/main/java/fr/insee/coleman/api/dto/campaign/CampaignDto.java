package fr.insee.coleman.api.dto.campaign;

import javax.validation.constraints.NotNull;

/**
 * CampaignDto
 */
public class CampaignDto {
    @NotNull
	private String label;

	@NotNull
	private Long collectionStartDate;

	@NotNull
	private Long collectionEndDate;

    public CampaignDto() {
		super();
	}

	public CampaignDto(@NotNull String label, @NotNull Long collectionStartDate, @NotNull Long collectionEndDate) {
		super();
		this.label = label;
		this.collectionStartDate = collectionStartDate;
		this.collectionEndDate = collectionEndDate;
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