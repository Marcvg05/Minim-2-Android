package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnBackToMain;
    ApiService apiService;
    ProgressBar PB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLoginSubmit);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        PB = findViewById(R.id.progressBar);

        apiService = RetrofitClient.getInstance().getApi();

        btnLogin.setOnClickListener(v -> handleLogin());
        btnBackToMain.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim().toLowerCase();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Introduce usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressBarActivity.show(PB);

        LoginRequest request = new LoginRequest(username, password);

        Call<User> call = apiService.loginUser(request);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    String sessionUsername = user.getUsername();
                    if (sessionUsername == null || sessionUsername.trim().isEmpty()) {
                        sessionUsername = username;
                    } else {
                        sessionUsername = sessionUsername.trim().toLowerCase();
                    }

                    Toast.makeText(LoginActivity.this,
                            "¡Bienvenido " + sessionUsername + "!", Toast.LENGTH_LONG).show();

                    SharedPreferences prefs = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", sessionUsername);
                    editor.putInt("userId", user.getId());
                    editor.putFloat("saldo", (float) user.getSaldo());
                    editor.putInt("level", user.getLevel());
                    String permissions = user.getPermissions();
                    if (permissions != null) {
                        editor.putString("permissions", permissions);
                    }
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, InicioLoginActivity.class));
                    finish();

                } else if (response.code() == 401) {
                    Toast.makeText(LoginActivity.this,
                            "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("LoginActivity", "Error: " + response.code());
                    Toast.makeText(LoginActivity.this,
                            "Error " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ProgressBarActivity.hide(PB);
                Toast.makeText(LoginActivity.this,
                        "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LoginActivity", "onFailure", t);
            }
        });
    }
}
