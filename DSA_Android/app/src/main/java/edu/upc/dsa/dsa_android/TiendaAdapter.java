package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.ViewHolder> {

    private List<GameObject> gameObjects;
    private LayoutInflater inflater;
    private OnItemBuyClickListener buyClickListener;

    // Mapa de emojis por tipo, igual que getIconForType() del BackFront common.js
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

    public interface OnItemBuyClickListener {
        void onBuyClick(GameObject gameObject);
    }

    public TiendaAdapter(Context context, List<GameObject> gameObjects, OnItemBuyClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.gameObjects = gameObjects;
        this.buyClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_tienda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameObject gameObject = gameObjects.get(position);
        holder.tvObjectIcon.setText(getEmojiForType(gameObject.getTipo()));
        holder.tvObjectName.setText(gameObject.getNombre());
        holder.tvObjectType.setText(gameObject.getTipo() != null ? gameObject.getTipo() : "");
        holder.tvObjectPrice.setText("💰 " + gameObject.getPrecio());

        holder.btnComprar.setOnClickListener(v -> {
            if (buyClickListener != null) {
                buyClickListener.onBuyClick(gameObject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvObjectIcon;
        TextView tvObjectName;
        TextView tvObjectType;
        TextView tvObjectPrice;
        Button btnComprar;

        ViewHolder(View itemView) {
            super(itemView);
            tvObjectIcon  = itemView.findViewById(R.id.textViewObjectIcon);
            tvObjectName  = itemView.findViewById(R.id.textViewObjectName);
            tvObjectType  = itemView.findViewById(R.id.textViewObjectType);
            tvObjectPrice = itemView.findViewById(R.id.textViewObjectPrice);
            btnComprar    = itemView.findViewById(R.id.btnComprar);
        }
    }
}