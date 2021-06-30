package fr.insee.coleman.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "management_monitoring_info")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idManagementMonitoringInfo")
@JsonRootName(value = "managementMonitoringInfo")
public class ManagementMonitoringInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_management_monitoring")
	@SequenceGenerator(name = "gen_management_monitoring", allocationSize = 1, sequenceName = "seq_management_monitoring")
	@Column(name = "idManagementMonitoringInfo", updatable = false, nullable = false)
	private Long idManagementMonitoringInfo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name = "idCampaign", nullable = false),
		@JoinColumn(name = "idContact", nullable = false),
		@JoinColumn(name = "idSu", nullable = false)
	})
	// @JsonIgnore
	@JsonManagedReference
	private SurveyUnit surveyUnit;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TypeManagementMonitoringInfo status;

	@Column(name = "dateinfo")
	private Long dateInfo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_upload")
	// @JsonIgnore
	@JsonManagedReference
	private Upload upload;

	public Long getidManagementMonitoringInfo() {
		return idManagementMonitoringInfo;
	}

	public SurveyUnit getSurveyUnit() {
		return surveyUnit;
	}

	public TypeManagementMonitoringInfo getStatus() {
		return status;
	}

	public void setStatus(TypeManagementMonitoringInfo status) {
		this.status = status;
	}

	public Long getDateInfo() {
		return dateInfo;
	}

	public Upload getUpload() {
		return upload;
	}

	public ManagementMonitoringInfo() {
		super();
	}

	public ManagementMonitoringInfo(Long idManagementMonitoringInfo, SurveyUnit surveyUnit, TypeManagementMonitoringInfo status, Long dateInfo,
			Upload upload) {
		super();
		this.idManagementMonitoringInfo = idManagementMonitoringInfo;
		this.surveyUnit = surveyUnit;
		this.status = status;
		this.dateInfo = dateInfo;
		this.upload = upload;
	}

}