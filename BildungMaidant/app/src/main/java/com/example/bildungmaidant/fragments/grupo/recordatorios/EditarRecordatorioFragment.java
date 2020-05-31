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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bildungmaidant.R;
import com.example.bildungmaidant.fragments.grupo.ContenedorGrupoFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class EditarRecordatorioFragment extends Fragment {

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

    private Button btActualizar;
    private Button btCancelar;
    private LinearLayout llRegresar;

    private EditText etNombreRecordatorio,etDescripcionRecordatorio, etHoraRecordatorio,etFechaRecordatorio;
    private String nombreRecordatorio, descripcionRecordatorio, fechaRecordatorio, horaRecordatorio,claveRecordatorio;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editar_recordatorio,container,false);
        ibtFecha=v.findViewById(R.id.ferIBFecha);
        etFecha=v.findViewById(R.id.ferETFecha);
        ibtHora=v.findViewById(R.id.ferIBHorario);
        etHora=v.findViewById(R.id.ferETHorario);
        btActualizar=v.findViewById(R.id.ferBTNActualizar);
        btCancelar=v.findViewById(R.id.ferBTNCancelar);
        llRegresar=v.findViewById(R.id.ferLLRegresar);
        etNombreRecordatorio=v.findViewById(R.id.ferETNombreRecordatorio);
        etDescripcionRecordatorio=v.findViewById(R.id.ferETDescripcionRecordatorio);
        etFechaRecordatorio=v.findViewById(R.id.ferETFecha);
        etHoraRecordatorio=v.findViewById(R.id.ferETHorario);

        Bundle bundle=this.getArguments();
        nombreRecordatorio=bundle.getString("nombreRecordatorio");
        descripcionRecordatorio=bundle.getString("descripcionRecordatorio");
        fechaRecordatorio=bundle.getString("fechaRecordatorio");
        horaRecordatorio=bundle.getString("horaRecordatorio");
        claveRecordatorio=bundle.getString("claveRecordatorio");

        etNombreRecordatorio.setText(nombreRecordatorio);
        etDescripcionRecordatorio.setText(descripcionRecordatorio);
        etFechaRecordatorio.setText(fechaRecordatorio);
        etHoraRecordatorio.setText(horaRecordatorio);

        llRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        ibtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ferIBFecha:
                        obtenerFecha();
                        break;
                }
            }
        });

        ibtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ferIBHorario:
                        obtenerHora();
                        break;
                }
            }
        });

        btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarInformacion(claveRecordatorio
                        ,etNombreRecordatorio.getText().toString()
                        ,etDescripcionRecordatorio.getText().toString()
                        ,etFechaRecordatorio.getText().toString()
                        ,etHoraRecordatorio.getText().toString());
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    private void actualizarInformacion(String claveRecordatorio, final String nombreRecordatorio, String descripcion, String fecha, String hora) {
        db = FirebaseFirestore.getInstance();
        db.collection("recordatorios").document(claveRecordatorio)
                .update("nombreRecordatorio",nombreRecordatorio
                        ,"descripcion",descripcion
                        ,"fecha",fecha
                        ,"hora",hora)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Se editó el recordatorio correctamente.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                });
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
}
