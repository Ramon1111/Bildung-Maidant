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

public class RecordatorioGeneralAdapter extends RecyclerView.Adapter<RecordatorioGeneralAdapter.RecordatorioGeneralViewHolder> {

    ArrayList<Recordatorio> recordatorios;
    Activity activity;

    public RecordatorioGeneralAdapter(ArrayList<Recordatorio> recordatorios, Activity activity) {
        this.recordatorios = recordatorios;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecordatorioGeneralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recordatorios_general,parent,false);
        return new RecordatorioGeneralAdapter.RecordatorioGeneralViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordatorioGeneralViewHolder holder, int position) {
        final Recordatorio recordatorio = recordatorios.get(position);
        holder.titulo.setText(recordatorio.getTitulo());
        holder.materia.setText(recordatorio.getGrupoPertenece());
        holder.descripcion.setText(recordatorio.getDescripcion());
        holder.fecha.setText(recordatorio.getFecha());
        holder.hora.setText(recordatorio.getHora());
    }

    @Override
    public int getItemCount() {
        return recordatorios.size();
    }

    public static class RecordatorioGeneralViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo;
        private TextView materia;
        private TextView descripcion;
        private TextView fecha;
        private TextView hora;

        public RecordatorioGeneralViewHolder(View itemView){
            super(itemView);
            titulo=itemView.findViewById(R.id.crgTVTitulo);
            materia=itemView.findViewById(R.id.crgTVMateria);
            descripcion=itemView.findViewById(R.id.crgTVDescripcion);
            fecha=itemView.findViewById(R.id.crgTVFecha);
            hora=itemView.findViewById(R.id.crgTVHora);
        }
    }
}
