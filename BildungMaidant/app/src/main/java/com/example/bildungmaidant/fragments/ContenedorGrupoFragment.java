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
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contenedor_grupo,container,false);

        viewPager=v.findViewById(R.id.fcgViewPager);
        tabLayout=v.findViewById(R.id.fcgTabLayout);
        toolbar=v.findViewById(R.id.fcgToolbar);
        setUpViewPager();

        return v;
    }

    private ArrayList<Fragment> agregarFragments(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new GrupoFragment());
        return fragments;
    }

    private void setUpViewPager() {
        viewPager.setAdapter(new PageAdapter(getChildFragmentManager(),agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_grupo_grupo_24);
    }
}
