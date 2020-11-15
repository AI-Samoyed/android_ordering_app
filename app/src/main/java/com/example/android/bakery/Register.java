package com.example.android.bakery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mbtnRegister;
    FirebaseAuth fAuth;
    ProgressBar progressBar ;
    TextView mLoginBtn;
    FirebaseFirestore fStore;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullname) ;
        mEmail = findViewById(R.id.email) ;
        mPassword = findViewById(R.id.password) ;
        mPhone = findViewById(R.id.phone) ;
        mbtnRegister = findViewById(R.id.btnLogout) ;
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.loading);
        mLoginBtn = findViewById(R.id.loginHere) ;
        fStore = FirebaseFirestore.getInstance() ;
        //User already has an account:
        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Logout.class));
            finish();
        }

        //Handle the event-click register button:
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString();
                String phone = mPhone.getText().toString();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required") ;
                    return ;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required") ;
                    return ;
                }

                progressBar.setVisibility(View.VISIBLE);
                //check if user account existed
                if (fAuth.getCurrentUser() != null){
                    startActivity((new Intent(getApplicationContext(), Logout.class)));
                    finish();
                }
                //register User with firebase:

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "User Created!!", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID) ;
                        Map<String,Object> user = new HashMap<>();
                        user.put("fname", fullName) ;
                        user.put("email", email);
                        user.put("phone", phone);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "user profile is created for " + userID);
                            }
                        });
                        startActivity(new Intent(getApplicationContext(), Logout.class));
                    }
                    else {
                        Toast.makeText(Register.this, "Error occurred!!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    }
                );

            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}