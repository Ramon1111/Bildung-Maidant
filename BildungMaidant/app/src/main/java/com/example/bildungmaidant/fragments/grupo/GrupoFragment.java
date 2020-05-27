package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.menu.TusGruposFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GrupoFragment extends Fragment {

    private String claveGrupo, nombreGrupo,adminNombre,TAG="GrupoFragment Mensaje",adminClave="";
    private ArrayList<String> listaMiembros;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    TextView fgTVTitulo,fgTVAdminNombre,fgTVClaveGrupoInfo;
    Button fgBTNDarseBaja;

    public GrupoFragment(String clave, String nomG, String adminNombre, ArrayList<String> listaMiembros){
        this.claveGrupo=clave;
        this.nombreGrupo=nomG;
        this.adminClave=adminNombre;
        this.listaMiembros=listaMiembros;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grupo,container,false);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("users").document(adminClave);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        adminNombre=document.get("nombres").toString()+" "+document.get("apellidos").toString();

                        fgTVTitulo.setText(nombreGrupo);
                        fgTVAdminNombre.setText(adminNombre);
                        fgTVClaveGrupoInfo.setText(claveGrupo);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });

        fgTVTitulo=v.findViewById(R.id.fgTVTitulo);
        fgTVAdminNombre=v.findViewById(R.id.fgTVAdminNombre);
        fgTVClaveGrupoInfo=v.findViewById(R.id.fgTVClaveGrupoInfo);
        fgBTNDarseBaja=v.findViewById(R.id.fgBTNDarseBaja);



        fgBTNDarseBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,adminClave);
                Log.d(TAG,currentUser.getUid());
                if(adminClave.equals(currentUser.getUid()))
                    Toast.makeText(getContext(), "No puedes darte de baja si eres administrador", Toast.LENGTH_SHORT).show();
                else
                    DarseDeBaja();
            }
        });


        return v;
    }

    private void DarseDeBaja() {

        String listaNueva="";
        listaMiembros.remove(currentUser.getUid());

        for(String miembro : listaMiembros)
            listaNueva=listaNueva+","+miembro;

        listaNueva=listaNueva.substring(1);

        //Para actualizar la lista de miembros en el grupo
        db.collection("grupos").document(claveGrupo)
            .update("miembrosGrupo",listaNueva)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    //Para actualizar la lista de grupos en el usuario
                    final DocumentReference docRef = db.collection("users").document(currentUser.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                if (document.exists()) {

                                    String gUnidos = document.get("gruposFormaParte").toString();
                                    ArrayList<String> listaGUnidos = new ArrayList<>();
                                    for(String grupo : gUnidos.split(","))
                                        listaGUnidos.add(grupo);

                                    listaGUnidos.remove(claveGrupo);
                                    gUnidos="";
                                    for(String miembro : listaGUnidos)
                                        gUnidos=gUnidos+","+miembro;

                                    gUnidos=gUnidos.substring(1);

                                    db.collection("users").document(currentUser.getUid())
                                        .update("gruposFormaParte",gUnidos)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                                for(int i = 0; i < fm.getBackStackEntryCount(); ++i)
                                                    fm.popBackStack();
                                                Toast.makeText(getContext(), "Ya no formas parte del grupo", Toast.LENGTH_SHORT).show();
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TusGruposFragment()).commit();

                                            }
                                        });

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            });
    }
}
