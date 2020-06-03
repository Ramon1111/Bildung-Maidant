package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActualDatosFragment extends Fragment {

    EditText faiETNombre,faiETApellido,faiETInstitucion,faiETDescripcion;
    Button faiBTNActualizar;
    RelativeLayout faiRLBack;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actualizar_informacion,container,false);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();

        faiETNombre=v.findViewById(R.id.faiETNombre);
        faiETApellido=v.findViewById(R.id.faiETApellido);
        faiETInstitucion=v.findViewById(R.id.faiETInstitucion);
        faiETDescripcion=v.findViewById(R.id.faiETDescripcion);
        faiBTNActualizar=v.findViewById(R.id.faiBTNActualizar);
        faiRLBack=v.findViewById(R.id.faiRLBack);

        faiRLBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        db.collection("users").document(currentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        faiETNombre.setText(document.get("nombres").toString());
                        faiETApellido.setText(document.get("apellidos").toString());
                        if(document.get("institucion").toString()!="")
                            faiETInstitucion.setText(document.get("institucion").toString());
                        if(document.get("descripcion").toString()!="")
                            faiETDescripcion.setText(document.get("descripcion").toString());
                    }
                }
            }
        });

        faiBTNActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(faiETNombre.getText()) && !TextUtils.isEmpty(faiETApellido.getText())){
                    String inst="", desc="";
                    if(!TextUtils.isEmpty(faiETInstitucion.getText()))
                        inst=faiETInstitucion.getText().toString();
                    if(!TextUtils.isEmpty(faiETDescripcion.getText()))
                        desc=faiETDescripcion.getText().toString();

                    db.collection("users").document(currentUser.getUid())
                        .update("nombres",faiETNombre.getText().toString(),
                                "apellidos",faiETApellido.getText().toString(),
                                "institucion",inst,
                                "descripcion",desc
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Se actualizó el usuario", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                    });
                }
                else
                    Toast.makeText(getContext(), "Asegurate de tener un nombre y apellido", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
