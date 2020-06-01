package com.example.bildungmaidant.fragments.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bildungmaidant.Menu;
import com.example.bildungmaidant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private String TAG="Mensaje HomeFragment";
    private int PICK_IMAGE_REQUEST=10001;

    View v;

    TextView nombreUsuario,institucion,sobreMi;
    ImageView fotoPerfil;
    Button fcgBTNEditarFoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home,container,false);

        db = FirebaseFirestore.getInstance();
        //Toast.makeText(getContext(), "Se creó la vista", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        nombreUsuario=v.findViewById(R.id.fhTVNombreInfo);
        institucion=v.findViewById(R.id.fhTVIntitucionInfo);
        sobreMi=v.findViewById(R.id.textView3);
        fotoPerfil=v.findViewById(R.id.imageView);
        fcgBTNEditarFoto= v.findViewById(R.id.fcgBTNEditarFoto);

        if(currentUser!=null) {
            final DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            nombreUsuario.setText(document.get("nombres").toString()+" "+document.get("apellidos").toString());
                            if(document.get("descripcion").toString()!="")
                                sobreMi.setText(document.get("descripcion").toString());
                            else
                                sobreMi.setText("Aún no has agregado una descripción :)");

                            if(document.get("institucion").toString()!="")
                                institucion.setText(document.get("institucion").toString());
                            else
                                institucion.setText("Sin institucion");

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });


            if(currentUser.getPhotoUrl()!=null){
                Glide.with(this)
                        .load(currentUser.getPhotoUrl())
                        .into(fotoPerfil);
            }

        }
        fcgBTNEditarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST&&resultCode==Menu.RESULT_OK)
        {
            Uri uriImage = data.getData();
            fotoPerfil.setImageURI(uriImage);
            Bitmap bitmap= ((BitmapDrawable)fotoPerfil.getDrawable()).getBitmap();
            handleUpload(bitmap);
        }
        Toast.makeText(getContext(), "Sigue", Toast.LENGTH_SHORT).show();

    }


    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference= FirebaseStorage.getInstance().getReference()
                .child("profileImages").child(uid+".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure", e.getCause());
                    }
                });
    }
    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess:" + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri)
    {
        UserProfileChangeRequest request= new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        currentUser.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Updated succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Profile image failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
}
