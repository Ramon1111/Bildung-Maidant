package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class InfoRecordatorioFragment extends Fragment {

    private Button btnCompletar;
    private Button btnEditar;
    private Button btnEliminar;
    private LinearLayout llRegresar;

    private TextView tvNombreRecordatorio,tvDescripcionRecordatorio, tvHoraRecordatorio,tvFechaRecordatorio;
    private String nombreRecordatorio, descripcionRecordatorio, fechaRecordatorio, horaRecordatorio, claveRecordatorio;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_recordatorio,container,false);

        btnCompletar=v.findViewById(R.id.firBTNCompletar);
        btnEditar=v.findViewById(R.id.firBTNEditar);
        btnEliminar=v.findViewById(R.id.firBTNEliminar);
        llRegresar=v.findViewById(R.id.firLLRegresar);

        tvNombreRecordatorio=v.findViewById(R.id.firTVEncabezado);
        tvDescripcionRecordatorio=v.findViewById(R.id.firTVDescripcion);
        tvHoraRecordatorio=v.findViewById(R.id.firTVHorario);
        tvFechaRecordatorio=v.findViewById(R.id.firTVFecha);

        Bundle bundle=this.getArguments();
        nombreRecordatorio=bundle.getString("nombreRecordatorio");
        descripcionRecordatorio=bundle.getString("descripcionRecordatorio");
        fechaRecordatorio=bundle.getString("fechaRecordatorio");
        horaRecordatorio=bundle.getString("horaRecordatorio");
        claveRecordatorio=bundle.getString("claveRecordatorio");

        tvNombreRecordatorio.setText(nombreRecordatorio);
        tvDescripcionRecordatorio.setText(descripcionRecordatorio);
        tvFechaRecordatorio.setText(fechaRecordatorio);
        tvHoraRecordatorio.setText(horaRecordatorio);

        llRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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
                //cargarFragment(new ContenedorGrupoFragment(),v);
                getActivity().onBackPressed();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("nombreRecordatorio",nombreRecordatorio);
                bundle.putString("descripcionRecordatorio",descripcionRecordatorio);
                bundle.putString("fechaRecordatorio",fechaRecordatorio);
                bundle.putString("horaRecordatorio",horaRecordatorio);
                bundle.putString("claveRecordatorio",claveRecordatorio);
                EditarRecordatorioFragment editarRecordatorioFragment = new EditarRecordatorioFragment();
                editarRecordatorioFragment.setArguments(bundle);
                cargarFragment(editarRecordatorioFragment,v);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Su recordatorio ha sido eliminado", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    private void cargarFragment(Fragment fragment,View v){
        /*
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).commit();
         */
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(),"fragment destruido: InfoRecordatorios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(),"Fragment reanudado", Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();
        db.collection("recordatorios").document(claveRecordatorio).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        tvNombreRecordatorio.setText(document.get("nombreRecordatorio").toString());
                        tvDescripcionRecordatorio.setText(document.get("descripcion").toString());
                        tvFechaRecordatorio.setText(document.get("fecha").toString());
                        tvHoraRecordatorio.setText(document.get("hora").toString());
                    }else{
                        Log.d(TAG, "No such document");
                    }
                }else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}















