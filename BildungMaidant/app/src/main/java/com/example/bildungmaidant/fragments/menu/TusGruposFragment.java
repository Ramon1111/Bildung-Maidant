package com.example.bildungmaidant.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.GrupoAdapter;
import com.example.bildungmaidant.pojos.Grupo;

import java.util.ArrayList;

public class TusGruposFragment extends Fragment {
    private RecyclerView listaGrupos;
    private GrupoAdapter adapter;
    ArrayList<Grupo> grupos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tus_grupos,container,false);

        listaGrupos=v.findViewById(R.id.ftgRVGrupos);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaGrupos.setLayoutManager(llm);
        inicializarListaGrupo();
        inicializarAdaptadorGrupos();

        return v;
    }

    private void inicializarAdaptadorGrupos() {
        adapter = new GrupoAdapter(grupos,getActivity());
        listaGrupos.setAdapter(adapter);
    }

    private void inicializarListaGrupo() {
        grupos=new ArrayList<Grupo>();
        grupos.add(new Grupo("Temas Selectos de Programacion I", "Ing. Faraday58"));
        grupos.add(new Grupo("Mecanismos", "Mtro. Buen Cuenqui"));
        grupos.add(new Grupo("Ingenieria de Manufactura", "Mtro. El Robin"));

    }
}
