package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bildungmaidant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GrupoFragment extends Fragment {

    private String claveGrupo, nombreGrupo,adminNombre,TAG="GrupoFragment Mensaje",adminClave="";

    TextView fgTVTitulo,fgTVAdminNombre,fgTVClaveGrupoInfo;

    public GrupoFragment(String clave,String nomG,String adminNombre){
        this.claveGrupo=clave;
        this.nombreGrupo=nomG;
        this.adminClave=adminNombre;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grupo,container,false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        //Toast.makeText(getContext(), claveGrupo, Toast.LENGTH_SHORT).show();

        return v;
    }
}
