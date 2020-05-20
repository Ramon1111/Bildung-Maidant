package com.example.bildungmaidant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.adapter.PageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ContenedorGrupoFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contenedor_grupo,container,false);
        viewPager=v.findViewById(R.id.fcgViewPager);
        tabLayout=v.findViewById(R.id.fcgTabLayout);
        setUpViewPager();
        return v;
    }

    private ArrayList<Fragment> agregarFragments(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new GrupoFragment());
        fragments.add(new RecordatoriosTareasFragment());
        fragments.add(new MensajeNuevoFragment());
        fragments.add(new RecursosDidacticosFragment());
        fragments.add(new MiembrosFragment());
        fragments.add(new AvisosFragment());
        return fragments;
    }

    private void setUpViewPager() {
        viewPager.setAdapter(new PageAdapter(getChildFragmentManager(),agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_grupo_grupo_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_menu_grupo_recordatorios_tareas_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_menu_grupo_mensaje_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_menu_grupo_recursos_didacticos_24);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_menu_grupo_miembros_24);
        tabLayout.getTabAt(5).setIcon(R.drawable.ic_menu_grupo_avisos_24);
    }
}
