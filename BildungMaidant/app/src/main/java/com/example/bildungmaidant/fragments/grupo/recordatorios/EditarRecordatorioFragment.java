package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

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

    private Button btCrear;
    private Button btCancelar;

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
