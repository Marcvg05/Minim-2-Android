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

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    public interface OnInscribirseClick {
        void onClick(Evento evento);
    }

    private final Context context;
    private final List<Evento> eventos;
    private final OnInscribirseClick listener;

    public EventoAdapter(Context context, List<Evento> eventos, OnInscribirseClick listener) {
        this.context = context;
        this.eventos = eventos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventos.get(position);

        holder.nombre.setText(evento.getName());
        holder.descripcion.setText(evento.getDescription());
        holder.fechaInicio.setText(evento.getStartDate());
        holder.fechaFin.setText(evento.getEndDate());

        int resId = context.getResources().getIdentifier(
                "evento_" + evento.getId(),
                "drawable",
                context.getPackageName()
        );

        if (resId != 0) {
            holder.imagen.setImageResource(resId);
        } else {
            holder.imagen.setImageResource(android.R.color.transparent);
        }

        holder.inscribirseBtn.setOnClickListener(v -> {
            if (listener != null) listener.onClick(evento);
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView nombre;
        TextView descripcion;
        TextView fechaInicio;
        TextView fechaFin;
        Button inscribirseBtn;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.eventImage);
            nombre = itemView.findViewById(R.id.eventName);
            descripcion = itemView.findViewById(R.id.eventDescription);
            fechaInicio = itemView.findViewById(R.id.eventStartDate);
            fechaFin = itemView.findViewById(R.id.eventEndDate);
            inscribirseBtn = itemView.findViewById(R.id.eventButton);
        }
    }
}