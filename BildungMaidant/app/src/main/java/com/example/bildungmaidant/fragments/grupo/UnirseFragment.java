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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
                                    if (task.getResult().size() == 0) {
                                        Toast.makeText(getContext(), "No se encontró el grupo", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            ArrayList<String> listaMiembros = new ArrayList<String>();
                                            for(String group : document.get("miembrosGrupo").toString().split(","))
                                                listaMiembros.add(group);

                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            //ChecarGruposUnidos(document.get("claveGrupo").toString(),listaMiembros);
                                            ChecarGruposUnidos(document.get("claveGrupo").toString(),(ArrayList)document.get("arrayMiembros"));
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

    private void ChecarGruposUnidos(final String claveGrupo, final ArrayList<String> listaMiembros) {
        final DocumentReference refGrupos = db.collection("users").document(currentUser.getUid());
        refGrupos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> listaG = new ArrayList<String>();
                        for(String group : document.get("gruposFormaParte").toString().split(",")){
                            listaG.add(group);
                        }
                        if(listaG.indexOf(claveGrupo)==-1){
                            //para hacer la cadena final de miembros
                            listaMiembros.add(currentUser.getUid());
                            String newList="";
                            for(int i=0;i<listaMiembros.size();i++)
                                newList=newList+","+listaMiembros.get(i);
                            newList=newList.substring(1);

                            listaG.add(claveGrupo);
                            AñadirGrupo(listaG,newList,claveGrupo);
                        }
                        else{
                            Toast.makeText(getContext(), "Ya tienes agregado este grupo", Toast.LENGTH_SHORT).show();
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

    private void AñadirGrupo(ArrayList<String> listaGrupos,String miembros,String claveG) {
        String newList="";

        for(int i=0;i<listaGrupos.size();i++)
            newList=newList+","+listaGrupos.get(i);

        newList=newList.substring(1);

        db.collection("users").document(currentUser.getUid())
                .update("gruposFormaParte",newList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        db.collection("grupos").document(claveG)
                .update("miembrosGrupo",miembros)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i)
                            fm.popBackStack();
                        Toast.makeText(getContext(), "Te uniste al grupo correctamente c:", Toast.LENGTH_SHORT).show();
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

    View.OnClickListener onClickCancel=(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fr= getFragmentManager().beginTransaction();
            fr.replace(R.id.fragment_container, new TusGruposFragment());
            fr.addToBackStack(null);
            fr.commit();

        }
    });

}
