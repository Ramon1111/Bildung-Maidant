package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.menu.TusGruposFragment;

public class InfoRecordatorioFragment extends Fragment {

    Button btnCompletar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_recordatorio,container,false);

        btnCompletar=v.findViewById(R.id.firBTNCompletar);
        btnCompletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                //activity.getSupportFragmentManager().beginTransaction().remove(new ContenedorGrupoFragment).commit();

                //getFragmentManager().popBackStack("RecordatoriosGrupoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //getFragmentManager().popBackStack("GruposFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
























                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,new TusGruposFragment()).commit();
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(),"fragment destruido: InfoRecordatorios", Toast.LENGTH_SHORT).show();
    }
}















