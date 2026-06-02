package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.upc.dsa.dsa_android.Team;
import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.recycler);
        btnVolver = findViewById(R.id.btnVolver);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnVolver.setOnClickListener(v -> {
            finish();
        });

        loadRanking();
    }

    private void loadRanking() {
        ApiService api = RetrofitClient.getInstance().getApi();
        Call<List<Team>> call = api.getTeamsRanking();

        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                if (response.isSuccessful()) {
                    List<Team> teams = response.body();

                    SharedPreferences sp = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                    String username = sp.getString("username", "");

                    recyclerView.setAdapter(new TeamAdapter(teams, RankingActivity.this, username));
                } else {
                    Toast.makeText(RankingActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                Toast.makeText(RankingActivity.this, "Fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}