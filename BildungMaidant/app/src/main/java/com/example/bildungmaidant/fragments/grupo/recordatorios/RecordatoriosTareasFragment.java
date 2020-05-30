package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.RecordatorioAdapter;
import com.example.bildungmaidant.pojos.Recordatorio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecordatoriosTareasFragment extends Fragment {
    private RecyclerView listaRecordatorios;
    private RecordatorioAdapter adapter;
    private ArrayList<Recordatorio> recordatorios;

    private ImageButton ibNuevoRecordatorio;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ArrayList<String> listaGrupoRecordatorio;

    private String claveGrupoActual;

    public RecordatoriosTareasFragment(String claveGrupoActual, ArrayList<String> listaGrupoRecordatorios){
        this.claveGrupoActual=claveGrupoActual;
        this.listaGrupoRecordatorio=listaGrupoRecordatorios;
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
        /*
        recordatorios=new ArrayList<Recordatorio>();
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
         */
        ObtenerRecordatoriosUsuario();
    }

    private void ObtenerRecordatoriosUsuario() {
        recordatorios = new ArrayList<>();
        int bandera=0;

        for(String recordatorio : listaGrupoRecordatorio) {
            bandera++;
            final int finalBandera = bandera;
            db.collection("recordatorios")

                .whereEqualTo("claveRecordatorio",recordatorio)
                .whereEqualTo("administrador",currentUser.getUid())
                .whereEqualTo("grupoPertenece",claveGrupoActual)

                .get() //esto obtiene el arreglo del documento
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            /*if (task.getResult().size() == 0) {
                                //Toast.makeText(getContext(), "No se encontró ningun recordatorio.", Toast.LENGTH_SHORT).show();
                            }*/
                            if(task.getResult().size()>0){
                            //else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Toast.makeText(getContext(),"Hola",Toast.LENGTH_SHORT).show();
                                    recordatorios.add(new Recordatorio(
                                            document.get("nombreRecordatorio").toString(),
                                            document.get("descripcion").toString(),
                                            document.get("fecha").toString(),
                                            document.get("hora").toString(),
                                            document.get("administrador").toString(),
                                            document.get("claveRecordatorio").toString(),
                                            document.get("grupoPertenece").toString(),
                                            document.getBoolean("estadoEnProceso")));
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(finalBandera ==listaGrupoRecordatorio.size()){
                        inicializarAdaptadorRecordatorios();
                    }
                }
            });
        }
    }

    private void cargarFragment(Fragment fragment, View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack("").commit();
    }
}
