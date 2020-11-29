package com.example.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calendarapp.calendar.activities.CalendarActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText emailAddress, username, password;
    private CheckBox tos;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.continueButton);
        emailAddress = findViewById(R.id.emailReg);
        username = findViewById(R.id.usernameReg);
        password = findViewById(R.id.passwordReg);
        tos = findViewById(R.id.checkBox);

        firebaseAuth = FirebaseAuth.getInstance();

        //Register Account Button Prompt
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                String stringEmail = emailAddress.getText().toString();
                String stringUsername = username.getText().toString();
                String stringPassword = password.getText().toString();

                if (TextUtils.isEmpty(stringEmail) || TextUtils.isEmpty(stringUsername) || TextUtils.isEmpty(stringPassword) || !tos.isChecked()){
                    Toast.makeText(RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                } else if (stringPassword.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, "password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                } else {
                    register(stringEmail, stringUsername, stringPassword);
                }
            }
        });
    }

    private void register(String userEmailAddress, final String username, String userPassword){
        firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //Check to see if the task is successful
                //if so, then we register the user, else we don't
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    hashMap.put("status", "offline");
                    hashMap.put("search", username.toLowerCase());

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, CalendarActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "You can't register with this email address or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}