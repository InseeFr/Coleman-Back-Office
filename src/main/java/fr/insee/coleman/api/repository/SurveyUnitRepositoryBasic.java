package fr.insee.coleman.api.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.coleman.api.domain.SurveyUnit;

public interface SurveyUnitRepositoryBasic extends JpaRepository<SurveyUnit, Long> {

	Collection<SurveyUnit> findByLastnameContainingIgnoreCaseOrFirstnameContainingIgnoreCase(String filter, String filter2);

	Collection<SurveyUnit> findByIdContactEqualsOrIdSuEquals(String filter, String filter2);

	SurveyUnit findByIdContact(String idContact);

	Page<SurveyUnit> findByIdContactContainingIgnoreCaseOrIdSuContainingIgnoreCaseOrderByIdSuAscCampaignIdAsc(String filter, String filter2,
			Pageable pageable);
	
	Collection<SurveyUnit> findByIdContactContainingIgnoreCaseOrIdSuContainingIgnoreCase(String filter, String filter2);

	SurveyUnit findByIdSu(String idSu);

	SurveyUnit findByIdSuAndCampaignId(String idSu, String campaignId);
	
	SurveyUnit findByIdContactAndCampaignId(String idContact, String campaignId);

	Collection<SurveyUnit> findByBatchNumber(int numLot);
	
}
