package fr.insee.coleman.api.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.coleman.api.domain.ResultUpload;
import fr.insee.coleman.api.domain.Campaign;
import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.domain.Upload;
import fr.insee.coleman.api.dto.managementmonitoringinfo.ManagementMonitoringInfoDto;
import fr.insee.coleman.api.dto.upload.UploadDto;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;
import fr.insee.coleman.api.repository.UploadRepository;


@Service
@Transactional
public class UploadService {
	
	static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

	@Autowired
	UploadRepository uploadRepository;
	@Autowired
	ManagementMonitoringInfoService managementMonitoringInfoService;
	@Autowired
	SurveyUnitService surveyUnitService;
	@Autowired
	CampaignService campaignService;

	public ResultUpload save(String idCampaign, UploadDto uploadDto) throws RessourceNotValidatedException {

		ResultUpload result = new ResultUpload();
		long timestamp = new Date().getTime();

		// Check campaign exists and date in intervals
		if (!checkUploadDate(idCampaign, timestamp))
			throw new RessourceNotValidatedException("Campaign", idCampaign);

		// Creating and saving the upload to get the id
		Upload up = new Upload(null, timestamp, null);
		up = saveAndFlush(up);
		// Creation of managementMonitoringInfo list and saving of link with upload
		List<ManagementMonitoringInfo> liste = new ArrayList<>();

		for (ManagementMonitoringInfoDto mmDto : uploadDto.getData()) {
			String identifier = (mmDto.getIdSu() != null) ? mmDto.getIdSu() : mmDto.getIdContact();
			try {
				liste.add(managementMonitoringInfoService.saveIhm(idCampaign, mmDto, up));
				result.ajouterIdOk(identifier);
			} catch (RessourceNotFoundException rnt) {
				LOGGER.error("Error in request");
				LOGGER.info("Info: id KO " + rnt.getMessage());
				result.ajouterIdKo(identifier, "RessourceNotFound");
			}
		}
		up.setManagementMonitoringInfos(liste);
		up = saveAndFlush(up);

		return result;
	}

	public boolean deleteUpload(Long id) {
		Optional<Upload> upOpt = findById(id);
		if(!upOpt.isPresent()) {
			return false;
		}
		Upload up = upOpt.get(); 
		managementMonitoringInfoService.deleteAll(up.getManagementMonitoringInfos());
		delete(up);
		return true;
	}

	public Optional<Upload> findById(long id) {
		return uploadRepository.findById(id);
	}

	public List<Upload> findAllByIdCampaign(String idCampaign) {
		// Keeps the uploads which first ISQ belongs to the survey
		return uploadRepository.findAll().stream().filter(upload -> !upload.getManagementMonitoringInfos().isEmpty())
				.filter(upload -> upload.getManagementMonitoringInfos().stream().findFirst().get().getSurveyUnit()
						.getCampaign().getId().equals(idCampaign))
				.collect(Collectors.toList());
	}

	public Upload saveAndFlush(Upload up) {
		return uploadRepository.saveAndFlush(up);
	}

	public void delete(Upload up) {
		uploadRepository.delete(up);
	}
	// Checks that the ISG of an upload belong to the same campaign
	public boolean checkUploadIsValid(String idCampaign, UploadDto upload) {
		List<ManagementMonitoringInfoDto> mmDtos = upload.getData();
		for (ManagementMonitoringInfoDto mmDto : mmDtos) {
			if (!managementMonitoringInfoService.checkManagementMonitoringInfoIsAssociatedToCampaign(idCampaign, mmDto)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifie que l'upload se déroule pendant la campaign
	 * 
	 * @param idCampaign
	 * @param timestamp
	 * @return boolean
	 */

	private boolean checkUploadDate(String idCampaign, long timestamp) {
		Campaign campaign = campaignService.findById(idCampaign);
		Long start = campaign.getCollectionStartDate();
		Long end = campaign.getCollectionEndDate();
		return (start < timestamp && timestamp < end);
	}

	public void removeEmptyUploads() {
		uploadRepository.findByManagementMonitoringInfosIsEmpty().forEach(u -> uploadRepository.delete(u));
	}
}
