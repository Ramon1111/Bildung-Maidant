package com.example.bildungmaidant.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.pojos.Avisos;

import java.util.ArrayList;

public class AvisosAdapter extends RecyclerView.Adapter<AvisosAdapter.AvisosViewHolder> {
    ArrayList<Avisos> avisos;
    Activity activity;

    public AvisosAdapter(ArrayList<Avisos> avisos, Activity activity) {
        this.avisos = avisos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AvisosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_avisos,parent,false);
        return new AvisosAdapter.AvisosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AvisosViewHolder holder, int position) {
        final Avisos aviso = avisos.get(position);
        holder.titulo.setText(aviso.getTitulo());
        holder.descripcion.setText(aviso.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return avisos.size();
    }

    public static class AvisosViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo;
        private TextView descripcion;

        public AvisosViewHolder(View view){
            super(view);
            titulo=view.findViewById(R.id.cvaTVTitulo);
            descripcion=view.findViewById(R.id.cvaTVDescripcion);
        }
    }

}
