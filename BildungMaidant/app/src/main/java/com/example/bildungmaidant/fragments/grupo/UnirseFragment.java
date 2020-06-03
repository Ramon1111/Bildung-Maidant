package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
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

import com.example.bildungmaidant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UnirseFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private String TAG = "UnirseFragment Mensaje";

    Button fugBTNCancelar,fugBTNUnirse;
    EditText fugETCodigoNumero;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unirse_grupo,container,false);

        //inicializar autenticación del usuario
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        fugBTNCancelar= v.findViewById(R.id.fugBTNCancelar);
        fugBTNCancelar.setOnClickListener(onClickCancel);

        fugETCodigoNumero=v.findViewById(R.id.fugETCodigoNumero);

        fugBTNUnirse=v.findViewById(R.id.fugBTNUnirse);
        fugBTNUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("grupos")
                        .whereEqualTo("claveGrupo", fugETCodigoNumero.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //String clavePrueba="";
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() == 0)
                                        Toast.makeText(getContext(), "No se encontró el grupo", Toast.LENGTH_SHORT).show();
                                    else
                                        for (QueryDocumentSnapshot document : task.getResult())
                                            ChecarGruposUnidos(document.get("claveGrupo").toString());

                                } else
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        });
            }
        });
        return v;
    }

    private void ChecarGruposUnidos(final String claveGrupo) {
        final DocumentReference refGrupos = db.collection("users").document(currentUser.getUid());
        refGrupos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(((ArrayList)document.get("arrayGruposFormaParte")).indexOf(claveGrupo)==-1)
                            AñadirGrupo(claveGrupo);
                        else
                            Toast.makeText(getContext(), "Ya está agregado a este grupo", Toast.LENGTH_SHORT).show();
                    } else
                        Log.d(TAG, "No such document");
                } else
                    Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void AñadirGrupo(String claveGrupo) {
        db.collection("users").document(currentUser.getUid())
                .update("arrayGruposFormaParte", FieldValue.arrayUnion(claveGrupo));
        db.collection("grupos").document(claveGrupo)
                .update("arrayMiembros",FieldValue.arrayUnion(currentUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getActivity().onBackPressed();
            }
        });
    }

    View.OnClickListener onClickCancel=(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    });

}
