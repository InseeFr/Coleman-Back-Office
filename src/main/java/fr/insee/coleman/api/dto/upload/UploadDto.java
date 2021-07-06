package fr.insee.coleman.api.dto.upload;

import java.util.List;

import fr.insee.coleman.api.dto.managementmonitoringinfo.ManagementMonitoringInfoDto;

public class UploadDto {

	private List <ManagementMonitoringInfoDto> data;

	public List<ManagementMonitoringInfoDto> getData() {
		return data;
	}
	public void setData(List<ManagementMonitoringInfoDto> data) {
		this.data = data;
	}

	
	
}
