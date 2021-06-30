package fr.insee.coleman.api;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import fr.insee.coleman.api.domain.Campaign;
import fr.insee.coleman.api.domain.ManagementMonitoringInfo;
import fr.insee.coleman.api.domain.Order;
import fr.insee.coleman.api.domain.TypeManagementMonitoringInfo;
import fr.insee.coleman.api.domain.SurveyUnit;
import fr.insee.coleman.api.services.CampaignService;
import fr.insee.coleman.api.services.ManagementMonitoringInfoService;
import fr.insee.coleman.api.services.OrderService;
import fr.insee.coleman.api.services.SurveyUnitService;

@Component
@ConditionalOnExpression("'${fr.insee.coleman.api.dataloader.load}'=='true'")
public class DataLoader implements ApplicationRunner {
	static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

	@Autowired
	ManagementMonitoringInfoService managementMonitoringInfoService;
	@Autowired
	SurveyUnitService surveyUnitService;
	@Autowired
	CampaignService campaignService;
	@Autowired
	OrderService orderService;
	@Autowired
	Environment environment;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		if(isDevProfile() || isTestProfile()) {
			if (surveyUnitService.findAllSurveyUnits().size() != 0) {
				LOGGER.info("Data already exists - no test data loaded");
				return;
			}

			LOGGER.info("No data in database - start loading test data");
	
			// Creating table order
			orderService.saveAndFlush(new Order(Long.parseLong("8"), TypeManagementMonitoringInfo.REFUSAL.toString(), 8));
			orderService.saveAndFlush(new Order(Long.parseLong("7"), TypeManagementMonitoringInfo.VALINT.toString(), 7));
			orderService.saveAndFlush(new Order(Long.parseLong("6"), TypeManagementMonitoringInfo.VALPAP.toString(), 6));
			orderService.saveAndFlush(new Order(Long.parseLong("5"), TypeManagementMonitoringInfo.HC.toString(), 5));
			orderService.saveAndFlush(new Order(Long.parseLong("4"), TypeManagementMonitoringInfo.PARTIELINT.toString(), 4));
			orderService.saveAndFlush(new Order(Long.parseLong("3"), TypeManagementMonitoringInfo.WASTE.toString(), 3));
			orderService.saveAndFlush(new Order(Long.parseLong("2"), TypeManagementMonitoringInfo.PND.toString(), 2));
			orderService.saveAndFlush(new Order(Long.parseLong("1"), TypeManagementMonitoringInfo.INITLA.toString(), 1));
	
			// Creating campaigns
			campaignService.save(
					new Campaign("simpsons2021x00","Survey on the Simpsons tv show 2021", 1605909600000l, 1640995199000l));
			campaignService.save(
					new Campaign("vqs2021x00","Everyday life and health survey 2021", 1605909600000l, 1640995199000l));
			campaignService.save(
					new Campaign("simpsons2022x00","Survey on the Simpsons tv show 2022", 1605909600000l, 1607644799000l));
			// campagneService.enregistrer(new Campagne("fpe", "Enquête auprès des salariés
			// de l'État", null, null));
	
			// Test files names
			String smallInitSampleInputFileName = "polymoog-recette-small-init.csv";
			String smallSampleInputFileName = "polymoog-recette-small.csv";
	
			// Map filename - campaign
			HashMap<String, String> fileNameForCampagne = new HashMap<String, String>();
			fileNameForCampagne.put("simpsons2021x00", smallInitSampleInputFileName);
			fileNameForCampagne.put("vqs2021x00", smallSampleInputFileName);
			fileNameForCampagne.put("simpsons2022x00", smallSampleInputFileName);
	
			for (Campaign campaign : campaignService.getCampaigns()) {
	
				LOGGER.info("Start loading for campaign : {}", campaign.getId());
	
				File csvFile = new ClassPathResource(fileNameForCampagne.get(campaign.getId())).getFile();
				List<String> csvList = new ArrayList<>();
	
				// LOADING FILE IN A LIST
				try (Stream<String> stream = Files.lines(csvFile.toPath())) {
	
					csvList = stream.skip(1).collect(Collectors.toList());
	
					// converting list in persisted SU list and in managementMonitoringInfo list
					Set<SurveyUnit> listSu = new HashSet<>();
					List<String[]> managementMonitoringInfoListToCreate = new ArrayList<>();
	
					for (String str : csvList) {
						String[] split = str.split(";", -1);
						listSu.add(convertToSurveyUnit((String[]) Arrays.copyOfRange(split, 0, 7)));
						managementMonitoringInfoListToCreate.add(split);
					}
	
					LOGGER.info("Persistence of su for campaign {}", campaign.getId());
					// Persisting surveyUnits
					for (SurveyUnit su : listSu) {
						SurveyUnit suToPersist = new SurveyUnit(su.getIdSu(), null, su.getIdContact(),
								su.getLastname(), su.getFirstName(), su.getAddress(), su.getBatchNumber(), campaign);
						suToPersist = surveyUnitService.saveAndFlush(suToPersist);
					}
	
					LOGGER.info("Saving of Survey-units OK");
	
					// Going through ISG list, and persisting them after retrieving the survey unit
					for (String[] ligne : managementMonitoringInfoListToCreate) {
	
						String idContactToFind = ligne[0].trim();
						TypeManagementMonitoringInfo typeInfo = TypeManagementMonitoringInfo.valueOf(ligne[6].trim());
						Long dateManagementMonitoringInfo = Long.parseLong(ligne[7].trim());
						SurveyUnit suResearch = surveyUnitService.findByIdContactAndIdCampaign(idContactToFind,
								campaign.getId());
						managementMonitoringInfoService.saveAndFlush(new ManagementMonitoringInfo(null, suResearch, typeInfo, dateManagementMonitoringInfo, null));
					}
	
					LOGGER.info("Save of MMI OK");
	
				} catch (Exception ioE) {
					// Fallback in case of crash
					LOGGER.error("Error during saving of campaign {}", campaign.getId() + ". "
							+ ioE.getMessage());
	
				}
			}
		}
	}

	private SurveyUnit convertToSurveyUnit(String[] split) {
		String idContact = split[0].trim();
		String id_SU = split[1].trim();
		int numeroDeLot = Integer.parseInt(split[2].trim());
		String lastName = split[3].trim();
		String firstName = split[4].trim();
		String address = split[5].trim();

		return new SurveyUnit(id_SU, null, idContact, lastName, firstName, address, numeroDeLot, null);
	}
	

	public boolean isDevProfile() {
		for (final String profileName : environment.getActiveProfiles()) {
	        if("dev".equals(profileName)) return true;
	    }   
	    return false;
	}
	
	public boolean isTestProfile() {
		for (final String profileName : environment.getActiveProfiles()) {
	        if("test".equals(profileName)) return true;
	    }   
	    return false;
	}
}