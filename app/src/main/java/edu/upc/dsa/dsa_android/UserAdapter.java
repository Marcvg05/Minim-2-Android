package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final List<User> users;
    private final LayoutInflater inflater;

    public UserAdapter(Context context, List<User> users) {
        this.inflater = LayoutInflater.from(context);
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        
        holder.tvUsername.setText(user.getUsername());
        holder.tvLevel.setText("Nivel: " + user.getLevel() + " | Permisos: " + user.getPermissions());
        holder.tvSaldo.setText(String.format("%.0f", user.getSaldo()));
        
        // Simular iniciales para el avatar (ej: "AD" para "admin")
        String initials = "US";
        if (user.getUsername() != null && user.getUsername().length() >= 2) {
            initials = user.getUsername().substring(0, 2).toUpperCase();
        } else if (user.getUsername() != null && user.getUsername().length() == 1) {
            initials = user.getUsername().toUpperCase() + "X";
        }
        holder.tvAvatar.setText(initials);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar;
        TextView tvUsername;
        TextView tvLevel;
        TextView tvSaldo;

        ViewHolder(View itemView) {
            super(itemView);
            tvAvatar = itemView.findViewById(R.id.tvAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            tvSaldo = itemView.findViewById(R.id.tvSaldo);
        }
    }
}
