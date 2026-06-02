package edu.upc.dsa.dsa_android;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.animation.ObjectAnimator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnVolver;
    private UserAdapter adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.recycler);
        btnVolver = findViewById(R.id.btnVolver);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> runLoadingAnimation(this::finish));
        }

        apiService = RetrofitClient.getInstance().getApi();
        loadUsers();
    }

    private void loadUsers() {
        Call<List<User>> call = apiService.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    adapter = new UserAdapter(RankingActivity.this, users);
                    if (recyclerView != null) {
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(RankingActivity.this, "Error cargando jugadores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("RankingActivity", "Fallo de conexión", t);
                Toast.makeText(RankingActivity.this, "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void runLoadingAnimation(Runnable onCompleteAction) {
        android.view.View loadingOverlay = findViewById(R.id.loadingOverlay);
        ProgressBar progressBar = findViewById(R.id.horizontalProgressBar);
        if (loadingOverlay != null && progressBar != null) {
            progressBar.setProgress(0);
            loadingOverlay.setVisibility(android.view.View.VISIBLE);

            ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
            animator.setDuration(1200);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator2) {
                    onCompleteAction.run();
                    loadingOverlay.setVisibility(android.view.View.GONE);
                }
            });
            animator.start();
        } else {
            onCompleteAction.run();
        }
    }
}