package com.example.bildungmaidant.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.grupo.recordatorios.InfoRecordatorioFragment;
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

        holder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nombreRecordatorio",recordatorio.getTitulo());
                bundle.putString("descripcionRecordatorio",recordatorio.getDescripcion());
                bundle.putString("fechaRecordatorio",recordatorio.getFecha());
                bundle.putString("horaRecordatorio",recordatorio.getHora());
                bundle.putString("claveRecordatorio",recordatorio.getClaveRecordatorio());
                InfoRecordatorioFragment infoRecordatorioFragment = new InfoRecordatorioFragment();
                infoRecordatorioFragment.setArguments(bundle);
                cargarFragment(infoRecordatorioFragment,v);
            }
        });
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

        public RecordatorioViewHolder(View itemView){
            super(itemView);
            titulo=itemView.findViewById(R.id.crtTVTitulo);
            descripcion=itemView.findViewById(R.id.crtTVDescripcion);
            fecha=itemView.findViewById(R.id.crtTVFecha);
            hora=itemView.findViewById(R.id.crtTVHora);
        }
    }

    private void cargarFragment(Fragment fragment,View v){
        /*
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack("").commit();
         */
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }

}
