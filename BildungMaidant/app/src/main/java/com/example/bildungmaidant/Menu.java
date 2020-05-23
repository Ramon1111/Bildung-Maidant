package com.example.bildungmaidant;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bildungmaidant.fragments.AjustesFragment;
import com.example.bildungmaidant.fragments.HomeFragment;
import com.example.bildungmaidant.fragments.MensajeMenuFragment;
import com.example.bildungmaidant.fragments.RecordatoriosTareasFragment;
import com.example.bildungmaidant.fragments.TusGruposFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    private String TAG = "Mensaje Menu", uidUsuario,emailUsuario,passUsuario;
    private FirebaseAuth mAuth;
    TextView toolbarText;
    private FirebaseUser user;

    private FirebaseFirestore db;

    private String nomUsuario="Usuario";



    Map<String,Object> UsuarioActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContenedorGrupoFragment()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.Inicio);

        }
    }

    private void IniciarInfoUsuario(String emailUsuario, String passUsuario) {
        mAuth.signInWithEmailAndPassword(emailUsuario, passUsuario)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            toolbarText.setText(user.getEmail());

                            Toast.makeText(Menu.this,"EmailUsuario: "+user.getEmail(),Toast.LENGTH_SHORT).show();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Menu.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                        // ...
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.Inicio:
                //Cada vez  que inicie sesión
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                break;
            case R.id.TusGrupos:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TusGruposFragment()).commit();
                break;
            case R.id.Recordatorios:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RecordatoriosTareasFragment()).commit();
                break;
            case R.id.Mensajes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MensajeMenuFragment()).commit();
                break;
            case R.id.Ajustes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AjustesFragment()).commit();
                break;
            case R.id.CerrarSesion:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LoginActivity()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            Toast.makeText(getApplicationContext(), "usuario: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            final DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Log.d(TAG, "DocumentSnapshot data: " + document.get("usuario"));
                            //toolbarText.setText(document.get("usuario").toString());
                            nomUsuario=document.get("usuario").toString();

                            getSupportActionBar().setTitle(nomUsuario);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }
}
