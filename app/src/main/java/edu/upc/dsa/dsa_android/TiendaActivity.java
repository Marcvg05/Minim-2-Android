package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.view.animation.DecelerateInterpolator;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiendaActivity extends AppCompatActivity {

    Button btnBackToInicioLogin;
    TextView tvMonedas;
    TextView tvUsuario;
    RecyclerView recyclerViewTienda;
    TiendaAdapter adapter;
    SharedPreferences sharedPreferences;
    ProgressBar PB;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tienda);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);

        btnBackToInicioLogin = findViewById(R.id.btnBackToInicioLogIn);
        tvMonedas   = findViewById(R.id.textViewMonedas);
        tvUsuario   = findViewById(R.id.textViewUsuario);
        recyclerViewTienda = findViewById(R.id.recyclerViewTienda);
        recyclerViewTienda.setLayoutManager(new LinearLayoutManager(this));
        PB = findViewById(R.id.progressBar);

        String savedUsername = sharedPreferences.getString("username", "...");
        if (tvUsuario != null) tvUsuario.setText(savedUsername);

        apiService = RetrofitClient.getInstance().getApi();

        actualizarSaldoUI();
        cargarTienda();

        btnBackToInicioLogin.setOnClickListener(v -> {
            runLoadingAnimation(() -> {
                startActivity(new Intent(TiendaActivity.this, InicioLoginActivity.class));
                finish();
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarSaldoUI();
    }

    private void cargarTienda() {
        ProgressBarActivity.show(PB);

        Call<List<Item>> call = apiService.getAllItems();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful() && response.body() != null) {
                    List<Item> items = response.body();
                    adapter = new TiendaAdapter(TiendaActivity.this, items, item -> handleCompra(item));
                    recyclerViewTienda.setAdapter(adapter);
                } else {
                    Toast.makeText(TiendaActivity.this,
                            "Error cargando la tienda: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("TiendaActivity", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                ProgressBarActivity.hide(PB);
                Toast.makeText(TiendaActivity.this,
                        "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("TiendaActivity", "onFailure", t);
            }
        });
    }

    private void handleCompra(Item item) {
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: Sesión no iniciada", Toast.LENGTH_LONG).show();
            return;
        }

        BuyItemRequest request = new BuyItemRequest(item.getId(), 1);

        ProgressBarActivity.show(PB);

        Call<Purchase> call = apiService.buyItem(userId, request);
        call.enqueue(new Callback<Purchase>() {
            @Override
            public void onResponse(Call<Purchase> call, Response<Purchase> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful() && response.body() != null) {
                    Purchase purchase = response.body();
                    Toast.makeText(TiendaActivity.this,
                            item.getName() + " comprado! Saldo restante: " +
                            String.format("%.2f", purchase.getUserSaldo()),
                            Toast.LENGTH_SHORT).show();

                    // Actualizar saldo en SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("saldo", (float) purchase.getUserSaldo());
                    editor.apply();

                    actualizarSaldoUI();

                } else if (response.code() == 409) {
                    Toast.makeText(TiendaActivity.this,
                            "Saldo insuficiente", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(TiendaActivity.this,
                            "Item o usuario no encontrado", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String errorMsg = response.errorBody() != null
                                ? response.errorBody().string() : "Error " + response.code();
                        Toast.makeText(TiendaActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("TiendaActivity", "Error parseando errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Purchase> call, Throwable t) {
                ProgressBarActivity.hide(PB);
                Toast.makeText(TiendaActivity.this,
                        "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("TiendaActivity", "onFailure compra", t);
            }
        });
    }

    private void actualizarSaldoUI() {
        float saldo = sharedPreferences.getFloat("saldo", 0f);
        tvMonedas.setText("💰 " + String.format("%.2f", saldo));
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
                public void onAnimationEnd(android.animation.Animator animator2) {
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
