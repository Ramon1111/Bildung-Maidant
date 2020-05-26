package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.RecordatorioAdapter;
import com.example.bildungmaidant.pojos.Recordatorio;

import java.util.ArrayList;

public class RecordatoriosTareasFragment extends Fragment {
    private RecyclerView listaRecordatorios;
    private RecordatorioAdapter adapter;
    ArrayList<Recordatorio> recordatorios;

    private ImageButton ibNuevoRecordatorio;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recordatorios_tareas,container,false);

        listaRecordatorios=v.findViewById(R.id.frtRVRecordatorios);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaRecordatorios.setLayoutManager(llm);
        inicializarListaRecordatorios();
        inicializarAdaptadorRecordatorios();

        ibNuevoRecordatorio=v.findViewById(R.id.frtIBNuevoRecordatorio);

        ibNuevoRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* NO FUNCIONO
                //NuevoRecordatorioFragment fragment = new NuevoRecordatorioFragment();
                Fragment fragmentNuevoRecordatorio = new NuevoRecordatorioFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fnrNuevoRecordatorioContent,fragmentNuevoRecordatorio);
                transaction.addToBackStack(null);
                transaction.commit();
                 */

                /* NO FUNCIONO TAMPOCO SO SAD BRUH
                Fragment fragmentNuevoRecordatorio = new NuevoRecordatorioFragment();
                LinearLayout mainLayout = v.findViewById(R.id.frtLLRecordatoriosTareas);
                LayoutInflater layoutInflater =getLayoutInflater();
                View myLayout = inflater.inflate(R.layout.fragment_nuevo_recordatorio,mainLayout,false);
                mainLayout.addView(myLayout);
                 */

                /* //SI FUNCIONA PERO ESTA MAS COOL EN METODO
                NuevoRecordatorioFragment fragment = new NuevoRecordatorioFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
                 */
                cargarFragment(new NuevoRecordatorioFragment(),v);
            }
        });


        return v;
    }

    private void inicializarAdaptadorRecordatorios() {
        adapter = new RecordatorioAdapter(recordatorios,getActivity());
        listaRecordatorios.setAdapter(adapter);
    }

    private void inicializarListaRecordatorios() {
        recordatorios=new ArrayList<Recordatorio>();
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
        recordatorios.add(new Recordatorio("Terminar el proyecto de TSP I","Hace falta hacer que la inicialización de la lista Recordatorio obtenga datos desde una base de datos.","07/05/2020","10:02 AM"));
    }

    private void cargarFragment(Fragment fragment, View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack("").commit();
    }
}
