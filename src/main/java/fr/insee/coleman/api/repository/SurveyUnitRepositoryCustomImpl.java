package fr.insee.coleman.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import fr.insee.coleman.api.domain.ExtractionRow;

public class SurveyUnitRepositoryCustomImpl implements SurveyUnitRepositoryCustom {

	// constructor-based injection
	public SurveyUnitRepositoryCustomImpl() {

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	final String surveyUnitFollowUpQuery = "SELECT TABLE1.id_su, TABLE1.batch_num , CASE WHEN TABLE2.id_su IS NULL THEN 0 ELSE 1 END as PND FROM ("
      + "SELECT DISTINCT ON (survey_unit.id_su) "
      + "survey_unit.id_campaign, survey_unit.id_contact, survey_unit.id_su, address, batch_num, firstname, lastname, id_management_monitoring_info, dateinfo, status, id_upload "
      + "FROM survey_unit JOIN management_monitoring_info ON ( "
      + "survey_unit.id_su=management_monitoring_info.id_su AND "
      + "survey_unit.id_contact=management_monitoring_info.id_contact AND "
      + "survey_unit.id_campaign=management_monitoring_info.id_campaign) "
	  + " WHERE (	survey_unit.id_campaign = ? AND  survey_unit.id_su NOT IN ("
	  + "SELECT DISTINCT ON (survey_unit.id_su) survey_unit.id_su FROM survey_unit JOIN management_monitoring_info ON ( "
      + "survey_unit.id_su=management_monitoring_info.id_su AND "
      + "survey_unit.id_contact=management_monitoring_info.id_contact AND "
      + "survey_unit.id_campaign=management_monitoring_info.id_campaign) "
      + "WHERE status in ('VALINT','VALPAP','REFUSAL','WASTE','HC') AND survey_unit.id_campaign = ? )" 
      + ")  ORDER BY survey_unit.id_su"
	  + ") AS TABLE1 LEFT OUTER JOIN ("
	  + "SELECT DISTINCT ON (survey_unit.id_su) survey_unit.id_su FROM survey_unit JOIN management_monitoring_info ON ( "
      + "survey_unit.id_su=management_monitoring_info.id_su AND "
      + "survey_unit.id_contact=management_monitoring_info.id_contact AND "
      + "survey_unit.id_campaign=management_monitoring_info.id_campaign) "
	  + "WHERE status in ('PND') AND survey_unit.id_campaign = ?) AS TABLE2 ON TABLE1.id_su=TABLE2.id_su";

	@Override
	public List<ExtractionRow> getSurveyUnitToFollowUp(String idCampaign) {

		List<ExtractionRow> followUp = jdbcTemplate.query(surveyUnitFollowUpQuery,
				new RowMapper<ExtractionRow>() {
					public ExtractionRow mapRow(ResultSet rs, int rowNum) throws SQLException {
						ExtractionRow er = new ExtractionRow();
						er.setIdSu(rs.getString("id_su"));
						er.setPnd(rs.getInt("PND"));
						er.setBatchNumber(rs.getInt("batch_num"));

						return er;
					}
				}, new Object[] { idCampaign, idCampaign, idCampaign });

		return followUp;

	}
}