package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.PageAdapter;
import com.example.bildungmaidant.fragments.grupo.recordatorios.NuevoRecordatorioFragment;
import com.example.bildungmaidant.fragments.grupo.recordatorios.RecordatoriosTareasFragment;
import com.example.bildungmaidant.pojos.Grupo;
import com.example.bildungmaidant.pojos.Recordatorio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ContenedorGrupoFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private String clave;

    private FirebaseFirestore db;

    private String nombreGrupo;
    private String administrador;
    private String claveGrupo;
    private ArrayList<String> numRecordatorios;
    private ArrayList<String> numRecursosDidacticos;
    private ArrayList<String> numAvisos;
    private ArrayList<String> miembrosGrupo;
    private Boolean estadoAltaBaja;

    private Grupo currentGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contenedor_grupo,container,false);

        db = FirebaseFirestore.getInstance();

        Bundle bundle=this.getArguments();
        clave=bundle.getString("claveGrupo");
        Toast.makeText(getContext(), clave, Toast.LENGTH_SHORT).show();

        final DocumentReference docRef = db.collection("grupos").document(clave);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nombreGrupo=document.get("nombreGrupo").toString();
                        administrador=document.get("administrador").toString();

                        claveGrupo=clave;
                        numRecordatorios=new ArrayList<String>();
                        if(document.get("numRecordatorios").toString()!="")
                            for(String rec : document.get("numRecordatorios").toString().split(","))
                                numRecordatorios.add(rec);
                        numRecursosDidacticos=new ArrayList<>();
                        if(document.get("numRecursosDidacticos").toString()!="")
                            for(String rd : document.get("numRecursosDidacticos").toString().split(","))
                                numRecordatorios.add(rd);
                        numAvisos=new ArrayList<>();
                        if(document.get("numAvisos").toString()!="")
                            for(String aviso : document.get("numAvisos").toString().split(","))
                                numRecordatorios.add(aviso);
                        miembrosGrupo=new ArrayList<String>();
                        if(document.get("miembrosGrupo").toString()!="")
                            for(String miembro : document.get("miembrosGrupo").toString().split(","))
                                miembrosGrupo.add(miembro);
                        estadoAltaBaja=document.getBoolean("estadoAltaBaja");

                        currentGroup=new Grupo(nombreGrupo,administrador,claveGrupo,numRecordatorios,numRecursosDidacticos,numAvisos,miembrosGrupo,estadoAltaBaja);

                        setUpViewPager();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });

        viewPager=v.findViewById(R.id.fcgViewPager);
        tabLayout=v.findViewById(R.id.fcgTabLayout);
        //setUpViewPager();
        return v;
    }


    //Hay que agregar como parámetros cada cosa que pudiera ser útil en cada vista
    public ArrayList<Fragment> agregarFragments(){

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new GrupoFragment(currentGroup.getClaveGrupo(),currentGroup.getNombreGrupo(),currentGroup.getAdministrador()));
        fragments.add(new RecordatoriosTareasFragment(currentGroup.getClaveGrupo()));
        fragments.add(new MensajeMenuFragment());
        fragments.add(new RecursosDidacticosFragment());
        fragments.add(new MiembrosFragment(currentGroup.getAdministrador(),currentGroup.getMiembrosGrupo()));
        fragments.add(new AvisosFragment());
        return fragments;
    }

    private void setUpViewPager() {
        viewPager.setAdapter(new PageAdapter(getChildFragmentManager(),agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_grupo_grupo_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_menu_grupo_recordatorios_tareas_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_menu_grupo_mensaje_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_menu_grupo_recursos_didacticos_24);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_menu_grupo_miembros_24);
        tabLayout.getTabAt(5).setIcon(R.drawable.ic_menu_grupo_avisos_24);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(),"fragment destruido: Cont. Grupos", Toast.LENGTH_SHORT).show();
    }
}
