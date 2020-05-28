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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.grupo.recordatorios.MiembroNuevoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MiembrosFragment extends Fragment {

    TextView fmiemTWlistadminis,fmiemTWlistamiembro;
    private String adminClave,claveGrupo,nombreGrupo;
    private ArrayList<String> listaMiembros;

    Button fmiemBTNagregarmiembro;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public MiembrosFragment(String adminClave, ArrayList<String> listaMiembros, String claveGrupo,String nombreGrupo){
        this.adminClave=adminClave;
        this.listaMiembros=listaMiembros;
        this.claveGrupo=claveGrupo;
        this.nombreGrupo=nombreGrupo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_miembros,container,false);

        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        fmiemBTNagregarmiembro=v.findViewById(R.id.fmiemBTNagregarmiembro);
        fmiemBTNagregarmiembro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adminClave.equals(currentUser.getUid())){
                    MiembroNuevoFragment contenedor = new MiembroNuevoFragment(claveGrupo,listaMiembros,nombreGrupo);
                    cargarFragment(contenedor,v);
                }
                else
                    Toast.makeText(getContext(), "No puedes añadir miembros si no eres administrador", Toast.LENGTH_SHORT).show();
            }
        });

        fmiemTWlistadminis=v.findViewById(R.id.fmiemTWlistadminis);
        fmiemTWlistamiembro=v.findViewById(R.id.fmiemTWlistamiembro);

        Log.d(TAG,adminClave);

        ObtenerAdmin();
        ObtenerMiembros();

        return v;
    }

    private void cargarFragment(Fragment fragment, View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack("").commit();
    }

    private void ObtenerMiembros() {
        for(String miembro : listaMiembros){
            ObtenerUsuario(miembro,fmiemTWlistamiembro);
            Log.d("Siexstealv","Sientraalv");
        }
    }

    private void ObtenerAdmin() {
        ObtenerUsuario(adminClave,fmiemTWlistadminis);
    }

    private String ObtenerUsuario(String usuario, final TextView vista) {

        final String[] nombreCompleto = new String[1];
        final DocumentReference docRef = db.collection("users").document(usuario);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        vista.setText(vista.getText().toString()+System.getProperty("line.separator")+document.get("nombres").toString()+" "+document.get("apellidos").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });
        return nombreCompleto[0];
    }
}
