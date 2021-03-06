package fr.insee.coleman.api.services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.coleman.api.exception.DuplicateResourceException;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.domain.ResultUpload;
import fr.insee.coleman.api.domain.Campaign;
import fr.insee.coleman.api.domain.ExtractionRow;
import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.domain.TypeManagementMonitoringInfo;
import fr.insee.coleman.api.domain.SurveyUnit;
import fr.insee.coleman.api.dto.surveyunit.SurveyUnitDto;
import fr.insee.coleman.api.repository.CampaignRepository;
import fr.insee.coleman.api.repository.SurveyUnitRepository;

@Service
public class SurveyUnitService {

    @Autowired
    ManagementMonitoringInfoService managementMonitoringInfoService;

    @Autowired
    CampaignService campaignService;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    SurveyUnitRepository surveyUnitRepository;

    public ResultUpload initializeSurveyUnits(String idCampaign, List<SurveyUnitDto> sus) {
        // check and retrieval of the campaign
        Campaign campaign = campaignRepository.findById(idCampaign)
                .orElseThrow(() -> new RessourceNotFoundException("Campaign", idCampaign));
        ResultUpload result = new ResultUpload();
        for (SurveyUnitDto su : sus) {
            try {
                intitializeSurveyUnits(campaign, su);
                result.addIdOk(su.getIdSu());
            } catch (DuplicateResourceException e) {
                result.addIdKo(su.getIdSu(), "RessourceInDouble");
            }
        }
        return result;
    }

    public Collection<SurveyUnit> findMultipleByIdContact(String idec) {
        return surveyUnitRepository.findByIdContactContainingIgnoreCaseOrderByIdContactAscCampaignIdAsc(idec);
    }

    private void intitializeSurveyUnits(Campaign campaign, SurveyUnitDto su) {
        String idCampaign = campaign.getId();
        if (surveyUnitRepository.findByIdContactAndCampaignId(su.getIdContact(), idCampaign) != null
                || surveyUnitRepository.findByIdSuAndCampaignId(su.getIdSu(), idCampaign) != null)
            throw new DuplicateResourceException("survey unit", su.getIdContact() + " " + su.getIdSu());
        SurveyUnit suSearched = surveyUnitRepository.saveAndFlush(new SurveyUnit(su.getIdSu(), null, su.getIdContact(),
                su.getLastname(), su.getFirstname(), su.getAddress(), su.getBatchNumber(), campaign));
        Date date = new Date();
        managementMonitoringInfoService.saveAndFlush(new ManagementMonitoringInfo(null, suSearched, TypeManagementMonitoringInfo.INITLA, date.getTime(), null));
    }

    public Collection<ExtractionRow> getSurveyUnitsToFollowUp(String idCampaign) {
        return surveyUnitRepository.getSurveyUnitToFollowUp(idCampaign);
    }

    public TypeManagementMonitoringInfo determineState(SurveyUnit su) {
        HashSet<TypeManagementMonitoringInfo> states = new HashSet<>();
        for (ManagementMonitoringInfo isg : managementMonitoringInfoService.findBySurveyUnit(su)) {
            states.add(isg.getStatus());
        }
        if (states.contains(TypeManagementMonitoringInfo.REFUSAL)) {
            return TypeManagementMonitoringInfo.REFUSAL;
        } else if (states.contains(TypeManagementMonitoringInfo.VALINT)) {
            return TypeManagementMonitoringInfo.VALINT;
        } else if (states.contains(TypeManagementMonitoringInfo.VALPAP)) {
            return TypeManagementMonitoringInfo.VALPAP;
        } else if (states.contains(TypeManagementMonitoringInfo.HC)) {
            return TypeManagementMonitoringInfo.HC;
        } else if (states.contains(TypeManagementMonitoringInfo.PARTIELINT)) {
            return TypeManagementMonitoringInfo.PARTIELINT;
        } else if (states.contains(TypeManagementMonitoringInfo.WASTE)) {
            return TypeManagementMonitoringInfo.WASTE;
        } else if (states.contains(TypeManagementMonitoringInfo.PND)) {
            return TypeManagementMonitoringInfo.PND;
        }
        return TypeManagementMonitoringInfo.INITLA;
    }

    public List<TypeManagementMonitoringInfo> getStatesList(SurveyUnit su) {
        return managementMonitoringInfoService.findBySurveyUnit(su).stream().map(ManagementMonitoringInfo::getStatus)
                .collect(Collectors.toList());
    }

    public SurveyUnit findByIdSurveyUnitAndIdCampaign(String idSu, String idCampaign) {

        return surveyUnitRepository.findByIdSuAndCampaignId(idSu, idCampaign);
    }

    public SurveyUnit findByIdContactAndIdCampaign(String idContact, String idCampaign) {

        return surveyUnitRepository.findByIdContactAndCampaignId(idContact, idCampaign);
    }

    public SurveyUnit findByIdSuAndIdContactAndCampaignId(String idSu, String idContact, String idCampaign) {

        return surveyUnitRepository.findByIdSuAndIdContactAndCampaignId(idSu, idContact, idCampaign);
    }

    public SurveyUnit findByIdSu(String idSu) {
        return surveyUnitRepository.findByIdSu(idSu);
    }

    public SurveyUnit findByIdContact(String idContact) {
        return surveyUnitRepository.findByIdContact(idContact);
    }

    public Collection<SurveyUnit> findByBatchNumber(int numLot) {
        return surveyUnitRepository.findByBatchNumber(numLot);
    }

    public Page<SurveyUnit> searchSurveyUnitByIdWithFilter(String filter, String filter2, Pageable pageable) {
        return surveyUnitRepository.findByIdContactContainingIgnoreCaseOrIdSuContainingIgnoreCaseOrderByIdSuAscCampaignIdAsc(
                filter, filter2, pageable);
    }

    public Page<SurveyUnit> searchSurveyUnitByIdWithFilterByIdContact(String filter, Pageable pageable) {
        return surveyUnitRepository.findByIdContactContainingIgnoreCaseOrderByIdContactAscCampaignIdAsc(filter, pageable);
    }

    public List<SurveyUnit> findAllSurveyUnits() {
        return surveyUnitRepository.findAll();
    }

    public SurveyUnit saveAndFlush(SurveyUnit ue) {
        return surveyUnitRepository.saveAndFlush(ue);
    }

    public void deleteByCampaignId(String idCampaign) {
        List<SurveyUnit> suToDelete = surveyUnitRepository.findByCampaignId(idCampaign);
        List<ManagementMonitoringInfo> mmToDelete = suToDelete.stream().map(su -> managementMonitoringInfoService.findBySurveyUnit(su))
                .flatMap(Collection::stream).collect(Collectors.toList());
        managementMonitoringInfoService.deleteAll(mmToDelete);
        surveyUnitRepository.deleteAll(suToDelete);
    }

    public boolean checkContact(String idSu, String idContact,String campaignId) {

        SurveyUnit su = findByIdSuAndIdContactAndCampaignId(idSu, idContact, campaignId);
        if (su == null) {
            return false;
        } else {
            return true;
        }

    }
}
