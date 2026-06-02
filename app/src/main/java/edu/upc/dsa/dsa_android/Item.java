package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("type")
    private String type;

    @SerializedName("price")
    private double price;

    @SerializedName("available")
    private boolean available;

    @SerializedName("assetName")
    private String assetName;

    public Item() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Alias para compatibilidad con adaptadores existentes
    public String getNombre() { return name; }
    public void setNombre(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescripcion() { return description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTipo() { return type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // Alias precio como int
    public int getPrecio() { return (int) price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }
}
