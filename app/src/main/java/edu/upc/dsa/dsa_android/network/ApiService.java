package edu.upc.dsa.dsa_android.network;

import java.util.List;

import edu.upc.dsa.dsa_android.BuyItemRequest;
import edu.upc.dsa.dsa_android.Faq.FaqAssistantRequest;
import edu.upc.dsa.dsa_android.Faq.FaqAssistantResponse;
import edu.upc.dsa.dsa_android.InventoryEntry;
import edu.upc.dsa.dsa_android.Item;
import edu.upc.dsa.dsa_android.LoginRequest;
import edu.upc.dsa.dsa_android.Purchase;
import edu.upc.dsa.dsa_android.RegisterRequest;
import edu.upc.dsa.dsa_android.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // ──────────────────────────────────────────────
    //  AUTENTICACIÓN
    // ──────────────────────────────────────────────

    /** POST /game/auth/register */
    @POST("game/auth/register")
    Call<User> registerUser(@Body RegisterRequest request);

    /** POST /game/auth/login */
    @POST("game/auth/login")
    Call<User> loginUser(@Body LoginRequest request);

    // ──────────────────────────────────────────────
    //  ITEMS (catálogo / tienda)
    // ──────────────────────────────────────────────

    /** GET /game/items — obtener todos los items del catálogo */
    @GET("game/items")
    Call<List<Item>> getAllItems();

    // ──────────────────────────────────────────────
    //  USUARIOS
    // ──────────────────────────────────────────────

    /** GET /game/users — obtener todos los usuarios */
    @GET("game/users")
    Call<List<User>> getAllUsers();

    // ──────────────────────────────────────────────
    //  INVENTARIO DEL JUGADOR
    // ──────────────────────────────────────────────

    /** POST /game/players/{playerId}/inventory — comprar un item */
    @POST("game/players/{playerId}/inventory")
    Call<Purchase> buyItem(@Path("playerId") int playerId, @Body BuyItemRequest request);

    /** GET /game/players/{playerId}/inventory — ver inventario del jugador */
    @GET("game/players/{playerId}/inventory")
    Call<List<InventoryEntry>> getInventory(@Path("playerId") int playerId);

    /** GET /game/players/{playerId}/purchases — historial de compras */
    @GET("game/players/{playerId}/purchases")
    Call<List<Purchase>> getPurchases(@Path("playerId") int playerId);

    // ──────────────────────────────────────────────
    //  ASISTENTE IA (FAQ)
    // ──────────────────────────────────────────────

    /** POST /game/assistant/faq */
    @POST("game/assistant/faq")
    Call<FaqAssistantResponse> askFaq(@Body FaqAssistantRequest request);
}