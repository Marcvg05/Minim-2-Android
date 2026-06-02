package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

public class InventoryEntry {

    @SerializedName("userId")
    private int userId;

    @SerializedName("itemId")
    private int itemId;

    @SerializedName("quantity")
    private int quantity;

    // Campo extra para mostrar info del item (rellenado localmente)
    private String itemName;
    private double itemPrice;
    private String itemDescription;
    private String itemType;

    public InventoryEntry() {
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Alias
    public int getCantidad() { return quantity; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getNombre() { return itemName != null ? itemName : "Item #" + itemId; }

    public double getItemPrice() { return itemPrice; }
    public void setItemPrice(double itemPrice) { this.itemPrice = itemPrice; }

    public int getPrecio() { return (int) itemPrice; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public String getDescripcion() { return itemDescription; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    private String itemAssetName;
    public String getItemAssetName() { return itemAssetName; }
    public void setItemAssetName(String itemAssetName) { this.itemAssetName = itemAssetName; }
}
