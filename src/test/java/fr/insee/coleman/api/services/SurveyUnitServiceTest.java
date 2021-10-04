package fr.insee.coleman.api.services;

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
import fr.insee.coleman.api.repository.SurveyUnitRepository;

@ExtendWith(MockitoExtension.class)
public class SurveyUnitServiceTest {

	@Mock
	private ManagementMonitoringInfoService managementMonitoringInfoService;

	@Mock
	private SurveyUnitRepository surveyUnitRepository;

	@InjectMocks
	private SurveyUnitService surveyUnitService;

	
	@Test
	public void testDetermineStateRefusal() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo1 = new ManagementMonitoringInfo();
		managementMonitoringInfo1.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo1);
		ManagementMonitoringInfo managementMonitoringInfo2 = new ManagementMonitoringInfo();
		managementMonitoringInfo2.setStatus(TypeManagementMonitoringInfo.PND);
		managementMonitoringInfoList.add(managementMonitoringInfo2);
		ManagementMonitoringInfo managementMonitoringInfo3 = new ManagementMonitoringInfo();
		managementMonitoringInfo3.setStatus(TypeManagementMonitoringInfo.WASTE);
		managementMonitoringInfoList.add(managementMonitoringInfo3);
		ManagementMonitoringInfo managementMonitoringInfo4 = new ManagementMonitoringInfo();
		managementMonitoringInfo4.setStatus(TypeManagementMonitoringInfo.PARTIELINT);
		managementMonitoringInfoList.add(managementMonitoringInfo4);
		ManagementMonitoringInfo managementMonitoringInfo5 = new ManagementMonitoringInfo();
		managementMonitoringInfo5.setStatus(TypeManagementMonitoringInfo.HC);
		managementMonitoringInfoList.add(managementMonitoringInfo5);
		ManagementMonitoringInfo managementMonitoringInfo6 = new ManagementMonitoringInfo();
		managementMonitoringInfo6.setStatus(TypeManagementMonitoringInfo.VALPAP);
		managementMonitoringInfoList.add(managementMonitoringInfo6);
		ManagementMonitoringInfo managementMonitoringInfo7 = new ManagementMonitoringInfo();
		managementMonitoringInfo7.setStatus(TypeManagementMonitoringInfo.VALINT);
		managementMonitoringInfoList.add(managementMonitoringInfo7);
		ManagementMonitoringInfo managementMonitoringInfo8 = new ManagementMonitoringInfo();
		managementMonitoringInfo8.setStatus(TypeManagementMonitoringInfo.REFUSAL);
		managementMonitoringInfoList.add(managementMonitoringInfo8);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.REFUSAL, actual);
	}
	
	@Test
	public void testDetermineStateValInt() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo1 = new ManagementMonitoringInfo();
		managementMonitoringInfo1.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo1);
		ManagementMonitoringInfo managementMonitoringInfo2 = new ManagementMonitoringInfo();
		managementMonitoringInfo2.setStatus(TypeManagementMonitoringInfo.PND);
		managementMonitoringInfoList.add(managementMonitoringInfo2);
		ManagementMonitoringInfo managementMonitoringInfo3 = new ManagementMonitoringInfo();
		managementMonitoringInfo3.setStatus(TypeManagementMonitoringInfo.WASTE);
		managementMonitoringInfoList.add(managementMonitoringInfo3);
		ManagementMonitoringInfo managementMonitoringInfo4 = new ManagementMonitoringInfo();
		managementMonitoringInfo4.setStatus(TypeManagementMonitoringInfo.PARTIELINT);
		managementMonitoringInfoList.add(managementMonitoringInfo4);
		ManagementMonitoringInfo managementMonitoringInfo5 = new ManagementMonitoringInfo();
		managementMonitoringInfo5.setStatus(TypeManagementMonitoringInfo.HC);
		managementMonitoringInfoList.add(managementMonitoringInfo5);
		ManagementMonitoringInfo managementMonitoringInfo6 = new ManagementMonitoringInfo();
		managementMonitoringInfo6.setStatus(TypeManagementMonitoringInfo.VALPAP);
		managementMonitoringInfoList.add(managementMonitoringInfo6);
		ManagementMonitoringInfo managementMonitoringInfo7 = new ManagementMonitoringInfo();
		managementMonitoringInfo7.setStatus(TypeManagementMonitoringInfo.VALINT);
		managementMonitoringInfoList.add(managementMonitoringInfo7);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.VALINT, actual);
	}
	
	@Test
	public void testDetermineStateValPap() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo1 = new ManagementMonitoringInfo();
		managementMonitoringInfo1.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo1);
		ManagementMonitoringInfo managementMonitoringInfo2 = new ManagementMonitoringInfo();
		managementMonitoringInfo2.setStatus(TypeManagementMonitoringInfo.PND);
		managementMonitoringInfoList.add(managementMonitoringInfo2);
		ManagementMonitoringInfo managementMonitoringInfo3 = new ManagementMonitoringInfo();
		managementMonitoringInfo3.setStatus(TypeManagementMonitoringInfo.WASTE);
		managementMonitoringInfoList.add(managementMonitoringInfo3);
		ManagementMonitoringInfo managementMonitoringInfo4 = new ManagementMonitoringInfo();
		managementMonitoringInfo4.setStatus(TypeManagementMonitoringInfo.PARTIELINT);
		managementMonitoringInfoList.add(managementMonitoringInfo4);
		ManagementMonitoringInfo managementMonitoringInfo5 = new ManagementMonitoringInfo();
		managementMonitoringInfo5.setStatus(TypeManagementMonitoringInfo.HC);
		managementMonitoringInfoList.add(managementMonitoringInfo5);
		ManagementMonitoringInfo managementMonitoringInfo6 = new ManagementMonitoringInfo();
		managementMonitoringInfo6.setStatus(TypeManagementMonitoringInfo.VALPAP);
		managementMonitoringInfoList.add(managementMonitoringInfo6);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.VALPAP, actual);
	}
	
	@Test
	public void testDetermineStateHC() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo1 = new ManagementMonitoringInfo();
		managementMonitoringInfo1.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo1);
		ManagementMonitoringInfo managementMonitoringInfo2 = new ManagementMonitoringInfo();
		managementMonitoringInfo2.setStatus(TypeManagementMonitoringInfo.PND);
		managementMonitoringInfoList.add(managementMonitoringInfo2);
		ManagementMonitoringInfo managementMonitoringInfo3 = new ManagementMonitoringInfo();
		managementMonitoringInfo3.setStatus(TypeManagementMonitoringInfo.WASTE);
		managementMonitoringInfoList.add(managementMonitoringInfo3);
		ManagementMonitoringInfo managementMonitoringInfo4 = new ManagementMonitoringInfo();
		managementMonitoringInfo4.setStatus(TypeManagementMonitoringInfo.PARTIELINT);
		managementMonitoringInfoList.add(managementMonitoringInfo4);
		ManagementMonitoringInfo managementMonitoringInfo5 = new ManagementMonitoringInfo();
		managementMonitoringInfo5.setStatus(TypeManagementMonitoringInfo.HC);
		managementMonitoringInfoList.add(managementMonitoringInfo5);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.HC, actual);
	}
	
	@Test
	public void testDetermineStatePartielInt() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo1 = new ManagementMonitoringInfo();
		managementMonitoringInfo1.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo1);
		ManagementMonitoringInfo managementMonitoringInfo2 = new ManagementMonitoringInfo();
		managementMonitoringInfo2.setStatus(TypeManagementMonitoringInfo.PND);
		managementMonitoringInfoList.add(managementMonitoringInfo2);
		ManagementMonitoringInfo managementMonitoringInfo3 = new ManagementMonitoringInfo();
		managementMonitoringInfo3.setStatus(TypeManagementMonitoringInfo.WASTE);
		managementMonitoringInfoList.add(managementMonitoringInfo3);
		ManagementMonitoringInfo managementMonitoringInfo4 = new ManagementMonitoringInfo();
		managementMonitoringInfo4.setStatus(TypeManagementMonitoringInfo.PARTIELINT);
		managementMonitoringInfoList.add(managementMonitoringInfo4);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.PARTIELINT, actual);
	}
	
	@Test
	public void testDetermineStateWaste() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo1 = new ManagementMonitoringInfo();
		managementMonitoringInfo1.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo1);
		ManagementMonitoringInfo managementMonitoringInfo2 = new ManagementMonitoringInfo();
		managementMonitoringInfo2.setStatus(TypeManagementMonitoringInfo.PND);
		managementMonitoringInfoList.add(managementMonitoringInfo2);
		ManagementMonitoringInfo managementMonitoringInfo3 = new ManagementMonitoringInfo();
		managementMonitoringInfo3.setStatus(TypeManagementMonitoringInfo.WASTE);
		managementMonitoringInfoList.add(managementMonitoringInfo3);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.WASTE, actual);
	}
	
	@Test
	public void testDetermineStatePND() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo1 = new ManagementMonitoringInfo();
		managementMonitoringInfo1.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo1);
		ManagementMonitoringInfo managementMonitoringInfo2 = new ManagementMonitoringInfo();
		managementMonitoringInfo2.setStatus(TypeManagementMonitoringInfo.PND);
		managementMonitoringInfoList.add(managementMonitoringInfo2);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.PND, actual);
	}
	
	@Test
	public void testDetermineStateInitla() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo = new ManagementMonitoringInfo();
		managementMonitoringInfo.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.INITLA, actual);
	}
	
	@Test
	public void testDetermineStateIfFollowUp() throws Exception {
		SurveyUnit suTest = new SurveyUnit();
		List<ManagementMonitoringInfo> managementMonitoringInfoList = new ArrayList<ManagementMonitoringInfo>();
		ManagementMonitoringInfo managementMonitoringInfo1 = new ManagementMonitoringInfo();
		managementMonitoringInfo1.setStatus(TypeManagementMonitoringInfo.INITLA);
		managementMonitoringInfoList.add(managementMonitoringInfo1);
		ManagementMonitoringInfo managementMonitoringInfo2 = new ManagementMonitoringInfo();
		managementMonitoringInfo2.setStatus(TypeManagementMonitoringInfo.FOLLOWUP);
		managementMonitoringInfoList.add(managementMonitoringInfo2);
		suTest.setManagementMonitoringInfos(managementMonitoringInfoList);

		Mockito.when(managementMonitoringInfoService.findBySurveyUnit(suTest)).thenReturn(suTest.getManagementMonitoringInfos());

		TypeManagementMonitoringInfo actual = surveyUnitService.determineState(suTest);

		Assert.assertEquals(TypeManagementMonitoringInfo.INITLA, actual);
	}
	
}
