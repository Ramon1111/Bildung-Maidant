package com.example.bildungmaidant.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.pojos.Recordatorio;

import java.util.ArrayList;

public class RecordatorioAdapter extends RecyclerView.Adapter<RecordatorioAdapter.RecordatorioViewHolder> {

    ArrayList<Recordatorio> recordatorios;
    Activity activity;

    public RecordatorioAdapter(ArrayList<Recordatorio> recordatorios, Activity activity) {
        this.recordatorios = recordatorios;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecordatorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recordatorios_tareas,parent,false);
        return new RecordatorioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordatorioViewHolder holder, int position) {
        final Recordatorio recordatorio = recordatorios.get(position);
        holder.titulo.setText(recordatorio.getTitulo());
        holder.descripcion.setText(recordatorio.getDescripcion());
        holder.fecha.setText(recordatorio.getFecha());
        holder.hora.setText(recordatorio.getHora());
    }

    @Override
    public int getItemCount() {
        return recordatorios.size();
    }

    public static class RecordatorioViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo;
        private TextView descripcion;
        private TextView fecha;
        private TextView hora;

        public RecordatorioViewHolder(View itemViem){
            super(itemViem);
            titulo=itemViem.findViewById(R.id.crtTVTitulo);
            descripcion=itemViem.findViewById(R.id.crtTVDescripcion);
            fecha=itemViem.findViewById(R.id.crtTVFecha);
            hora=itemViem.findViewById(R.id.crtTVHora);
        }
    }
}
