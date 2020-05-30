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
import java.util.Arrays;

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

    int nR,nG,nnG;

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
                        if(document.get("recordatoriosAbiertos").toString()!="") {
                            for (String recordatorio : document.get("recordatoriosAbiertos").toString().split(",")) {
                                recordatoriosUsuario.add(recordatorio);
                            }
                            nR=recordatoriosUsuario.size();
                            if(document.get("gruposFormaParte").toString() != ""){
                                for (String grupo : document.get("gruposFormaParte").toString().split(",")){
                                    gruposUsuario.add(grupo);
                                    Log.d("nnGn",grupo);
                                }
                                Log.d("nG",""+gruposUsuario.size());
                                ObtenerNombreGrupo();
                            }
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

    private void ObtenerNombreGrupo() {
        nombresGrupo = new ArrayList<>();
        for(String clave : gruposUsuario){
            db.collection("grupos")
                    .whereEqualTo("claveGrupo",clave).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        if(task.getResult().size()>0){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nombresGrupo.add(document.get("nombreGrupo").toString());
                                Log.d("nnGn",document.get("nombreGrupo").toString());
                                Log.d("nnG",""+nombresGrupo.size());
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        ObtenerRecordatoriosGeneralUsuario();
    }

/*
    private void ObtenerGruposUsuarioRecordatorio() {
        recordatoriosGrupo=new ArrayList<>();

        for(int i=0;i<gruposUsuario.size();i++){
            final DocumentReference refGrupos = db.collection("grupos").document(gruposUsuario.get(i));
            refGrupos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            if (document.get("numRecordatorios").toString() != ""){
                                for (String grupo : document.get("numRecordatorios").toString().split(","))
                                    recordatoriosGrupo.add(grupo);
                                    //ObtenerRecordatoriosGeneralUsuario();
                            }else{

                            }

                            if(document.get("gruposFormaParte").toString() != ""){
                                for (String grupo : document.get("gruposFormaParte").toString().split(","))

                            }
                        }else{
                            Log.d(TAG, "No such document");
                        }
                    }else{
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }
 */

    private void ObtenerRecordatoriosGeneralUsuario() {
        Log.d("nnG",""+nombresGrupo.size());
        Log.d("nnG",""+gruposUsuario.size());

        recordatorios = new ArrayList<>();
        int bandera=0;

        for(String recordatorio : recordatoriosUsuario) {
            bandera++;
            final int finalBandera = bandera;

            db.collection("recordatorios")
                    .whereEqualTo("claveRecordatorio",recordatorio)
                    .get() //esto obtiene el arreglo del documento
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().size()>0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String nombreG = "";
                                        String grupoP=document.get("grupoPertenece").toString();

                                        if(gruposUsuario.size() == nombresGrupo.size()){
                                            for(int i=0;i<gruposUsuario.size();i++){
                                                if(grupoP.equals(gruposUsuario.get(i))){
                                                    nombreG = nombresGrupo.get(i);
                                                    Log.d("NOMBRE",nombreG);
                                                }else{
                                                    Log.d("NOMBRE","No se encontró el nombre del grupo.");
                                                }
                                            }
                                        }else{
                                            nombreG=grupoP;
                                            Log.d("COINCIDENCIA","Arreglos de tamanios distintos");
                                        }

                                        recordatorios.add(new Recordatorio(
                                                document.get("nombreRecordatorio").toString(),
                                                document.get("descripcion").toString(),
                                                document.get("fecha").toString(),
                                                document.get("hora").toString(),
                                                document.get("administrador").toString(),
                                                document.get("claveRecordatorio").toString(),
                                                nombreG,
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
                    if(finalBandera ==nR){
                        inicializarAdaptadorRecordatoriosGeneral();
                    }
                }
            });
        }
    }

    private void inicializarAdaptadorRecordatoriosGeneral() {
        adapter = new RecordatorioGeneralAdapter(recordatorios,getActivity());
        listaRecordatorios.setAdapter(adapter);
        nombresGrupo.clear();
        gruposUsuario.clear();
    }
}
