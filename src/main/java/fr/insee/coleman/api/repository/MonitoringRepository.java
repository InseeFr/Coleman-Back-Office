package fr.insee.coleman.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fr.insee.coleman.api.domain.ExtractionRow;
import fr.insee.coleman.api.domain.RowProgress;
import fr.insee.coleman.api.domain.UpRow;

@Repository
public class MonitoringRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	final String progressQuery = "SELECT COUNT(id_su) as total,m.status,m.batch_num FROM " + 
			"			(SELECT DISTINCT ON (id_su) id_su,A.status,ordre,batch_num " + 
			"			FROM (SELECT survey_unit.id_campaign, survey_unit.id_contact, survey_unit.id_su, \r\n" + 
			"		  address, batch_num, firstname, lastname, id_management_monitoring_info, dateinfo, \r\n" + 
			"		  status, id_upload FROM survey_unit JOIN management_monitoring_info ON ( " + 
			"     survey_unit.id_su=management_monitoring_info.id_su AND " +
			"     survey_unit.id_contact=management_monitoring_info.id_contact AND " +
			"     survey_unit.id_campaign=management_monitoring_info.id_campaign) " +
			"			WHERE survey_unit.id_campaign=?)  AS A " + 
			"			JOIN public.order AS ordre ON ordre.status=A.status " + 
			"			ORDER BY id_su,ordre DESC) as m " + 
			"			GROUP BY m.status,m.batch_num";

	
	final String followUpQuery = "SELECT COUNT(survey_unit.id_su) as nb,survey_unit.batch_num,freq FROM survey_unit " + 
			"			LEFT JOIN(SELECT COUNT(status) as freq,survey_unit.id_su, batch_num FROM survey_unit " + 
			"			JOIN management_monitoring_info ON ( " + 
      "     survey_unit.id_su=management_monitoring_info.id_su AND " +
      "     survey_unit.id_contact=management_monitoring_info.id_contact AND " +
      "     survey_unit.id_campaign=management_monitoring_info.id_campaign) " +
			"			WHERE (status='FOLLOWUP' AND survey_unit.id_campaign=?) " + 
			"			GROUP BY survey_unit.id_su,batch_num) as B ON survey_unit.id_su = B.id_su " + 
			"			WHERE survey_unit.id_campaign=? " + 
			"			GROUP BY survey_unit.batch_num,freq";
	
	final String extractionQuery = "SELECT survey_unit.id_su as id_su, "
			+ "	survey_unit.id_contact as id_contact,"
			+ "	survey_unit.lastname as lastname,"
			+ "	survey_unit.firstname as firstname,"
			+ "	survey_unit.address as address,	\r\n"
			+ "	survey_unit.batch_num as batch_num,"
			+ "	B.status	as status,"
			+ "	B.dateinfo	as dateinfo "
			+ " FROM survey_unit "+
			"			LEFT JOIN ( management_monitoring_info LEFT JOIN uploads ON management_monitoring_info.id_upload =uploads.id) AS B "+
			"			ON ( " + 
      "     survey_unit.id_su=B.id_su AND " +
      "     survey_unit.id_contact=B.id_contact AND " +
      "     survey_unit.id_campaign=B.id_campaign) " +
			"			WHERE survey_unit.id_campaign=?";
	

	
	public List<RowProgress> getProgress(String idCampaign) {
		List<RowProgress> progress = jdbcTemplate.query(progressQuery, new RowMapper<RowProgress>() {
			public RowProgress mapRow(ResultSet rs, int rowNum) throws SQLException {
				RowProgress av = new RowProgress();
				av.setBatchNum(Integer.parseInt(rs.getString("batch_num")));
				av.setStatus(rs.getString("status"));
				av.setTotal(Integer.parseInt(rs.getString("total")));

				return av;
			}
		}, new Object[]{idCampaign});

		return progress;
	}

	public List<UpRow> getFollowUp(String idCampaign) {
		List<UpRow> relance = jdbcTemplate.query(followUpQuery, new RowMapper<UpRow>() {
			public UpRow mapRow(ResultSet rs, int rowNum) throws SQLException {
				UpRow rel = new UpRow();
				rel.setFreq(rs.getString("freq") != null ? Integer.parseInt(rs.getString("freq")) : 0);
				rel.setBatchNum(Integer.parseInt(rs.getString("batch_num")));
				rel.setNb(Integer.parseInt(rs.getString("nb")));

				return rel;
			}
		}, new Object[]{idCampaign, idCampaign});

		return relance;

	}

	public List<ExtractionRow> getExtraction(String idCampaign) {
		List<ExtractionRow> extraction = jdbcTemplate.query(extractionQuery, new RowMapper<ExtractionRow>() {
			
			public ExtractionRow mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExtractionRow ev = new ExtractionRow();
				
				ev.setStatus(rs.getString("status"));
				ev.setDateInfo(Long.parseLong(rs.getString("dateinfo")));
				ev.setIdSu(rs.getString("id_su"));
				ev.setIdContact(rs.getString("id_contact"));
				ev.setLastname(rs.getString("lastname"));
				ev.setFirstname(rs.getString("firstname"));
				ev.setAddress(rs.getString("address"));
				ev.setBatchNumber(Integer.parseInt(rs.getString("batch_num")));

				return ev;
			}
		}, new Object[]{idCampaign});

		return extraction;
	}

}
