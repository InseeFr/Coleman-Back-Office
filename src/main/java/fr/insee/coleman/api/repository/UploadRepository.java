package fr.insee.coleman.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.coleman.api.domain.Upload;

public interface UploadRepository extends JpaRepository<Upload, Long> {

    Collection<Upload> findByManagementMonitoringInfosIsEmpty();

}
