package fr.insee.coleman.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.domain.SurveyUnit;

@Repository
public interface ManagementMonitoringInfoRepository extends JpaRepository<ManagementMonitoringInfo, Long> {

	Collection<ManagementMonitoringInfo> findBySurveyUnit(SurveyUnit findOne);

	ManagementMonitoringInfo findByIdManagementMonitoringInfo(long id);

}