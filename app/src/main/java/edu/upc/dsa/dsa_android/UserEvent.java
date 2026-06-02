package edu.upc.dsa.dsa_android;
import com.google.gson.annotations.SerializedName;
public class UserEvent {
    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellidos")
    private String apellidos;

    @SerializedName("imagen")
    private String imagen;

    public UserEvent() {}

    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getImagen() { return imagen; }
}

