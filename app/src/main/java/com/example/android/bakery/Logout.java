package com.example.android.bakery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Logout extends AppCompatActivity {
    TextView fullName, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button changeProfile, resetPass, logOut, btnOrder;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        phone = findViewById(R.id.profilePhone);
        email = findViewById(R.id.profileEmail);
        fullName = findViewById(R.id.profileName);
        fStore= FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        resetPass = findViewById(R.id.resetPass);
        changeProfile = findViewById(R.id.changeProfile);
        logOut = findViewById(R.id.btnLogout);
        btnOrder = findViewById(R.id.btnOrder);
        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser() ;


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot  documentSnapshot, @Nullable FirebaseFirestoreException error) {
                phone.setText(documentSnapshot.getString("phone")) ;
                email.setText(documentSnapshot.getString("email")) ;
                fullName.setText(documentSnapshot.getString("fname")) ;
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),Home.class);
                startActivity(i);
            }
        });
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());
                final AlertDialog.Builder passResetDialog = new AlertDialog.Builder(v.getContext());
                passResetDialog.setTitle("Reset Password");
                passResetDialog.setMessage("Enter New Password");
                passResetDialog.setView(resetPassword);
                passResetDialog.setPositiveButton("Save", (dialog, which) ->  {
                    String newPass = resetPassword.getText().toString();
                    user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Logout.this, "Password Reset Successfully", Toast.LENGTH_SHORT);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Logout.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                passResetDialog.setNegativeButton("Cancel", (dialog, which) -> {
                   //close
                });
                passResetDialog.create().show();
            }
        });
        changeProfile.setOnClickListener((v) -> {
            Intent i = new Intent(v.getContext(),EditProfile.class);
            i.putExtra("fullName",fullName.getText().toString());
            i.putExtra("email",email.getText().toString());
            i.putExtra("phone",phone.getText().toString());
            startActivity(i);

        });
        logOut.setOnClickListener((v) -> {
            Intent i = new Intent(v.getContext(),Login.class);
            startActivity(i);

        });

    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}
