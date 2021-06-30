package fr.insee.coleman.api.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.domain.SurveyUnit;
import fr.insee.coleman.api.domain.TypeManagementMonitoringInfo;
import fr.insee.coleman.api.domain.Upload;
import fr.insee.coleman.api.dto.managementmonitoringinfo.ManagementMonitoringInfoDto;
import fr.insee.coleman.api.exception.RessourceNotFoundException;
import fr.insee.coleman.api.repository.ManagementMonitoringInfoRepository;

@ExtendWith(MockitoExtension.class)
public class ManagementMonitoringInfoServiceTest {

	@Mock
	private SurveyUnitService surveyUnitService;

	@Mock
	private ManagementMonitoringInfoRepository managementMonitoringInfoRepository;

	@InjectMocks
	private ManagementMonitoringInfoService managementMonitoringInfoService;

	@Test
	public void testgetMonitoringInfoById() throws Exception {
		ManagementMonitoringInfo expected = new ManagementMonitoringInfo();

		Mockito.when(managementMonitoringInfoRepository.findByIdManagementMonitoringInfo(10)).thenReturn(expected);

		ManagementMonitoringInfo actual = managementMonitoringInfoService.getMonitoringInfoById(10);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSaveIhmError() throws Exception {
		String idCampagne="toto";
		ManagementMonitoringInfoDto managementMonitoringInfoDto = new ManagementMonitoringInfoDto();
		managementMonitoringInfoDto.setIdSu("A00000789");
		managementMonitoringInfoDto.setStatus(TypeManagementMonitoringInfo.FOLLOWUP);

		Upload upload = new Upload();
		upload.setDate((long) 100);

//		Mockito.when(ueService.findByIdUe("A00000789")).thenReturn(null);

		assertThrows(RessourceNotFoundException.class, () -> {managementMonitoringInfoService.saveIhm(idCampagne, managementMonitoringInfoDto, upload);});
	}

	@Test
	public void testsaveBatchErrorSu() throws Exception {
		ManagementMonitoringInfoDto managementMonitoringInfoDto = new ManagementMonitoringInfoDto();
		managementMonitoringInfoDto.setIdSu("B00000789");
		managementMonitoringInfoDto.setStatus(TypeManagementMonitoringInfo.PARTIELINT);

		//Mockito.when(ueService.findByIdUe("B00000789")).thenReturn(null);
		assertThrows(RessourceNotFoundException.class, () -> {managementMonitoringInfoService.saveBatch(null, managementMonitoringInfoDto);});

	}
	
	@Test()
	public void testSaveBatchErrorType() throws Exception {
		ManagementMonitoringInfoDto managementMonitoringInfoDto = new ManagementMonitoringInfoDto();
		managementMonitoringInfoDto.setIdSu("B00000123");
		managementMonitoringInfoDto.setStatus(TypeManagementMonitoringInfo.INITLA);

		SurveyUnit su = new SurveyUnit();
		su.setIdSu("B00000123");
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo = new ManagementMonitoringInfo();
		managementMonitoringInfo.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo);
		su.setManagementMonitoringInfos(managementMonitoringInfoList);

	//	Mockito.when(ueService.findByIdUe("B00000123")).thenReturn(su);

		assertThrows(RessourceNotFoundException.class, () -> {managementMonitoringInfoService.saveBatch(null, managementMonitoringInfoDto);});

	}
}
