package edu.upc.dsa.dsa_android.network;

import java.util.List;
import edu.upc.dsa.dsa_android.CompraRequest;
import edu.upc.dsa.dsa_android.Credentials;
import edu.upc.dsa.dsa_android.Evento;
import edu.upc.dsa.dsa_android.FaqAssistantRequest;
import edu.upc.dsa.dsa_android.FaqAssistantResponse;
import edu.upc.dsa.dsa_android.GameObject;
import edu.upc.dsa.dsa_android.RegistroEventoRequest;
import edu.upc.dsa.dsa_android.User;
import edu.upc.dsa.dsa_android.UserEvent;
import edu.upc.dsa.dsa_android.Team;
import edu.upc.dsa.dsa_android.TeamInfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("game/users/register")
    Call<User> registerUser(@Body Credentials credentials);

    @POST("game/users/login")
    Call<User> loginUser(@Body Credentials credentials);

    @POST("game/users/objects/buy")
    Call<User> comprarItem(@Body CompraRequest request);

    @GET("game/shop/objects")
    Call<List<GameObject>> getALLGameObjects();

    @GET("game/users/{username}")
    Call<User> getUser(@Path("username") String username);

    @GET("game/users/objects/list")
    Call<List<GameObject>> getUserObjects(@Query("nombre") String nombre);

    @GET("game/events")
    Call<List<Evento>> getEventos();

    @POST("game/events/{id}/register")
    Call<Void> registerEvento(@Path("id") String id, @Body RegistroEventoRequest request);

    @GET("game/events/{eventId}/users")
    Call<List<UserEvent>> getUsersByEvent(@Path("eventId") String eventId);

    @GET("teams/ranking")
    Call<List<Team>> getTeamsRanking();

    @PUT("teams/join/{teamName}/{userName}")
    Call<Team> joinTeam(@Path("teamName") String teamName, @Path("userName") String userName);

    @GET("teams/user/{userName}/team")
    Call<TeamInfoResponse> getMyTeamInfo(@Path("userName") String userName);

    @POST("game/assistant/faq")
    Call<FaqAssistantResponse> askFaq(@Body FaqAssistantRequest request);
}