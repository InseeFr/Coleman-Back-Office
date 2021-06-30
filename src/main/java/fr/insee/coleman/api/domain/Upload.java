package fr.insee.coleman.api.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "uploads")
public class Upload {

	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE , generator = "gen_upload")
	@SequenceGenerator (name = "gen_upload" , allocationSize = 1 , sequenceName = "seq_upload")
	@Column (name = "id" , updatable = false , nullable = false)
	private Long id;

	@Column (name = "dateupload")
	private Long date;

	@JsonBackReference
	@OneToMany (mappedBy = "upload")
	private List <ManagementMonitoringInfo> managementMonitoringInfos;

	public Upload() {
		super(); 
	}

	public Upload(Long id, Long date, List<ManagementMonitoringInfo> managementMonitoringInfos) {
		super();
		this.id = id;
		this.date = date;
		this.managementMonitoringInfos = managementMonitoringInfos;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public List<ManagementMonitoringInfo> getManagementMonitoringInfos() {
		return managementMonitoringInfos;
	}

	public void setManagementMonitoringInfos(List<ManagementMonitoringInfo> managementMonitoringInfos) {
		this.managementMonitoringInfos = managementMonitoringInfos;
	}

	public Long getId() {
		return id;
	}
}
