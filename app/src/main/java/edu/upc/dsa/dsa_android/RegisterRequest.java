package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

/**
 * Request para POST /game/auth/register
 */
public class RegisterRequest {

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("saldo")
    private double saldo;

    @SerializedName("role")
    private String role;

    @SerializedName("level")
    private int level;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.saldo = 100.0; // saldo inicial por defecto
        this.role = "PLAYER";
        this.level = 1;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}
