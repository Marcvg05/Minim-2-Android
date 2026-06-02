package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

/**
 * Request para POST /game/players/{playerId}/inventory
 */
public class BuyItemRequest {

    @SerializedName("itemId")
    private int itemId;

    @SerializedName("quantity")
    private int quantity;

    public BuyItemRequest() {
    }

    public BuyItemRequest(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
