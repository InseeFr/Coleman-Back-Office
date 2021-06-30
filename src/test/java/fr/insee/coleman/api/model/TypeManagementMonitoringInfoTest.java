package fr.insee.coleman.api.model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import fr.insee.coleman.api.domain.TypeManagementMonitoringInfo;

public class TypeManagementMonitoringInfoTest {
	
	@Test
	public void testToString() throws Exception {
		TypeManagementMonitoringInfo typeInfo=TypeManagementMonitoringInfo.WASTE;
		assertEquals("WASTE",typeInfo.toString());
	}

}
