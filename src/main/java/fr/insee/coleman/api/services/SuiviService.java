package fr.insee.coleman.api.services;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.coleman.api.configuration.JSONCollectionWrapper;
import fr.insee.coleman.api.domain.BatchProgress;
import fr.insee.coleman.api.domain.RowProgress;
import fr.insee.coleman.api.domain.ExtractionRow;
import fr.insee.coleman.api.domain.UpRow;
import fr.insee.coleman.api.repository.MonitoringRepository;

@Service
public class SuiviService {

	@Autowired
	MonitoringRepository suiviRepo;

	public JSONCollectionWrapper<BatchProgress> getProgress(String idCampaign) {

		List<RowProgress> rows = suiviRepo.getProgress(idCampaign);

		HashMap<Integer, BatchProgress> lots = new HashMap<Integer, BatchProgress>();

		if (!rows.isEmpty()) {
			for (RowProgress row : rows) {

				int batchNumber = row.getBatchNum();

				if (!lots.containsKey(Integer.valueOf(batchNumber))) {
					lots.put(Integer.valueOf(batchNumber), new BatchProgress(batchNumber));
				}

				BatchProgress lot = lots.get(Integer.valueOf(batchNumber));

				lot.setNbSu(lot.getNbSu() + row.getTotal());

				switch (row.getStatus().trim()) {
					case "REFUSAL":
						lot.setNbRefusal(row.getTotal());
						break;
					case "VALINT":
						lot.setNbIntReceived(row.getTotal());
						break;
					case "VALPAP":
						lot.setNbPapReceived(row.getTotal());
						break;
					case "HC":
						lot.setNbHC(row.getTotal());
						break;
					case "PARTIELINT":
						lot.setNbIntPart(row.getTotal());
						break;
					case "WASTE":
						lot.setNbOtherWastes(row.getTotal());
						break;
					case "PND":
						lot.setNbPND(row.getTotal());
						break;
					case "INITLA":
						break;
				}
			}
		}
		return new JSONCollectionWrapper<>(lots.values());
	}

	public JSONCollectionWrapper<UpRow> getFollowUp(String idCampaign) {
		return new JSONCollectionWrapper<UpRow>(suiviRepo.getFollowUp(idCampaign));
	}

	public JSONCollectionWrapper<ExtractionRow> getExtraction(String idCampaign) {
		return new JSONCollectionWrapper<>(suiviRepo.getExtraction(idCampaign));
	}

}
