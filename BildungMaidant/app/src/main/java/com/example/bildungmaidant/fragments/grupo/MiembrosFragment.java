package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.MiembroAdapter;
import com.example.bildungmaidant.fragments.grupo.recordatorios.MiembroNuevoFragment;
import com.example.bildungmaidant.pojos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MiembrosFragment extends Fragment {

    TextView fmiemTWlistadminis,fmiemTWlistamiembro,fmiemTWAdminCorreo;
    private String adminClave,claveGrupo,nombreGrupo;
    private ArrayList<String> listaMiembros;
    ListView fmLVMiembros;
    MiembroAdapter miembroAdaptador;

    Button fmiemBTNagregarmiembro;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public MiembrosFragment(String adminClave, String claveGrupo,String nombreGrupo){
        this.adminClave=adminClave;
        //this.listaMiembros=listaMiembros;
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
        //fmiemTWlistamiembro=v.findViewById(R.id.fmiemTWlistamiembro);
        fmLVMiembros=v.findViewById(R.id.fmLVMiembros);
        fmiemTWAdminCorreo=v.findViewById(R.id.fmiemTWAdminCorreo);

        Log.d(TAG,adminClave);

        ObtenerAdmin();

        db.collection("grupos").document(claveGrupo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                listaMiembros = (ArrayList) document.get("arrayMiembros");
                                final ArrayList<Usuario> nombresMiembros = new ArrayList<>();
                                if(listaMiembros.size()>1)
                                    for (String miembro : listaMiembros){
                                        if(!miembro.equals(adminClave))
                                        db.collection("users").document(miembro)
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists())
                                                        nombresMiembros.add(new Usuario(document.get("nombres").toString(), document.get("apellidos").toString(), document.get("correo").toString()));
                                                }
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (listaMiembros.size()-1 == nombresMiembros.size()) {
                                                    miembroAdaptador = new MiembroAdapter(getContext(), nombresMiembros);
                                                    fmLVMiembros.setAdapter(miembroAdaptador);
                                                }
                                            }
                                        });
                                    }
                                else {
                                    nombresMiembros.add(new Usuario("Aún no se han agregado miembros", "", "Comparta su clave de grupo o agregue un usuario"));
                                    miembroAdaptador = new MiembroAdapter(getContext(), nombresMiembros);
                                    fmLVMiembros.setAdapter(miembroAdaptador);
                                }
                            }
                        }
                    }
                });

        return v;
    }

    private void cargarFragment(Fragment fragment, View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack("").commit();
    }

    private void ObtenerAdmin() {
        ObtenerUsuario(adminClave,fmiemTWlistadminis,fmiemTWAdminCorreo);
    }

    private void ObtenerUsuario(String usuario, final TextView vista1, final TextView vista2) {
        db.collection("users").document(usuario).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        vista1.setText(document.get("nombres").toString() + " " + document.get("apellidos").toString() + System.getProperty("line.separator"));
                        vista2.setText(document.get("correo").toString());
                    }else
                        Log.d(TAG, "No such document");
                } else
                    Log.d(TAG, "get failed with ", task.getException());
            }

        });
    }
}
