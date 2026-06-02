package edu.upc.dsa.dsa_android;
import com.google.gson.annotations.SerializedName;

public class CompraRequest {

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("objectId")
    private String objectId;

    public CompraRequest(String nombre, String objectId) {
        this.nombre = nombre;
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getUserName() {
        return nombre;
    }

    public void setUserName(String userName) {
        this.nombre = userName;
    }
}

