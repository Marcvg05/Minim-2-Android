package edu.upc.dsa.dsa_android;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventUsersActivity extends AppCompatActivity {

    ApiService apiService;

    Button btnVolverAlMenu;

    LinearLayout contenedorUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_users);

        setContentView(R.layout.activity_event_users);

        contenedorUsuarios = findViewById(R.id.contenedorUsuarios);

        btnVolverAlMenu = findViewById(R.id.btnVolver);

        apiService = RetrofitClient.getInstance().getApi();

        getUsers();

        btnVolverAlMenu.setOnClickListener(v -> {
            finish();
        });
    }

    private void getUsers() {
        Call<List<UserEvent>> call = apiService.getUsersByEvent("Evento1");

        call.enqueue(new Callback<List<UserEvent>>() {
            public void onResponse(Call<List<UserEvent>> call, Response<List<UserEvent>> response) {
                if (response.isSuccessful()) {
                    List<UserEvent> usuarios = response.body();
                    contenedorUsuarios.removeAllViews();

                    for (UserEvent u : usuarios) {
                        LinearLayout fila = new LinearLayout(EventUsersActivity.this);
                        fila.setOrientation(LinearLayout.HORIZONTAL);
                        fila.setPadding(0, 0, 0, 30);

                        ImageView imagen = new ImageView(EventUsersActivity.this);

                        Picasso.get().load(u.getImagen()).resize(150,150).into(imagen);

                        LinearLayout.LayoutParams paramsImg = new LinearLayout.LayoutParams(150, 150);
                        paramsImg.setMargins(0, 0, 20, 0);
                        imagen.setLayoutParams(paramsImg);

                        TextView texto = new TextView(EventUsersActivity.this);
                        texto.setText(u.getNombre() + "\n" + u.getApellidos());
                        texto.setTextSize(18);
                        texto.setTextColor(Color.BLACK);

                        fila.addView(imagen);
                        fila.addView(texto);

                        contenedorUsuarios.addView(fila);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserEvent>> call, Throwable t) {
                Toast.makeText(EventUsersActivity.this, "Error red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
