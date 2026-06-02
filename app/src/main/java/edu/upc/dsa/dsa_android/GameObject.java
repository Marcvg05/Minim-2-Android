package edu.upc.dsa.dsa_android;
import com.google.gson.annotations.SerializedName;

public class GameObject {

    @SerializedName("id")
    private String id;
    private String nombre;
    private int precio;
    private String descripcion;
    @SerializedName("tipo")
    private String tipo;
    private int cantidad;

    public GameObject() {
    }

    public GameObject(String objectId, String nombre, int precio) {
        this.id = objectId;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
