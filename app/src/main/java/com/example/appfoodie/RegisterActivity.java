package com.example.appfoodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, repassword;
    Button signup, signin;
    DataBaseHelper DB;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        firebaseAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> {
            username.setTextColor(Color.BLACK);
            password.setTextColor(Color.BLACK);
            repassword.setTextColor(Color.BLACK);

            String user = username.getText().toString();
            String pass = password.getText().toString();
            String repass = repassword.getText().toString();

            if (user.equals("") || pass.equals("") || repass.equals(""))
                Toast.makeText(RegisterActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else {
                if (pass.equals(repass)) {
                    ProgressDialog loadingIndicator = new ProgressDialog(RegisterActivity.this);//loading animation
                    loadingIndicator.setMessage("creating user");
                    loadingIndicator.show();
                    firebaseAuth.createUserWithEmailAndPassword(user, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            loadingIndicator.cancel();
                        }
                    }).addOnFailureListener(e -> {
                        /**
                         * if the email is in use/false we mark it red
                         */
                        Toast.makeText(RegisterActivity.this, "failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        if (Objects.requireNonNull(e.getMessage()).contains("email")) {
                            username.setTextColor(Color.RED);
                            password.setTextColor(Color.RED);

                            Toast.makeText(RegisterActivity.this, "Failed to register, need email", Toast.LENGTH_SHORT).show();
                        }
                        loadingIndicator.cancel();
                    });

                } else {
                    password.setTextColor(Color.RED);
                    repassword.setTextColor(Color.RED);
                    Toast.makeText(RegisterActivity.this, "Password not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

    }

}