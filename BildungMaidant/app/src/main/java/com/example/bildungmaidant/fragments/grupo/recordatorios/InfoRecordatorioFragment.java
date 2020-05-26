package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.grupo.ContenedorGrupoFragment;
import com.example.bildungmaidant.fragments.menu.TusGruposFragment;

public class InfoRecordatorioFragment extends Fragment {

    private Button btnCompletar;
    private Button btnEditar;
    private Button btnEliminar;

    private TextView tvRegresar;
    private ImageView ivRegresar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_recordatorio,container,false);

        btnCompletar=v.findViewById(R.id.firBTNCompletar);
        btnEditar=v.findViewById(R.id.firBTNEditar);
        btnEliminar=v.findViewById(R.id.firBTNEliminar);

        tvRegresar=v.findViewById(R.id.firTVRegresar);
        ivRegresar=v.findViewById(R.id.firIVBackArrow);

        tvRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new ContenedorGrupoFragment(),v);
            }
        });

        ivRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new ContenedorGrupoFragment(),v);
            }
        });

        btnCompletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppCompatActivity activity = (AppCompatActivity) v.getContext();

                //activity.getSupportFragmentManager().beginTransaction().remove(new ContenedorGrupoFragment).commit();

                //getFragmentManager().popBackStack("RecordatoriosGrupoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //getFragmentManager().popBackStack("GruposFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Toast.makeText(getContext(),"Su recordatorio ha sido completado", Toast.LENGTH_SHORT).show();
                cargarFragment(new ContenedorGrupoFragment(),v);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,new EditarRecordatorioFragment()).addToBackStack("").commit();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Su recordatorio ha sido eliminado", Toast.LENGTH_SHORT).show();
                cargarFragment(new ContenedorGrupoFragment(),v);
            }
        });

        return v;
    }

    private void cargarFragment(Fragment fragment,View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(),"fragment destruido: InfoRecordatorios", Toast.LENGTH_SHORT).show();
    }
}















