package com.example.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailAddress, password;
    private TextView forgotPassword;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get reference of the email, password, and login
        emailAddress = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgot_password_prompt);

        firebaseAuth = FirebaseAuth.getInstance();

        //login account direction
        //Check in to see if all the fields are entered, if not, then don't sign in
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringEmailAddress = emailAddress.getText().toString();
                String stringPassword = password.getText().toString();

                if (TextUtils.isEmpty(stringEmailAddress) || TextUtils.isEmpty(stringPassword)) {
                    Toast.makeText(LoginActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                } else {
                    validateLogin(stringEmailAddress, stringPassword);
                }

            }
        });

        //TODO: make forgot password activity
        //forgot password direction
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });


    }

    //validateLogin function
    //validate all the credentials to see if everything is in order here
    private void validateLogin(String userEmailAddress, String userPassword){
        firebaseAuth.signInWithEmailAndPassword(userEmailAddress, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //If login was successful by firebase, they can login, else no
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}