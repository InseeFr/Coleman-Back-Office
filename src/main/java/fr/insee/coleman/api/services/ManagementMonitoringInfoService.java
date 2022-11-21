package fr.insee.coleman.api.services;

import java.util.*;
import java.util.stream.Collectors;

import fr.insee.coleman.api.utils.StatusOrderComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.coleman.api.domain.ResultUpload;
import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.domain.SurveyUnit;
import fr.insee.coleman.api.domain.TypeManagementMonitoringInfo;
import fr.insee.coleman.api.domain.Upload;
import fr.insee.coleman.api.dto.managementmonitoringinfo.ManagementMonitoringInfoDto;
import fr.insee.coleman.api.exception.DuplicateResourceException;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.exception.RessourceNotValidatedException;
import fr.insee.coleman.api.repository.ManagementMonitoringInfoRepository;
import fr.insee.coleman.api.utils.DateImportConverter;

@Service
@Transactional
public class ManagementMonitoringInfoService {

    @Autowired
	SurveyUnitService surveyUnitService;

	@Autowired
	ManagementMonitoringInfoRepository managementMonitoringInfoRepository;

	@Autowired
	StatusOrderComparator statusOrderComparator;

	public ManagementMonitoringInfo saveIhm(String idCampaign, ManagementMonitoringInfoDto managementMonitoringInfoDto, Upload up) {
		SurveyUnit su = null;
		if (managementMonitoringInfoDto.getIdSu() != null) {
			su = surveyUnitService.findByIdSurveyUnitAndIdCampaign(managementMonitoringInfoDto.getIdSu(), idCampaign);
		}
		if (managementMonitoringInfoDto.getIdContact() != null) {
			su = surveyUnitService.findByIdContactAndIdCampaign(managementMonitoringInfoDto.getIdContact(), idCampaign);
		}
		if (su == null) {
			throw new RessourceNotFoundException("survey unit",
					(managementMonitoringInfoDto.getIdSu() != null) ? managementMonitoringInfoDto.getIdSu() : managementMonitoringInfoDto.getIdContact());
		}
		ManagementMonitoringInfo mm = new ManagementMonitoringInfo(null, su, managementMonitoringInfoDto.getStatus(), up.getDate(), up);
		return saveAndFlush(mm);
	}

	public ManagementMonitoringInfo saveBatch(String idCampaign, ManagementMonitoringInfoDto infoDto)
			throws RessourceNotFoundException, DuplicateResourceException, RessourceNotValidatedException {

		SurveyUnit su = null;

		// if no date in isgDto date is set to current date
		Date date = new Date();
		if (infoDto.getDate() != null) {
			date = DateImportConverter.convertToDate(infoDto.getDate());
		}
		// find ue by idue or idcontact
		if (infoDto.getIdSu() != null) {
			su = surveyUnitService.findByIdSurveyUnitAndIdCampaign(infoDto.getIdSu(), idCampaign);
		}
		if (infoDto.getIdContact() != null) {
			su = surveyUnitService.findByIdContactAndIdCampaign(infoDto.getIdContact(), idCampaign);
		}
		// if no ue with given idcontact or idue has been found throw not found
		// exception
		if (su == null) {
			throw new RessourceNotFoundException("survey unit",
					(infoDto.getIdSu() != null) ? infoDto.getIdSu() : infoDto.getIdContact());
		}

		ManagementMonitoringInfo mm;

		// check if isg with given status = VALINT or PARTIELINT or FOLLOWUP
		boolean isValidStatus = infoDto.getStatus().equals(TypeManagementMonitoringInfo.PARTIELINT)
				|| infoDto.getStatus().equals(TypeManagementMonitoringInfo.VALINT)
				|| infoDto.getStatus().equals(TypeManagementMonitoringInfo.FOLLOWUP);

		if (!isValidStatus) {
			throw new RessourceNotValidatedException("survey unit",
					(infoDto.getIdSu() != null) ? infoDto.getIdSu() : infoDto.getIdContact());
		}
		// check if status already present, except for FOLLOWUP
		if (!infoDto.getStatus().equals(TypeManagementMonitoringInfo.FOLLOWUP)) {

			for (ManagementMonitoringInfo is : su.getManagementMonitoringInfos()) {
				if (is.getStatus().equals(infoDto.getStatus())) {
					throw new DuplicateResourceException("survey unit",
							(infoDto.getIdSu() != null) ? infoDto.getIdSu() : infoDto.getIdContact());
				}
			}
		}

		mm = new ManagementMonitoringInfo(null, su, infoDto.getStatus(), date.getTime(), null);

		return saveAndFlush(mm);
	}

	public ResultUpload saveManagementMonitoringInfoInMassProcessingBatch(String idCampaign, List<ManagementMonitoringInfoDto> request) {

		ResultUpload result = new ResultUpload();

		for (ManagementMonitoringInfoDto mm : request) {
			String id = mm.getIdSu() != null ? mm.getIdSu() : mm.getIdContact();
			try {
				saveBatch(idCampaign, mm);
				result.addIdOk(id);

			} catch (DuplicateResourceException e) {
				result.addIdOk(id);

			} catch (RessourceNotFoundException e) {
				result.addIdKo(id, "RessourceNotFound");

			} catch (RessourceNotValidatedException e) {
				result.addIdKo(id, "RessourceNoneValide");
			}
		}

		return result;
	}

	public boolean checkManagementMonitoringInfoIsAssociatedToCampaign(String idCampaign, ManagementMonitoringInfoDto mm) {
		return surveyUnitService.findByIdSurveyUnitAndIdCampaign(mm.getIdSu(), idCampaign) != null
				|| surveyUnitService.findByIdContactAndIdCampaign(mm.getIdContact(), idCampaign) != null;
	}

	public ManagementMonitoringInfo getMonitoringInfoById(long id) {
		return managementMonitoringInfoRepository.findByIdManagementMonitoringInfo(id);
	}
	
	public Collection<ManagementMonitoringInfo> findBySurveyUnit(SurveyUnit findOne) {
		return managementMonitoringInfoRepository.findBySurveyUnit(findOne);
	}

	public void delete(ManagementMonitoringInfo isg) {
		managementMonitoringInfoRepository.delete(isg);
	}

	public void deleteAll(List<ManagementMonitoringInfo> managementMonitoringInfo) {
		managementMonitoringInfoRepository.deleteAll(managementMonitoringInfo);
	}

	public ManagementMonitoringInfo saveAndFlush(ManagementMonitoringInfo mm) {
		return managementMonitoringInfoRepository.saveAndFlush(mm);
	}

	public Optional<TypeManagementMonitoringInfo> getState(SurveyUnit su) {
		Collection<ManagementMonitoringInfo> mmis = managementMonitoringInfoRepository.findBySurveyUnit(su);
		List<TypeManagementMonitoringInfo> mmiStatusList = mmis.stream().map(ManagementMonitoringInfo::getStatus).collect(Collectors.toList());
		Collections.sort(mmiStatusList, statusOrderComparator);
		return  mmiStatusList.stream().findFirst();
	}
}
