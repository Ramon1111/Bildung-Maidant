package com.example.bildungmaidant.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.grupo.ContenedorGrupoFragment;
import com.example.bildungmaidant.pojos.Grupo;

import java.util.ArrayList;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoViewHolder> {

    ArrayList<Grupo> grupos;
    Activity activity;

    public GrupoAdapter(ArrayList<Grupo> grupos, Activity activity) {
        this.grupos = grupos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GrupoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_grupo,parent,false);
        return new GrupoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoAdapter.GrupoViewHolder holder, int position) {
        final Grupo grupo = grupos.get(position);
        holder.titulo.setText(grupo.getNombreGrupo());
        holder.administrador.setText(grupo.getAdministrador());

        holder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new ContenedorGrupoFragment(),v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return grupos.size();
    }

    public static class GrupoViewHolder extends RecyclerView.ViewHolder {
        private TextView titulo;
        private TextView administrador;

        public GrupoViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo=itemView.findViewById(R.id.cgTVTitulo);
            administrador=itemView.findViewById(R.id.cgTVAdministrador);
        }
    }

    private void cargarFragment(Fragment fragment, View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }
}
