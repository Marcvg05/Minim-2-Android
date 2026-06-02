package edu.upc.dsa.dsa_android;

public class RegistroEventoRequest {
    private String userId;

    public RegistroEventoRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
