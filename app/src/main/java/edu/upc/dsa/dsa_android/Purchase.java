package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

public class Purchase {

    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("itemId")
    private int itemId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("totalPrice")
    private double totalPrice;

    @SerializedName("userSaldo")
    private double userSaldo;

    @SerializedName("date")
    private String date;

    public Purchase() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public double getUserSaldo() { return userSaldo; }
    public void setUserSaldo(double userSaldo) { this.userSaldo = userSaldo; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
