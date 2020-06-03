package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bildungmaidant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GrupoFragment extends Fragment {

    private String claveGrupo, nombreGrupo,adminNombre,TAG="GrupoFragment Mensaje",adminClave="",descripcion;
    private ArrayList<String> listaMiembros;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    TextView fgTVTitulo,fgTVdescripcionSust,fgTVClaveGrupoInfo;
    Button fgBTNDarseBaja,fgBTNEliminar;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public GrupoFragment(String clave, String nomG, String adminNombre, ArrayList<String> listaMiembros, String descripcion){
        this.claveGrupo=clave;
        this.nombreGrupo=nomG;
        this.adminClave=adminNombre;
        this.listaMiembros=listaMiembros;
        this.descripcion=descripcion;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grupo,container,false);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        fgTVTitulo=v.findViewById(R.id.fgTVTitulo);
        fgTVdescripcionSust=v.findViewById(R.id.fgTVdescripcionSust);
        fgTVClaveGrupoInfo=v.findViewById(R.id.fgTVClaveGrupoInfo);
        fgBTNDarseBaja=v.findViewById(R.id.fgBTNDarseBaja);
        fgBTNEliminar=v.findViewById(R.id.fgBTNEliminar);

        fgBTNEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para obtener los recordatorios del grupo actal
                db.collection("grupos").document(claveGrupo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                //para eliminar los recordatorios
                                ArrayList<String> recordatorios = (ArrayList) document.get("arrayRecordatorios");
                                for(String claveRecordatorio:recordatorios){
                                    //Para eliminar cada recordatorio de quien le pertenece
                                    db.collection("users").document(document.get("administrador").toString()).update("arrayRecordatoriosAbiertos",FieldValue.arrayRemove(claveRecordatorio));

                                    db.collection("recordatorios").document(claveRecordatorio)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG,"Se eliminaron los recordatorios");
                                                }
                                            });

                                }
                                for(String aviso:(ArrayList<String>)document.get("arrayAvisos")){
                                    db.collection("avisos").document(aviso)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG,"Se eliminaron los avisos");
                                                }
                                            });
                                }
                                for(String miembro:(ArrayList<String>)document.get("arrayMiembros"))
                                    db.collection("users").document(miembro).update("arrayGruposFormaParte",FieldValue.arrayRemove(claveGrupo));
                            }
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        db.collection("grupos").document(claveGrupo)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Se eliminó el grupo exitosamente", Toast.LENGTH_SHORT).show();
                                        getActivity().onBackPressed();
                                    }
                                });
                    }
                });
            }
        });

        if(!adminClave.equals(currentUser.getUid()))
            fgBTNEliminar.setVisibility(View.GONE);

        fgTVdescripcionSust.setText(descripcion);

        fgBTNDarseBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,adminClave);
                Log.d(TAG,currentUser.getUid());
                if(adminClave.equals(currentUser.getUid()))
                    Toast.makeText(getContext(), "No puedes darte de baja si eres administrador", Toast.LENGTH_SHORT).show();
                else
                    DarseDeBaja();
            }
        });

        return v;
    }

    private void DarseDeBaja() {
        db.collection("grupos").document(claveGrupo)
                .update("arrayMiembros", FieldValue.arrayRemove(currentUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                db.collection("users").document(currentUser.getUid())
                        .update("arrayGruposFormaParte",FieldValue.arrayRemove(claveGrupo)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Ya no formas parte del grupo", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                });
            }
        });
    }
}
