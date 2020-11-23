package com.example.android.bakery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.proto.TargetGlobal;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    public static final String TAG = "TAG" ;
    EditText profileFullName, profileEmail , profilePhoneno ;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button saveBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent() ;
        String fullName = data.getStringExtra("fullName") ;
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");
        fStore= FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser() ;
        saveBtn = findViewById(R.id.saveProfileInfo);
        profileFullName = findViewById(R.id.profileFullName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhoneno = findViewById(R.id.profilePhoneno);

        //Click on save Button to save editted info into firebase firestore
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileEmail.getText().toString().isEmpty() ||profilePhoneno.getText().toString().isEmpty()||profileFullName.getText().toString().isEmpty()) {
                    Toast.makeText(EditProfile.this, "One or more fields are empty", Toast.LENGTH_SHORT).show() ;
                    return;
                }
                String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> editedMap= new HashMap<>();
                        editedMap.put("email",email);
                        editedMap.put("fName",profileFullName.getText().toString());
                        editedMap.put("phone",profilePhoneno.getText().toString());
                        docRef.update(editedMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this,"Profile is Updated",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Logout.class));
                                finish();
                            }
                        });
                        Toast.makeText(EditProfile.this,"Email is changed!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        profileFullName.setText(fullName);
        profilePhoneno.setText(phone);
        profileEmail.setText(email);

        Log.d(TAG,"onCreate " + fullName + email + phone) ;

    }
}