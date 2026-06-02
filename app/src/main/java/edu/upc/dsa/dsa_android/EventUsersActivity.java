package edu.upc.dsa.dsa_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * EventUsersActivity - No disponible en el backend actual (DSA_BackEnd_2.0).
 */
public class EventUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_users);

        Button btnVolver = findViewById(R.id.btnVolver);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }

        Toast.makeText(this,
                "Los usuarios de eventos no están disponibles en esta versión",
                Toast.LENGTH_LONG).show();
    }
}
