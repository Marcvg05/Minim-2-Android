package edu.upc.dsa.dsa_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.content.Context;

import android.widget.Toast;

public class InicioLoginActivity extends AppCompatActivity {

    Button buttonTienda;
    Button buttonLogOut;
    Button buttonInventario;
    Button buttonExtras;

    Button buttonJugar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonLogOut = findViewById(R.id.buttonLogOut);
        buttonInventario = findViewById(R.id.buttonInventario);
        buttonExtras = findViewById(R.id.buttonExtras);
        buttonTienda = findViewById(R.id.buttonTienda);
        buttonJugar = findViewById(R.id.buttonJugar);


        buttonLogOut.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(InicioLoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        buttonInventario.setOnClickListener(v -> {
            Intent intent = new Intent(InicioLoginActivity.this, InventarioActivity.class);
            startActivity(intent);
        });

        buttonExtras.setOnClickListener(v -> {
            Intent intent = new Intent(InicioLoginActivity.this, ExtrasActivity.class);
            startActivity(intent);
        });

        buttonTienda.setOnClickListener(v -> {
            Intent intent = new Intent(InicioLoginActivity.this, TiendaActivity.class);
            startActivity(intent);
        });

        buttonJugar.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String packageName = "com.UnityTechnologies.com.unity.template.urpblank";

            if (!username.isEmpty()) {
                // Intentar obtener el intent de lanzamiento del paquete
                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

                if (intent != null) {
                    // Pasamos el username como extra
                    intent.putExtra("USERNAME", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        startActivity(intent);
                        Toast.makeText(this, "Abriendo juego...", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error al abrir: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Si el intent es null, es que la app no está instalada o el nombre del paquete es incorrecto
                    Toast.makeText(this, "El juego no está instalado. Verifica el nombre del paquete.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Error: No hay usuario logueado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
