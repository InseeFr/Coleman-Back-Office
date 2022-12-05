package fr.insee.coleman.api.utils;

import org.springframework.http.HttpStatus;

public class ResponseFromExternalService {

    public ResponseFromExternalService(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public ResponseFromExternalService() {}

    private String message;
    private HttpStatus status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

}
