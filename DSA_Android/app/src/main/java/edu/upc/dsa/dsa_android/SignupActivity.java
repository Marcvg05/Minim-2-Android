package edu.upc.dsa.dsa_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import edu.upc.dsa.dsa_android.network.ApiService;


import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText etUsuari, etEmail, etPassword, etRepeatPassword;
    Button btnSignUp, btnBackToMain;
    ApiService apiService;
    boolean showPass1 = false;
    boolean showPass2 = false;
    ProgressBar PB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsuari = findViewById(R.id.editUsuari);
        etEmail = findViewById(R.id.editEmail);
        etPassword = findViewById(R.id.EditPassword);
        etRepeatPassword = findViewById(R.id.editPassword2);
        btnSignUp = findViewById(R.id.SignUp);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        apiService = RetrofitClient.getInstance().getApi();
        PB = findViewById(R.id.progressBar);

        btnSignUp.setOnClickListener(v -> handleSignUp());
        btnBackToMain.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });

        etPassword.setOnTouchListener((v, event) -> {
            int right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= etPassword.getRight() - etPassword.getCompoundDrawables()[right].getBounds().width()) {
                    togglePassword();
                    return true;
                }
            }
            return false;
        });

        etRepeatPassword.setOnTouchListener((v, event) -> {
            int right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= etRepeatPassword.getRight() - etRepeatPassword.getCompoundDrawables()[right].getBounds().width()) {
                    togglePassword2();
                    return true;
                }
            }
            return false;
        });
    }

    private void handleSignUp() {
        String usuari = etUsuari.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String repeatPassword = etRepeatPassword.getText().toString();

        if (usuari.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        if (!email.matches(emailRegex)) {
            Toast.makeText(this, "El formato del correo no es válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailMinuscula = email.toLowerCase();
        String usuariMinuscula = usuari.toLowerCase();

        Credentials credentials = new Credentials();
        credentials.setNombre(usuariMinuscula);
        credentials.setEmail(emailMinuscula);
        credentials.setPassword(password);

        ProgressBarActivity.show(PB);

        Call<User> call = apiService.registerUser(credentials);
        call.enqueue(new Callback<User>() {
            @Override public void onResponse(Call<User> call, Response<User> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Usuario registrado! Ya puedes iniciar sesión.", Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.code() == 409) {
                    Toast.makeText(SignupActivity.this, "Error: El usuario ya existe.", Toast.LENGTH_LONG).show();
                } else if (response.code() == 410) {
                    Toast.makeText(SignupActivity.this, "Error: El correo ya está registrado.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Error desconocido en el registro.", Toast.LENGTH_LONG).show();
                }
            }
            @Override public void onFailure(Call<User> call, Throwable t) {
                ProgressBarActivity.hide(PB);

                Toast.makeText(SignupActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void togglePassword() {
        if (showPass1) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
            etPassword.setSelection(etPassword.getText().length());
            showPass1 = false;
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
            etPassword.setSelection(etPassword.getText().length());
            showPass1 = true;
        }
    }

    private void togglePassword2() {
        if (showPass2) {
            etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etRepeatPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
            etRepeatPassword.setSelection(etRepeatPassword.getText().length());
            showPass2 = false;
        } else {
            etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etRepeatPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
            etRepeatPassword.setSelection(etRepeatPassword.getText().length());
            showPass2 = true;
        }
    }
}
