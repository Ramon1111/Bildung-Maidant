package com.example.bildungmaidant.fragments.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.RecordatorioGeneralAdapter;
import com.example.bildungmaidant.pojos.Recordatorio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecordatorioGeneralFragment extends Fragment {
    private RecyclerView listaRecordatorios;
    private RecordatorioGeneralAdapter adapter;
    private ArrayList<Recordatorio> recordatorios;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ArrayList<String> recordatoriosUsuario;
    private ArrayList<String> nombresGrupo;
    private ArrayList<String> gruposUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recordatorios_general,container,false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        listaRecordatorios=v.findViewById(R.id.frgRVRecordatoriosGeneral);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaRecordatorios.setLayoutManager(llm);

        inicializarListaRecordatoriosGeneral();
        return v;
    }

    private void inicializarListaRecordatoriosGeneral() {
        recordatoriosUsuario = new ArrayList<>();
        gruposUsuario=new ArrayList<>();

        final DocumentReference docRefUsuario = db.collection("users").document(currentUser.getUid());

        docRefUsuario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        recordatoriosUsuario=(ArrayList)document.get("arrayRecordatoriosAbiertos");
                        if(recordatoriosUsuario.size()>0){
                            ObtenerRecordatoriosGeneralUsuario();
                        }else{
                            Toast.makeText(getContext(),"No hay recordatorios de usuario",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void ObtenerRecordatoriosGeneralUsuario() {
        recordatorios = new ArrayList<>();

        for(final String recordatorio : recordatoriosUsuario) {

            db.collection("recordatorios")
                    .whereEqualTo("claveRecordatorio",recordatorio)
                    .get() //esto obtiene el arreglo del documento
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().size()>0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String nombreRecordatorio,descripcion, fecha, hora, administrador, claveRecordatorio, grupoPertenece;
                                        Boolean estadoEnProceso;

                                        nombreRecordatorio=document.get("nombreRecordatorio").toString();
                                        descripcion=document.get("descripcion").toString();
                                        fecha=document.get("fecha").toString();
                                        hora=document.get("hora").toString();
                                        administrador=document.get("administrador").toString();
                                        claveRecordatorio=document.get("claveRecordatorio").toString();
                                        grupoPertenece=document.get("grupoPertenece").toString();
                                        estadoEnProceso=document.getBoolean("estadoEnProceso");

                                        CambiarClaveNombreGrupo(nombreRecordatorio,descripcion,fecha,hora,administrador,claveRecordatorio,grupoPertenece,estadoEnProceso);
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void CambiarClaveNombreGrupo(final String nombreRecordatorio, final String descripcion, final String fecha, final String hora, final String administrador, final String claveRecordatorio, final String grupoPertenece, final Boolean estadoEnProceso) {
            db.collection("grupos")
                    .document(grupoPertenece).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){

                            if(estadoEnProceso){
                                recordatorios.add(new Recordatorio(
                                        nombreRecordatorio,
                                        descripcion,
                                        fecha,
                                        hora,
                                        administrador,
                                        claveRecordatorio,
                                        documentSnapshot.get("nombreGrupo").toString(),
                                        estadoEnProceso));
                            }

                        }
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(recordatorios.size() == recordatoriosUsuario.size()){
                        inicializarAdaptadorRecordatoriosGeneral();
                    }
                }
            });
    }

    private void inicializarAdaptadorRecordatoriosGeneral() {
        adapter = new RecordatorioGeneralAdapter(recordatorios,getActivity());
        listaRecordatorios.setAdapter(adapter);
    }
}
