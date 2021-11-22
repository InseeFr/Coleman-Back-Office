package fr.insee.coleman.api.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.coleman.api.domain.ExtractionRow;
import fr.insee.coleman.api.domain.SurveyUnit;

public interface SurveyUnitRepository extends SurveyUnitRepositoryBasic, SurveyUnitRepositoryCustom {

	Collection<SurveyUnit> findByLastnameContainingIgnoreCaseOrFirstnameContainingIgnoreCase(String filter, String filter2);

	Collection<SurveyUnit> findByIdContactEqualsOrIdSuEquals(String filter, String filter2);

	SurveyUnit findByIdContact(String idContact);

	Page<SurveyUnit> findByIdContactContainingIgnoreCaseOrIdSuContainingIgnoreCaseOrderByIdSuAscCampaignIdAsc(
			String filter, String filter2, Pageable pageable);

	SurveyUnit findByIdSu(String idSu);

	SurveyUnit findByIdSuAndCampaignId(String idSu, String campaignId);

	SurveyUnit findByIdContactAndCampaignId(String idContact, String campaignId);

	SurveyUnit findByIdSuAndIdContactAndCampaignId(String idSu, String idContact, String campaignId);

	Collection<SurveyUnit> findByBatchNumber(int numLot);

	List<ExtractionRow> getSurveyUnitToFollowUp(String idCampaign);

	Page<SurveyUnit> findByIdContactContainingIgnoreCaseOrderByIdContactAscCampaignIdAsc(String filter,
			Pageable pageable);
	
	Collection<SurveyUnit> findByIdContactContainingIgnoreCaseOrderByIdContactAscCampaignIdAsc(String idec);

	List<SurveyUnit> findByCampaignId(String idCampaign);

}
