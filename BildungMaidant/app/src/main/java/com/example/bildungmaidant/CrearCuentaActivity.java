package com.example.bildungmaidant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CrearCuentaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String TAG = "Mensaje Adplicación";
    Button btnCrear;
    EditText edtUsuario,edtNombres,edtApellido,edtCorreo,edtPass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        mAuth = FirebaseAuth.getInstance();

        //Se asignan las vistas
        btnCrear=findViewById(R.id.accBRegistrarse);
        edtNombres=findViewById(R.id.accETNombres);
        edtApellido=findViewById(R.id.accETApellidos);
        edtUsuario=findViewById(R.id.accETUsuario);
        edtCorreo=findViewById(R.id.accETCorreo);
        edtPass=findViewById(R.id.accETContrasena);


        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtNombres.getText())){
                    Toast.makeText(CrearCuentaActivity.this, "Llenar el campo de Nombres", Toast.LENGTH_SHORT).show();
                }else{
                    if(TextUtils.isEmpty(edtApellido.getText())){
                        Toast.makeText(CrearCuentaActivity.this, "Llenar el campo de Apellidos", Toast.LENGTH_SHORT).show();
                    }else{
                        if(TextUtils.isEmpty(edtUsuario.getText())){
                            Toast.makeText(CrearCuentaActivity.this, "Llenar el campo de Usuario", Toast.LENGTH_SHORT).show();
                        }else {
                            if (TextUtils.isEmpty(edtCorreo.getText())) {
                                Toast.makeText(CrearCuentaActivity.this, "Llenar el campo de Correo", Toast.LENGTH_SHORT).show();
                            }else {
                                if (TextUtils.isEmpty(edtPass.getText())) {
                                    Toast.makeText(CrearCuentaActivity.this, "Llenar el campo de contrasena", Toast.LENGTH_SHORT).show();
                                }else{
                                    //Este es en el caso de que haya información en todos lados
                                    //Para registrar usuarios
                                    RegistrarUsuario();
                                    Toast.makeText(CrearCuentaActivity.this, "todos los campos están llenos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    //Para subir la app a la Appstore
    //https://play.google.com/about/developer-content-policy/?hl=es

    private void RegistrarUsuario() {
        CrearUsuario(edtNombres.getText().toString(),edtApellido.getText().toString(),edtUsuario.getText().toString(),edtCorreo.getText().toString(),edtPass.getText().toString());
    }

    private void CrearUsuario (final String nombres, final String apellido, final String nUsuario, final String correo, final String contrasena){
        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UsuarioCreado(user);

                            //Parte para subir los datos del usuario
                            SubirDatos(nombres,apellido,nUsuario,user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CrearCuentaActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            UsuarioCreado(null);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            HayUsuario(currentUser);
        }catch(Exception e){

            e.printStackTrace();
        }
    }

    private void HayUsuario(FirebaseUser currentUser) {
        Log.i(TAG,currentUser.getUid());
    }

    /*Información a cambiar si es que cambiamos los campos en firebase*/
    private void SubirDatos(String nombres, String apellidos, String nusuario, final FirebaseUser user) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nombres", nombres);
        usuario.put("apellidos", apellidos);
        usuario.put("usuario", nusuario);

        String id=user.getUid();

        db.collection("users")
                .document(id)
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + user.getUid());


                        Intent cambiar = new Intent(CrearCuentaActivity.this,LoginActivity.class);
                        //cambiar.putExtra("uidUser1",user.getIdToken(false).toString());
                        //cambiar.putExtra("uidUser2",user.getIdToken(true).toString());
                        //cambiar.putExtra("uidUser3",user.getIdToken(false).toString());
                        cambiar.putExtra("uidUser",user.getUid());
                        startActivity(cambiar);
                        finish();

                        //IniciaSesion(correo,contrasena);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void UsuarioCreado(FirebaseUser user) {
        if(user!=null){
            Toast.makeText(getApplicationContext(),"Se creó el usuario",Toast.LENGTH_SHORT).show();
            Log.i(TAG,user.getEmail());
            Log.i(TAG,user.getUid());
        }

    }
}
