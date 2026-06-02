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

    private final List<InventoryEntry> entries;
    private final LayoutInflater inflater;

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
            case "WEAPON":   return "⚔️";
            case "ARMOR":    return "🦺";
            case "POTION":   return "🧪";
            case "RING":     return "💍";
            case "BOOST":    return "⚡";
            case "CONSUMABLE": return "🔧";
            default:         return "📦";
        }
    }

    public InventarioAdapter(Context context, List<InventoryEntry> entries) {
        this.inflater = LayoutInflater.from(context);
        this.entries = entries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_inventario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryEntry entry = entries.get(position);
        holder.tvObjectName.setText(entry.getNombre());
        holder.tvObjectDescription.setText(entry.getItemDescription() != null ? entry.getItemDescription() : "ID item: " + entry.getItemId());
        holder.tvObjectQuantity.setText("x " + entry.getQuantity());

        // Cargar imagen local dinámica según el assetName
        Context context = holder.itemView.getContext();
        String asset = entry.getItemAssetName();
        int resId = 0;
        if (asset != null && !asset.trim().isEmpty()) {
            if (asset.endsWith(".png")) {
                asset = asset.substring(0, asset.length() - 4);
            }
            resId = context.getResources().getIdentifier(asset, "drawable", context.getPackageName());
        }

        if (resId != 0) {
            holder.ivObjectIcon.setImageResource(resId);
        } else {
            // Imagen por defecto si no se encuentra
            holder.ivObjectIcon.setImageResource(R.drawable.combined_logo);
        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView ivObjectIcon;
        TextView tvObjectName;
        TextView tvObjectDescription;
        TextView tvObjectQuantity;

        ViewHolder(View itemView) {
            super(itemView);
            ivObjectIcon        = itemView.findViewById(R.id.imageViewObjectIcon);
            tvObjectName        = itemView.findViewById(R.id.textViewObjectName);
            tvObjectDescription = itemView.findViewById(R.id.textViewObjectDescription);
            tvObjectQuantity    = itemView.findViewById(R.id.textViewObjectQuantity);
        }
    }
}