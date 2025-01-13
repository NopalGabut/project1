package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {
    EditText signinUsername, signinPassword;
    Button signinButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signinUsername = findViewById(R.id.signinUsername);
        signinPassword = findViewById(R.id.signinPassword);
        signupRedirectText = findViewById(R.id.signinRedirectText);
        signinButton = findViewById(R.id.signinButton);

        String newUsername = getIntent().getStringExtra("username");
        if (newUsername != null) {
            signinUsername.setText(newUsername);
        }

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername () | !validatePassword ()){

                }else {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkUser() {
        String enteredUsername = signinUsername.getText().toString().trim();
        String enteredPassword = signinPassword.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.child(enteredUsername).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String databaseUsername = dataSnapshot.child("username").getValue(String.class);
                    String databasePassword = dataSnapshot.child("password").getValue(String.class);

                    System.out.println("Retrieved Username: " + databaseUsername + " Retrieved Password: " + databasePassword);
                    System.out.println("Entered Username: " + enteredUsername + " Entered Password: " + enteredPassword);

                    if (databaseUsername.equals(enteredUsername) && databasePassword.equals(enteredPassword)) {
                        // Simpan username ke dalam Intent
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        intent.putExtra("username", enteredUsername); // Kirim username yang diloginkan
                        startActivity(intent);
                        finish();
                    } else {
                        signinPassword.setError("Invalid Password");
                    }
                } else {
                    signinUsername.setError("User does not exist");
                }
            } else {
                Toast.makeText(SigninActivity.this, "Error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });


    }



    public Boolean validateUsername(){
        String val = signinUsername.getText().toString();
        if (val.isEmpty()){
            signinUsername.setError("Username cannot be empty");
            return false;
        } else {
            signinUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = signinPassword.getText().toString();
        if (val.isEmpty()){
            signinPassword.setError("Password cannot be empty");
            return false;
        } else {
            signinPassword.setError(null);
            return true;
        }
    }
}