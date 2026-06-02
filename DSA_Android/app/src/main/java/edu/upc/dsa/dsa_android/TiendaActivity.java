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
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import edu.upc.dsa.dsa_android.network.ApiService;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.upc.dsa.dsa_android.network.ApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        tvMonedas = findViewById(R.id.textViewMonedas);
        tvUsuario = findViewById(R.id.textViewUsuario);
        recyclerViewTienda = findViewById(R.id.recyclerViewTienda);
        recyclerViewTienda.setLayoutManager(new LinearLayoutManager(this));
        PB = findViewById(R.id.progressBar);

        // Mostrar username en el banner (como el BackFront muestra "Bienvenido de nuevo, usuario")
        String savedUsername = sharedPreferences.getString("username", "...");
        if (tvUsuario != null) tvUsuario.setText(savedUsername);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        apiService = RetrofitClient.getInstance().getApi();

        actualizarMonedasUI();
        cargarTienda();

        btnBackToInicioLogin.setOnClickListener(v -> {
            Intent intent = new Intent(TiendaActivity.this, InicioLoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        String username = sharedPreferences.getString("username", null);
        if (username == null) {
            Toast.makeText(this, "Error: Sesión no iniciada", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressBarActivity.show(PB);

        Call<User> call = apiService.getUser(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful() && response.body() != null) {
                    User usuarioActualizado = response.body();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("monedas", usuarioActualizado.getMonedas());
                    editor.apply();

                    actualizarMonedasUI();
                    Log.d("TiendaActivity", "Datos del usuario actualizados desde la API.");

                } else {
                    Log.e("TiendaActivity", "Error al cargar datos del usuario: " + response.code());
                    actualizarMonedasUI();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ProgressBarActivity.hide(PB);

                Log.e("TiendaActivity", "Fallo de red al cargar datos del usuario.", t);
                Toast.makeText(TiendaActivity.this, "Fallo de conexión. Mostrando datos locales.", Toast.LENGTH_SHORT)
                        .show();
                actualizarMonedasUI();
            }
        });
    }

    private void cargarTienda() {
        ProgressBarActivity.show(PB);

        Call<List<GameObject>> call = apiService.getALLGameObjects();
        call.enqueue(new Callback<List<GameObject>>() {
            @Override
            public void onResponse(Call<List<GameObject>> call, Response<List<GameObject>> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful() && response.body() != null) {
                    List<GameObject> objetos = response.body();

                    // Configura el adaptador con la lista de objetos
                    adapter = new TiendaAdapter(TiendaActivity.this, objetos, gameObject -> {
                        // Lógica de compra al hacer clic en el botón
                        handleCompra(gameObject);
                    });
                    recyclerViewTienda.setAdapter(adapter);

                } else {
                    Toast.makeText(TiendaActivity.this, "Error cargando la tienda", Toast.LENGTH_SHORT).show();
                    Log.e("TiendaActivity", "Error cargando tienda: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<GameObject>> call, Throwable t) {
                ProgressBarActivity.hide(PB);

                Toast.makeText(TiendaActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("TiendaActivity", "Error en onFailure al cargar tienda", t);
            }
        });
    }

    private void handleCompra(GameObject item) {
        SharedPreferences prefs = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username == null) {
            Toast.makeText(this, "Error: Sesión no iniciada", Toast.LENGTH_LONG).show();
            return;
        }

        String objectId = item.getId();
        if (objectId == null) {
            Toast.makeText(this, "Error: Objeto no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        CompraRequest request = new CompraRequest(username, objectId);

        ProgressBarActivity.show(PB);

        Call<User> call = apiService.comprarItem(request);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful() && response.body() != null) {
                    User usuarioActualizado = response.body();
                    Toast.makeText(TiendaActivity.this, item.getNombre() + " comprado con éxito!", Toast.LENGTH_SHORT)
                            .show();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("monedas", usuarioActualizado.getMonedas());
                    editor.apply();

                    actualizarMonedasUI();
                } else {
                    String errorMessage = "Error " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e("TiendaActivity", "Error al parsear el errorBody", e);
                    }
                    Toast.makeText(TiendaActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("TiendaActivity", "Error en la compra: " + response.code() + " - " + errorMessage);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ProgressBarActivity.hide(PB);

                Toast.makeText(TiendaActivity.this, "Fallo de conexión:: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("TiendaActivity", "Error en onFailure al comprar", t);
            }
        });
    }

    private void actualizarMonedasUI() {
        int monedas = sharedPreferences.getInt("monedas", 0);
        tvMonedas.setText("💰 " + monedas);
    }
}
