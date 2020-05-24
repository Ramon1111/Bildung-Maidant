package com.example.bildungmaidant.fragments.grupo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.menu.TusGruposFragment;

public class UnirseFragment extends Fragment {
    Button fugBTNCancelar,fugBTNUnirse;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unirse_grupo,container,false);
        fugBTNCancelar= v.findViewById(R.id.fugBTNCancelar);
       fugBTNCancelar.setOnClickListener(onClickCancel);
        //fugBTNUnirse.setOnClickListener(onClickUnirse);
        return v;
    }
    View.OnClickListener onClickCancel=(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fr= getFragmentManager().beginTransaction();
            fr.replace(R.id.fragment_container, new TusGruposFragment());
            fr.commit();

        }
    });
    /*View.OnClickListener onClickUnirse=(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    });*/

}
