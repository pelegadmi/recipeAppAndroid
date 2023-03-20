package com.example.appfoodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText username, password;
    Button btnlogin;
    Button btnback1;

    String TAG = "LogInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        btnback1 = (Button) findViewById(R.id.btback1);
        firebaseAuth = FirebaseAuth.getInstance();
        /**
         * first we set the loading indicator
         * then we get the info from the text view.
         * then we pass the data to db to log in.
         * and move to class
         */

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog loadingIndicator = new ProgressDialog(LoginActivity.this);
                loadingIndicator.setMessage("logging in");
                loadingIndicator.show();
                String email = String.valueOf(username.getText());
                String password1 = String.valueOf(password.getText());
                password1 = password1.replaceAll("\\s+", "");//delete all spaces in the password
                if (email.length() > 0 && password.length() > 0) {
                    firebaseAuth.signInWithEmailAndPassword(email, password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            loadingIndicator.cancel();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish(); // Call once you redirect to another activity

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(LoginActivity.this, "failed 2 log in: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingIndicator.cancel();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "please enter email/ password", Toast.LENGTH_SHORT).show();
                    loadingIndicator.cancel();
                }

            }
        });
        /**
         * move to resistor activity
         */
        btnback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}