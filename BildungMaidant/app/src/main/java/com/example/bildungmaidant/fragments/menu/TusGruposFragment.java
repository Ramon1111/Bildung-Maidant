package com.example.bildungmaidant.fragments.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.GrupoAdapter;
import com.example.bildungmaidant.fragments.grupo.AddgrupoFragment;
import com.example.bildungmaidant.fragments.grupo.UnirseFragment;
import com.example.bildungmaidant.pojos.Grupo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TusGruposFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private RecyclerView listaGrupos;
    private GrupoAdapter adapter;
    ArrayList<Grupo> grupos;
    Button fgrubtnañadir, fgrubtnunirse;

    private ArrayList<String> gruposUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tus_grupos,container,false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        listaGrupos=v.findViewById(R.id.ftgRVGrupos);
        fgrubtnañadir=v.findViewById(R.id.fgrubtnañadir);
        fgrubtnunirse=v.findViewById(R.id.fgrubtnunirse);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaGrupos.setLayoutManager(llm);
        inicializarListaGrupo();

        fgrubtnañadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //me lleva a Unirse a dar de alta uno
                FragmentTransaction fr= getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new AddgrupoFragment());
                fr.addToBackStack(null);
                fr.commit();

            }
        });
        fgrubtnunirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //me lleva a Unirse a un nuevo grupo
                FragmentTransaction fr= getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new UnirseFragment());
                fr.addToBackStack(null);
                fr.commit();
            }
        });
        return v;
    }

    private void inicializarAdaptadorGrupos() {
        adapter = new GrupoAdapter(grupos,getActivity());
        listaGrupos.setAdapter(adapter);
    }

    private void inicializarListaGrupo() {
        grupos=new ArrayList<Grupo>();
        gruposUsuario=new ArrayList<String>();
        GetGruposUnidos();
    }

    private void GetGruposUnidos(){
        db.collection("users").document(currentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    if(document.exists()){
                        gruposUsuario=(ArrayList)document.get("arrayGruposFormaParte");

                        if(gruposUsuario.size()>0)
                            creaGrupos();
                        else
                            Toast.makeText(getContext(),"Aún no tienes ningún grupo",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void creaGrupos() {
        for(final String grupo : gruposUsuario){
            Log.d("CLAVE",grupo);
            db.collection("grupos").document(grupo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists())
                            ConseguirNombreAdmin(document.get("nombreGrupo").toString(),document.get("administrador").toString(),grupo);
                    }
                }
            });
        }
    }

    private void ConseguirNombreAdmin(final String nombreGrupo, String administrador, final String claveGrupo) {
        db.collection("users").document(administrador).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        grupos.add(new Grupo(nombreGrupo,document.get("nombres")+" "+document.get("apellidos"),claveGrupo));
                    }
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(grupos.size()==gruposUsuario.size())
                    inicializarAdaptadorGrupos();
            }
        });
    }
}
