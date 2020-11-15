package com.example.android.bakery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mbtnRegister;
    FirebaseAuth fAuth;
    ProgressBar progressBar ;
    TextView mLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullname) ;
        mEmail = findViewById(R.id.email) ;
        mPassword = findViewById(R.id.password) ;
        mPhone = findViewById(R.id.phone) ;
        mbtnRegister = findViewById(R.id.btnCreate) ;
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.loading);
        //User already has an account:
        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        //Handle the event-click register button:
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
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
                    startActivity((new Intent(getApplicationContext(),MainActivity.class)));
                    finish();
                }
                //register User with firebase:

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "User Created!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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