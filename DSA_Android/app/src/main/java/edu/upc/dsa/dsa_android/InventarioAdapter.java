package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> {

    private List<GameObject> userObjects;
    private LayoutInflater inflater;

    // Mismo mapa de emojis que TiendaAdapter / BackFront common.js
    private static String getEmojiForType(String tipo) {
        if (tipo == null) return "📦";
        switch (tipo.toUpperCase()) {
            case "ESPADA":   return "⚔️";
            case "ESCUDO":   return "🛡️";
            case "POCION":   return "🧪";
            case "ARMADURA": return "🦺";
            case "ANILLO":   return "💍";
            case "AMULETO":  return "💎";
            case "ARCO":     return "🏹";
            case "HACHA":    return "🪓";
            case "LANZA":    return "🔱";
            case "BASICA":   return "🎯";
            case "HIELO":    return "❄️";
            case "AOE":      return "💣";
            case "SNIPER":   return "🔭";
            default:         return "📦";
        }
    }

    public InventarioAdapter(Context context, List<GameObject> userObjects) {
        this.inflater = LayoutInflater.from(context);
        this.userObjects = userObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_inventario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameObject gameObject = userObjects.get(position);
        holder.tvObjectIcon.setText(getEmojiForType(gameObject.getTipo()));
        holder.tvObjectName.setText(gameObject.getNombre());
        // textViewObjectDescription muestra el tipo del objeto (como el BackFront)
        holder.tvObjectDescription.setText(gameObject.getTipo() != null ? gameObject.getTipo() : "");
        holder.tvObjectQuantity.setText("x " + gameObject.getCantidad());
    }

    @Override
    public int getItemCount() {
        return userObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvObjectIcon;
        TextView tvObjectName;
        TextView tvObjectDescription;
        TextView tvObjectQuantity;

        ViewHolder(View itemView) {
            super(itemView);
            tvObjectIcon        = itemView.findViewById(R.id.textViewObjectIcon);
            tvObjectName        = itemView.findViewById(R.id.textViewObjectName);
            tvObjectDescription = itemView.findViewById(R.id.textViewObjectDescription);
            tvObjectQuantity    = itemView.findViewById(R.id.textViewObjectQuantity);
        }
    }
}