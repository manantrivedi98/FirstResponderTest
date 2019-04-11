package com.example.firstrespondertest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListner;
    private Button submitReg;
    private EditText email, password, name, contactName, emergencyPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null){
//                    // UNSURE (Listens for if the user logs in and the returns to the main page)?
//                    Intent intent = new Intent(getApplication(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                    return;
//                }

            }
        };

        mAuth = FirebaseAuth.getInstance();
        submitReg = findViewById(R.id.submitReg);
        email = findViewById(R.id.email);
        password = findViewById(R.id.passwordInput);
        name = findViewById(R.id.name);
        contactName = findViewById(R.id.contactName);
        emergencyPhone = findViewById(R.id.emergencyPhone);


        submitReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameString = name.getText().toString();
                final String emailString = email.getText().toString();
                final String passwordString = password.getText().toString();
                final String contactNameString = contactName.getText().toString();
                final String contactNumberString = emergencyPhone.getText().toString();
                mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // notice of Successful Login or not
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplication(), "Invalid Login", Toast.LENGTH_SHORT).show();
                        }else{
                            // create child in Database for the user
                            String userID = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

                            //Map contains the information email, name. Adds everything at the same time
                            //Map userInfo = new HashMap<>();
                            //userInfo.put("email", emailString);
                            //userInfo.put("name", nameString);
                            //userInfo.put("contact name", contactNameString);
                            //userInfo.put("contact number", contactNumberString);

                            currentUserDb.child("email").setValue(emailString);
                            currentUserDb.child("name").setValue(nameString);
                            currentUserDb.child("contact name").setValue(contactNameString);
                            currentUserDb.child("contact phone number").setValue(contactNumberString);

                            //currentUserDb.updateChildren(userInfo);

                        }

                    }
                });
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
               // return;
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListner);
    }
}
