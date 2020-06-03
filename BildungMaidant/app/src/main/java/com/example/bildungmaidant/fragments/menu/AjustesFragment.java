package com.example.bildungmaidant.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.grupo.ActualDatosFragment;

public class AjustesFragment extends Fragment {
    String ajustes[]= new String []{"Actualizar Datos"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ajustes,container,false);
        final ListView faLVajustes= v.findViewById(R.id.faLVajustes);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,ajustes);
        faLVajustes.setAdapter(adapter);
        faLVajustes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position==0)
                {
                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, new ActualDatosFragment());
                    fr.addToBackStack(null);
                    fr.commit();
                }

                //Toast.makeText(getContext(),"Pulsando el elemento no. " + position, Toast.LENGTH_LONG).show();

            }

        });
        return v;

    }

}
