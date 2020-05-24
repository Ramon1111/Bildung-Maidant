package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.menu.TusGruposFragment;

public class AddgrupoFragment extends Fragment {
    Button fcgBTNCancelar,fcgBTNCrear;
    EditText fcgETNombreGrupo, fcgETDescripcionGrupo,fcgETNEscuelaInstitucion;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_grupo,container,false);
        fcgBTNCancelar= v.findViewById(R.id.fcgBTNCancelar);
        fcgBTNCrear=v.findViewById(R.id.fcgBTNCrear);
        fcgETNombreGrupo=v.findViewById(R.id.fcgETNombreGrupo);
        fcgETDescripcionGrupo=v.findViewById(R.id.fcgETDescripcionGrupo);
        fcgETNEscuelaInstitucion=v.findViewById(R.id.fcgETNEscuelaInstitucion);
        fcgBTNCancelar.setOnClickListener(onclickCancel);
        
        return v;
    }
   View.OnClickListener onclickCancel= (new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           FragmentTransaction fr= getFragmentManager().beginTransaction();
           fr.replace(R.id.fragment_container, new TusGruposFragment());
           fr.commit();
       }
   });
}
