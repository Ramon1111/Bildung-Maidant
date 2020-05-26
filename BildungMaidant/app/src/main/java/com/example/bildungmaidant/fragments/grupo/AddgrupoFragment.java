package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.menu.TusGruposFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddgrupoFragment extends Fragment {

    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private ArrayList<String> listaGrupos;

    private FirebaseAuth mAuth;
    private String TAG = "MensajeCrearGrupo";

    Button fcgBTNCancelar,fcgBTNCrear;
    EditText fcgETNombreGrupo, fcgETDescripcionGrupo,fcgETNEscuelaInstitucion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_grupo,container,false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        fcgBTNCancelar= v.findViewById(R.id.fcgBTNCancelar);
        fcgBTNCrear=v.findViewById(R.id.fcgBTNCrear);
        fcgETNombreGrupo=v.findViewById(R.id.fcgETNombreGrupo);
        fcgETDescripcionGrupo=v.findViewById(R.id.fcgETDescripcionGrupo);
        fcgETNEscuelaInstitucion=v.findViewById(R.id.fcgETNEscuelaInstitucion);
        fcgBTNCancelar.setOnClickListener(onclickCancel);

        fcgBTNCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(fcgETNombreGrupo.getText())){
                    Toast.makeText(getContext(), "Llenar el campo de Nombre de Grupo", Toast.LENGTH_SHORT).show();
                }else{
                    if(TextUtils.isEmpty(fcgETDescripcionGrupo.getText())){
                        Toast.makeText(getContext(), "Llenar el campo de Descripción de Grupo", Toast.LENGTH_SHORT).show();
                    }else{
                        if(TextUtils.isEmpty(fcgETNEscuelaInstitucion.getText())){
                            Toast.makeText(getContext(), "Llenar el campo de Escuela o Institución", Toast.LENGTH_SHORT).show();
                        }else{
                            CrearGrupo();
                        }
                    }
                }
            }
        });

        return v;
    }

    private void CrearGrupo() {
        String claveUsuarioBase1 = currentUser.getUid().substring(5,7);
        String claveUsuarioBase2 = currentUser.getUid().substring(7,9);
        SimpleDateFormat date1 = new SimpleDateFormat("D");
        SimpleDateFormat date2 = new SimpleDateFormat("y");
        SimpleDateFormat date3 = new SimpleDateFormat("kms");
        Date calendario = Calendar.getInstance().getTime();

        final String claveGrupo=date1.format(calendario)+claveUsuarioBase1+date2.format(calendario)+claveUsuarioBase2+date3.format(calendario);
        Log.d(TAG,"ClaveGrupo: "+claveGrupo);

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                listaGrupos= new ArrayList<String>();

                                if(document.get("gruposFormaParte").toString()!="")
                                    for (String grupo : document.get("gruposFormaParte").toString().split(","))
                                        listaGrupos.add(grupo);

                                listaGrupos.add(claveGrupo);
                                SubirGrupoCreado(claveGrupo);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void SubirGrupoCreado(final String clave) {
        Map<String, Object> newGroup = new HashMap<>();
        newGroup.put("nombreGrupo", fcgETNombreGrupo.getText().toString());
        newGroup.put("administrador", currentUser.getUid());
        newGroup.put("claveGrupo", clave);
        newGroup.put("numRecordatorios", "");
        newGroup.put("numRecursosDidacticos", "");
        newGroup.put("numAvisos","");
        newGroup.put("miembrosGrupo",currentUser.getUid());
        newGroup.put("estadoAltaBaja",true);
        newGroup.put("institucion",fcgETNEscuelaInstitucion.getText().toString());
        newGroup.put("descripcion",fcgETDescripcionGrupo.getText().toString());

        // Add a new document with a generated ID
        db.collection("grupos")
                .document(clave)
                .set(newGroup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CambiarGruposUnidos(clave);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void CambiarGruposUnidos(String clave) {
        String newList="";

        for(int i=0;i<listaGrupos.size();i++)
            newList=newList+","+listaGrupos.get(i);

        newList=newList.substring(1);

        db.collection("users").document(currentUser.getUid())
                .update("gruposFormaParte",newList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i)
                            fm.popBackStack();
                        Toast.makeText(getContext(), "Se creó el grupo correctamente", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TusGruposFragment()).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    View.OnClickListener onclickCancel= (new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           FragmentManager fm = getActivity().getSupportFragmentManager();
           FragmentTransaction fr= getFragmentManager().beginTransaction();
           for(int i = 0; i < fm.getBackStackEntryCount(); ++i)
               fm.popBackStack();
           fr.replace(R.id.fragment_container, new TusGruposFragment());
           fr.commit();
       }
   });


}
