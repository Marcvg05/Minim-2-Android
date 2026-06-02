package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * TeamAdapter - Adaptador de equipos. Los equipos no están en el backend actual.
 * Se mantiene para que compile correctamente, pero no hace llamadas de red.
 */
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private final List<Team> data;

    public TeamAdapter(List<Team> data, Context context, String username) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team t = data.get(position);
        holder.tvName.setText(t.getName());
        holder.tvPoints.setText(t.getPoints() + " pts");
        // Sin llamadas de red - funcionalidad no disponible en el backend actual
        holder.btnJoin.setEnabled(false);
        holder.btnJoin.setText("No disponible");
    }

    @Override
    public int getItemCount() { return data.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPoints;
        ImageView ivAvatar;
        Button btnJoin;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvPoints = v.findViewById(R.id.tvPoints);
            ivAvatar = v.findViewById(R.id.ivAvatar);
            btnJoin = v.findViewById(R.id.btnJoin);
        }
    }
}