package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.grupo.ContenedorGrupoFragment;
import com.example.bildungmaidant.fragments.menu.TusGruposFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class NuevoRecordatorioFragment extends Fragment {
    private static final String CERO ="0";
    private static final String BARRA ="/";
    private static final String DOS_PUNTOS=":";

    public final Calendar calendar = Calendar.getInstance();

    final int dia = calendar.get(Calendar.DAY_OF_MONTH);
    final int mes = calendar.get(Calendar.MONTH);
    final int anio = calendar.get(Calendar.YEAR);

    final int hora=calendar.get(Calendar.HOUR_OF_DAY);
    final int minuto=calendar.get(Calendar.MINUTE);

    private ImageButton ibtFecha;
    private EditText etFecha;
    private ImageButton ibtHora;
    private EditText etHora;

    private Button btCrear;
    private Button btCancelar;

    private TextView tvRegresar;
    private ImageView ivRegresar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nuevo_recordatorio,container,false);

        btCrear=v.findViewById(R.id.fnrBTNCrear);
        btCancelar=v.findViewById(R.id.fnrBTNCancelar);

        etFecha=v.findViewById(R.id.fnrETFecha);
        ibtFecha=v.findViewById(R.id.fnrIBFecha);

        etHora=v.findViewById(R.id.fnrETHorario);
        ibtHora=v.findViewById(R.id.fnrIBHorario);

        tvRegresar=v.findViewById(R.id.fnrTVRegresar);
        ivRegresar=v.findViewById(R.id.fnrIVBackArrow);

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

        btCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new ContenedorGrupoFragment(),v);
            }
        });

        btCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new ContenedorGrupoFragment(),v);
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragment(new ContenedorGrupoFragment(),v);
            }
        });

        ibtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.fnrIBFecha:
                        obtenerFecha();
                        break;
                }
            }
        });

        ibtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.fnrIBHorario:
                        obtenerHora();
                        break;
                }
            }
        });

        return v;
    }

    private void obtenerHora() {
        TimePickerDialog tomarHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormato = (hourOfDay<10)? CERO+hourOfDay: String.valueOf(hourOfDay);
                String minutoFormato = (minute<10)? CERO+minute:String.valueOf(minute);

                String AM_PM;
                if(hourOfDay<12){
                    AM_PM=" AM";
                }else{
                    AM_PM=" PM";
                }
                etHora.setText(horaFormato+DOS_PUNTOS+minutoFormato+AM_PM);
            }
        },hora,minuto,false);
        tomarHora.show();
    }

    private void obtenerFecha() {
        DatePickerDialog tomarFecha = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month +1;
                String diaFormato=(dayOfMonth<10)? CERO + dayOfMonth:String.valueOf(dayOfMonth);
                String mesFormato=(mesActual<10)? CERO +mesActual:String.valueOf(mesActual);

                etFecha.setText(diaFormato+BARRA+mesFormato+BARRA+year);
            }
        },anio,mes,dia);
        tomarFecha.show();
    }

    private void cargarFragment(Fragment fragment,View v){
        //FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        //ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).commit();

    }
}
