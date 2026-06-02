package edu.upc.dsa.dsa_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
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

public class SignupActivity extends AppCompatActivity {

    EditText etUsuari, etEmail, etPassword, etRepeatPassword;
    android.view.View strengthBar;
    Button btnSignUp, btnBackToMain;
    ApiService apiService;
    boolean showPass1 = false;
    boolean showPass2 = false;
    ProgressBar PB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsuari        = findViewById(R.id.editUsuari);
        etEmail         = findViewById(R.id.editEmail);
        etPassword      = findViewById(R.id.EditPassword);
        etRepeatPassword= findViewById(R.id.editPassword2);
        strengthBar     = findViewById(R.id.passwordStrengthBar);
        btnSignUp       = findViewById(R.id.SignUp);
        btnBackToMain   = findViewById(R.id.btnBackToMain);
        PB              = findViewById(R.id.progressBar);

        apiService = RetrofitClient.getInstance().getApi();

        btnSignUp.setOnClickListener(v -> handleSignUp());
        btnBackToMain.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });

        etPassword.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateStrengthBar(s.toString());
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        etPassword.setOnTouchListener((v, event) -> {
            int right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= etPassword.getRight()
                        - etPassword.getCompoundDrawables()[right].getBounds().width()) {
                    togglePassword();
                    return true;
                }
            }
            return false;
        });

        etRepeatPassword.setOnTouchListener((v, event) -> {
            int right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= etRepeatPassword.getRight()
                        - etRepeatPassword.getCompoundDrawables()[right].getBounds().width()) {
                    togglePassword2();
                    return true;
                }
            }
            return false;
        });
    }

    private void handleSignUp() {
        String usuari   = etUsuari.getText().toString().trim().toLowerCase();
        String email    = etEmail.getText().toString().trim().toLowerCase();
        String password = etPassword.getText().toString();
        String repeatPw = etRepeatPassword.getText().toString();

        if (usuari.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPw.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        if (!email.matches(emailRegex)) {
            Toast.makeText(this, "El formato del correo no es válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repeatPw)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordStrong(password)) {
            Toast.makeText(this,
                    "La contraseña debe tener mínimo 8 caracteres, mayúsculas, minúsculas y números.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Construir la petición con los campos del nuevo backend
        RegisterRequest request = new RegisterRequest(usuari, password, email);

        ProgressBarActivity.show(PB);

        Call<User> call = apiService.registerUser(request);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ProgressBarActivity.hide(PB);

                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this,
                            "Usuario registrado. ¡Ya puedes iniciar sesión!", Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.code() == 400) {
                    Toast.makeText(SignupActivity.this,
                            "Error: Datos inválidos o usuario ya existente.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignupActivity.this,
                            "Error desconocido: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ProgressBarActivity.hide(PB);
                Toast.makeText(SignupActivity.this,
                        "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isPasswordStrong(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(\\S+$).{8,}$";
        return password.matches(regex);
    }

    private void updateStrengthBar(String pass) {
        if (pass.length() < 4) {
            strengthBar.setBackgroundColor(Color.RED);
        } else if (pass.length() < 8) {
            strengthBar.setBackgroundColor(Color.YELLOW);
        } else if (isPasswordStrong(pass)) {
            strengthBar.setBackgroundColor(Color.GREEN);
        } else {
            strengthBar.setBackgroundColor(Color.YELLOW);
        }
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
