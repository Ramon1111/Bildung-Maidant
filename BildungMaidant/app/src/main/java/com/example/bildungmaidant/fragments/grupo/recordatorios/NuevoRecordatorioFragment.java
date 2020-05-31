package com.example.bildungmaidant.fragments.grupo.recordatorios;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private EditText etNombre, etDescripcion;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    ArrayList<String> listaRecordatoriosAUsuario;
    ArrayList<String> listaRecordatoriosAGrupo;
    private FirebaseAuth mAuth;

    private String TAG = "MensajeCrearRecordatorio";
    private String claveGrupoActual;

    private RelativeLayout regresar;

    public NuevoRecordatorioFragment(String claveGrupoActual){
        this.claveGrupoActual=claveGrupoActual;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nuevo_recordatorio,container,false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        btCrear=v.findViewById(R.id.fnrBTNCrear);
        btCancelar=v.findViewById(R.id.fnrBTNCancelar);
        etFecha=v.findViewById(R.id.fnrETFecha);
        ibtFecha=v.findViewById(R.id.fnrIBFecha);
        etHora=v.findViewById(R.id.fnrETHorario);
        ibtHora=v.findViewById(R.id.fnrIBHorario);
        tvRegresar=v.findViewById(R.id.fnrTVRegresar);
        ivRegresar=v.findViewById(R.id.fnrIVBackArrow);
        etNombre=v.findViewById(R.id.fnrETNombreRecordatorio);
        etDescripcion=v.findViewById(R.id.fnrETDescripcion);
        regresar=v.findViewById(R.id.fnrRLRegresar);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Bundle bundle=new Bundle();
                bundle.putString("claveGrupo",claveGrupoActual);
                ContenedorGrupoFragment contenedor = new ContenedorGrupoFragment();
                contenedor.setArguments(bundle);
                cargarFragment(contenedor,v);
                 */
                getActivity().onBackPressed();
            }
        });

        btCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etNombre.getText())){
                    Toast.makeText(getActivity(), "Agregar nombre de recordatorio", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etFecha.getText())){
                    Toast.makeText(getActivity(), "No se agrego fecha", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etHora.getText())){
                    Toast.makeText(getActivity(), "No se agrego hora", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etDescripcion.getText())){
                    Toast.makeText(getActivity(), "No se agrego fecha", Toast.LENGTH_SHORT).show();
                }

                if(!TextUtils.isEmpty(etNombre.getText()) && !TextUtils.isEmpty(etFecha.getText()) && !TextUtils.isEmpty(etHora.getText()) && !TextUtils.isEmpty(etDescripcion.getText())){
                    CrearRecordatorio(v);
                }
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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

    private void CrearRecordatorio(final View v) {
        String claveUsuarioBase1 = currentUser.getUid().substring(5,7);
        String claveUsuarioBase2 = currentUser.getUid().substring(7,9);
        SimpleDateFormat date1 = new SimpleDateFormat("D");
        SimpleDateFormat date2 = new SimpleDateFormat("y");
        SimpleDateFormat date3 = new SimpleDateFormat("kms");
        Date calendario = Calendar.getInstance().getTime();

        final String claveRecordatorio=date1.format(calendario)+claveUsuarioBase1+date2.format(calendario)+claveUsuarioBase2+date3.format(calendario);
        Log.d(TAG,"ClaveRecordatorio: "+claveRecordatorio);

        db.collection("grupos").document(claveGrupoActual)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                listaRecordatoriosAGrupo= new ArrayList<>();

                                if(document.get("numRecordatorios").toString()!="")
                                    for (String recordatorio : document.get("numRecordatorios").toString().split(","))
                                        listaRecordatoriosAGrupo.add(recordatorio);
                                listaRecordatoriosAGrupo.add(claveRecordatorio);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                listaRecordatoriosAUsuario= new ArrayList<>();

                                if(document.get("recordatoriosAbiertos").toString()!="")
                                    for (String recordatorio : document.get("recordatoriosAbiertos").toString().split(","))
                                        listaRecordatoriosAUsuario.add(recordatorio);

                                listaRecordatoriosAUsuario.add(claveRecordatorio);
                                SubirRecordatorioCreado(claveRecordatorio,v);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void SubirRecordatorioCreado(final String claveRecordatorio, final View v) {
        Map<String, Object> newReminder = new HashMap<>();
        newReminder.put("nombreRecordatorio",etNombre.getText().toString());
        newReminder.put("administrador",currentUser.getUid());
        newReminder.put("claveRecordatorio", claveRecordatorio);
        newReminder.put("fecha",etFecha.getText().toString());
        newReminder.put("hora",etHora.getText().toString());
        newReminder.put("descripcion",etDescripcion.getText().toString());
        newReminder.put("grupoPertenece",claveGrupoActual);
        newReminder.put("estadoEnProceso",true);

        db.collection("recordatorios")
                .document(claveRecordatorio)
                .set(newReminder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CambiarRecordatorios(claveRecordatorio,v);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void CambiarRecordatorios(String clave, final View v) {
        String newList1="";
        String newList2="";

        for(int i=0;i<listaRecordatoriosAUsuario.size();i++)
            newList1=newList1+","+listaRecordatoriosAUsuario.get(i);

        for(int i=0;i<listaRecordatoriosAGrupo.size();i++)
            newList2=newList2+","+listaRecordatoriosAGrupo.get(i);

        newList1=newList1.substring(1);
        newList2=newList2.substring(1);

        db.collection("grupos").document(claveGrupoActual)
                .update("numRecordatorios",newList2);

        db.collection("users").document(currentUser.getUid())
                .update("recordatoriosAbiertos",newList1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        //for(int i = 0; i < fm.getBackStackEntryCount(); ++i)
                         //   fm.popBackStack();
                        Toast.makeText(getContext(), "Se creó el recordatorio correctamente.", Toast.LENGTH_SHORT).show();
                        Bundle bundle=new Bundle();
                        bundle.putString("claveGrupo",claveGrupoActual);
                        ContenedorGrupoFragment contenedor = new ContenedorGrupoFragment();
                        contenedor.setArguments(bundle);
                        getActivity().onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error actualizando recordatorio en usuario.", e);
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

/*
    private void cargarFragment(Fragment fragment,View v){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        //getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,fragment).commit();
    }
 */

}
