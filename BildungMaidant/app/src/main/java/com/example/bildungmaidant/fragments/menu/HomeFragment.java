package com.example.bildungmaidant.fragments.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String TAG="Mensaje HomeFragment";

    View v;

    TextView nombreUsuario,institucion,sobreMi;
    ImageView fotoPerfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home,container,false);

        db = FirebaseFirestore.getInstance();
        Toast.makeText(getContext(), "Se creó la vista", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        nombreUsuario=v.findViewById(R.id.fhTVNombreInfo);
        institucion=v.findViewById(R.id.fhTVIntitucionInfo);
        sobreMi=v.findViewById(R.id.textView3);
        fotoPerfil=v.findViewById(R.id.imageView);

        if(currentUser!=null) {
            final DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Log.d(TAG, "DocumentSnapshot data: " + document.get("usuario"));

                            nombreUsuario.setText(document.get("nombres").toString()+" "+document.get("apellidos").toString());
                            if(document.get("sobreMi").toString()!="")
                                sobreMi.setText(document.get("sobreMi").toString());
                            else
                                sobreMi.setText("Aún no has agregado una descripción :)");

                            if(document.get("institucion").toString()!="")
                                institucion.setText(document.get("institucion").toString());
                            else
                                institucion.setText("Sin institucion");


                            //Descomentar para cuando se realice la consulta de la foto de perfil
                            //Se debe de hacer una consulta al storage con la cadena que se tiene
                            /*if(document.get("fotoPerfil").toString()!="")
                                fotoPerfil.set
                                */

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(),"fragment destruido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Toast.makeText(getContext(),"fragment quitado", Toast.LENGTH_SHORT).show();
    }
}
