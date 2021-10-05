package fr.insee.coleman.api.domain;

import java.util.List;

public class RedirectUnit {

    private String idUe;
    private String idContact;
    private List<String> openedCampaignsIds;

    public RedirectUnit() {
    }

    public String getIdUe() {
        return idUe;
    }

    public void setIdUe(String idUe) {
        this.idUe = idUe;
    }

    public String getIdContact() {
        return idContact;
    }

    public void setIdContact(String idContact) {
        this.idContact = idContact;
    }

    public List<String> getOpenedCampaignsIds() {
        return openedCampaignsIds;
    }

    public void setOpenedCampaignsIds(List<String> openedCampaignsIds) {
        this.openedCampaignsIds = openedCampaignsIds;
    }
}
