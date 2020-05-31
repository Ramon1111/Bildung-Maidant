package com.example.bildungmaidant.fragments.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TusGruposFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private RecyclerView listaGrupos;
    private GrupoAdapter adapter;
    ArrayList<Grupo> grupos;
    Button fgrubtnañadir, fgrubtnunirse;

    private ArrayList<String> gruposUsuario;

    /*private String nombreGrupo,administradorClave,administradorNombre, claveGrupo,listG;
    private ArrayList<Integer> numRecordatorios,numRecursosDidacticos,numAvisos;
    private ArrayList<String> miembrosGrupo;
    private Boolean estadoAltaBaja;*/

    private int nG;

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
        //inicializarAdaptadorGrupos();

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
                        creaGrupos();
                    }
                }
            }
        });
    }

    private void creaGrupos() {
        for(String grupo : gruposUsuario){
            db.collection("grupos").document(grupo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists())
                            ConseguirNombreAdmin(document.get("nombreGrupo").toString(),document.get("administrador").toString());
                    }
                }
            });
        }
    }

    private void ConseguirNombreAdmin(final String nombreGrupo, String administrador) {
        db.collection("users").document(administrador).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        grupos.add(new Grupo(nombreGrupo,document.get("nombres")+" "+document.get("apellidos")));
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

    private void ObtenerGrupos() {

        final DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("gruposFormaParte").toString()!="") {

                            for (String grupo : document.get("gruposFormaParte").toString().split(","))
                                gruposUsuario.add(grupo);

                            nG=gruposUsuario.size();

                            CrearGrupos();
                        }else{

                            //Poner mensaje de que aún no ha creado grupos
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

    private void CrearGrupos() {
        for(int i=0;i<gruposUsuario.size();i++) {
            final DocumentReference refGrupos = db.collection("grupos").document(gruposUsuario.get(i));
            refGrupos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                String adClave;

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            String administradorClave, listG;
                            //ArrayList<Integer> numRecordatorios,numRecursosDidacticos,numAvisos;
                            //ArrayList<String> miembrosGrupo;
                            //Boolean estadoAltaBaja;

                            ArrayList<String> numRecordatorios = new ArrayList<>();
                            ArrayList<String> numRecursosDidacticos = new ArrayList<>();
                            ArrayList<String> numAvisos = new ArrayList<>();
                            ArrayList<String> miembrosGrupo = new ArrayList<>();

                            String nombreGrupo = document.get("nombreGrupo").toString();
                            adClave = document.get("administrador").toString();
                            String claveGrupo = document.get("claveGrupo").toString();
                            Boolean estadoAltaBaja = document.getBoolean("estadoAltaBaja");

                            if (document.get("numRecordatorios").toString() != "")
                                for (String recordatorio : document.get("numRecordatorios").toString().split(","))
                                    numRecordatorios.add(recordatorio);

                            if (document.get("numAvisos").toString() != "")
                                for (String aviso : document.get("numAvisos").toString().split(","))
                                    numAvisos.add(aviso);

                            if (document.get("miembrosGrupo").toString() != "")
                                for (String miembro : document.get("miembrosGrupo").toString().split(","))
                                    miembrosGrupo.add(miembro);

                            if(document.get("numRecursosDidacticos").toString()!="")
                                for(String recurso:document.get("numRecursosDidacticos").toString().split(","))
                                    numRecordatorios.add(recurso);


                            //administradorNombre=ObtenerUsuario(administradorClave);
                            ObtenerUsuario(nombreGrupo,adClave,claveGrupo,estadoAltaBaja,numRecordatorios,numAvisos,miembrosGrupo, numRecursosDidacticos);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    private void activarF(){
        Log.d(TAG,String.valueOf(grupos.size()));
        if(nG==grupos.size()){
            inicializarAdaptadorGrupos();
        }
    }

    private void ObtenerMiembrosGrupo(String nGrupo,String adNom,String clavGrupo,Boolean estAB, ArrayList<String> nRec,ArrayList<String> nAv,ArrayList<String> miemG, ArrayList<String> nRD) {
        final ArrayList<String> miembros = new ArrayList<String>();
        final String[] otroMiembro = {""};

        for (String miem : miemG){
            //final DocumentReference refGrupos = db.collection("users").document(miem);
            final DocumentReference refGrupos = db.collection("users").document(miem);
            refGrupos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            miembros.add(document.get("nombres").toString()+" "+document.get("apellidos").toString());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }


        grupos.add(new Grupo(nGrupo,adNom,clavGrupo,nRec,nRD,nAv,miembros,estAB));

        //grupos.add(new Grupo(nombreGrupo, administradorNombre));
        //grupos.add(new Grupo(nGrupo, adNom));

        activarF();

        Log.d("TamañoF",String.valueOf(grupos.size()));
            //miembros.add(ObtenerUsuario(miembro));
    }

    private void ObtenerUsuario(final String nGrupo, String adClave, final String clavGrupo,final Boolean estAB,final  ArrayList<String> nRec,final ArrayList<String> nAv,final ArrayList<String> miemG,final ArrayList<String> nRD) {
        final DocumentReference refGrupos = db.collection("users").document(adClave);
        refGrupos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            String adNom;
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //administradorNombre=document.get("nombres").toString()+" "+document.get("apellidos").toString();
                        adNom=document.get("nombres").toString()+" "+document.get("apellidos").toString();

                        ObtenerMiembrosGrupo(nGrupo,adNom,clavGrupo,estAB,nRec,nAv,miemG,nRD);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
