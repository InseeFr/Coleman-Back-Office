package fr.insee.coleman.api.repository;

import java.util.List;

import fr.insee.coleman.api.domain.ExtractionRow;

public interface SurveyUnitRepositoryCustom {

	List<ExtractionRow> getSurveyUnitToFollowUp(String idCampaign);

}