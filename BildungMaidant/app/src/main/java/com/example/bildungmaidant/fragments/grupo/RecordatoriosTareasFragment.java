package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.RecordatorioAdapter;
import com.example.bildungmaidant.fragments.NuevoRecordatorioFragment;
import com.example.bildungmaidant.pojos.Recordatorio;

import java.util.ArrayList;

public class RecordatoriosTareasFragment extends Fragment {
    private RecyclerView listaRecordatorios;
    private RecordatorioAdapter adapter;
    ArrayList<Recordatorio> recordatorios;

    private ImageButton ibNuevoRecordatorio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recordatorios_tareas,container,false);

        listaRecordatorios=v.findViewById(R.id.frtRVRecordatorios);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaRecordatorios.setLayoutManager(llm);
        inicializarListaRecordatorios();
        inicializarAdaptadorRecordatorios();

        ibNuevoRecordatorio=v.findViewById(R.id.frtIBNuevoRecordatorio);

        ibNuevoRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Fragment fragmentNuevoRecordatorio = new NuevoRecordatorioFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fnrNuevoRecordatorioContent,fragmentNuevoRecordatorio);
                transaction.addToBackStack(null);
                transaction.commit();
                 */
            }
        });


        return v;
    }

    private void inicializarAdaptadorRecordatorios() {
        adapter = new RecordatorioAdapter(recordatorios,getActivity());
        listaRecordatorios.setAdapter(adapter);
    }

    private void inicializarListaRecordatorios() {
        recordatorios=new ArrayList<Recordatorio>();
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
    }
}
