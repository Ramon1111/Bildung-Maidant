package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bildungmaidant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
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
    private FirebaseAuth mAuth;
    private String TAG = "MensajeCrearGrupo";

    Button fcgBTNCancelar,fcgBTNCrear;
    EditText fcgETNombreGrupo, fcgETDescripcionGrupo,fcgETNEscuelaInstitucion;
    RelativeLayout fcgRLBack;

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
        fcgRLBack=v.findViewById(R.id.fcgRLBack);

        fcgRLBack.setOnClickListener(onclickCancel);

        fcgBTNCancelar.setOnClickListener(onclickCancel);

        fcgBTNCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(fcgETNombreGrupo.getText()))
                    Toast.makeText(getContext(), "Llenar el campo de Nombre de Grupo", Toast.LENGTH_SHORT).show();
                else
                    if(TextUtils.isEmpty(fcgETDescripcionGrupo.getText()))
                        Toast.makeText(getContext(), "Llenar el campo de Descripción de Grupo", Toast.LENGTH_SHORT).show();
                    else
                        if(TextUtils.isEmpty(fcgETNEscuelaInstitucion.getText()))
                            Toast.makeText(getContext(), "Llenar el campo de Escuela o Institución", Toast.LENGTH_SHORT).show();
                        else
                            CrearGrupo();
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
                .update("arrayGruposFormaParte",FieldValue.arrayUnion(claveGrupo)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SubirGrupoCreado(claveGrupo);
            }
        });
    }

    private void SubirGrupoCreado(final String clave) {

        ArrayList<String> arrayMiembros=new ArrayList<>();
        arrayMiembros.add(currentUser.getUid());

        Map<String, Object> newGroup = new HashMap<>();
        newGroup.put("nombreGrupo", fcgETNombreGrupo.getText().toString());
        newGroup.put("administrador", currentUser.getUid());
        newGroup.put("claveGrupo", clave);
        newGroup.put("estadoAltaBaja",true);
        newGroup.put("institucion",fcgETNEscuelaInstitucion.getText().toString());
        newGroup.put("descripcion",fcgETDescripcionGrupo.getText().toString());
        newGroup.put("arrayRecordatorios",new ArrayList<String>());
        newGroup.put("arrayRecursos",new ArrayList<String>());
        newGroup.put("arrayAvisos",new ArrayList<String>());
        newGroup.put("arrayMiembros",arrayMiembros);

        // Add a new document with a generated ID
        db.collection("grupos")
                .document(clave)
                .set(newGroup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getActivity().onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    View.OnClickListener onclickCancel= (new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           getActivity().onBackPressed();
       }
   });


}
