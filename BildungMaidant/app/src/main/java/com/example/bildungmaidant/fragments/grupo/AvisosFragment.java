package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.NuevoAvisoFragment;
import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.AvisosAdapter;
import com.example.bildungmaidant.pojos.Avisos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AvisosFragment extends Fragment {

    private RecyclerView listaAvisos;
    private AvisosAdapter adapter;
    private ArrayList<Avisos> avisos;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    String claveGrupo, administradorGrupo;
    ArrayList<String> listaAvisosGrupo;

    ImageButton crearAvisoIB;
    LinearLayout crearAvisoLL;

    public AvisosFragment(String claveGrupo, String administradorGrupo){
        this.claveGrupo=claveGrupo;
        this.administradorGrupo=administradorGrupo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_avisos,container,false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        crearAvisoIB=v.findViewById(R.id.faIBNuevoAviso);
        crearAvisoLL=v.findViewById(R.id.faLLAgregar);


        listaAvisos=v.findViewById(R.id.faRVAvisos);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaAvisos.setLayoutManager(llm);

        db.collection("grupos").document(claveGrupo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists())
                        listaAvisosGrupo=(ArrayList)document.get("arrayAvisos");
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ObtenerAvisosGrupo();
            }
        });


        if(!currentUser.getUid().equals(administradorGrupo)){
            crearAvisoLL.setVisibility(View.GONE);

        }

        crearAvisoIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new NuevoAvisoFragment(claveGrupo,administradorGrupo),v);
            }
        });

        return v;
    }

    private void ObtenerAvisosGrupo() {
        avisos=new ArrayList<>();
        int bandera=0;

        for(String aviso : listaAvisosGrupo){
            bandera++;
            final int finalBandera = bandera;
            db.collection("avisos")
                    .whereEqualTo("claveAviso",aviso)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().size()>0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        avisos.add(new Avisos(
                                                document.get("claveAviso").toString(),
                                                document.get("administrador").toString(),
                                                document.get("grupoPertenece").toString(),
                                                document.get("titulo").toString(),
                                                document.get("descripcion").toString()
                                        ));
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(finalBandera ==listaAvisosGrupo.size()){
                        inicializarAdaptadorAvisos();
                    }
                }
            });
        }
    }

    private void inicializarAdaptadorAvisos() {
        adapter = new AvisosAdapter(avisos,getActivity());
        listaAvisos.setAdapter(adapter);
    }

    private void cargarFragment(Fragment fragment, View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }
}
