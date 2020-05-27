package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.RecordatorioAdapter;
import com.example.bildungmaidant.pojos.Recordatorio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecordatoriosTareasFragment extends Fragment {
    private RecyclerView listaRecordatorios;
    private RecordatorioAdapter adapter;
    ArrayList<Recordatorio> recordatorios;

    private ImageButton ibNuevoRecordatorio;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ArrayList<String> recordatoriosUsuario;

    private String claveGrupoActual;

    public RecordatoriosTareasFragment(String claveGrupoActual){
        this.claveGrupoActual=claveGrupoActual;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recordatorios_tareas,container,false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

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
                cargarFragment(new NuevoRecordatorioFragment(claveGrupoActual),v);
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

    private void cargarFragment(Fragment fragment, View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack("").commit();
    }
}
