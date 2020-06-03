package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bildungmaidant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MiembroNuevoFragment extends Fragment {

    private String claveGrupo;
    private ArrayList<String> listaMiembros;

    EditText fmnETClaveMiembro;
    Button fmnBTNAgregar,fmnBTNCancelar;
    private String TAG="MiembroNuevo mensaje",nombreGrupo;
    TextView fmnTVEncabezado;
    RelativeLayout fmnRLBack;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public MiembroNuevoFragment(String claveGrupo, ArrayList<String> listaMiembros,String nombreGrupo){
        this.claveGrupo=claveGrupo;
        this.listaMiembros=listaMiembros;
        this.nombreGrupo=nombreGrupo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_miembro_nuevo,container,false);

        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        fmnETClaveMiembro=v.findViewById(R.id.fmnETClaveMiembro);
        fmnTVEncabezado=v.findViewById(R.id.fmnTVEncabezado);
        fmnTVEncabezado.setText(fmnTVEncabezado.getText().toString()+" a "+nombreGrupo);
        fmnBTNAgregar=v.findViewById(R.id.fmnBTNAgregar);
        fmnBTNCancelar=v.findViewById(R.id.fmnBTNCancelar);
        fmnRLBack=v.findViewById(R.id.fmnRLBack);

        fmnBTNCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        fmnRLBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        fmnBTNAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String[] newList = {""};
                db.collection("users")
                    .whereEqualTo("correo", fmnETClaveMiembro.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() == 0) {
                                    Toast.makeText(getContext(), "No existe el usuario", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        //Para confirmar si ya existe el usuario como miembro
                                        if(listaMiembros.indexOf(document.get("llavePrimaria").toString())>=0)
                                            Toast.makeText(getContext(), "Ya forma parte del grupo", Toast.LENGTH_SHORT).show();
                                        else{

                                            db.collection("grupos").document(claveGrupo)
                                                    .update("arrayMiembros", FieldValue.arrayUnion(currentUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    db.collection("users").document(currentUser.getUid())
                                                            .update("arrayGruposFormaParte",FieldValue.arrayUnion(claveGrupo)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(getContext(), "Se agregó el usuario", Toast.LENGTH_SHORT).show();
                                                            getActivity().onBackPressed();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            }
        });

        return v;
    }
}
