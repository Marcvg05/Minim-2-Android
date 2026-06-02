package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.dsa_android.TeamInfoResponse;
import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTeamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTeamTitle;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        recyclerView = findViewById(R.id.recyclerMembers);
        tvTeamTitle = findViewById(R.id.tvTeamTitle);
        btnVolver = findViewById(R.id.btnVolver);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnVolver.setOnClickListener(v -> finish());

        loadMyTeam();
    }

    private void loadMyTeam() {
        SharedPreferences sp = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String username = sp.getString("username", "");

        if (username.isEmpty()) {
            Toast.makeText(this, "No estás logueado", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = RetrofitClient.getInstance().getApi();
        Call<TeamInfoResponse> call = api.getMyTeamInfo(username);

        call.enqueue(new Callback<TeamInfoResponse>() {
            @Override
            public void onResponse(Call<TeamInfoResponse> call, Response<TeamInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TeamInfoResponse info = response.body();
                    tvTeamTitle.setText("EQUIPO: " + info.getTeam());
                    recyclerView.setAdapter(new MemberAdapter(info.getMembers()));
                } else {
                    tvTeamTitle.setText("SIN EQUIPO");
                    Toast.makeText(MyTeamActivity.this, "No perteneces a ningún equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TeamInfoResponse> call, Throwable t) {
                Toast.makeText(MyTeamActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}