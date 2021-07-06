package fr.insee.coleman.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import fr.insee.coleman.api.domain.Campaign;

@Service
public interface CampaignRepository extends JpaRepository<Campaign, String> {

	List<Campaign> findAll();

}