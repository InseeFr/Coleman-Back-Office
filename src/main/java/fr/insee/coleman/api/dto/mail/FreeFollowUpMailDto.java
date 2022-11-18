package fr.insee.coleman.api.dto.mail;

public class FreeFollowUpMailDto {

    private String email;
    private DataFreeFollowUpMailDto data;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DataFreeFollowUpMailDto getData() {
        return data;
    }

    public void setData(DataFreeFollowUpMailDto data) {
        this.data = data;
    }

}
