package com.example.bildungmaidant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NuevoAvisoFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private Button btEnviar;
    private Button btCancelar;

    private EditText etNombre, etDescripcion;
    private LinearLayout regresar;

    private String TAG = "MensajeCrearAviso";

    String claveGrupo, administradorGrupo;

    public NuevoAvisoFragment(String claveGrupo, String administradorGrupo) {
        this.claveGrupo=claveGrupo;
        this.administradorGrupo=administradorGrupo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_nuevo_aviso, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        btEnviar=v.findViewById(R.id.fnaBTNEnviar);
        btCancelar=v.findViewById(R.id.fnaBTNCancelar);
        etDescripcion=v.findViewById(R.id.fnaETDescripcion);
        etNombre=v.findViewById(R.id.fnaETNombreAviso);
        regresar=v.findViewById(R.id.fnaLLRegresar);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etNombre.getText())){
                    Toast.makeText(getActivity(), "Agregar nombre de aviso.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etDescripcion.getText())) {
                    Toast.makeText(getActivity(), "No se agrego descripcion.", Toast.LENGTH_SHORT).show();
                }

                if(!TextUtils.isEmpty(etNombre.getText()) && !TextUtils.isEmpty(etDescripcion.getText())){
                    CrearAviso(v);
                }
            }
        });
        return v;
    }

    private void CrearAviso(View v) {
        String claveUsuarioBase1 = currentUser.getUid().substring(5,7);
        String claveUsuarioBase2 = currentUser.getUid().substring(7,9);
        SimpleDateFormat date1 = new SimpleDateFormat("D");
        SimpleDateFormat date2 = new SimpleDateFormat("y");
        SimpleDateFormat date3 = new SimpleDateFormat("kms");
        Date calendario = Calendar.getInstance().getTime();

        final String claveAviso=date1.format(calendario)+claveUsuarioBase1+date2.format(calendario)+claveUsuarioBase2+date3.format(calendario);
        Log.d(TAG,"ClaveRecordatorio: "+claveAviso);
        SubirAvisoCreado(claveAviso,v);
    }

    private void SubirAvisoCreado(final String claveAviso, View v) {
        Map<String, Object> newNotice = new HashMap<>();
        newNotice.put("titulo",etNombre.getText().toString());
        newNotice.put("descripcion",etDescripcion.getText().toString());
        newNotice.put("claveAviso",claveAviso);
        newNotice.put("grupoPertenece", claveGrupo);
        newNotice.put("administrador", administradorGrupo);

        db.collection("avisos").document(claveAviso).set(newNotice).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("grupos").document(claveGrupo)
                        .update("arrayAvisos", FieldValue.arrayUnion(claveAviso));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getActivity().onBackPressed();
            }
        });
    }
}
