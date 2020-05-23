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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button btnCrear, btnIngresar;
    EditText edtEMail, edtPass;
    private String uidUsuario1,uidUsuario2,uidUsuario3;
    String TAG = "Mensaje Aplicación";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btnIngresar=findViewById(R.id.alBIngresar);
        edtEMail=findViewById(R.id.alETCorreo);
        edtPass=findViewById(R.id.alETPass);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtEMail.getText())||TextUtils.isEmpty(edtPass.getText())){
                    Toast.makeText(LoginActivity.this,"Completar campos", Toast.LENGTH_SHORT).show();
                }else{
                    PedirInfo();
                }
            }
        });

        try{
            Bundle bundleExtras=getIntent().getExtras();
            uidUsuario1=bundleExtras.getString("uidUser");
            Log.d(TAG,"Se registro usuario con id: "+uidUsuario1);

        }catch(Exception e){
            Log.i(TAG,e.getMessage());
        }

        btnCrear=findViewById(R.id.alBCrear);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Registro = new Intent(getApplicationContext(),CrearCuentaActivity.class);
                startActivity(Registro);
                finish();
            }
        });

    }

    private void PedirInfo() {
        mAuth.signInWithEmailAndPassword(edtEMail.getText().toString(), edtPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "SÍ existe el usuario");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent cambiar = new Intent(LoginActivity.this, Menu.class);
                            cambiar.putExtra("emailUser",user.getEmail());
                            cambiar.putExtra("passUser",edtPass.getText().toString());
                            startActivity(cambiar);
                            finish();
                            

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "NO existe el usuario");
                        }

                        // ...
                    }
                });
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}
