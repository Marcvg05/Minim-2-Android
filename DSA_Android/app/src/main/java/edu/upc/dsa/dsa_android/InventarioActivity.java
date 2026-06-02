package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import edu.upc.dsa.dsa_android.network.ApiService;



import java.util.List;

import edu.upc.dsa.dsa_android.network.ApiService;
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
        btnVolverAlMenu = findViewById(R.id.btnVolverAlMenu);
        PB = findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        apiService = RetrofitClient.getInstance().getApi();

        recyclerViewInventario.setLayoutManager(new LinearLayoutManager(this));

        cargarObjetosDelUsuario();

        btnVolverAlMenu.setOnClickListener(v -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarObjetosDelUsuario() {
        String username = sharedPreferences.getString("username", null);
        if (username == null) {
            Toast.makeText(this, "Error: Sesión no iniciada", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressBarActivity.show(PB);

        Call<List<GameObject>> call = apiService.getUserObjects(username);
        call.enqueue(new Callback<List<GameObject>>() {
            @Override
            public void onResponse(Call<List<GameObject>> call, Response<List<GameObject>> response) {
                ProgressBarActivity.hide(PB);
                if (response.isSuccessful() && response.body() != null) {
                    List<GameObject> objetos = response.body();
                    if (objetos.isEmpty()) {
                        Toast.makeText(InventarioActivity.this, "No tienes objetos", Toast.LENGTH_SHORT).show();
                    }
                    adapter = new InventarioAdapter(InventarioActivity.this, objetos);
                    recyclerViewInventario.setAdapter(adapter);
                } else {
                    Log.e("InventarioActivity", "Error al cargar inventario: " + response.code());
                    Toast.makeText(InventarioActivity.this, "No se pudo cargar el inventario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GameObject>> call, Throwable t) {
                ProgressBarActivity.hide(PB);

                Log.e("InventarioActivity", "Fallo de red al cargar inventario", t);
                Toast.makeText(InventarioActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}