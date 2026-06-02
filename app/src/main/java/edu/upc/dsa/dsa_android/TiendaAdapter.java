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

    private final List<Item> items;
    private final LayoutInflater inflater;
    private final OnItemBuyClickListener buyClickListener;

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

    public interface OnItemBuyClickListener {
        void onBuyClick(Item item);
    }

    public TiendaAdapter(Context context, List<Item> items, OnItemBuyClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
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
        Item item = items.get(position);
        holder.tvObjectName.setText(item.getName());
        holder.tvObjectType.setText(item.getType() != null ? item.getType() : "");
        holder.tvObjectPrice.setText("💰 " + String.format("%.0f", item.getPrice()));

        // Cargar imagen local dinámica según el assetName
        Context context = holder.itemView.getContext();
        String asset = item.getAssetName();
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

        // Deshabilitar botón si el item no está disponible
        holder.btnComprar.setEnabled(item.isAvailable());
        holder.btnComprar.setAlpha(item.isAvailable() ? 1f : 0.4f);

        holder.btnComprar.setOnClickListener(v -> {
            if (buyClickListener != null) {
                buyClickListener.onBuyClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView ivObjectIcon;
        TextView tvObjectName;
        TextView tvObjectType;
        TextView tvObjectPrice;
        Button btnComprar;

        ViewHolder(View itemView) {
            super(itemView);
            ivObjectIcon  = itemView.findViewById(R.id.imageViewObjectIcon);
            tvObjectName  = itemView.findViewById(R.id.textViewObjectName);
            tvObjectType  = itemView.findViewById(R.id.textViewObjectType);
            tvObjectPrice = itemView.findViewById(R.id.textViewObjectPrice);
            btnComprar    = itemView.findViewById(R.id.btnComprar);
        }
    }
}