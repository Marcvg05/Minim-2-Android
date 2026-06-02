package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class InventarioActivity extends AppCompatActivity {

    RecyclerView recyclerViewInventario;
    InventarioAdapter adapter;
    Button btnVolverAlMenu;
    ProgressBar PB;

    ApiService apiService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventario);

        recyclerViewInventario = findViewById(R.id.recyclerViewInventario);
        btnVolverAlMenu        = findViewById(R.id.btnVolverAlMenu);
        PB                     = findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        apiService        = RetrofitClient.getInstance().getApi();

        recyclerViewInventario.setLayoutManager(new LinearLayoutManager(this));

        cargarInventario();

        btnVolverAlMenu.setOnClickListener(v -> runLoadingAnimation(this::finish));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarInventario() {
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: Sesión no iniciada", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressBarActivity.show(PB);

        // Primero cargamos todos los items para mapear los nombres e iconos correctos
        apiService.getAllItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> callItems, Response<List<Item>> responseItems) {
                java.util.HashMap<Integer, Item> itemsMap = new java.util.HashMap<>();
                if (responseItems.isSuccessful() && responseItems.body() != null) {
                    for (Item item : responseItems.body()) {
                        itemsMap.put(item.getId(), item);
                    }
                }
                cargarDetallesInventario(userId, itemsMap);
            }

            @Override
            public void onFailure(Call<List<Item>> callItems, Throwable t) {
                // Si falla el catálogo, procedemos con un mapa vacío para no bloquear al usuario
                cargarDetallesInventario(userId, new java.util.HashMap<>());
            }
        });
    }

    private void cargarDetallesInventario(int userId, java.util.HashMap<Integer, Item> itemsMap) {
        Call<List<InventoryEntry>> call = apiService.getInventory(userId);
        call.enqueue(new Callback<List<InventoryEntry>>() {
            @Override
            public void onResponse(Call<List<InventoryEntry>> call, Response<List<InventoryEntry>> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful() && response.body() != null) {
                    List<InventoryEntry> entries = response.body();
                    for (InventoryEntry entry : entries) {
                        Item item = itemsMap.get(entry.getItemId());
                        if (item != null) {
                            entry.setItemName(item.getName());
                            entry.setItemDescription(item.getDescription());
                            entry.setItemType(item.getType());
                            entry.setItemAssetName(item.getAssetName());
                        }
                    }

                    if (entries.isEmpty()) {
                        Toast.makeText(InventarioActivity.this,
                                "Tu inventario está vacío", Toast.LENGTH_SHORT).show();
                    }
                    adapter = new InventarioAdapter(InventarioActivity.this, entries);
                    recyclerViewInventario.setAdapter(adapter);
                } else if (response.code() == 404) {
                    Toast.makeText(InventarioActivity.this,
                            "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("InventarioActivity", "Error: " + response.code());
                    Toast.makeText(InventarioActivity.this,
                            "No se pudo cargar el inventario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InventoryEntry>> call, Throwable t) {
                ProgressBarActivity.hide(PB);
                Log.e("InventarioActivity", "onFailure", t);
                Toast.makeText(InventarioActivity.this,
                        "Fallo de conexión", Toast.LENGTH_SHORT).show();
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