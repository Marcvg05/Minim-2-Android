package edu.upc.dsa.dsa_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * MyTeamActivity - Los equipos no están implementados en el backend actual (DSA_BackEnd_2.0).
 */
public class MyTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        TextView tvTeamTitle = findViewById(R.id.tvTeamTitle);
        Button btnVolver = findViewById(R.id.btnVolver);
        RecyclerView recyclerView = findViewById(R.id.recyclerMembers);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        if (tvTeamTitle != null) {
            tvTeamTitle.setText("SIN EQUIPO");
        }
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }

        Toast.makeText(this,
                "Los equipos no están disponibles en esta versión del servidor",
                Toast.LENGTH_LONG).show();
    }
}