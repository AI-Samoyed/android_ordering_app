package com.example.android.bakery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    EditText mPassword, mEmail;
    Button mLoginBtn;
    FirebaseAuth fAuth;
    TextView mRegisterBtn;
    ProgressBar progressBar ;
    CheckBox mcheckBox;
    SharedPreferences sp;
    SharedPreferences.Editor mEditor;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email) ;
        mPassword = findViewById(R.id.password) ;
        mLoginBtn = findViewById(R.id.btnLogout) ;
        progressBar = findViewById(R.id.loading) ;
        mRegisterBtn = findViewById(R.id.loginHere);
        fAuth = FirebaseAuth.getInstance();
        mcheckBox = findViewById(R.id.checkBox);
        sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);


        //checkSharedPreferences();



        mLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
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

                //authenticate the users:
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mcheckBox.isChecked()){

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Email", email);
                                editor.putString("Password", password);
                                editor.commit();
                                Toast.makeText(Login.this, "Info saved", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(Login.this, "Logged in Successfully!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Logout.class));
                        }
                        else {
                            Toast.makeText(Login.this, "Incorrect password or email", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

    }

    /*Check the shared preferences and set them accordingly
    private void checkSharedPreferences(){
        String checkbox = sp.getString(getString(R.string.checkbox),"False");
        String password = sp.getString(getString(R.string.password),"");
        String email = sp.getString(getString(R.string.email),"");
        mEmail.setText(email);
        mPassword.setText(password);
        if(checkbox.equals("True")){
            mcheckBox.setChecked(true);
        }
        else {
            mcheckBox.setChecked(false);
        }

    }
    */
}
