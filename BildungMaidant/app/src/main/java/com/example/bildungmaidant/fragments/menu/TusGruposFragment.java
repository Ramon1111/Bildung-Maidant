package com.example.bildungmaidant.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.GrupoAdapter;
import com.example.bildungmaidant.fragments.grupo.AddgrupoFragment;
import com.example.bildungmaidant.fragments.grupo.UnirseFragment;
import com.example.bildungmaidant.pojos.Grupo;

import java.util.ArrayList;

public class TusGruposFragment extends Fragment {
    private RecyclerView listaGrupos;
    private GrupoAdapter adapter;
    ArrayList<Grupo> grupos;
    Button fgrubtnañadir, fgrubtnunirse;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tus_grupos,container,false);

        listaGrupos=v.findViewById(R.id.ftgRVGrupos);
        fgrubtnañadir=v.findViewById(R.id.fgrubtnañadir);
        fgrubtnunirse=v.findViewById(R.id.fgrubtnunirse);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaGrupos.setLayoutManager(llm);
        inicializarListaGrupo();
        inicializarAdaptadorGrupos();

        fgrubtnañadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //me lleva a Unirse a dar de alta uno
                FragmentTransaction fr= getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new AddgrupoFragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        fgrubtnunirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //me lleva a Unirse a un nuevo grupo
                FragmentTransaction fr= getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new UnirseFragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(),"fragment destruido: TusGrupos Fragment", Toast.LENGTH_SHORT).show();
    }
}
