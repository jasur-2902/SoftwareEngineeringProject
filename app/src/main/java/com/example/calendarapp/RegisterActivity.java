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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/*
    TODO: set up email verification
 */

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

                final String stringEmail = emailAddress.getText().toString();
                final String stringUsername = username.getText().toString();
                final String stringPassword = password.getText().toString();
                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(stringUsername);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0){
                            Toast.makeText(RegisterActivity.this, "Username Already Taken", Toast.LENGTH_SHORT).show();
                        } else {
                            if (TextUtils.isEmpty(stringEmail) || TextUtils.isEmpty(stringUsername) || TextUtils.isEmpty(stringPassword) || !tos.isChecked()){
                                Toast.makeText(RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                            } else if (stringPassword.length() < 6 ){
                                Toast.makeText(RegisterActivity.this, "password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                            } else {
                                register(stringEmail, stringUsername, stringPassword);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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

                    //We input our values into a hashmap so we can have our json file be read to our database
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
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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